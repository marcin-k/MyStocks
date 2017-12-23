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

//import org.swtchart.Chart;
//import org.swtchart.IAxis;
//import org.swtchart.IAxisSet;
//import org.swtchart.IBarSeries;
//import org.swtchart.ISeries.SeriesType;

import com.marcin_k.mystocks.model.IDownloadStocksService;
import com.marcin_k.mystocks.model.Stock;

import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;

public class Graph {
	
	// define DataBindingContext as field 
	DataBindingContext ctx = new DataBindingContext();
	private Stock stock;
	
	
	private String tickerString;
	Text ticker;
	
	@PostConstruct
	public void createControls( Composite parent, IDownloadStocksService downloadStocksService) {
		
		ticker = new Text(parent, SWT.BORDER);
		
		Chart chart = new Chart(parent, SWT.NONE); 
		chart.getTitle().setText("SWT Chart");
		chart.getAxisSet().getXAxis(0).getTitle().setText("Operating Systems");
		chart.getAxisSet().getYAxis(0).getTitle().setText("Love");
		IAxisSet axisSet = chart.getAxisSet();
		IAxis xAxis = axisSet.getXAxis(0);
		xAxis.setCategorySeries(new String[] { "Linux", "Windows" });
		xAxis.enableCategory(true);

		IBarSeries series = (IBarSeries) chart.getSeriesSet().createSeries(
		SeriesType.BAR, "line series");
		series.setBarColor(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
		double[] values = { 0.7, 0.2 };
		series.setYSeries(values);
		
//mac sync test 
		

		
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

