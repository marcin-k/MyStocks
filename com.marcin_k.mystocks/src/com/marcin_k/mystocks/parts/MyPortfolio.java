package com.marcin_k.mystocks.parts;

import java.awt.Label;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.marcin_k.mystocks.handlers.NewPortfolioStockHandler;

public class MyPortfolio {
	
	/** Variables **/
	//Stocks Table
	private TableViewer viewer;
	
	//Signals Table
	private TableViewer signalViewer;
	
	//used for command handling
	@Inject
	EHandlerService handlerService;
	@Inject
	ECommandService commandService;
	
	@PostConstruct
	public void createControls( Composite parent) {
		
		parent.setLayout(new GridLayout(1, false));		
		
		//--------------------------TABLE--------------------------
		viewer = new TableViewer(parent, SWT.MULTI|SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		
		
		GridData gridLabel = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1); 
		gridLabel.minimumWidth = 160;
		table.setLayoutData( gridLabel); 
		
//		viewer.setInput(<arrayList> as input);
		ArrayList<String> test = new ArrayList<>();
		test.add("test1");
		test.add("test2");
		
		viewer.setInput(test);
		
		//creates column for stock symbol
		TableViewerColumn stockNameCol = new TableViewerColumn(viewer, SWT.None);
		stockNameCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				//<myObjectType> obj = (<myObjectType>) element;
				//return obj.someMethod();
				return "test123";
			}			
		});
		stockNameCol.getColumn().setWidth(150);
		stockNameCol.getColumn().setText("Thicker");
		
		//creates column for recommendations
		TableViewerColumn recommendationsCol = new TableViewerColumn(viewer, SWT.None);
		recommendationsCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				//<myObjectType> obj = (<myObjectType>) element;
				//return obj.someMethod();
				return "test456";
			}			
		});
		recommendationsCol.getColumn().setWidth(150);
		recommendationsCol.getColumn().setText("Change 24H");
	
		//--------------------------Button to add more stocks to list--------------------------
		Button addButton = new Button(parent, SWT.BORDER);
		addButton.setText("Add Stock");
		GridData gridLabel3 = new GridData( SWT.CENTER, SWT.CENTER, true, false); 
		gridLabel.minimumWidth = 160;
		addButton.setLayoutData( gridLabel3); 
		
		addButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				Command command = commandService.getCommand("com.marcin_k.mystocks.command.newportfoliostock");

				// check if the command is defined
				System.out.println(command.isDefined());

				// activate handler, assumption: the AboutHandler() class exists already
				handlerService.activateHandler("com.marcin_k.mystocks.command.newportfoliostock", 
				    new NewPortfolioStockHandler());

				// prepare execution of command
				ParameterizedCommand cmd =
				  commandService.createCommand("com.marcin_k.mystocks.command.newportfoliostock", null);

				// check if the command can get executed
				if (handlerService.canExecute(cmd)){
				  // execute the command
				  handlerService.executeHandler(cmd);
				}
				
			}
		});
		
		
		
		
		//--------------------------STOCK SIGNALS--------------------------
		Group signalGroup = new Group(parent, SWT.NONE);
		signalGroup.setText("Signals");
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		signalGroup.setLayout(gridLayout);

		
		
		org.eclipse.swt.widgets.Label macdLabel = new org.eclipse.swt.widgets.Label(signalGroup, SWT.NONE);
		macdLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		macdLabel.setText("MACD(12,26,9)");
		
		org.eclipse.swt.widgets.Label macdIndicator = new org.eclipse.swt.widgets.Label(signalGroup, SWT.NONE);
		macdIndicator.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
		macdIndicator.setText("----");
		//sets a colour to green
		macdIndicator.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_GREEN));

		
		
	}
}
