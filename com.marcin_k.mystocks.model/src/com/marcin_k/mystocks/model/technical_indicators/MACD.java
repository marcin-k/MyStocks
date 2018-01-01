package com.marcin_k.mystocks.model.technical_indicators;

import com.marcin_k.mystocks.model.exceptions.NotEnoughRecordsException;

/***********************************************************************
 * MACD, short for moving average convergence/divergence, is a trading 
 * indicator used in technical analysis of stock prices, created by 
 * Gerald Appel in the late 1970s.[1] It is supposed to reveal changes 
 * in the strength, direction, momentum, and duration of a trend in 
 * a stock's price.
 * 
 * The MACD indicator (or "oscillator") is a collection of three time 
 * series calculated from historical price data, most often the closing 
 * price. These three series are: the MACD series proper, the "signal" 
 * or "average" series, and the "divergence" series which is the 
 * difference between the two. The MACD series is the difference between 
 * a "fast" (short period) exponential moving average (EMA), and a 
 * "slow" (longer period) EMA of the price series. 
 * The average series is an EMA of the MACD series itself.
 * 
 ************************************************************************/
public class MACD {
	//The MACD indicator thus depends on three time parameters, namely 
	//the time constants of the three EMAs. The notation "MACD(a,b,c)"
	private int EMAa;
	private int EMAb;
	private int EMAc;
	
	private double[] closePrices;
	private double[] EMAaArray;
	private double[] EMAbArray;
	private double[] MACD;
	private double[] Signal;
	
	//default constructor
	//The most commonly used values for MACD are 12, 26, and 9 days
	public MACD(double[] closePrices) throws NotEnoughRecordsException {
		this.closePrices = closePrices;
		EMAa = 12;
		EMAb = 26;
		EMAc = 9;
		
		if (EMAb + EMAc - 1 > closePrices.length) {
			throw new NotEnoughRecordsException("To calculate MACD stock requires a history of minimum "+(EMAb + EMAc 
					- 1)+"t rading days");
		}
		setUpArrays(closePrices.length);
	}
	
	//custom constructor when custom values are used
	public MACD(double[] closePrices, int EMAa, int EMAb, int EMAc) throws NotEnoughRecordsException{
		this.closePrices = closePrices;
		this.EMAa = EMAa;
		this.EMAb = EMAb;
		this.EMAc = EMAc;
		
		if (EMAb + EMAc - 1 > closePrices.length) {
			throw new NotEnoughRecordsException("To calculate MACD stock requires a history of minimum "+(EMAb + 
					EMAc - 1)+"t rading days");
		}
		setUpArrays(closePrices.length);
	}
	
	private void setUpArrays(int size) {
		EMAaArray = new double[size];
		EMAbArray = new double[size];
		MACD = new double[size];
		Signal = new double[size];
		
		fillZeros(EMAa-1, EMAaArray);
		fillZeros(EMAb-1, EMAbArray);
		fillZeros(EMAb-1, MACD);
		fillZeros((EMAb+EMAc-1), Signal);
	}
	
	//fill the arrays with 0s for the record that can not be calucalted
	private void fillZeros(int duration, double[] array) {
		for (int i=0; i<duration; i++) {
			array[i]=0;
		}
	}
	
	//TODO : calculate all of the arrays 
	//TODO : get return method to get a range required
	
}
