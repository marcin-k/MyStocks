package com.marcin_k.mystocks.handlers;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import com.marcin_k.mystocks.events.MyEventConstants;
import com.marcin_k.mystocks.functions.controllers.MyPortfolioController;
import com.marcin_k.mystocks.wizards.StockWizard;

public class NewPortfolioStockHandler {
	@Execute public void execute( Shell shell, IEventBroker broker) { 
	
		WizardDialog dialog = new WizardDialog( shell, new StockWizard()); 
		if (dialog.open() == WizardDialog.OK) { 
			// call service to save Todo object
			System.out.println("from handler");
			MyPortfolioController.getInstance().updateFile(MyPortfolioController.getInstance().getTempTicker());
			broker.post(MyEventConstants.TOPIC_STOCKS_MYPORTFOLIO,
                    createEventData(MyEventConstants.TOPIC_STOCKS_MYPORTFOLIO));
		}
	}
	
	//sends the events 
    private Map<String, String> createEventData(String topic) {
        Map<String, String> map = new HashMap<String, String>();
        // in case the receiver wants to check the topic
        map.put(MyEventConstants.TOPIC_STOCKS_MYPORTFOLIO, topic);
        
        return map;
    }
	
}
