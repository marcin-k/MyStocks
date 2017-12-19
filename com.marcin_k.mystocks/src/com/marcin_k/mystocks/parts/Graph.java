package com.marcin_k.mystocks.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.marcin_k.mystocks.model.IDownloadStocksService;
import com.marcin_k.mystocks.model.Stock;


public class Graph {
	
	// define DataBindingContext as field 
	DataBindingContext ctx = new DataBindingContext();
	private Stock stock;
	
	
	private String tickerString;
	Text ticker;
	
	@PostConstruct
	public void createControls( Composite parent, IDownloadStocksService downloadStocksService) {
		
		ticker = new Text(parent, SWT.BORDER);
		

		
//		//temp using as web browser
//		Browser browser;
//		browser = new Browser(parent, SWT.NONE);
//		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
//		browser.setUrl("http://www.google.com");	
	}
	
	//called to set object from other class
	@Inject
	public void setStock(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Stock stock){
		
		if (stock != null) { 
			// remember todo as field 
			this.stock = stock; 
			} 
		// update the user interface 
		updateUserInterface( stock); 
	}
	
	private void updateUserInterface( Stock stock) { 
			if(ticker !=null && !ticker.isDisposed()) {
				// ctx is defined as field in the class!!! 
				// disposes existing bindings 
				ctx.dispose();
				// summary 
				IObservableValue oWidgetTicker = WidgetProperties.text( SWT.Modify).observe( ticker);
				IObservableValue oStockTicker = BeanProperties.value( Stock.TICKER).observe( stock);
				ctx.bindValue( oWidgetTicker, oStockTicker);
			}
	}

}

