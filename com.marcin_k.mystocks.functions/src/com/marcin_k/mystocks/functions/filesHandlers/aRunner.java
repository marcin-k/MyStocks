package com.marcin_k.mystocks.functions.filesHandlers;

import java.io.IOException;

import com.marcin_k.mystocks.functions.controllers.FilesController;

/********************************************
 *
 * Class for testing
 *
 ********************************************/
public class aRunner {

	public static void main(String[] args) throws IOException {
		System.out.println("Testing reading stock file");
		
		//downlaods all the files and unzipps them
		FilesController.getInstance().getAllFiles();
		ReadStockFile testReader = new ReadStockFile();
		//testReader.getStockHistoryFromFile("AGROLIGA");
		FilesController.getInstance().getAllTickers();

	}

}
