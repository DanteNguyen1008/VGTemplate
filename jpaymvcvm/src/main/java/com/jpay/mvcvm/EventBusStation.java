package com.jpay.mvcvm;


import de.greenrobot.event.EventBus;

/**
 * Bus station to request event buses
 * 
 * @author anguyen
 *
 */
public class EventBusStation {
	
	/*singleton*/
	private static EventBusStation _instance = null;
	private EventBus criticalExceptionBus;
	private EventBus interControllerBus;

	private EventBusStation() {}
	
	public static EventBusStation getInstance() {
		if(_instance == null)
			_instance = new EventBusStation();
		
		return _instance;
	}
	
	/**
	 * Get the default event bus for VM <-> Controller road
	 * 
	 * @return The default event bus for this controller.
	 */
	public EventBus getViewModelControllerEventBus() {
		return EventBus.getDefault();
	}
	
	/**
	 * get custom event bus for handling exception, this bus will deliver to Activity
	 * 
	 * @return The exception event bus for this controller.
	 */
	public EventBus getExceptionBus() {
		if(criticalExceptionBus == null)
			criticalExceptionBus = EventBus.builder().build();
		
		return criticalExceptionBus;
	}

	public EventBus getControllerControllerEventBus() {
		if (interControllerBus == null) {
            interControllerBus = EventBus.builder().build();
        }

        return interControllerBus;
	}
}
