package com.jpay.mvcvm.exceptions;

import com.jpay.mvcvm.EventBusStation;
import com.jpay.mvcvm.events.ExceptionEvent;

public class MissingControllerException extends Exception{

	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;
	
    public MissingControllerException(String controllerName) {
    	super("The controller was missing at " + controllerName);
    	
    	EventBusStation.getInstance().getExceptionBus().post(new ExceptionEvent(this));
    }
}
