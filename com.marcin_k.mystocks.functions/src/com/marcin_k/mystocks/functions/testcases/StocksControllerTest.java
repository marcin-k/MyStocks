package com.marcin_k.mystocks.functions.testcases;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.marcin_k.mystocks.functions.controllers.StocksController;

class StocksControllerTest {

//	@Test
//	void testAverage()  {
//		double[] doubleArray = { 0.6, 1.4, 4.0 };
//		assertEquals(2	, StocksController.getInstance().getAverage(doubleArray));
//	}
	
	@Test
	void testFindDecrement() {
		System.out.println(StocksController.getInstance().findDecrement(4.754, 152.8));
		System.out.println(StocksController.getInstance().findDecrement(4.754, 4.5));
		assertEquals(2, StocksController.getInstance().findDecrement(10.0, 7.5));
	}
	
	
}
