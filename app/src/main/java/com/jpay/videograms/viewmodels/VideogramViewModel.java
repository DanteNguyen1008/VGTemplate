package com.jpay.videograms.viewmodels;

import com.jpay.videograms.controllers.JPayEvent;
import com.jpay.mvcvm.events.VMControllerRequestDataEvent;

/**
 * Created by anguyen on 12/11/2015.
 */
public class VideogramViewModel extends JBaseViewModel{

    private static VideogramViewModel _instance;

    public static VideogramViewModel getInstance() {
        if(_instance == null) {
            _instance = new VideogramViewModel();
        }

        return _instance;
    }

    @Override
    public void onEvent(Object data) {
        super.onEvent(data);

        final VMControllerRequestDataEvent event = (VMControllerRequestDataEvent) data;

        JPayEvent eventType = (JPayEvent)event.params[0];

        switch (eventType) {

            default:
                break;
        }

    }

    private void startDeserializationTask(final VMControllerRequestDataEvent event) {

    }
}
