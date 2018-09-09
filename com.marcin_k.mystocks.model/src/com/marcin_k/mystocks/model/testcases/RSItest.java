package com.marcin_k.mystocks.model.testcases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.marcin_k.mystocks.model.exceptions.NotEnoughRecordsException;
import com.marcin_k.mystocks.model.technical_indicators.MACD;
import com.marcin_k.mystocks.model.technical_indicators.RSI;

class RSItest {

	@Test
	void testRSIDoubleArray() throws NotEnoughRecordsException {
		double[] array = {120.7,
				120.15,
				122,
				122.95,
				122.9,
				122.6,
				124.75,
				122.35,
				121.35,
				120.65,
				120.4,
				122.05,
				119.1,
				112.15,
				110.7,
				112,
				112,
				115.5,
				114.25,
				112.7,
				113.75,
				112.2,
				109.85,
				107.4,
				106.8,
				108.5
};

		RSI rsi = new RSI(array);

	}


}