package com.marcin_k.mystocks.wizards;

import javax.inject.Inject;
import org.eclipse.jface.wizard.Wizard;

import com.marcin_k.mystocks.model.Stock;

public class StockWizard extends Wizard{

//	private String ticker; 
	boolean finish = false; 
	
	@Inject public StockWizard() { 
//		this.ticker = ticker; 
		setWindowTitle(" New Wizard"); 
	} 
	
	@Override public void addPages() { 
		addPage( new StockWizardPage1( )); 
		addPage( new StockWizardPage2()); 
	} 
	
	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public boolean canFinish() {
		return finish;
	}

}
