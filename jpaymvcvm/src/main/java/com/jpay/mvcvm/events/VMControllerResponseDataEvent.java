package com.jpay.mvcvm.events;

import com.jpay.mvcvm.exceptions.IBaseError;
import com.jpay.mvcvm.viewmodels.IEventENum;

/**
 * Event bus object for VM <-> controller
 * <p>
 * This event object's sent from VM to controller for returning data/error
 * 
 * @author anguyen
 * 
 */
public class VMControllerResponseDataEvent {
	public Object data;
	public IBaseError error;
	public boolean isStillRunning;
	public boolean isLocalData;
	public VMControllerRequestDataEvent requestEvent;

	public VMControllerResponseDataEvent(VMControllerRequestDataEvent requestEvent, Object data, IBaseError error) {
		this.requestEvent = requestEvent;
		this.data = data;
		this.error = error;
	}

	public VMControllerResponseDataEvent(VMControllerRequestDataEvent requestEvent, Object data, IBaseError error, boolean isLocalData) {
		this(requestEvent, data, error);
		this.isLocalData = isLocalData;
	}

	public VMControllerResponseDataEvent(VMControllerRequestDataEvent requestEvent, boolean isStillRunning) {
		this.requestEvent = requestEvent;
		this.isStillRunning = isStillRunning;
	}

	public IEventENum getEventType() {
		return requestEvent.eventType;
	}

	public int getHashCode() {
		return requestEvent.hashCode;
	}
}
