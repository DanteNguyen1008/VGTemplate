package com.jpay.mvcvm.events;

import com.jpay.mvcvm.viewmodels.IEventENum;

/**
 * Event bus object for VM <-> controller
 * <p>
 * This event object's sent from controller to VM for requesting data
 * 
 * @author anguyen
 * 
 */
public class VMControllerRequestDataEvent {
	public IEventENum eventType;
	public Object[] params;
	public int hashCode;

	public VMControllerRequestDataEvent(IEventENum eventType, Object[] params, int hashCode) {
		this.eventType = eventType;
		this.params = params;
		this.hashCode = hashCode;
	}
}
