package com.marcin_k.mystocks.model.technical_indicators;

public class Indicator {
	
	//fill the arrays with 0s for the record that can not be calculated
	protected void fillZeros(int duration, double[] array) {
		for (int i=0; i<duration; i++) {
			array[i]=0;
		}
	}
	
	//returns an average from a array of numbers 
	protected double calculateAverage(double[] numbers) {
		double sum = 0;
		for(double number: numbers) {
//			System.out.println("number: "+number);
			sum += number;
//			System.out.println("calc average "	+number);
		}
//		System.out.println("calc average numbers "	+numbers.length);
		return sum/numbers.length;
	}
	
	//returns the double array, sub array of close prices with first and last position of the array
	protected double[] getSubArray(double[] referencedArray, int firstIndex, int lastIndex) {
		double[] array = new double[lastIndex-firstIndex];
		int positionInNewArray = 0;
		for(int i=firstIndex; i<lastIndex; i++) {
			array[positionInNewArray]=referencedArray[i];
//			System.out.println("getSubArray "	+array[positionInNewArray]);
			positionInNewArray++;
		}
		return array;	
	}
	
	//increments the every element in the array by the value passed in
	protected double [] incrementElements(double[] array, double increment) {
		double [] arrayToBeReturned = new double[array.length];
		for (int i=0; i<array.length; i++) {
			arrayToBeReturned[i]=array[i]+increment;
		}
		return arrayToBeReturned;
	}
}
