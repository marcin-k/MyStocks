package com.marcin_k.mystocks.functions.download_stock_files;

import java.util.ArrayList;
import com.marcin_k.mystocks.functions.ReadStockFile;
import com.marcin_k.mystocks.model.DailyStockRecord;
import com.marcin_k.mystocks.model.Stock;

/***********************************************************
 * Controller utilize the Singleton design pattern/
 * StockController deals witch the queries made on Stocks
 * objects, contains array list of all stock objects.
 *
 ***********************************************************/

public class StocksController {
	/** static Singleton instance **/
	private static volatile StocksController instance;

	/** Return a singleton instance of FilesController **/
	public static StocksController getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (FilesController.class) {
				if (instance == null) {
					instance = new StocksController();
				}
			}
		}
		return instance;
	}
//---------------------------------------- Non Singleton Part of Class -------------------------------------------------
	/** Variables **/
	//stores all Stock objects with their history
	private ArrayList<Stock> allStocks;
	
//------------------------------------------------ Constructor ---------------------------------------------------------
	private StocksController() {
		allStocks = new ArrayList<Stock>();
	}
	
//----------------------------------- Setup method creates all allStocks Object ----------------------------------------	
	// Method has to run prior to any other operations on that Controller, requires a list of 
	// tickers(file names) in the form of array list to be passed in, in order to create
	// array list of stock objects
	public void setupStocks(ArrayList<String> tickers) {
		for(String ticker: tickers) {
			ReadStockFile reader = new ReadStockFile();
			allStocks.add(reader.getStockHistoryFromFile(ticker));
		}
	}
	
//---------------------------------------------- Getters and Setters ---------------------------------------------------	
	//get all stocks object
	public ArrayList<Stock> getAllStocksObjects(){
		return allStocks;
	}
	
	//returns a Stock object with a ticker symbol passed in
	public Stock getStockWithTicker(String ticker) {
		Stock stockToReturn = null;
		for (Stock stock : allStocks) {
			if (ticker.equalsIgnoreCase(stock.getTicker())) {
				stockToReturn = stock;
			}
		}
		if (stockToReturn != null) {
			return stockToReturn; 
		}
		else {
			return new Stock("error", new ArrayList<DailyStockRecord>());
		}
	}
	
	//next method?
	
}
