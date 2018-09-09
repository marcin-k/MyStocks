package com.marcin_k.mystocks.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import com.marcin_k.mystocks.functions.controllers.StocksController;
import com.marcin_k.mystocks.model.Stock;

/****************************************************************
 * AllStocks is GUI class, used to display list of all stocks
 * available in the system. 
 * Stocks are binded with Graph class to represent a stock
 * selected on the graph.
 *
 ****************************************************************/
public class Wig20Side {

	/** Variables **/
	private WritableList<Stock> writableList;
	private TableViewer viewer;

	//access to selection service
	@Inject
	ESelectionService service;
	
//-------------------------------------------- Create Controls ---------------------------------------------------------		
	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService) {
		parent.setLayout(new GridLayout(1, false));

		
		// Table Viewer:
		viewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		TableViewerColumn column = new TableViewerColumn(viewer, SWT.None);
		column.getColumn().setWidth(120);
		writableList = new WritableList<>(StocksController.getInstance().getWig20StocksObjects(), Stock.class);
		ViewerSupport.bind(viewer, writableList, BeanProperties.values(new String[] { Stock.TICKER }));


		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				// TODO Auto-generated method stub
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				service.setSelection(selection.getFirstElement());

			}
		});
	}
}