package com.marcin_k.mystocks.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;

public class MyStocks {
	
	@Inject
	com.marcin_k.mystocks.model.IDownloadStocksService downloadStocksService;
	
	@PostConstruct
	public void createControls( Composite parent) {
	
	}
}
