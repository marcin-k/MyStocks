package com.marcin_k.mystocks.model.testcases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.marcin_k.mystocks.model.exceptions.NotEnoughRecordsException;
import com.marcin_k.mystocks.model.technical_indicators.MACD;

class MACDTest {

	@Test
	void testMACDDoubleArray() throws NotEnoughRecordsException {
		double[] array = {12.0, 14.0, 14.0, 21.0, 16.0, 31.0, 84.0, 2.0, 2.0, 3.0 ,1, 16, 
				31, 84, 2 ,2, 3, 1, 74, 6, 46,
				44, 32, 46, 2, 3, 1, 16, 31, 84, 2, 2,
				3, 1, 74, 6, 46, 44};

		MACD macd = new MACD(array);
	}


}
