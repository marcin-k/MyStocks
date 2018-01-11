package com.marcin_k.mystocks.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


import com.marcin_k.mystocks.parts.AllStocks;

public class StockWizardPage1 extends WizardPage{

	private String ticker;
	
	protected StockWizardPage1() {
		super(" page1"); 
//		this.ticker = ticker; 
		setTitle(" New Todo"); 
		setDescription(" Enter the todo data"); 
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite( parent, SWT.NONE); 
		// We reuse the part and 
		// inject the values 
		AllStocks part = new AllStocks(); 

		setControl( container); 
		
	}

}
