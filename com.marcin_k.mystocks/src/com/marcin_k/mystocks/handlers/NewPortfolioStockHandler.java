package com.marcin_k.mystocks.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import com.marcin_k.mystocks.wizards.StockWizard;

public class NewPortfolioStockHandler {
	@Execute public void execute( Shell shell) { 
	
		WizardDialog dialog = new WizardDialog( shell, new StockWizard()); 
		if (dialog.open() == WizardDialog.OK) { 
			// call service to save Todo object
			System.out.println("from handler");
		}
	}
}
