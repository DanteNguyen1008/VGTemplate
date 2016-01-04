package com.jpay.mvcvm.controllers;

import android.app.Activity;

import com.jpay.mvcvm.EventBusStation;
import com.jpay.mvcvm.events.InterControllerRequestEvent;
import com.jpay.mvcvm.events.InterControllerResponseEvent;
import com.jpay.mvcvm.events.VMControllerRequestDataEvent;
import com.jpay.mvcvm.events.VMControllerResponseDataEvent;
import com.jpay.mvcvm.exceptions.MissingControllerException;
import com.jpay.mvcvm.utils.JLog;
import com.jpay.mvcvm.utils.Utils;
import com.jpay.mvcvm.viewmodels.IEventENum;
import com.jpay.mvcvm.views.JView;

/**
 * Base class for all controller
 * <p>
 * Controller takes care of app logic:
 * <p>
 * - What will be display on view?
 * <p>
 * - What data will be request?
 * 
 * <p>
 * 
 * The implementation VIEW has to put the controller to its life cycle
 * 
 * @author anguyen
 * 
 */
public abstract class JBaseController {
	private static final String TAG = JBaseController.class.getSimpleName();
	protected int identityHashCode = 0;
	protected IEventENum[] mEventFilter;

	/**
	 * listener for the VIEW
	 * */
	protected JView view;

	/**
	 * Initializes the controller.
	 * Called in the lifecycle of android onCreate().
	 * @param view The view to associate with this controller.
	 * @throws MissingControllerException If there is there is a mismatch between a view and this controller.
	 */
	public void onCreate(JView view) throws MissingControllerException {
		if (view == null)
			throw new MissingControllerException(getClassName());

		this.view = view;
		JLog.d(TAG, "Controller " + getClassName() + " onCreate");
	}

	/**
	 * Register for event when VIEW's displayed
	 */
	public void onResume() {
		JLog.d(TAG, "Controller " + getClassName() + " OnResume");
        /* register event bus for ViewModel road */
		if (!EventBusStation.getInstance().getViewModelControllerEventBus().isRegistered(this)) {
			EventBusStation.getInstance().getViewModelControllerEventBus().register(this);
		}

        /* register event bus for Controller to Controller road */
        if (!EventBusStation.getInstance().getControllerControllerEventBus().isRegistered(this)) {
            EventBusStation.getInstance().getControllerControllerEventBus().register(this);
        }
	}

	public void onPause() {
		JLog.d(TAG, "Controller " + getClassName() + " onPause");
	}

	public void onStop() {
		JLog.d(TAG, "Controller " + getClassName() + " onStop");
	}

	/**
	 * UnRegister for event when VIEW's hided.
	 */
	public void onDestroy() {
		JLog.d(TAG, "Controller " + getClassName() + " onDestroy");
        /* unregister event bus for ViewModel road */
		if (EventBusStation.getInstance().getViewModelControllerEventBus().isRegistered(this)) {
			EventBusStation.getInstance().getViewModelControllerEventBus().unregister(this);
		}

        /* unregister event bus for Controller to Controller road */
        if (EventBusStation.getInstance().getControllerControllerEventBus().isRegistered(this)) {
			EventBusStation.getInstance().getControllerControllerEventBus().unregister(this);
		}

		view = null;
	}

	/**
	 * Send data request to VM
	 * 
	 * @param eventType The enum type event. Used for filtering to the correct controller.
	 * @param params The object data associated with the event request.
	 */
	public void sendVMRequest(IEventENum eventType, Object... params) {
		if (identityHashCode == 0) {
			identityHashCode = System.identityHashCode(this);
		}
		JLog.d(TAG, "Sending a vm request: " + eventType.toString());
		EventBusStation.getInstance().getViewModelControllerEventBus().post(new VMControllerRequestDataEvent(eventType, params, identityHashCode));
	}

    public void sendControllerRequest(IEventENum eventType, Object... params) {
        if (identityHashCode == 0) {
            identityHashCode = System.identityHashCode(this);
        }

        JLog.d(TAG, "Sending controller request: " + eventType.toString());

        EventBusStation.getInstance().getControllerControllerEventBus().post(new InterControllerRequestEvent(eventType, params, identityHashCode));
    }

