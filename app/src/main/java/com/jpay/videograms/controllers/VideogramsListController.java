package com.jpay.videograms.controllers;

import com.jpay.mvcvm.EventBusStation;
import com.jpay.mvcvm.events.InterControllerRequestEvent;
import com.jpay.mvcvm.events.VMControllerRequestDataEvent;
import com.jpay.mvcvm.events.VMControllerResponseDataEvent;
import com.jpay.mvcvm.viewmodels.IEventENum;
import com.jpay.videograms.models.Contact;
import com.jpay.videograms.models.Videograms;
import com.jpay.videograms.utils.SyncStatus;
import com.jpay.videograms.utils.Utils;
import com.jpay.videograms.views.VideogramsListFragmentView;

import java.util.ArrayList;

/**
 * Created by anguyen on 12/10/2015.
 */
public class VideogramsListController extends JBaseController {

    private SyncStatus mScreenType;

    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public void onRequestDataSuccess(VMControllerResponseDataEvent event) {
        super.onRequestDataSuccess(event);

        VMControllerRequestDataEvent request = event.requestEvent;
        JPayEvent requestEventType = (JPayEvent) request.params[0];

        switch (requestEventType) {
            case EVENT_LOAD_ALL_VIDEOGRAM: {
                handleLoadVideogramsSuccess(event);
                break;
            }
        }
    }

    @Override
    public void onRequestDataError(VMControllerResponseDataEvent event) {
        super.onRequestDataError(event);
    }

    @Override
    public void onRequestDataFail(VMControllerResponseDataEvent event) {

    }

    @Override
    public IEventENum[] getEventFilter() {
        return new IEventENum[]{JPayEvent.C_VIDEOGRAM_CONTROLLER, JPayEvent.VM_CONTACT};
    }

    @Override
    public void onRequestDataOnLoad() {

    }

    @Override
    public void onControllerEventRequest(InterControllerRequestEvent event) {
        super.onControllerEventRequest(event);

        switch ((JPayEvent) event.params[0]) {
            case C_EVENT_REFRESH_VIDEOGRAMS:
                Contact contact = (Contact) event.params[1];
                if (Utils.isAllDefaultContact(contact)) {
                    /*Load all videograms*/
                    loadAllVideograms();
                } else {
                    refreshVideograms((ArrayList) contact.getVideogrames());
                }
                break;
        }
    }

    public void onRemoveButtonClicked(Videograms removeVideograms, int position) {
        removeVideograms(removeVideograms, position);
    }



    public void onSyncButtonClicked(Videograms syncVideograms, int position) {
        sendVGToBeDownloaded(syncVideograms, position);
    }



    public void onUndoButtonClicked(Videograms undoVideograms, int position) {
        undoDeletedVG(undoVideograms, position);
    }


    /**
     * Set up variables
     *
     * @param screenType
     */
    public void initVariables(SyncStatus screenType) {
        this.mScreenType = screenType;
    }

    public void onVideogramsClicked(Videograms videograms, int position) {
        if (identityHashCode == 0) {
            identityHashCode = System.identityHashCode(this);
        }

        EventBusStation.getInstance().getControllerControllerEventBus().post(new InterControllerRequestEvent(JPayEvent.C_MAINACTIVITY_CONTROLLER, new Object[]{JPayEvent.C_EVENT_PLAY_VIDEO, videograms, position}, identityHashCode));
    }

    private void loadAllVideograms() {
        sendVMRequest(JPayEvent.VM_CONTACT, new Object[]{JPayEvent.EVENT_LOAD_ALL_VIDEOGRAM});
    }

    private void refreshVideograms(ArrayList<Videograms> videogrames) {
        /*filter based on screen type*/
        ArrayList<Videograms> filteredVideogrames = new ArrayList<>();
        for (Videograms videogram : videogrames) {
            switch (mScreenType) {
                case NOT_AVAILABLE:
                    if (videogram.syncStatus == SyncStatus.NOT_AVAILABLE || videogram.syncStatus == SyncStatus.WILL_SYNC) {
                        filteredVideogrames.add(videogram);
                    }
                    break;

                case ON_DEVICE:
                    if (videogram.syncStatus == SyncStatus.ON_DEVICE || videogram.syncStatus == SyncStatus.DELETED) {
                        filteredVideogrames.add(videogram);
                    }
                    break;
            }
        }

        ((VideogramsListFragmentView) view).refreshVideograms(filteredVideogrames);
    }

    private void handleLoadVideogramsSuccess(VMControllerResponseDataEvent event) {
        refreshVideograms((ArrayList) event.data);
    }

    private void sendVGToBeDownloaded(Videograms syncVideograms, int position) {
        sendVMRequest(JPayEvent.VM_CONTACT, new Object[] {JPayEvent.EVENT_SET_VIDEOGRAM_TO_BE_DOWNLOADED, syncVideograms, position});
    }

    private void removeVideograms(Videograms removeVideograms, int position) {
        sendVMRequest(JPayEvent.VM_CONTACT, new Object[] {JPayEvent.EVENT_LOCALLY_REMOVE_VIDEOGRAM, removeVideograms, position});
    }

    private void undoDeletedVG(Videograms undoVideograms, int position) {
        sendVMRequest(JPayEvent.VM_CONTACT, new Object[] {JPayEvent.EVENT_UNDO_LOCALLY_REMOVED_VIDEOGRAM, undoVideograms, position});
    }
}
