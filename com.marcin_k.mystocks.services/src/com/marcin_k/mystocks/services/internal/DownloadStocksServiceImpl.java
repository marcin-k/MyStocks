package com.marcin_k.mystocks.services.internal;

import com.marcin_k.mystocks.functions.controllers.FilesController;
import com.marcin_k.mystocks.functions.controllers.MyPortfolioController;
import com.marcin_k.mystocks.model.IDownloadStocksService;

/**********************************************************
 * Class runs as a service to download stock files
 * prior to application launch.
 * 
 **********************************************************/
public class DownloadStocksServiceImpl implements IDownloadStocksService{

	// DownloadStocksServiceImpl class implements 
	// downloadStockFiles method and calls it from its 
	// constructor. 
	public DownloadStocksServiceImpl() {
		downloadStocksFiles();
	}
	
	// DownloadStockFiles method calls the getAllFiles method
	// in FilesController, which checks the time stamp in the 
	// config files and downloads and unpacks the stock files
	@Override
	public void downloadStocksFiles() {
		System.out.println("*****************************************************************\n"
				+ "******************************service running******************************\n"
				+ "***************************************************************************");
		FilesController.getInstance().getAllFiles();
		MyPortfolioController.getInstance();
		
	}
}
