package com.jpay.videograms.controllers;

import com.jpay.mvcvm.EventBusStation;
import com.jpay.mvcvm.events.InterControllerRequestEvent;
import com.jpay.mvcvm.events.VMControllerResponseDataEvent;
import com.jpay.videograms.JPayMainActivity;
import com.jpay.videograms.models.Videograms;

/**
 * <p>
 * The controller for the {@link JPayMainActivity} class.
 * </p>
 * <p>
 * <p>
 * Controllers are one aspect of the Model View Controller ViewModel pattern (MVCVVM).
 * Controllers are tied to a particular view. Anytime an Activity or Fragment needs to request data
 * or talk to another Activity or Fragment within the application, controllers are used as an
 * intermediary.
 * </p>
 * <p>
 * <p>
 * In JPay specific implementation Controllers employ the use of 'buses' from the github project
 * <a href="https://github.com/greenrobot/EventBus">greenrobot/EventBus</a>.
 * There are three event buses that controllers talk on:
 * </p>
 * <li>Controller to ViewModel - Bus used for requesting data (Tasks, db, etc).</li>
 * <li>Error - Bus for propagating errors or exceptions.</li>
 * <li>Controller to Controller - Bus for notifying other controllers that their UI component should change.</li>
 */
public class JPayMainActivityController extends JBaseController {

    private static JPayMainActivityController _instance;

    public static JPayMainActivityController Instance() {
        if (_instance == null) {
            _instance = new JPayMainActivityController();
        }

        return _instance;
    }

    @Override
    public String getClassName() {
        return getClass().getSimpleName();
    }

    @Override
    public void onRequestDataSuccess(VMControllerResponseDataEvent event) {
        super.onRequestDataSuccess(event);
    }

    @Override
    public void onRequestDataError(VMControllerResponseDataEvent event) {
        super.onRequestDataError(event);
    }

    @Override
    public void onRequestDataFail(VMControllerResponseDataEvent event) {
    }

    @Override
    public JPayEvent[] getEventFilter() {
        return new JPayEvent[]{JPayEvent.C_MAINACTIVITY_CONTROLLER};
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!EventBusStation.getInstance().getExceptionBus().isRegistered(this)) {
            EventBusStation.getInstance().getExceptionBus().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBusStation.getInstance().getExceptionBus().isRegistered(this)) {
            EventBusStation.getInstance().getExceptionBus().unregister(this);
        }
    }

    @Override
    public void onControllerEventRequest(InterControllerRequestEvent event) {
        super.onControllerEventRequest(event);

        final JPayEvent eventType = (JPayEvent) event.params[0];

        switch (eventType) {
            case C_EVENT_PLAY_VIDEO:
                playVideo(event.params);
                break;
            default:
                break;
        }
    }

    private void playVideo(Object[] params) {
        ((JPayMainActivity) view).openAndPlayVideo((Videograms) params[1], (Integer) params[2]);
    }

    @Override
    public void onRequestDataOnLoad() {

    }
}
