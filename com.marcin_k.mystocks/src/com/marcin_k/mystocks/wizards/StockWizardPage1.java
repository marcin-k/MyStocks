package com.marcin_k.mystocks.wizards;

import java.util.ArrayList;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.marcin_k.mystocks.functions.controllers.StocksController;
import com.marcin_k.mystocks.model.Stock;
import com.marcin_k.mystocks.parts.AllStocks;


public class StockWizardPage1 extends WizardPage{

	private String ticker;
	
	protected StockWizardPage1() {
		super(" page1"); 
//		this.ticker = ticker; 
		setTitle("Add stock to your portfolio"); 
		setDescription("Select the stock from the list"); 
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite( parent, SWT.NONE); 
		GridLayout layout = new GridLayout( 2, true); 
		container.setLayout( layout); 
		Label label = new Label( container, SWT.NONE); 
		label.setText("Ticker symbol: "); 
		
		Combo dropDownMenu = new Combo(container, SWT.READ_ONLY);
		String [] stringArrayOfTickerSymbols = new String[StocksController.getInstance().getAllTickers().size()];
		//{"one", "two", "three", "two", "three"};//
		for (int i=0; i<StocksController.getInstance().getAllTickers().size(); i++) {
			stringArrayOfTickerSymbols[i]=StocksController.getInstance().getAllTickers().get(i);
		}
	
			

		dropDownMenu.setItems(stringArrayOfTickerSymbols);
		// We reuse the part and 
		// inject the values 
//		Button testB = new Button(parent, SWT.NONE);
//		testB.setText("test");
//		ArrayList<Stock> getAllStocksObjects(){
		setControl( container); 
		
	}

}
