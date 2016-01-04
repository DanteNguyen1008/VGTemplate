package com.jpay.mvcvm.events;

import com.jpay.mvcvm.exceptions.IBaseError;
import com.jpay.mvcvm.viewmodels.IEventENum;

/**
 *
 */
public class InterControllerResponseEvent {
    public Object data;
    public IBaseError error;
    public InterControllerRequestEvent requestEvent;

    public InterControllerResponseEvent(InterControllerRequestEvent requestEvent, Object data, IBaseError error) {
        this.data = data;
        this.error = error;
        this.requestEvent = requestEvent;
    }

    public IEventENum getEventType() {
        return requestEvent.eventType;
    }

    public int getHashCode() {
        return requestEvent.hashCode;
    }
}
