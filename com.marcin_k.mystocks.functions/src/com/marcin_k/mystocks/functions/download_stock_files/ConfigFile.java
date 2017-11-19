package com.marcin_k.mystocks.functions.download_stock_files;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**********************************************************************
 * Config file is responsible for reading and 
 * writing to a config file, which contains the URL
 * of zip file with the stocks, date of last update
 * (to prevent the app of downloading the files twice
 * the same day
 **********************************************************************/
public class ConfigFile {
	
	//TODO: read file and update date - if filed download
	//		do nothing if date is the same in the file as current
	//		create a default configuration file 
	String zipFileName ="stocksListing.zip";
	String filesExportedDirectoryName= "filesExported";
	String URLOfZippedStockFiles = "http://bossa.pl/pub/metastock/mstock/mstall.zip";
	String lastModificationDate = "0000-00-00 00:00:00";
	String FILENAME = "config.txt";
	
//---------------------------------------- Constructor ------------------------------------------------------	
	public ConfigFile(){
		readFile();
	}
	
//------------------------------------ Reads the config file ------------------------------------------------
	private void readFile() {
		int linesToRead = 1;
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {	
				if(sCurrentLine.length()>0) {
						if(!(sCurrentLine.charAt(0)=='#')){
							if(linesToRead==1) {
								zipFileName=sCurrentLine;
							}
							else if (linesToRead==2) {
								filesExportedDirectoryName=sCurrentLine;
							}
							else if (linesToRead==3) {
								URLOfZippedStockFiles=sCurrentLine;
							}
							else if (linesToRead==4) {
								lastModificationDate=sCurrentLine;
							}
							linesToRead++;
						}
				}
			}
			if (linesToRead!=5) {
				throw new IOException();
			}

			System.out.println(zipFileName);
			System.out.println(filesExportedDirectoryName);
			System.out.println(URLOfZippedStockFiles);
			System.out.println(lastModificationDate);
			
		} catch (IOException e) {
			//if the IO Exception occurs the default file is created
			createFile();
			e.printStackTrace();
		}
	}
	
//-------------------------------- Creates a new (or overrides) config file ---------------------------------	
	public void createFile() {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		
		String updatedDate = dtf.format(now);
		
		String defaultFileContent = "#################################################################\n" + 
				"#                                                               #\n" + 
				"#             __  __        _____ _             _               #\n" + 
				"#            |  \\/  |      / ____| |           | |              #\n" + 
				"#            | \\  / |_   _| (___ | |_ ___   ___| | _____        #\n" + 
				"#            | |\\/| | | | |\\___ \\| __/ _ \\ / __| |/ / __|       #\n" + 
				"#            | |  | | |_| |____) | || (_) | (__|   <\\__ \\       #\n" + 
				"#            |_|  |_|\\__, |_____/ \\__\\___/ \\___|_|\\_\\___/       #\n" + 
				"#                     __/ |                                     #\n" + 
				"#                    |___/                                      #\n" + 
				"#                                                               #\n" + 
				"#################################################################\n" + 
				"\n" + 
				"# File contains basic configuration allowing for adjusting \n" + 
				"# the way application obtains and persist data\n" + 
				"# Lines which begins with \"#\" are treated as comments\n" + 
				"# and skipped by the application, please adjust only the \n" + 
				"# required fields \n" + 
				"\n" + 
				"# Name under which the zip file is temporarily stored:\n" + 
				zipFileName + 
				"\n\n" + 
				"# Directory for storing unzipped stock files:\n" + 
				filesExportedDirectoryName +"\n" + 
				"\n" + 
				"# URL of the source of the zipped stock files \n" + 
				"# Make sure address starts with http/https and end with .zip\n" + 
				URLOfZippedStockFiles+"\n" + 
				"\n" + 
				"# Date of the last update (not to be updated by a user)\n" + 
				updatedDate;
		
		File configFile = new File("config.txt");
		try {
			configFile.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(configFile, false);
			fileOutputStream.write(defaultFileContent.getBytes());
			fileOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  		 
	}

//------------------------------------ Getters and Setters --------------------------------------------------	
	public String getZipFileName() {
		return zipFileName;
	}

	public String getFilesExportedDirectoryName() {
		return filesExportedDirectoryName;
	}

	public String getURLOfZippedStockFiles() {
		return URLOfZippedStockFiles;
	}

	public String getLastModificationDate() {
		return lastModificationDate;
	}
	
}
