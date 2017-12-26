package com.marcin_k.mystocks.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
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
	
	//Constant added for data binding
	private PropertyChangeSupport changes = new PropertyChangeSupport(this);
	
	private String ticker;
	private ArrayList<DailyStockRecord> stockRecords;
	
	public Stock(String ticker, ArrayList<DailyStockRecord> stockRecords) {
		this.ticker = ticker;
		this.stockRecords = stockRecords;
	}
	
//------------------------------------ Getters and Setters --------------------------------------------------	
	//returns ticker symbol
	public String getTicker() {
		return ticker;
	}
	
	//returns the Array list of daily records objects
	public ArrayList<DailyStockRecord> getDailyRecords() {
		return stockRecords;
	}
	
	//returns a double array of close prices
	//dateRange defines how many records to return
	//if -1 is passed in all records are returned
	public double[] getArrayOfClosePrices(int dateRange) {
		double [] closePriceArray;
		int firstRecordToReturn = 0;
		int indexOfArrayToReturn = 0;
		
		//if file contains less than 5 days of records all records are returned
		if (stockRecords.size()<5) {
			closePriceArray = new double [stockRecords.size()];
			firstRecordToReturn = 0;
		}
		//if file contains less than 1 month of records a week or max can be selected
		else if (stockRecords.size()<21) {
			if (dateRange==5) {
				closePriceArray = new double [5];
				firstRecordToReturn = stockRecords.size()-5;
			}
			else{
				closePriceArray = new double [stockRecords.size()];
				firstRecordToReturn = 0;
			}
		}
		//if file contains less than 3 months of records a week, month or max can be selected
		else if (stockRecords.size()<62) {
			if (dateRange==21) {
				closePriceArray = new double [21];
				firstRecordToReturn = stockRecords.size()-21;
			}
			else if (dateRange==5) {
				closePriceArray = new double [5];
				firstRecordToReturn = stockRecords.size()-5;
			}
			else{
				closePriceArray = new double [stockRecords.size()];
				firstRecordToReturn = 0;
			}
		}
		//if file contains less than 6 months of records a week, month, 3 months or max can be selected
		else if (stockRecords.size()<125) {
			if (dateRange==62) {
				closePriceArray = new double [62];
				firstRecordToReturn = stockRecords.size()-62;
			}
			else if (dateRange==21) {
				closePriceArray = new double [21];
				firstRecordToReturn = stockRecords.size()-21;
			}
			else if (dateRange==5) {
				closePriceArray = new double [5];
				firstRecordToReturn = stockRecords.size()-5;
			}
			else{
				closePriceArray = new double [stockRecords.size()];
				firstRecordToReturn = 0;
			}
		}
		//if file contains less than 1 year of records a week, month, 3 months, 6 months or max can be selected
		else if (stockRecords.size()<250) {
			if (dateRange==125) {
				closePriceArray = new double [125];
				firstRecordToReturn = stockRecords.size()-125;
			}
			else if (dateRange==62) {
				closePriceArray = new double [62];
				firstRecordToReturn = stockRecords.size()-62;
			}
			else if (dateRange==21) {
				closePriceArray = new double [21];
				firstRecordToReturn = stockRecords.size()-21;
			}
			else if (dateRange==5) {
				closePriceArray = new double [5];
				firstRecordToReturn = stockRecords.size()-5;
			}
			else{
				closePriceArray = new double [stockRecords.size()];
				firstRecordToReturn = 0;
			}
		}
		//if file contains less than 3 years of records a week, month, 3 months. 6 months, 1 year or max can be selected
		else if (stockRecords.size()<750) {
			if (dateRange==250) {
				closePriceArray = new double [250];
				firstRecordToReturn = stockRecords.size()-250;
			}
			else if (dateRange==125) {
				closePriceArray = new double [125];
				firstRecordToReturn = stockRecords.size()-125;
			}
			else if (dateRange==62) {
				closePriceArray = new double [62];
				firstRecordToReturn = stockRecords.size()-62;
			}
			else if (dateRange==21) {
				closePriceArray = new double [21];
				firstRecordToReturn = stockRecords.size()-21;
			}
			else if (dateRange==5) {
				closePriceArray = new double [5];
				firstRecordToReturn = stockRecords.size()-5;
			}
			else{
				closePriceArray = new double [stockRecords.size()];
				firstRecordToReturn = 0;
			}
		}
		//if file contains more than 3 years of records all options can be selected
		else {
			if (dateRange==750) {
				closePriceArray = new double [750];
				firstRecordToReturn = stockRecords.size()-750;
			}
			else if (dateRange==250) {
				closePriceArray = new double [250];
				firstRecordToReturn = stockRecords.size()-250;
			}
			else if (dateRange==125) {
				closePriceArray = new double [125];
				firstRecordToReturn = stockRecords.size()-125;
			}
			else if (dateRange==62) {
				closePriceArray = new double [62];
				firstRecordToReturn = stockRecords.size()-62;
			}
			else if (dateRange==21) {
				closePriceArray = new double [21];
				firstRecordToReturn = stockRecords.size()-21;
			}
			else if (dateRange==5) {
				closePriceArray = new double [5];
				firstRecordToReturn = stockRecords.size()-5;
			}
			else{
				closePriceArray = new double [stockRecords.size()];
				firstRecordToReturn = 0;
			}
		}
		
		for(int i=firstRecordToReturn; i<stockRecords.size(); i++) {
			closePriceArray[indexOfArrayToReturn] = stockRecords.get(i).getClosePrice();
			indexOfArrayToReturn++;
		}
		return closePriceArray;
	}
	
//------------------------------------------ changed for data binding ----------------------------------- 
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
