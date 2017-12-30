package com.marcin_k.mystocks.functions.filesHandlers;

import com.marcin_k.mystocks.functions.controllers.FilesController;
import com.marcin_k.mystocks.model.DailyStockRecord;
import com.marcin_k.mystocks.model.Stock;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**********************************************************************
 * File contains a function to read a stock file with the name
 * passed in(without file extension), stored in the location
 * specified in config file
 * ReadStockFile() returns a Stock object containing a array list of
 * stock history
 **********************************************************************/

public class ReadStockFile {
	
	
//-------------------------------- Returns Stock object based on file content ------------------------------------------	
	public Stock getStockHistoryFromFile(String stockTicker) {
		
		/** Variables **/
		//exported files are stored in directory specified in config file
		String FILENAME = FilesController.getInstance().getDestDirectory()+"/"+stockTicker+".mst";
		
		//ArrayList to store the stock daily history
		ArrayList<DailyStockRecord> stockHistory = new ArrayList<DailyStockRecord>();
		
		try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
			String sCurrentLine;
			
			//skips the first line - "<TICKER>,<DTYYYYMMDD>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>"
			sCurrentLine = br.readLine();
			
			//Array to store values from each line
			String[] partsOfEachLine = new String[7];
						
			while ((sCurrentLine = br.readLine()) != null) {	
				partsOfEachLine = sCurrentLine.split(",");
				
				DailyStockRecord dailyStockRecord = new DailyStockRecord(partsOfEachLine[0],
						Integer.parseInt(partsOfEachLine[1]), Double.parseDouble(partsOfEachLine[2]), 
						Double.parseDouble(partsOfEachLine[3]), Double.parseDouble(partsOfEachLine[4]), 
						Double.parseDouble(partsOfEachLine[5]), 
						Double.parseDouble(partsOfEachLine[6]));
				
				stockHistory.add(dailyStockRecord);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Stock object containing stock history
		return new Stock(stockTicker.toUpperCase(), stockHistory);
	}
}
