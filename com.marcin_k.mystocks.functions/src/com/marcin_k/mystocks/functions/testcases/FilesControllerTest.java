package com.marcin_k.mystocks.functions.testcases;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.marcin_k.mystocks.functions.controllers.FilesController;
import com.marcin_k.mystocks.functions.filesHandlers.ReadStockFile;

class FilesControllerTest {

//	@Test
//	void readFile() {
//		System.out.println("Testing reading stock file");
//		
//		//downlaods all the files and unzipps them
//		FilesController.getInstance().getAllFiles();
//		ReadStockFile testReader = new ReadStockFile();
//		//testReader.getStockHistoryFromFile("AGROLIGA");
//		FilesController.getInstance().getAllTickers();
//	}
	
	@Test
	void testDeletingFiles() {
		FilesController.getInstance().deleteUnusedFiles("INTL,BPHFI,RC,DB,TRIGONPP,UCEX");
	}

}
