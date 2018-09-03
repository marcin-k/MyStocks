package com.marcin_k.mystocks.functions.filesHandlers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**********************************************************************
 * MyPortfolioFile class is responsible for reading and 
 * writing to a myPortfolio.dat file, which contains the list
 * of stocks observed by a user. 
 * If file is missing class will generate a new one.
 **********************************************************************/

public class MyPortfolioFile {

	/** Variables **/
	String FILENAME = "myPortfolio.dat";
	ArrayList<String> myList;
	private File portfolioFile;
	
//---------------------------------------------- Constructor -----------------------------------------------------------
	public MyPortfolioFile() {
		portfolioFile = new File(FILENAME);
		myList = new ArrayList<>();
		readFile();
	}
	
//--------------------------------------- Reads the myPortfolio file ---------------------------------------------------
		private void readFile() {
			try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
				String sCurrentLine;
				while ((sCurrentLine = br.readLine()) != null) {	
					if(sCurrentLine.length()>0) {
						myList.add(sCurrentLine);
					}
				}
				
				
			} catch (IOException e) {
				//if the IO Exception occurs the default file is created
				createFile();
				e.printStackTrace();
			}
		}
		
//----------------------------------- Creates a new (or overrides) myPortfolio file ------------------------------------	
		private void createFile() {
			String defaultFileContent="";
			
			try {
				portfolioFile.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(portfolioFile, false);
				fileOutputStream.write(defaultFileContent.getBytes());
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  		 
		}
		
		private void createFile(ArrayList<String> stockList) {
			String defaultFileContent="";
			for(String element : stockList) {
				defaultFileContent+=element+System.lineSeparator();
			}
						
			try {
				portfolioFile.createNewFile();
				FileOutputStream fileOutputStream = new FileOutputStream(portfolioFile, false);
				fileOutputStream.write(defaultFileContent.getBytes());
				fileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}  		 
		}

//----------------------------------------------- Getters and Setters --------------------------------------------------	
		public ArrayList<String> getList(){
			System.out.println("list length "+myList.size());
			return myList;
		}
		public void saveFile(String additonalTicker) {
			myList.add(additonalTicker);
			createFile(myList);
		}
		
}



