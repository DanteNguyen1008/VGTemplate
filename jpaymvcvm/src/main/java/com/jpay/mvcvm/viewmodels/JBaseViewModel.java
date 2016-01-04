package com.jpay.mvcvm.viewmodels;

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
public abstract class JBaseViewModel {

	/**
	 * Send on error event to Controller by VMM
	 * 
	 * @param requestEvent
	 * @param error
	 */
	public abstract void onError(VMControllerRequestDataEvent requestEvent, IBaseError error);

	/**
	 * Send successful event to Controller by VMM
	 * 
	 * @param requestEvent
	 * @param data
	 */
	public abstract void onReturnDataSuccess(VMControllerRequestDataEvent requestEvent, Object data);

	/**
	 * Generic methods to be call when VMM recevie event from Controller.
	 * 
	 * @param data
	 */
	public abstract void onEvent(Object data);
}
