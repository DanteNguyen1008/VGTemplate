package com.jpay.videograms.controllers;

import com.jpay.mvcvm.events.InterControllerResponseEvent;
import com.jpay.videograms.exceptions.Errors;
import com.jpay.videograms.viewmodels.ViewModelManager;
import com.jpay.mvcvm.EventBusStation;
import com.jpay.mvcvm.events.ExceptionEvent;
import com.jpay.mvcvm.events.InterControllerRequestEvent;
import com.jpay.mvcvm.events.VMControllerRequestDataEvent;
import com.jpay.mvcvm.events.VMControllerResponseDataEvent;

import java.io.IOException;

/**
 * <p>
 *     Controllers are one aspect of the Model View Controller ViewModel pattern (MVCVVM).
 *     Controllers are tied to a particular view. Anytime an Activity or Fragment needs to request data
 *     or talk to another Activity or Fragment within the application, controllers are used as an
 *     intermediary. They are also used to store the application logic.
 * </p>
 *
 * <p>
 *     In JPay specific implementation Controllers employ the use of 'buses' from the GitHub project
 *     <a href="https://github.com/greenrobot/EventBus">greenrobot/EventBus</a>.
 *     There are three event buses that controllers talk on:
 * </p>
 *         <li>Controller to ViewModel - Bus used for requesting data (Tasks, db, etc). All requests are managed by the {@link ViewModelManager} class.</li>
 *         <li>Error - Bus for propagating errors or exceptions.</li>
 *         <li>Controller to Controller - Bus for notifying other controllers that their UI component should change. All requests are managed by the {@link ControllerManager} class.</li>
 */
public abstract class JBaseController extends com.jpay.mvcvm.controllers.JBaseController {

	@Override
	protected void baseOnRequestDataError(VMControllerResponseDataEvent event) {
		Errors eventError = (Errors) event.error;
		switch (eventError.error) {
		case IO_ERROR:
			EventBusStation.getInstance().getExceptionBus().post(new ExceptionEvent(new IOException(eventError.error.toString())));
			break;

		case UNKNOWN_EXCEPTION:
			EventBusStation.getInstance().getExceptionBus().post(new ExceptionEvent(new Exception(eventError.errorMessage)));
			break;

		default:
			break;
		}
	}

    @Override
    public void baseOnControllerResponseError(InterControllerResponseEvent event) {
        Errors eventError = (Errors) event.error;
        switch (eventError.error) {
            case UNKNOWN_EXCEPTION:
                EventBusStation.getInstance().getExceptionBus().post(new ExceptionEvent(new Exception(eventError.errorMessage)));
                break;

            default:
                break;
        }
    }

    @Override
    public void onRequestDataSuccess(VMControllerResponseDataEvent event) {
        validateVMEventResponseData(event);
    }

    @Override
    public void onRequestDataError(VMControllerResponseDataEvent event) {
        validateVMEventResponseData(event);
    }

    private void validateVMEventResponseData(VMControllerResponseDataEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Event data supplied is null");
        }

        if (event.requestEvent == null) {
            throw new IllegalArgumentException("Event request data cannot be null");
        }

        VMControllerRequestDataEvent request = event.requestEvent;

        if (request.params == null || request.params.length == 0) {
            throw new IllegalArgumentException("Event params cannot be null or empty in this context ");
        }

        if (!(request.params[0] instanceof JPayEvent)) {
            throw new IllegalArgumentException("First argument must be of type JPayEvent");
        }
    }

    @Override
    public void onControllerEventRequest(InterControllerRequestEvent event) {
        validateControllerEventRequestData(event);
    }

	private void validateControllerEventRequestData(InterControllerRequestEvent event) {
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

    private void validateControllerEventResponseData(InterControllerResponseEvent event) {
        if (event == null) {
            throw new IllegalArgumentException("Event data supplied is null");
        }

        if (event.requestEvent == null) {
            throw new IllegalArgumentException("Event request data cannot be null");
        }

        InterControllerRequestEvent request = event.requestEvent;

        if (request.params == null || request.params.length == 0) {
            throw new IllegalArgumentException("Event params cannot be null or empty in this context ");
        }

        if (!(request.params[0] instanceof JPayEvent)) {
            throw new IllegalArgumentException("First argument must be of type JPayEvent");
        }
    }

    @Override
    public void onControllerResponseError(InterControllerResponseEvent event) {
        validateControllerEventResponseData(event);
    }

    @Override
    public void onControllerResponseSuccess(InterControllerResponseEvent event) {
        validateControllerEventResponseData(event);
    }
}
