package com.marcin_k.mystocks.parts;

import java.util.ArrayList;
import javax.annotation.PostConstruct;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

public class Recommendations {
	
	//Table
	private TableViewer viewer;
	
	@PostConstruct
	public void createControls( Composite parent) {		
		
		viewer = new TableViewer(parent, SWT.MULTI|SWT.FULL_SELECTION);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		
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
				return "test";
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
				return "test5";
			}			
		});
		recommendationsCol.getColumn().setWidth(150);
		recommendationsCol.getColumn().setText("Recommendation");
		
	}
}
