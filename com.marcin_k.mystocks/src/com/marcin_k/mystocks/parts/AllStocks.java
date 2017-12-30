
package com.marcin_k.mystocks.parts;

import com.marcin_k.mystocks.functions.controllers.StocksController;
import com.marcin_k.mystocks.functions.filesHandlers.*;
import com.marcin_k.mystocks.model.Stock;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

/****************************************************************
 * AllStocks is GUI class, used to display list of all stocks
 * available in the system. 
 * Stocks are binded with Graph class to represent a stock
 * selected on the graph.
 *
 ****************************************************************/
public class AllStocks {

	/** Variables **/
	private WritableList writableList;
	private TableViewer viewer;
	protected String searchString ="";

	//access to selection service
	@Inject
	ESelectionService service;
	
//-------------------------------------------- Create Controls ---------------------------------------------------------		
	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService) {
		parent.setLayout(new GridLayout(1, false));

		// search bar:
		Text search = new Text(parent, SWT.SEARCH | SWT.CANCEL | SWT.ICON_SEARCH);
		// assuming that GridLayout is used
		search.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		search.setMessage(" Filter");
		// filter at every keystroke
		search.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				Text source = (Text) e.getSource();
				searchString = source.getText();
				// trigger update in the viewer
				viewer.refresh();
			}
		});
		// SWT.SEARCH | SWT.CANCEL is not supported under Windows7 and
		// so the following SelectionListener will not work under Windows7
		search.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent e) {
				if (e.detail == SWT.CANCEL) {
					Text text = (Text) e.getSource();
					text.setText("");
					//
				}
			}
		});

		// Table Viewer:
		viewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		TableViewerColumn column = new TableViewerColumn(viewer, SWT.None);
		column.getColumn().setWidth(120);
		column.getColumn().setText(" Ticker");
		writableList = new WritableList<>(StocksController.getInstance().getAllStocksObjects(), Stock.class);
		ViewerSupport.bind(viewer, writableList, BeanProperties.values(new String[] { Stock.TICKER }));

		viewer.addFilter(new ViewerFilter() {
			@Override
			public boolean select(Viewer viewer, Object parentElement, Object element) {
				String ticker = ((Stock) element).getTicker();
				return ticker.toUpperCase().contains(searchString.toUpperCase());
			}
		});

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