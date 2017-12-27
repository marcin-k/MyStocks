package com.marcin_k.mystocks.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;

/*****************************************************
 * Class will contains a list of preselected by user
 * Stock objects, short list for easy access
 * 
 * List of stock for that list will be stored in 
 * a text file 
 * 
 *****************************************************/
public class MyStocks {
	
	@Inject
	com.marcin_k.mystocks.model.IDownloadStocksService downloadStocksService;
	
//-------------------------------------------- Create Controls ---------------------------------------------------------		
	@PostConstruct
	public void createControls( Composite parent) {
	
	}
}
