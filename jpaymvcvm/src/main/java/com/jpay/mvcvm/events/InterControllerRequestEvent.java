package com.jpay.mvcvm.events;

import com.jpay.mvcvm.viewmodels.IEventENum;

/**
 *
 */
public class InterControllerRequestEvent {
    public IEventENum eventType;
    public Object[] params;
    public int hashCode;

    public InterControllerRequestEvent(IEventENum eventType, Object[] params, int hashCode) {
        this.eventType = eventType;
        this.params = params;
        this.hashCode = hashCode;
    }
}
