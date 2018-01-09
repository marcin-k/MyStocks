package com.marcin_k.mystocks.parts;

import org.eclipse.swt.layout.GridLayout;

import java.util.Arrays;

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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.swtchart.Chart;
import org.swtchart.IAxis;
import org.swtchart.IAxisSet;
import org.swtchart.IBarSeries;
import org.swtchart.ILineSeries;
import org.swtchart.ILineSeries.PlotSymbolType;
import org.swtchart.ISeries.SeriesType;
import org.swtchart.ISeriesSet;
import org.swtchart.ITitle;
import org.swtchart.Range;

import com.marcin_k.mystocks.functions.controllers.StocksController;
import com.marcin_k.mystocks.model.IDownloadStocksService;
import com.marcin_k.mystocks.model.Stock;
import com.marcin_k.mystocks.model.StockComponent;
import com.marcin_k.mystocks.model.exceptions.NotEnoughRecordsException;

/****************************************************************
 * GUI class, used to display a stock chart.
 * Class will allow user to define a time frame,
 * and technical analysis indicator on the diagram. 
 *
 ****************************************************************/
public class Graph {
	
	/** Variables **/
	// define DataBindingContext as field 
	DataBindingContext ctx = new DataBindingContext();
	private Stock stock;
	private String tickerString;
	Text ticker;
	Chart priceChart;
	Chart volumeChart;
	IBarSeries barSeries;
	IAxisSet axisSetPrice;
	IAxisSet axisSetVolume;
	private Combo dateRangeCombo;
	//used to track if during the update different ticker was selected
	//if so range is changed to max
	private String previousTickerSymbol ="";
	Button macdButton;
	private int dateRange;
	
	//MACD line series
	ILineSeries macdSeries;
	ILineSeries macdSignalSeries;
	
	
//-------------------------------------------- Create Controls ---------------------------------------------------------	
	@PostConstruct
	public void createControls( Composite parent, IDownloadStocksService downloadStocksService) {
		parent.setLayout(new GridLayout(1, false));

		//LABEL
		ticker = new Text(parent, SWT.BORDER);
		ticker.setEditable(false);
		GridData gridLabel = new GridData( SWT.CENTER, SWT.CENTER, true, false); 
		gridLabel.minimumWidth = 160;
		ticker.setLayoutData( gridLabel); 
		
		//Price CHART
		priceChart = new Chart(parent, SWT.NONE); 
		GridData gridPriceChart = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gridPriceChart.heightHint = 400;
		priceChart.setLayoutData(gridPriceChart);
		axisSetPrice = priceChart.getAxisSet();
		IAxis xAxis = axisSetPrice.getXAxis(0);
		IAxis yAxis = axisSetPrice.getYAxis(0);
		ITitle xAxisTitle = xAxis.getTitle();
		ITitle yAxisTitle = yAxis.getTitle();
		yAxisTitle.setText("Price");
		xAxisTitle.setVisible(false);
		xAxis.enableCategory(true);
		
		//Volume CHART
		volumeChart = new Chart(parent, SWT.NONE); 
		GridData volumeGridChart = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		volumeGridChart.heightHint = 175;
		volumeChart.setLayoutData(volumeGridChart);
		barSeries = (IBarSeries) volumeChart.getSeriesSet().createSeries(SeriesType.BAR, "bar series");
		barSeries.setVisibleInLegend(false);
		ITitle volumeTitle = volumeChart.getTitle();
		volumeTitle.setVisible(false);
		axisSetVolume = volumeChart.getAxisSet();
		IAxis xAxisVolume = axisSetVolume.getXAxis(0);
		IAxis yAxisVolume = axisSetVolume.getYAxis(0);
		ITitle xAxisVolumeTitle = xAxisVolume.getTitle();
		ITitle yAxisVolumeTitle = yAxisVolume.getTitle();
		xAxisVolumeTitle.setText("Date");
		yAxisVolumeTitle.setText("Volume");
		
		
		//CHART SETTINGS
		//Group of elements to adjust ranges in the graph
		Group graphAdjustments = new Group(parent, SWT.NONE);
		graphAdjustments.setText("Graph Settings");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		graphAdjustments.setLayout(gridLayout);
		
		GridData gridData = new GridData(GridData.FILL, GridData.CENTER, true, false);
		gridData.horizontalSpan = 2;
		graphAdjustments.setLayoutData(gridData);
		
		Label rangeLabel = new Label(graphAdjustments, SWT.NONE);
		rangeLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		rangeLabel.setText("Date Range");
		
		
		
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
			// refreshes the graph for the date range selected, value of days passed in
			// is based on average on number of working days in a year
			public void widgetSelected(SelectionEvent e) {
				if (dateRangeCombo.getText().equals("max")) {
					updateGraph(ticker.getText(), -1);
					dateRange = -1;
				} else if (dateRangeCombo.getText().equals("3 years")) {
					updateGraph(ticker.getText(), 750);
					dateRange = 750;
				} else if (dateRangeCombo.getText().equals("1 year")) {
					updateGraph(ticker.getText(), 250);
					dateRange = 250;
				} else if (dateRangeCombo.getText().equals("6 months")) {
					updateGraph(ticker.getText(), 125);
					dateRange = 125;
				} else if (dateRangeCombo.getText().equals("3 months")) {
					updateGraph(ticker.getText(), 62);
					dateRange = 62;
				} else if (dateRangeCombo.getText().equals("1 month")) {
					updateGraph(ticker.getText(), 21);
					dateRange = 21;
				} else {
					updateGraph(ticker.getText(), 5);
					dateRange = 5;
				}
			}
		});
		
		Label macdLabel = new Label(graphAdjustments, SWT.NONE);
		macdLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		macdLabel.setText("MACD");
		
		macdButton = new Button(graphAdjustments, SWT.CHECK);
		macdButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		
		macdButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (!ticker.getText().equals("")) {
					updateGraph(ticker.getText(), dateRange);
				}
			}
		});
	}
	
