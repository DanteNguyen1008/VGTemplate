package com.jpay.mvcvm.events;

/**
 * Exception event for bus event
 * 
 * @author anguyen
 *
 */
public class ExceptionEvent {

	public Exception exception;

	public ExceptionEvent(Exception exception) {
		this.exception = exception;
	}
}
