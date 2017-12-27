package com.marcin_k.mystocks.model;

/*************************************************************
 * Class contains the daily record of the stock performance,
 * including Ticker, date, open price, high price, low price,
 * close price, and daily volume
 * 
 *************************************************************/

public class DailyStockRecord {
	
	private String ticker;
	//date in the format YYYYMMDD
	private int date;
	private double openPrice;
	private double highPrice;
	private double lowPrice;
	private double closePrice;
	//some contracts stores a volume as an average therefore variable is defined as double 
	private double volume;
	
//-------------------------------------------- Constructor -------------------------------------------------------------
	public DailyStockRecord(String ticker, int date, double openPrice, double highPrice, double lowPrice,
			double closePrice, double volume) {
		this.ticker = ticker;
		this.date = date;
		this.openPrice = openPrice;
		this.highPrice = highPrice;
		this.lowPrice = lowPrice;
		this.closePrice = closePrice;
		this.volume = volume;
	}
	
//----------------------------------------------- Getters --------------------------------------------------------------
	public String getTicker() {
		return ticker;
	}
	public int getDate() {
		return date;
	}
	public double getOpenPrice() {
		return openPrice;
	}
	public double getHighPrice() {
		return highPrice;
	}
	public double getLowPrice() {
		return lowPrice;
	}
	public double getClosePrice() {
		return closePrice;
	}
	public double getVolume() {
		return volume;
	}
}
