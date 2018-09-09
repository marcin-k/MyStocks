package com.marcin_k.mystocks.model.technical_indicators;

import com.marcin_k.mystocks.model.exceptions.NotEnoughRecordsException;

/***********************************************************************
 * The Relative Strength Index (RSI), developed by J. Welles Wilder, is 
 * a momentum oscillator that measures the speed and change of price 
 * movements. The RSI oscillates between zero and 100. Traditionally 
 * the RSI is considered over-bought when above 70 and over-sold when 
 * below 30. Signals can be generated by looking for divergences and 
 * failure swings. RSI can also be used to identify the general trend.
***********************************************************************/
public class RSI extends Indicator{
	//time frame to calculate rsi 
	private int timeFrame;
	
	private double[] closePrices;
	private double[] upwardsMovement;
	private double[] downwardsMovement;
	private double[] averageUpward;
	private double[] averageDownward;
	private double[] relativeStrength;
	private double[] rsi;
	
	//default constructor
	//The most commonly used values for RSI is 14
	public RSI(double[] closePrices) throws NotEnoughRecordsException {
		this.closePrices = closePrices;
		timeFrame = 14;
		
		if (timeFrame + 1 > closePrices.length) {
			throw new NotEnoughRecordsException("To calculate RSI, stock requires a history of minimum "+(timeFrame + 1)
					+" trading days");
		}
		setUpArrays(closePrices.length);
	}
	//TODO: replace the if in the constructor with super()
	//custom constructor when custom values are used
	public RSI(double[] closePrices, int timeFrame) throws NotEnoughRecordsException{
		this.closePrices = closePrices;
		this.timeFrame = timeFrame;
		
		if (timeFrame + 1 > closePrices.length) {
			throw new NotEnoughRecordsException("To calculate RSI, stock requires a history of minimum "+(timeFrame + 1)
					+" trading days");
		}
		setUpArrays(closePrices.length);
	}
	
	private void setUpArrays(int size) {
		upwardsMovement = new double[size];
		downwardsMovement = new double[size];
		averageUpward = new double[size];
		averageDownward = new double[size];
		relativeStrength = new double[size];
		rsi = new double[size];
		//only first elements in those arrays are zeros
		upwardsMovement[0] = 0;
		downwardsMovement[0] = 0;
		fillZeros(timeFrame, averageUpward);
		fillZeros(timeFrame, averageDownward);
		fillZeros(timeFrame, relativeStrength);
		fillZeros(timeFrame, rsi);
		
		fillArraysWithNumbers();
	}
	
	private void fillArraysWithNumbers() {
		//Upwards Movement and Downwards Movement 
		for(int i=1; i < closePrices.length; i++) {
			//upwards movement
			if(closePrices[i] > closePrices[i-1]) {
				upwardsMovement[i] = closePrices[i] - closePrices[i-1];
				downwardsMovement[i] = 0;
			}
			//downwards movement
			else if(closePrices[i] < closePrices[i-1]) {
				upwardsMovement[i] = 0;
				downwardsMovement[i] = closePrices[i-1] - closePrices[i];
			}
			else {
				upwardsMovement[i] = 0;
				downwardsMovement[i] = 0;
			}
		}
		
		//average upward || average downward
		averageUpward[timeFrame] = calculateAverage(getSubArray(upwardsMovement, 1, timeFrame+1));
		averageDownward[timeFrame] = calculateAverage(getSubArray(downwardsMovement, 1, timeFrame+1));
		System.out.println(averageDownward[timeFrame]);
		for(int i = timeFrame+1; i < closePrices.length; i++) {
			averageUpward[i] = (((averageUpward[i-1])*(timeFrame-1)+upwardsMovement[i])/timeFrame);
			averageDownward[i] = (((averageDownward[i-1])*(timeFrame-1)+downwardsMovement[i])/timeFrame);
		}

		//rs || rsi
		for(int i = timeFrame; i < closePrices.length; i++) {
			relativeStrength[i] = averageUpward[i] / averageDownward[i];
			rsi[i] = 100 - (100/(relativeStrength[i]+1));
		}
		
		
	}
}