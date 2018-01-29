package com.marcin_k.mystocks.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;

/*************************************************************
 * Class represent each stock, contains an array list of
 * DailyStockRecord, ticker symbol (corresponding to file
 * name, should match symbol on all records)
 * 
 *  In the future release stock will also contains the 
 *  recommendation and brief of the company's situation
 * 
 *************************************************************/
public class Stock {
	
	//Constant for data binding
	public static final String TICKER = "ticker";
	
	//Constant added for data binding
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
	private String ticker;
	private ArrayList<DailyStockRecord> stockRecords;
	
	public Stock(String ticker, ArrayList<DailyStockRecord> stockRecords) {
		this.ticker = ticker;
		this.stockRecords = stockRecords;
	}
	
//------------------------------------------ Getters and Setters -------------------------------------------------------	
	//returns ticker symbol
	public String getTicker() {
		return ticker;
	}
	
	//returns the Array list of daily records objects
	public ArrayList<DailyStockRecord> getDailyRecords() {
		return stockRecords;
	}
	
	//returns close price from last session
	public double getLastClosePrice() {
		return stockRecords.get(stockRecords.size()-1).getClosePrice();
	}
	
	//returns close price from the second last session
	public double getSecondLastClosePrice() {
		return stockRecords.get(stockRecords.size()-2).getClosePrice();
	}
	
	public double getLastVolume() {
		return stockRecords.get(stockRecords.size()-1).getVolume();
	}
	
//------------------------------------------------ Data binding methods ------------------------------------------------ 
	public void setTicker(String ticker) {
		changes.firePropertyChange( TICKER, this.ticker, this.ticker = ticker);
	}

	public void addPropertyChangeListener(PropertyChangeListener l) {
		changes.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		changes.removePropertyChangeListener(l);
	}	
	
}
