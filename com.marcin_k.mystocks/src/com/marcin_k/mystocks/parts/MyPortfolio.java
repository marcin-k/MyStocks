package com.marcin_k.mystocks.parts;

import java.awt.Label;
import java.util.ArrayList;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.commands.Command;
import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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

import com.marcin_k.mystocks.events.MyEventConstants;
import com.marcin_k.mystocks.functions.controllers.MyPortfolioController;
import com.marcin_k.mystocks.handlers.NewPortfolioStockHandler;
import com.marcin_k.mystocks.model.Stock;


public class MyPortfolio {
	
	/** Variables **/
	
	//Signals Table
	private TableViewer signalViewer;
	
	//used for command handling
	@Inject
	EHandlerService handlerService;
	@Inject
	ECommandService commandService;
	

	private TableViewer viewer;
	
	//event listener
	@Inject
	@Optional
	private void subscribeTopicTodoAllTopics
	  (@UIEventTopic(MyEventConstants.TOPIC_STOCKS_MYPORTFOLIO) Map<String, String> event) {
	  if (viewer != null) {
		  viewer.setInput(MyPortfolioController.getInstance().getMyPortfolioStocks());
		  System.out.println("refreshing list");
		  
	  }
	} 
	
	@PostConstruct
	public void createControls( Composite parent) {
		parent.setLayout(new GridLayout(1, false));		
		
		//--------------------------TABLE--------------------------
		viewer = new TableViewer(parent, SWT.MULTI|SWT.FULL_SELECTION);
		
		GridData tableGrid = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		tableGrid.heightHint = 150;
		
		
	    Table table = viewer.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    table.setLayoutData(tableGrid);
	    viewer.setContentProvider(ArrayContentProvider.getInstance());
		
//--------------------------------------------TESTING-------------------------------------------------------------------		
		
		    // create column for the summary property
		    TableViewerColumn colSummary = new TableViewerColumn(viewer, SWT.NONE);
		    colSummary.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		        Stock todo = (Stock) element;
		        return todo.getTicker();
		      }
		    });
		    colSummary.getColumn().setWidth(100);
		    colSummary.getColumn().setText("Summary");

		    // create column for description property
		    TableViewerColumn colDescription = new TableViewerColumn(viewer, SWT.NONE);
		    colDescription.setLabelProvider(new ColumnLabelProvider() {
		      @Override
		      public String getText(Object element) {
		        Stock todo = (Stock) element;
		        return todo.getTicker();
		      }
		    });
		    colDescription.getColumn().setWidth(200);
		    colDescription.getColumn().setText("Description");

		    // initially the table is also filled
		    // the button is used to update the data if the model changes
//		    viewer.setInput(ArrayContentProvider.getInstance());
		    viewer.setInput(MyPortfolioController.getInstance().getMyPortfolioStocks());
		
		    
		    // calling the command to add new stock
			Button addStockButton = new Button(parent, SWT.NONE);
			addStockButton.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, false));
			addStockButton.setText("Add Stock to Portfolio");
			
			addStockButton.addSelectionListener(new SelectionAdapter() {
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
		    
		    	
//---------------------------------------------TESTING------------------------------------------------------------------		
		
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

	    viewer.addSelectionChangedListener(new ISelectionChangedListener() {
	    	  @Override
	    	  public void selectionChanged(SelectionChangedEvent event) {
	    	    IStructuredSelection selection = viewer.getStructuredSelection();
	    	    Stock firstElement = (Stock)selection.getFirstElement();
	    	    macdIndicator.setText(firstElement.getTicker());
	    	  }
	    }); 
		
	}
}
