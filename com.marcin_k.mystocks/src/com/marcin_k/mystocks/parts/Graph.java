package com.marcin_k.mystocks.parts;

import javax.annotation.PostConstruct;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import com.marcin_k.mystocks.model.IDownloadStocksService;


public class Graph {
	

	
	@PostConstruct
	public void createControls( Composite parent, IDownloadStocksService downloadStocksService) {
		
		
		//temp using as web browser
		Browser browser;
		browser = new Browser(parent, SWT.NONE);
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		browser.setUrl("http://www.google.com");

		
	}
}
