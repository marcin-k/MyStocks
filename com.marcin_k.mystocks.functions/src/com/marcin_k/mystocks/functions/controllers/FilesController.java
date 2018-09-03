package com.marcin_k.mystocks.functions.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import com.marcin_k.mystocks.functions.filesHandlers.ConfigFile;
import com.marcin_k.mystocks.functions.filesHandlers.ReadWig20File;
import com.marcin_k.mystocks.functions.filesHandlers.UnzipUtility;
import com.marcin_k.wig20downloader.DownloadWig20;

/********************************************************************
 * Controller used to deal with the stock files, responsible for
 * download, unpack and deletion of the archive file. 
 * Directories and URL of the archive are defined in config file 
 * 
 ********************************************************************/
public class FilesController {
	/** static Singleton instance **/
	private static volatile FilesController instance;

	/** Return a singleton instance of FilesController **/
	public static FilesController getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (FilesController.class) {
				if (instance == null) {
					instance = new FilesController();
				}
			}
		}
		return instance;
	}

//------------------------------------------ Non Singleton Part of Class -----------------------------------------------
	/** Variables **/
	ConfigFile configFile;
	UnzipUtility unzipUtility;
	ReadWig20File readWig20File;
	String sourceDirectory;
	String destDirectory;
	String urlOfZipFile;
	String lastUpdateDate;
	ArrayList<String> wig20list;
	
//-------------------------------------------------- Constructor -------------------------------------------------------
	private FilesController() {
		configFile = new ConfigFile();
		unzipUtility = new UnzipUtility();
		readWig20File = new ReadWig20File();
		sourceDirectory = configFile.getZipFileName();
		destDirectory =configFile.getFilesExportedDirectoryName();
		urlOfZipFile = configFile.getURLOfZippedStockFiles();
		lastUpdateDate = configFile.getLastModificationDate();
		wig20list = readWig20File.getWig20Companies();
	}
	

//-------------------------- Returns a list of all stocks based on file names in directory -----------------------------
	//This method should be called once the files are download and exported - after getAllFiles()
	public ArrayList<String> getAllTickers(){
		
		ArrayList<String> stockTickers = new ArrayList<String>();
		File directory = new File(destDirectory);
		File[] listOfFiles = directory.listFiles();

		    for (int i = 0; i < listOfFiles.length; i++) {
		      if (listOfFiles[i].isFile()) {
		    	  	//adds to the list the name of the file (ticker) without extension
		        stockTickers.add(listOfFiles[i].getName().toUpperCase().substring(0, 
		        		(listOfFiles[i].getName().length()-4)));
		      }
		    }
		    
		return stockTickers;
	}
	
//--------------------------------- Download all files and unzip them if files are old ---------------------------------
	//TODO: remove the exception from here and catch it in UnzipUtility
	public void getAllFiles() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String dateNow = dtf.format(now);
		
		if (!(dateNow.substring(0, 10).equals(lastUpdateDate.substring(0, 10)))) {
			downloadStockFile();
			downloadListOfWig20();
			unzipUtility.unzip(sourceDirectory, destDirectory);
			deleteUnusedFiles(configFile.getPrefixesToDelete());
		}
		//creates objects of all download files
		StocksController.getInstance().setupStocks(getAllTickers());
	}

//----------------------------------------- Getters and Setters --------------------------------------------------------	
	public ConfigFile getConfigFile() {
		return configFile;
	}
	
	public String getDestDirectory() {
		return destDirectory;
	}
	
	public ArrayList<String> getWig20list(){
		return wig20list;
	}
//---------------------------------------- Creates file with list of wig20 companies -----------------------------------
	private void downloadListOfWig20() {
		try {
			DownloadWig20.main(new String[]{});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//--------------------------------- Downloads stock file in the ZIP format from the URL ---------------------------------
	public void downloadStockFile() {
		try {
			URL website = new URL(urlOfZipFile);
			ReadableByteChannel rbc;
			rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos;
			fos = new FileOutputStream(sourceDirectory);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			//Overrides the file when new files download
			configFile.createFile();
			
		} catch (MalformedURLException e) {
			System.out.println("wrong url");
			e.printStackTrace();
		} catch (FileNotFoundException e1) {
			System.out.println("missing file");
			e1.printStackTrace();
		} catch (IOException e2) {
			System.out.println("IO problem");
			e2.printStackTrace();
		} 
	}	
//------------------------------------------------- Unpack Files -------------------------------------------------------
	public void unzipFiles() {
		unzipUtility.unzip(sourceDirectory, destDirectory);
	}
	
//---------------------------------- Deletes the ZIP file and unused stock files----------------------------------------
	public void deleteUnusedFiles(String prefixesToOfFilesToBeDeleted) {
		//delete ZIP file
		File file = new File(sourceDirectory);
	    file.delete();
	    
	    //deletes other files
	    String[] prefixes = prefixesToOfFilesToBeDeleted.split(",");
	    //looping though all prefixes and deletes all files that match them
	    for(int i=0; i<prefixes.length; i++) {
		    try (DirectoryStream<Path> newDirectoryStream = Files.newDirectoryStream(Paths.get(destDirectory), prefixes[i] + "*")) {
		        for (final Path newDirectoryStreamItem : newDirectoryStream) {
		            Files.delete(newDirectoryStreamItem);
		        }
		    } catch (final Exception e) {
		        e.printStackTrace();
		    }
	    }
	}
}
