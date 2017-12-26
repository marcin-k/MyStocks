package com.marcin_k.mystocks.parts;

import org.eclipse.swt.layout.GridLayout;
import java.awt.datatransfer.FlavorListener;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;
import org.swtchart.ITitle;
import org.swtchart.LineStyle;

import com.marcin_k.mystocks.functions.download_stock_files.StocksController;
import com.marcin_k.mystocks.model.IDownloadStocksService;
import com.marcin_k.mystocks.model.Stock;




public class Graph {
	
	// define DataBindingContext as field 
	DataBindingContext ctx = new DataBindingContext();
	private Stock stock;
	
	
	private String tickerString;
	Text ticker;
	Chart chart;
	private Combo dateRangeCombo;
	//used to track if during the update different ticker was selected
	//if so range is changed to max
	private String previousTickerSymbol ="";
	
	@PostConstruct
	public void createControls( Composite parent, IDownloadStocksService downloadStocksService) {
		
		parent.setLayout(new GridLayout(1, false));

		ticker = new Text(parent, SWT.BORDER);
		ticker.setEditable(false);
		
		GridData gridLabel = new GridData( SWT.CENTER, SWT.CENTER, true, false); 
//		gridLabel.horizontalIndent = 22; 
		gridLabel.minimumWidth = 160;
		ticker.setLayoutData( gridLabel); 
		
		chart = new Chart(parent, SWT.NONE); 
		//adjusts range for all axes
		IAxisSet axisSet = chart.getAxisSet();
		axisSet.adjustRange();

		GridData gridChart = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gridChart.heightHint = 450;
		chart.setLayoutData(gridChart);
		
		//Group of elements to adjust ranges in the graph
		Group graphAdjustments = new Group(parent, SWT.NONE);
		graphAdjustments.setText("Graph Settings");
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		graphAdjustments.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 2;
		graphAdjustments.setLayoutData(gridData);
		
		Label label = new Label(graphAdjustments, SWT.NONE);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		label.setText("Date Range");
		
		//drop down menu for date range
		dateRangeCombo = new Combo(graphAdjustments, SWT.READ_ONLY);
		dateRangeCombo.add("max");
		dateRangeCombo.add("3 years");
		dateRangeCombo.add("1 year");
		dateRangeCombo.add("6 months");
		dateRangeCombo.add("3 months");
		dateRangeCombo.add("1 month");
		dateRangeCombo.add("1 week");
		dateRangeCombo.select(0);
		
		
		dateRangeCombo.addSelectionListener(new SelectionAdapter() {
		    //refreshes the graph for the date range selected, value of days passed in 
			// is based on average on number of working days in a year
			public void widgetSelected(SelectionEvent e) {
		        if (dateRangeCombo.getText().equals("max")) {
		        	updateGraph(ticker.getText(), -1);
		        } else if (dateRangeCombo.getText().equals("3 years")) {
		        	updateGraph(ticker.getText(), 750);
		        } else if (dateRangeCombo.getText().equals("1 year")) {
		        	updateGraph(ticker.getText(), 250);
		        } else if (dateRangeCombo.getText().equals("6 months")) {
		        	updateGraph(ticker.getText(), 125);
		        } else if (dateRangeCombo.getText().equals("3 months")) {
		        	updateGraph(ticker.getText(), 62);
		        } else if (dateRangeCombo.getText().equals("1 month")) {
		        	updateGraph(ticker.getText(), 21);        
		        } else {
		        	updateGraph(ticker.getText(), 5); 
		        }
		      }
		    });
		
		
//$$$$$$$$$$$$$$ TODO: Here we are $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$		
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
				
				//System.out.println(ticker.getText());
				updateGraph(ticker.getText(), -1);

			}
	}
	
	//Updates graph for a stock with a ticker passed in
	//dateRange represents number of days to display, if -1 passed all data is displayed
	private void updateGraph(String tickerSymbolString, int dateRange) {
		
		
		//update title
		ITitle graphTitle = chart.getTitle();
		graphTitle.setText(ticker.getText());
						 //returns a double array of close prices based on ticker provided
		double[] ySeries = StocksController.getInstance().getStockWithTicker(tickerSymbolString).getArrayOfClosePrices(dateRange);

		ISeriesSet seriesSet = chart.getSeriesSet();
		

//------------------------------------TESTING-------------------------------------------------------------
//		ISeries series = seriesSet.createSeries(SeriesType.LINE, "line series");
		ILineSeries series = (ILineSeries)chart.getSeriesSet().createSeries( SeriesType.LINE, "line series" );
	    series.setSymbolType(PlotSymbolType.NONE);
//	    series.setSymbolColor( getSharedColors().getColor( UsusColors.USUS_LIGHT_BLUE ) );
		
		
//----------------------------------TESTING END-----------------------------------------------------------		
		//hides the legend
		series.setVisibleInLegend(false);
		series.setYSeries(ySeries);
		IAxisSet axisSet = chart.getAxisSet();
		axisSet.adjustRange();
		

		


		IAxis xAxis = axisSet.getXAxis(0);
		IAxis yAxis = axisSet.getYAxis(0);
		ITitle xAxisTitle = xAxis.getTitle();
		ITitle yAxisTitle = yAxis.getTitle();
//TODO: change that for dates and display then vertically		
//		xAxis.setCategorySeries(new String[] { "Jan", "Feb", "Mar", "Apr", "May" });
		xAxis.enableCategory(true);
		
		
		xAxisTitle.setText("Date");
		yAxisTitle.setText("Price");
		
		//if new ticker is selected change the range to max
		if (!previousTickerSymbol.equals(tickerSymbolString)) {
			
			dateRangeCombo.select(0);
		}
					
		chart.update();
		previousTickerSymbol = tickerSymbolString;
	}
}

