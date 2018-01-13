package com.marcin_k.mystocks.functions.testcases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.marcin_k.mystocks.functions.controllers.FilesController;
import com.marcin_k.mystocks.functions.controllers.MyPortfolioController;
import com.marcin_k.mystocks.functions.filesHandlers.MyPortfolioFile;
import com.marcin_k.mystocks.model.Stock;

class MyPortfolioFileTest {

//	@Test
//	void testCreateFileArrayListOfString() {
//		ArrayList<String> testList= new ArrayList<>();
//		testList.add("one");
//		testList.add("two");
//		testList.add("three");
//		testList.add("four");
//		
//		MyPortfolioFile myPortfolioFile = new MyPortfolioFile();
//		myPortfolioFile.saveFile(testList);
//	}

//	@Test
//	void getStringFromPortfolioController() {
//		FilesController.getInstance().getAllFiles();
//		ArrayList<Stock> myStocks = MyPortfolioController.getInstance().getMyPortfolioStocks();
//		
//		for(Stock stock : myStocks) {
//			System.out.println(stock.getTicker());
//		}
//	}
	
	@Test
	void getPortfolioListTest() {
		MyPortfolioFile myPortfolioFile = new MyPortfolioFile();
		for(String str : myPortfolioFile.getList()) {
			System.out.println(str);
		}
		
	}
	
}
