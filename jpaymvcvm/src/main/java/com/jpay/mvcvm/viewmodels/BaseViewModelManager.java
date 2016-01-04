package com.jpay.mvcvm.viewmodels;

import com.jpay.mvcvm.EventBusStation;
import com.jpay.mvcvm.events.VMControllerRequestDataEvent;
import com.jpay.mvcvm.events.VMControllerResponseDataEvent;
import com.jpay.mvcvm.exceptions.IBaseError;

/**
 * This manager will follow the app life cycle
 * 
 * @author anguyen
 * 
 */
public abstract class BaseViewModelManager {

	/***
	 * 
	 * life cycle call back
	 * 
	 * */
	public void onCreate() {
		/* for communicating Controller <-> VM Manager */
		if (!EventBusStation.getInstance().getViewModelControllerEventBus().isRegistered(this)) {
			EventBusStation.getInstance().getViewModelControllerEventBus().register(this);
		}
	}

	public void onClose() {
		if (EventBusStation.getInstance().getViewModelControllerEventBus().isRegistered(this)) {
			EventBusStation.getInstance().getViewModelControllerEventBus().unregister(this);
		}
	}

	/**
	 * -- Controller <-> VM Manager road
	 * <p>
	 * This method implement encapsulation of OOP: outside Controller only can
	 * call these below event:
	 * */
	public abstract void onEventAsync(VMControllerRequestDataEvent event);

	/**
	 * Push WS parsed result to controller
	 * @param requestEvent
	 * @param data
	 * @param error
	 */
	public void pushEventToController(VMControllerRequestDataEvent requestEvent, Object data, IBaseError error) {
		EventBusStation.getInstance().getViewModelControllerEventBus().post(new VMControllerResponseDataEvent(requestEvent, data, error));
	}

	/**
	 * Push successful data to controller
	 * @param requestEvent
	 * @param data
	 */
	public void pushEventToController(VMControllerRequestDataEvent requestEvent, Object data) {
		pushEventToController(requestEvent, data, null);
	}

	/**
	 * Push error to controller
	 * @param requestEvent
	 * @param error
	 */
	public void pushEventToController(VMControllerRequestDataEvent requestEvent, IBaseError error) {
		pushEventToController(requestEvent, null, error);
	}
}
