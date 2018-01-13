package com.marcin_k.mystocks.functions.controllers;

import java.util.ArrayList;
import java.util.List;

import com.marcin_k.mystocks.functions.filesHandlers.MyPortfolioFile;
import com.marcin_k.mystocks.model.Stock;

/********************************************************************
 * Controller used to deal with list of stocks in MyPortfolio,
 * responsible for reading the list of stocks from file myStocks.dat,
 * adding new entries to file, 
 ********************************************************************/
public class MyPortfolioController {
	/** static Singleton instance **/
	private static volatile MyPortfolioController instance;

	/** Return a singleton instance of FilesController **/
	public static MyPortfolioController getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (MyPortfolioController.class) {
				if (instance == null) {
					instance = new MyPortfolioController();
				}
			}
		}
		return instance;
	}

//------------------------------------------ Non Singleton Part of Class -----------------------------------------------
	/** Variables **/
	ArrayList<String> myPortfolioStrings;
	ArrayList<Stock> myPortfolioStocks;
	MyPortfolioFile portfolioFile;
	
//-------------------------------------------------- Constructor -------------------------------------------------------
	private MyPortfolioController() {
		
		portfolioFile = new MyPortfolioFile();
		
		myPortfolioStocks = new ArrayList<>();
		myPortfolioStrings = portfolioFile.getList();
		for (String str : myPortfolioStrings) {
			System.out.println("myPortfolioStrings: "+str);
		}
		
		makeArrayOfStocks(myPortfolioStrings);
	}
	
//------------------------------------------ Make Objects from Strings -------------------------------------------------	
	private void makeArrayOfStocks(ArrayList<String> stringList) {
			
		for(String ticker : stringList) {
			Stock stock = StocksController.getInstance().getStockWithTicker(ticker);
			myPortfolioStocks.add(StocksController.getInstance().getStockWithTicker(ticker));
		}
	}
	
//--------------------------------------------------- Getters ----------------------------------------------------------
	public List<Stock> getMyPortfolioStocks() {
		return myPortfolioStocks;
	}
	
}
