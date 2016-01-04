package com.jpay.mvcvm.controllers;

import com.jpay.mvcvm.EventBusStation;
import com.jpay.mvcvm.events.InterControllerRequestEvent;
import com.jpay.mvcvm.events.InterControllerResponseEvent;
import com.jpay.mvcvm.exceptions.IBaseError;

/**
 * Managers are a special instance of the Controller in the Model View Controller ViewModel pattern
 * (MVCVM) architecture. They are the master of a particular bus. In this the BaseControllerManager
 * is the master of the Controller to Controller bus. It intercepts all requests to this bus and
 * reroutes the request to the proper controller.
 */
public abstract class BaseControllerManager {
    public void onCreate() {
        if (!EventBusStation.getInstance().getControllerControllerEventBus().isRegistered(this)) {
            EventBusStation.getInstance().getControllerControllerEventBus().register(this);
        }
    }

    public void onClose() {
        if (EventBusStation.getInstance().getControllerControllerEventBus().isRegistered(this)) {
            EventBusStation.getInstance().getControllerControllerEventBus().unregister(this);
        }
    }

    /**
     * Defines pushing an event directly to a controller, enabling Controller<->Controller communication
     * @param event
     */
    public void onEvent(InterControllerRequestEvent event) {

    }

    private void pushEventToController(InterControllerRequestEvent requestEvent, Object data, IBaseError error) {
        EventBusStation.getInstance().getControllerControllerEventBus().post(new InterControllerResponseEvent(requestEvent, data, error));
    }

    public void pushEventToController(InterControllerRequestEvent requestEvent, Object data) {
        pushEventToController(requestEvent, data, null);
    }

    public void pushEventToController(InterControllerRequestEvent requestEvent, IBaseError error) {
        pushEventToController(requestEvent, null, error);
    }
}
