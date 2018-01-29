package com.marcin_k.mystocks.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.marcin_k.mystocks.functions.controllers.MyPortfolioController;
import com.marcin_k.mystocks.functions.controllers.StocksController;
import com.marcin_k.mystocks.model.Stock;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class StockWizardPage1 extends WizardPage{

	private boolean checked = false;
	public int testInt = 0;

	/** * create the wizard. */ 
	public StockWizardPage1() { 
		super(" wizardPage"); 
		setTitle(" Validate"); 
		setDescription(" Check to add stock"); 
		}
	

	/** 
	 * create contents of the wizard. 
	 * 
	 * @param parent 
	 */ 
	public void createControl( Composite parent) { 
		Composite container = new Composite( parent, SWT.NONE); 
		GridLayout layout = new GridLayout( 2, true); 
		container.setLayout( layout); 
		
		//--------------------------------NEW----------------------------
		
		Label label = new Label( container, SWT.NONE); 
		label.setText("Ticker symbol: "); 
		
//		Combo dropDownMenu = new Combo(container, SWT.READ_ONLY);
//		String [] stringArrayOfTickerSymbols = new String[StocksController.getInstance().getAllStocksObjects().size()];
//		//{"one", "two", "three", "two", "three"};//
//		for (int i=0; i<StocksController.getInstance().getAllStocksObjects().size(); i++) {
//			stringArrayOfTickerSymbols[i]=StocksController.getInstance().getAllStocksObjects().get(i).getTicker();
//		}
//		dropDownMenu.setItems(stringArrayOfTickerSymbols);
		// We reuse the part and 
		// inject the values 
//		Button testB = new Button(parent, SWT.NONE);
//		testB.setText("test");
//		ArrayList<Stock> getAllStocksObjects(){
		setPageComplete(false);
		Text ticker = new Text(container, SWT.NONE);
		Button checkNameButton = new Button(container, SWT.NONE);
		checkNameButton.setText("Check name");
		Label checkStatus = new Label(container, SWT.NONE);
		checkStatus.setText("----------------------");
		
		
		Label label3 = new Label( container, SWT.NONE); 
		label3.setText(" Add stock to the list"); 
		Button button = new Button( container, SWT.CHECK);
		button.setText(" Check"); 
		button.addSelectionListener( new SelectionAdapter() { 
			@Override public void widgetSelected( SelectionEvent e) { 
				checked = (( Button) e.getSource()). getSelection(); 
				StockWizard wizard = (StockWizard) getWizard(); 
				wizard.finish = checked; 
				// the following code updates the 
				// buttons in the wizard 
				wizard.getContainer(). updateButtons(); 
				} 
			}); 
		setControl( container); 
		
		button.setEnabled(false);
		
		checkNameButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				if (checkIfNameIsCorrect(ticker.getText())) {
					checkStatus.setText("name correct");
					MyPortfolioController.getInstance().setTempTicker(ticker.getText());
					//Wizard")getWizard()).getPages()
					button.setEnabled(true);
				}
				else {
					checkStatus.setText("name incorrect");
					button.setEnabled(false);
				}

			}
		});
	} 
	
	private boolean checkIfNameIsCorrect(String ticker) {
		for(Stock stock : StocksController.getInstance().getAllStocksObjects()) {
			if (ticker.equalsIgnoreCase(stock.getTicker())) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isChecked() { 
		return checked; 
	} 

}
