package com.jpay.mvcvm.exceptions;

import com.jpay.mvcvm.EventBusStation;
import com.jpay.mvcvm.events.ExceptionEvent;


/**
 * Exception for broken flow
 * <p>
 * Can use new Google Analytic for send error event here (can not use EasyTracker)
 * 
 * @author anguyen
 *
 */
public class BrokenFlowException extends Exception{

	/**
	 * 
	 */
    private static final long serialVersionUID = 1L;
    
    public BrokenFlowException(String where) {
    	super("Processing flow was broken at " + where);
    	
    	EventBusStation.getInstance().getExceptionBus().post(new ExceptionEvent(this));
    }
}
