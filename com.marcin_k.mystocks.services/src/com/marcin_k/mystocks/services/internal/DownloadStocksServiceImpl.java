package com.marcin_k.mystocks.services.internal;

import com.marcin_k.mystocks.functions.download_stock_files.FilesController;
import com.marcin_k.mystocks.model.IDownloadStocksService;

public class DownloadStocksServiceImpl implements IDownloadStocksService{

	public DownloadStocksServiceImpl() {
		// TODO Auto-generated constructor stub
		downloadStocksFiles();
	}
	
	@Override
	public void downloadStocksFiles() {
		// TODO Auto-generated method stub
		System.out.println("*****************************************************************\n"
				+ "******************************service running******************************\n"
				+ "***************************************************************************");
		FilesController.getInstance().getAllFiles();
	}



}