//------------------------------- Called to set object from other class ------------------------------------------------	
	@Inject
	public void setStock(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Stock stock){
		
		if (stock != null) { 
			// remember todo as field 
			this.stock = stock; 
			} 
		// update the user interface 
		updateUserInterface( stock); 
	}
	
//--------------------------- Updates interface calls update graph method ----------------------------------------------	
	private void updateUserInterface( Stock stock) { 
		if(ticker !=null && !ticker.isDisposed()) {
			// ctx is defined as field in the class!!! 
			// disposes existing bindings 
			ctx.dispose();
			// summary 
			IObservableValue oWidgetTicker = WidgetProperties.text( SWT.Modify).observe( ticker);
			IObservableValue oStockTicker = BeanProperties.value( Stock.TICKER).observe( stock);
			ctx.bindValue( oWidgetTicker, oStockTicker);
			//update the graph	
			updateGraph(ticker.getText(), -1);
		}
	}
	
//----------------------------------------- Updates graph --------------------------------------------------------------	
	//Updates graph for a stock with a ticker passed in
	//dateRange represents number of days to display, if -1 passed all data is displayed
	private void updateGraph(String tickerSymbolString, int dateRange) {
		try {
		//Price CHART
		//update title
		ITitle graphTitle = priceChart.getTitle();
		graphTitle.setText(tickerSymbolString);
						 //returns a double array of close prices based on ticker provided
		double[] closePrices = StocksController.getInstance().getArrayOfValues(dateRange, 
				StockComponent.CLOSE_PRICE, tickerSymbolString);
		ISeriesSet seriesSet = priceChart.getSeriesSet();
		ILineSeries series = (ILineSeries)priceChart.getSeriesSet().createSeries( SeriesType.LINE, "line series" );
	    series.setSymbolType(PlotSymbolType.NONE);
		//hides the legend
		series.setVisibleInLegend(false);
		series.setYSeries(closePrices);
		axisSetPrice.adjustRange();
		
		//Volume CHART
		double[] volume = StocksController.getInstance().getArrayOfValues(dateRange, 
				StockComponent.VOLUME	, tickerSymbolString);
		
		
		IAxisSet volumeAxisSet = volumeChart.getAxisSet();
		barSeries.setYSeries(volume);
		volumeAxisSet.adjustRange();
		
		}catch (NotEnoughRecordsException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

		
//TODO: change that for dates and display then vertically		
//		xAxis.setCategorySeries(new String[] { "Jan", "Feb", "Mar", "Apr", "May" });
		
		
		//if new ticker is selected change the range to max
		if (!previousTickerSymbol.equals(tickerSymbolString)) {
			dateRangeCombo.select(0);
			macdButton.setSelection(false);
		}
		
//TODO: need to find more elegant way to create those series
//		if the series are not used the below doesnt need to be executed 
		
		macdSeries = (ILineSeries) priceChart.getSeriesSet().createSeries(SeriesType.LINE,
				"macd line series");
		macdSignalSeries = (ILineSeries) priceChart.getSeriesSet().createSeries(SeriesType.LINE,
				"macd-signal line series");
		if (macdButton.getSelection()) {
			try {
				drawMACD(tickerSymbolString, dateRange);
			} catch (NotEnoughRecordsException e) {
				// TODO Auto-generated catch block
				System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
				e.printStackTrace();
				priceChart.getSeriesSet().deleteSeries("macd line series");
				priceChart.getSeriesSet().deleteSeries("macd-signal line series");
			}
		}
		else {
			priceChart.getSeriesSet().deleteSeries("macd line series");
			priceChart.getSeriesSet().deleteSeries("macd-signal line series");
		}
		
		priceChart.redraw();
		volumeChart.redraw();
		previousTickerSymbol = tickerSymbolString;
	}
	
//------------------------------------- Draw MACD on the graph ---------------------------------------------------------
	
	private void drawMACD(String tickerString, int dateRange) throws NotEnoughRecordsException {

			double[] macdLine = StocksController.getInstance().getArrayOfValues(dateRange, StockComponent.MACD, tickerString);
			
	//		System.out.println(macdLine[0]);
	//		for(double d: macdLine) {
	//			System.out.println("whats comes back from controller: "+d);
	//		}
			
	
			Color color = new Color(Display.getDefault(), 0, 153, 255);
			macdSeries.setLineColor(color);
			macdSeries.setSymbolType(PlotSymbolType.NONE);
			// hides the legend
			macdSeries.setVisibleInLegend(false);
			macdSeries.setYSeries(macdLine);
	
			double[] macdSignalLine = StocksController.getInstance().getArrayOfValues(dateRange, StockComponent.MACD_SIGNAL,
					tickerString);
	
			Color color2 = new Color(Display.getDefault(), 255, 0, 0);
			macdSignalSeries.setLineColor(color2);
			macdSignalSeries.setSymbolType(PlotSymbolType.NONE);
			// hides the legend
			macdSignalSeries.setVisibleInLegend(false);
			macdSignalSeries.setYSeries(macdSignalLine);
	
			
			
			axisSetPrice.adjustRange();

	}
	
}

