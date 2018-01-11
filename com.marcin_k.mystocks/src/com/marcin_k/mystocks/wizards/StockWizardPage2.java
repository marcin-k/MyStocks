package com.marcin_k.mystocks.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;


public class StockWizardPage2 extends WizardPage{

	private boolean checked = false;

	/** * create the wizard. */ 
	public StockWizardPage2() { 
		super(" wizardPage"); 
		setTitle(" Validate"); 
		setDescription(" Check to create the todo item"); 
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
		Label label = new Label( container, SWT.NONE); 
		label.setText(" Create the todo"); 
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
	} 
	
	public boolean isChecked() { 
		return checked; 
	} 

}
