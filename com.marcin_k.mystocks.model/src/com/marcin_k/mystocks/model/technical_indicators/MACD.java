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
		
		fillArraysWithNumbers();
	}
	
	//fill the arrays with 0s for the record that can not be calucalted
	private void fillZeros(int duration, double[] array) {
		for (int i=0; i<duration; i++) {
			array[i]=0;
		}
	}
	
	//TODO : calculate all of the arrays 
	//TODO : get return method to get a range required
	private void fillArraysWithNumbers() {
		
		//EMAaArray
		EMAaArray[EMAa-1] = calculateAverage(getSubArray(closePrices, 0, EMAa));
		for (int i = EMAa; i< closePrices.length; i++) {
			EMAaArray[i]=closePrices[i]*(2/(EMAa+1))+EMAaArray[i-1]*(1-(2/(EMAa+1)));
		}
		
		//EMAbArray
		EMAbArray[EMAb-1] = calculateAverage(getSubArray(closePrices, 0, EMAb));
		for (int i = EMAb; i < closePrices.length; i++) {
			EMAbArray[i]=closePrices[i]*(2/(EMAb+1))+EMAaArray[i-1]*(1-(2/(EMAb+1)));
		}
		
		//MACD
		for (int i = EMAb-1; i < closePrices.length; i++) {
			MACD[i]=EMAaArray[i]-EMAbArray[i];
		}
		
		//Signal
		Signal[EMAb+EMAc-1] = calculateAverage(getSubArray(MACD, EMAb-1, EMAb-1+EMAc));
		for (int i = EMAb+EMAc; i < closePrices.length; i++) {
			Signal[i]=MACD[i]*(2/(EMAc+1))+Signal[i-1]*(1-(2/(EMAc+1)));
		}
	}
	
	//returns the double array, sub array of close prices with first and last position of the array
	private double[] getSubArray(double[] referencedArray, int firstIndex, int lastIndex) {
		double[] array = new double[lastIndex+1];
		int positionInNewArray = 0;
		for(int i=firstIndex; i<array.length; i++) {
			array[positionInNewArray]=referencedArray[i];
			positionInNewArray++;
		}
		return array;
		
	}
	
	//returns an average from a array of numbers 
	private double calculateAverage(double[] numbers) {
		double sum = 0;
		for(double number: numbers) {
			sum += number;
		}
		return sum/numbers.length;
	}
	
	//returns last number of records requested for MACD
	//controller checks if sufficient number is available based on number of 
	//close price records
	public double[] getMACD(int numberOfRecords) {
		return getSubArray(MACD, closePrices.length-1-numberOfRecords, closePrices.length-1);
	}
	
	//returns last number of records requested for Signal
	//controller checks if sufficient number is available based on number of 
	//close price records
	public double[] getSignal(int numberOfRecords) {
		return getSubArray(Signal, closePrices.length-1-numberOfRecords, closePrices.length-1);
	}
}
