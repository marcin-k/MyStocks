package com.marcin_k.mystocks.services.prepare_files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

//------------------------------------ Non Singleton Part of Class ------------------------------------------
	/** Variables **/
	ConfigFile configFile;
	UnzipUtility unzipUtility;
	String sourceDirectory;
	String destDirectory;
	String urlOfZipFile;
	String lastUpdateDate;
	
//--------------------------------------------- Constructor -------------------------------------------------
	private FilesController() {
		configFile = new ConfigFile();
		unzipUtility = new UnzipUtility();
		sourceDirectory = configFile.getZipFileName();
		destDirectory =configFile.getFilesExportedDirectoryName();
		urlOfZipFile = configFile.getURLOfZippedStockFiles();
		lastUpdateDate = configFile.getLastModificationDate();
	}
	
	//

//-------------------------- Download all files and unzip them if files are old -----------------------------
	//TODO: remove the exception from here and catch it in UnzipUtility
	public void getAllFiles() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String dateNow = dtf.format(now);
		
		if (!(dateNow.substring(0, 10).equals(lastUpdateDate.substring(0, 10)))) {
			downloadFile();
			unzipUtility.unzip(sourceDirectory, destDirectory);
			deleteZIP();
		}	
	}

//------------------------------------ Getter method for ConfigFile -----------------------------------------	
	public ConfigFile getConfigFile() {
		return configFile;
	}
	
//-------------------------- Downloads the file in the ZIP format from the URL ------------------------------
	public void downloadFile() {
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
//------------------------------------------ Unzip Files ----------------------------------------------------
	public void unzipFiles() {
		unzipUtility.unzip(sourceDirectory, destDirectory);
	}
	
//--------------------------------------- Deletes the ZIP file ----------------------------------------------
	private void deleteZIP() {
		File file = new File(sourceDirectory);
	    file.delete();
	}
}
