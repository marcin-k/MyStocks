package com.marcin_k.mystocks.functions.filesHandlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import com.marcin_k.wig20downloader.DownloadWig20;

/**********************************************************************
 * Class read the file with the list of wig20 companies
 * returns array list of strings 
 * 
 * If file is missing new one will be created
 **********************************************************************/
public class ReadWig20File {
	
	/** Variables **/
	ArrayList<String> wig20list;
	String FILENAME = "wig20companies.txt";
	
//--------------------------------------------- Constructor ------------------------------------------------------------	
	public ReadWig20File(){
		readFile();
	}
	
//------------------------------------------ Reads the config file -----------------------------------------------------
	private void readFile() {
		wig20list = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {	
				if(sCurrentLine.length()>0) {
					if(!(sCurrentLine.charAt(0)=='#')){
						wig20list.add(sCurrentLine);
					}
				}
			}
			
		} catch (IOException e) {
			//if the IO Exception occurs the default file is created
			createFile();
			System.out.println("***** Wig20 file missing *****");
			readFile();
		}
	}
	
//----------------------------------------- Creates a new file ---------------------------------------------------------	
	public void createFile() {
		try {
			DownloadWig20.main(new String[]{});
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

//--------------------------------------------- Getters ----------------------------------------------------------------	
	public ArrayList<String> getWig20Companies(){
		return wig20list;
	}
}
