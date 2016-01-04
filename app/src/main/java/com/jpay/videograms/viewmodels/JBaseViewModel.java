package com.jpay.videograms.viewmodels;

import com.jpay.videograms.controllers.JPayEvent;
import com.jpay.mvcvm.events.VMControllerRequestDataEvent;
import com.jpay.mvcvm.exceptions.IBaseError;

/**
 * Base class for all VIEW MODEL classes
 * <p>
 * Defines action with View model manager (VMM)
 * 
 * @author anguyen
 * 
 */
public abstract class JBaseViewModel extends com.jpay.mvcvm.viewmodels.JBaseViewModel {

	/**
	 * Send on error event to Controller by VMM
	 * 
	 * @param requestEvent
	 * @param error
	 */
	@Override
	public void onError(VMControllerRequestDataEvent requestEvent, IBaseError error) {
		ViewModelManager.getInstance().pushEventToController(requestEvent, error);
	}

	/**
	 * Send successful event to Controller by VMM
	 * 
	 * @param requestEvent
	 * @param data
	 */
	@Override
	public void onReturnDataSuccess(VMControllerRequestDataEvent requestEvent, Object data) {
		ViewModelManager.getInstance().pushEventToController(requestEvent, data);
	}

	@Override
    public void onEvent(Object data) {
        validateOnEventData(data);
    }

	private void validateOnEventData(Object data) {

		final VMControllerRequestDataEvent event = (VMControllerRequestDataEvent) data;

		if (event == null) {
			throw new IllegalArgumentException("Event data supplied is null");
		}

		if (event.params == null || event.params.length == 0) {
			throw new IllegalArgumentException("Event params cannot be null or empty in this context ");
		}

		if (!(event.params[0] instanceof JPayEvent)) {
			throw new IllegalArgumentException("First argument must be of type JPayEvent");
		}
	}
}
