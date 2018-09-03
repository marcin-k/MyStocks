package com.marcin_k.mystocks.functions.controllers;


import java.util.ArrayList;

import com.marcin_k.mystocks.functions.filesHandlers.ReadStockFile;
import com.marcin_k.mystocks.model.DailyStockRecord;
import com.marcin_k.mystocks.model.Stock;
import com.marcin_k.mystocks.model.StockComponent;
import com.marcin_k.mystocks.model.exceptions.NotEnoughRecordsException;
import com.marcin_k.mystocks.model.technical_indicators.MACD;




/***********************************************************
 * Controller utilise the Singleton design pattern
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
	//stores a String array of tickers
	private ArrayList<String> tickersArray;
	
	//divider for volume
	private int volumeDivider;
	
	//to maintain one object for MACD and MACD_SINGAL
	private MACD macd;
	
//------------------------------------------------ Constructor ---------------------------------------------------------
	private StocksController() {
	
		allStocks = new ArrayList<Stock>();
		
		tickersArray = new ArrayList<>();
		int count =0;
		for(Stock stock : allStocks) {
			System.out.println("setting up array of strings "+stock.getTicker());
			tickersArray.set(count, stock.getTicker());
			count++;
		}
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
	
	public ArrayList<String> getAllTickers(){
		return tickersArray;
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
	
	public int getVolumeDivider() {
		return this.volumeDivider;
	}
//----------------------------------------- Arrays of values for a graph -----------------------------------------------	
	//next method?
	//returns a double array of close prices
	//dateRange defines how many records to return
	//if -1 is passed in all records are returned
	public double[] getArrayOfValues(int dateRange, StockComponent valueType, String tickerSymbolString) throws 
	NotEnoughRecordsException {
		
		double [] arrayToReturn;
		int firstRecordToReturn = 0;
		int indexOfArrayToReturn = 0;
		
		arrayToReturn = new double[getArraySize(dateRange,tickerSymbolString)];
		
		// if the size of the array is the same as array list of objects
		// method will return all results so value remains as 0
		// otherwise position of first record is its length away from the end
		if (arrayToReturn.length != getStockWithTicker(tickerSymbolString).getDailyRecords().size()) {
			firstRecordToReturn = getStockWithTicker(tickerSymbolString).getDailyRecords().size() - 
					arrayToReturn.length;
		}
		if (valueType == StockComponent.MACD) {

				
//				for (double d : getArrayOfValues(-1, StockComponent.CLOSE_PRICE, tickerSymbolString)) {
//					System.out.println("what is being created as object: " + d);
//				}
				macd = new MACD(getArrayOfValues(-1, StockComponent.CLOSE_PRICE, tickerSymbolString));
//				System.out.println("date range_ "+dateRange);
//				for(double d: macd.getMACD(dateRange)) {
//					System.out.println("whats controller sees: "+d);
//				}
				
				//lowest value is passed in to move the macd diagram to align within the same range as the price
				//of the stock
				return macd.getMACD(dateRange, getLowestValue(getArrayOfValues(dateRange, StockComponent.CLOSE_PRICE, 
						tickerSymbolString)));

		}
		if (valueType == StockComponent.MACD_SIGNAL) {
			//lowest value is passed in to move the macd diagram to align within the same range as the price
			//of the stock
			return macd.getSignal(dateRange, getLowestValue(getArrayOfValues(dateRange, StockComponent.CLOSE_PRICE, 
					tickerSymbolString)));
		}
		
		for(int i=firstRecordToReturn; i<getStockWithTicker(tickerSymbolString).getDailyRecords().size(); i++) {
			if (valueType == StockComponent.CLOSE_PRICE) {
				arrayToReturn[indexOfArrayToReturn] = getStockWithTicker(tickerSymbolString).getDailyRecords().
						get(i).getClosePrice();
			}
			else if (valueType == StockComponent.VOLUME) {
				arrayToReturn[indexOfArrayToReturn] = getStockWithTicker(tickerSymbolString).getDailyRecords().
						get(i).getVolume();
			}
			
			indexOfArrayToReturn++;
		}
		
		// if volume is returned values of the volume are adjusted 
		// so the two charts align the same way below one another
		if (valueType == StockComponent.VOLUME) {
			double highestPrice = getHighestValue(StocksController.getInstance().getArrayOfValues(dateRange, 
					StockComponent.CLOSE_PRICE, tickerSymbolString));
			double highestVolume = getHighestValue(arrayToReturn);
			int divider = findDecrement(highestPrice, highestVolume);
			
			if (divider > 1) {
				double[] decrementedArray = new double[arrayToReturn.length];
				
				for (int i=0; i<arrayToReturn.length; i++) {
					decrementedArray[i]=arrayToReturn[i]/divider;
				}
				return decrementedArray;
			}	
		}
		return arrayToReturn;
	}

//---------------------------------------- Figures out decrement multiplier --------------------------------------------	
	// find out value to decrement the volume to fit below the
	// price on the diagram
	public int findDecrement(double highPrice, double highVolume) {
		System.out.println("high volume "+highVolume);
		int numberToDevideVolume = 1;
		while (highVolume > roundUpNumber(highPrice)) {
			numberToDevideVolume *= 10;
			highVolume /= 10;

		}
		volumeDivider = numberToDevideVolume;
		return numberToDevideVolume;
	}
			
//-------------------------------------------- Rounds up number --------------------------------------------------------
	// round up the number up to make sure the volume and price have the same amount of 
	// 0's on the diagram
	private double roundUpNumber(double number) {
		double roundedNumber = 0;
		if (number >= 100000) {
			roundedNumber=999999;
		}
		else if (number >= 10000) {
			roundedNumber=99999;
		}
		else if (number >= 1000) {
			roundedNumber=9999;
		}
		else if (number >= 100) {
			roundedNumber=999;
		}
		else if (number >= 10) {
			roundedNumber=99;
		}
		else {
			roundedNumber=9;
		}
		return roundedNumber;
	}
	
//------------------------------------------------- Highest for a range ------------------------------------------------	
	//returns an highest price/volume for a range passed in
	private double getHighestValue(double[] array) {
		double highest = 0;
		for(double value: array) {
			if (value > highest) {
				highest = value;
			}
		}
		return highest;
	}
	
//-------------------------------------------------- Lowest for a range ------------------------------------------------	
	//returns the lowest price/volume for a range passed in
	private double getLowestValue(double[] array) {
			double lowest = array[0];
			for(double value: array) {
				if (value < lowest) {
					lowest = value;
				}
			}
			return lowest;
		}
			
//------------------------------------------- Helper method for getters method -----------------------------------------
	// Method helps getters methods which returns arrays of double values to figure out
	// size of the array 
	private int getArraySize(int dateRange, String tickerSymbolString) {
		int arraySize = 0;
				
		// if file contains less than 5 days of records all records are returned
		if (getStockWithTicker(tickerSymbolString).getDailyRecords().size() < 5) {
			arraySize = getStockWithTicker(tickerSymbolString).getDailyRecords().size();
		}
		// if file contains less than 1 month of records a week or max can be selected
		else if (getStockWithTicker(tickerSymbolString).getDailyRecords().size() < 21) {
			if (dateRange == 5) {
				arraySize = 5;
			} else {
				arraySize = getStockWithTicker(tickerSymbolString).getDailyRecords().size();
			}
		}
		// if file contains less than 3 months of records a week, month or max can be
		// selected
		else if (getStockWithTicker(tickerSymbolString).getDailyRecords().size() < 62) {
			if (dateRange == 21) {
				arraySize = 21;
			} else if (dateRange == 5) {
				arraySize = 5;
			} else {
				arraySize = getStockWithTicker(tickerSymbolString).getDailyRecords().size();
			}
		}
		// if file contains less than 6 months of records a week, month, 3 months or max
		// can be selected
		else if (getStockWithTicker(tickerSymbolString).getDailyRecords().size() < 125) {
			if (dateRange == 62) {
				arraySize = 62;
			} else if (dateRange == 21) {
				arraySize = 21;
			} else if (dateRange == 5) {
				arraySize = 5;
			} else {
				arraySize = getStockWithTicker(tickerSymbolString).getDailyRecords().size();
			}
		}
		// if file contains less than 1 year of records a week, month, 3 months, 6
		// months or max can be selected
		else if (getStockWithTicker(tickerSymbolString).getDailyRecords().size() < 250) {
			if (dateRange == 125) {
				arraySize = 125;
			} else if (dateRange == 62) {
				arraySize = 62;
			} else if (dateRange == 21) {
				arraySize = 21;
			} else if (dateRange == 5) {
				arraySize = 5;
			} else {
				arraySize = getStockWithTicker(tickerSymbolString).getDailyRecords().size();
			}
		}
		// if file contains less than 3 years of records a week, month, 3 months. 6
		// months, 1 year
		// or max can be selected
		else if (getStockWithTicker(tickerSymbolString).getDailyRecords().size() < 750) {
			if (dateRange == 250) {
				arraySize = 250;
			} else if (dateRange == 125) {
				arraySize = 125;
			} else if (dateRange == 62) {
				arraySize = 62;
			} else if (dateRange == 21) {
				arraySize = 21;
			} else if (dateRange == 5) {
				arraySize = 5;
			} else {
				arraySize = getStockWithTicker(tickerSymbolString).getDailyRecords().size();
			}
		}
		// if file contains more than 3 years of records all options can be selected
		else {
			if (dateRange == 750) {
				arraySize = 750;
			} else if (dateRange == 250) {
				arraySize = 250;
			} else if (dateRange == 125) {
				arraySize = 125;
			} else if (dateRange == 62) {
				arraySize = 62;
			} else if (dateRange == 21) {
				arraySize = 21;
			} else if (dateRange == 5) {
				arraySize = 5;
			} else {
				arraySize = getStockWithTicker(tickerSymbolString).getDailyRecords().size();
			}
		}
		return arraySize;
	}
	
	
}
