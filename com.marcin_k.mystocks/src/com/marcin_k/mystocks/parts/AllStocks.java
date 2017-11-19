
package com.marcin_k.mystocks.parts;

import com.marcin_k.mystocks.functions.download_stock_files.*;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
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

public class AllStocks {

	private TableViewer viewer;
	protected String searchString ="";
	
	@PostConstruct
	public void postConstruct(Composite parent, EMenuService menuService) {
		parent.setLayout( new GridLayout( 1, false));
		
				//search bar:
				Text search = new Text( parent, SWT.SEARCH | SWT.CANCEL | SWT.ICON_SEARCH); 
				// assuming that GridLayout is used 
				search.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, false, false, 1, 1)); 
				search.setMessage(" Filter"); 
				// filter at every keystroke 
				search.addModifyListener( new ModifyListener() { 
					@Override 
					public void modifyText( ModifyEvent e) { 
						Text source = (Text) e.getSource(); 
						searchString = source.getText(); 
						// trigger update in the viewer 
						viewer.refresh(); 
					} 
				});
				// SWT.SEARCH | SWT.CANCEL is not supported under Windows7 and 
				// so the following SelectionListener will not work under Windows7 
				search.addSelectionListener( new SelectionAdapter() {
					public void widgetDefaultSelected( SelectionEvent e) { 
						if (e.detail == SWT.CANCEL) { 
							Text text = (Text) e.getSource(); 
							text.setText(""); 
							// 
						} 
					} 
				});
				
				//Table Viewer:
				viewer = new TableViewer( parent, SWT.MULTI | SWT.FULL_SELECTION); 
				Table table = viewer.getTable(); 
				table.setHeaderVisible( true); 
				table.setLinesVisible( true); 
				table.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true)); 
				
				viewer.setContentProvider( ArrayContentProvider.getInstance()); 
				
//!!!!! TODO: PROBABLY WILL NEED TO PASS IN OBJECTS RATHER THAT ARRAY OF STRINGS
				
				// create column for the ticker property 
				TableViewerColumn colSummary = new TableViewerColumn( viewer, SWT.NONE); 
				colSummary.setLabelProvider( new ColumnLabelProvider() { 
					@Override public String getText(Object element) { 
						String ticker = (String) element;
						return ticker; 
						} 
					}); 
				colSummary.getColumn().setWidth( 120); 
				colSummary.getColumn().setText(" Ticker");
				
				viewer.setInput(FilesController.getInstance().getAllTickers());
				// add a filter which will search in the summary and description field 
				
				viewer.addFilter( new ViewerFilter() { 
					@Override 
					public boolean select( Viewer viewer, Object parentElement, Object element) { 
						String ticker = (String) element; 
						return ticker.toUpperCase().contains(searchString.toUpperCase()); 
						} 
					});
				
				
	}

}