	/**
	 * If the returned hash code is mismatch with current one, do nothing
	 * 
	 * @param returnHashCode
	 * @return
	 */
	public boolean checkHashCode(int returnHashCode) {
		return identityHashCode == returnHashCode;
	}

	public Activity getActivity() {
		return view.getActivity();
	}

	public abstract String getClassName();

	/**
	 * Called when successfully get data back from VM
	 * 
	 * @param event
	 */
	public abstract void onRequestDataSuccess(VMControllerResponseDataEvent event);

	/**
	 * Called when get app error, exception from VM
	 * 
	 * @param event
	 */
	public abstract void onRequestDataError(VMControllerResponseDataEvent event);

	/**
	 * Called when get fail status from backend returned from VM
	 * 
	 * @param event
	 */
	public abstract void onRequestDataFail(VMControllerResponseDataEvent event);

	public abstract IEventENum[] getEventFilter();

	/**
	 * Called when view finished attaching
	 * */
	public abstract void onRequestDataOnLoad();

	/**
	 * Handle generic errors like network
	 * 
	 * @param event
	 */
	protected abstract void baseOnRequestDataError(VMControllerResponseDataEvent event);

	/**
	 * Event bus endpoint: receive event data from VM and perform action base on
	 * result status: success, error or fail
	 * 
	 * @param event
	 */
	public void onEventAsync(VMControllerResponseDataEvent event) {
		/* do nothing if the hash code were mismatched */
		if (!checkHashCode(event.getHashCode())) {
			return;
		}

		/* event filter */
		if (mEventFilter == null) {
			mEventFilter = getEventFilter();
		}

		if (Utils.contains(mEventFilter, event.getEventType())) {

			/* Event error */
			if (event.error != null) {
				baseOnRequestDataError(event);
				onRequestDataError(event);
				return;
			}

			onRequestDataSuccess(event);
		}
	}

    /**
     * Event bus endpoint: receive request event data from another Controller and perform action
     *
     * @param event
     */
    public void onEventAsync(InterControllerRequestEvent event) {
        _onControllerEventRequest(event);
    }

    /**
     * Event bus endpoint: receive response event data from sending request data/action to another controller
     * and perform action base on result status: success, error or fail
     *
     * @param event
     */
    public void onEventAsync(InterControllerResponseEvent event) {
        _onControllerEventResponse(event);
    }

    /**
     * pre-processing for receiving response event from sending request data/action to another controller
     * @param event
     */
    private void _onControllerEventResponse(InterControllerResponseEvent event) {
        /* do nothing if the hash code were mismatched */
        if (!checkHashCode(event.getHashCode())) {
            return;
        }

		/* event filter */
        if (mEventFilter == null) {
            mEventFilter = getEventFilter();
        }

        if (Utils.contains(mEventFilter, event.getEventType())) {

			/* Event error */
            if (event.error != null) {
                baseOnControllerResponseError(event);
                onControllerResponseError(event);
                return;
            }

            onControllerResponseSuccess(event);
        }
    }

    /**
     * pre-processing for receiving request event from another controller
     *
     * @param event
     */
    private void _onControllerEventRequest(InterControllerRequestEvent event) {
        /* event filter */
        if (mEventFilter == null) {
            mEventFilter = getEventFilter();
        }

        if (Utils.contains(mEventFilter, event.eventType)) {
            onControllerEventRequest(event);
        }
    }

    /**
     * On Controller request
     * @param event
     */
    public abstract void onControllerEventRequest(InterControllerRequestEvent event);

    /**
     *
     * On controller response
     *
     *
     * */

    /**
     * On controller response general error handler method
     * @param event
     */
    public abstract void baseOnControllerResponseError(InterControllerResponseEvent event);

    /**
     * On controller response error handler method
     * @param event
     */
    public abstract void onControllerResponseError(InterControllerResponseEvent event);

    /**
     * On controller response success handler method
     * @param event
     */
    public abstract void onControllerResponseSuccess(InterControllerResponseEvent event);
}
