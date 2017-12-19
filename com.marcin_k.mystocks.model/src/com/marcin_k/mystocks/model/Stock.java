package com.marcin_k.mystocks.model;

import java.util.ArrayList;

/*************************************************************
 * Class represent each stock, contains an array list of
 * DailyStockRecord, ticker symbol (corresponding to file
 * name, should match symbol on all records)
 * 
 *  In the future stock will also contains the recommendation
 *  and brief of the company's situation
 * 
 *************************************************************/
public class Stock {
	
	//Constant for data binding
	public static final String TICKER = "ticker";
	
	private String ticker;
	private ArrayList<DailyStockRecord> stockRecords;
	
	public Stock(String ticker, ArrayList<DailyStockRecord> stockRecords) {
		this.ticker = ticker;
		this.stockRecords = stockRecords;
	}
	
//------------------------------------ Getters and Setters --------------------------------------------------	
	public String getTicker() {
		return ticker;
	}
	
}
