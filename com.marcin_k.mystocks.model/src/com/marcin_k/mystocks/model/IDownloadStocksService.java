package com.marcin_k.mystocks.model;
/***********************************************************
 * Interface defines the method of IDownloadStocksService,
 * implementation should prepare stock records prior
 * to application start 
 * 
 ***********************************************************/

public interface IDownloadStocksService {
	//Downloads and prepare the stocks files
	public void downloadStocksFiles();
}
