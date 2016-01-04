package com.jpay.videograms.controllers;

import android.support.annotation.Nullable;

import com.jpay.mvcvm.EventBusStation;
import com.jpay.mvcvm.events.InterControllerRequestEvent;
import com.jpay.mvcvm.events.VMControllerRequestDataEvent;
import com.jpay.videograms.models.Contact;
import com.jpay.videograms.models.ContactList;
import com.jpay.mvcvm.events.VMControllerResponseDataEvent;
import com.jpay.mvcvm.utils.JLog;
import com.jpay.mvcvm.viewmodels.IEventENum;
import com.jpay.videograms.views.ContactFragmentView;

/**
 * Created by anguyen on 12/11/2015.
 */
public class ContactController extends JBaseController {
    private static final String TAG = ContactController.class.getSimpleName();

    /**
     * Override methods
     */
    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public void onRequestDataFail(VMControllerResponseDataEvent event) {

    }

    @Override
    public void onRequestDataSuccess(VMControllerResponseDataEvent event) {
        super.onRequestDataSuccess(event);

        VMControllerRequestDataEvent request = event.requestEvent;
        JPayEvent requestEventType = (JPayEvent) request.params[0];

        switch (requestEventType) {
            case EVENT_LOAD_CONTACT: {
                handleLoadContactListSuccess(event);
                break;
            }
        }
    }

    @Override
    public void onRequestDataError(VMControllerResponseDataEvent event) {
        super.onRequestDataError(event);
    }

    @Override
    public IEventENum[] getEventFilter() {
        return new IEventENum[]{JPayEvent.VM_CONTACT};
    }

    @Override
    public void onRequestDataOnLoad() {
        getContactList();
    }

    /** End override methods*/

    /**
     * start public methods
     */
    public void OnContactClicked(@Nullable Contact contact) {
        if (contact == null) {
            JLog.d(TAG, "Data corrupted");
            return;
        }

        /*load videograms*/
        sendRefreshVideograms(contact);
    }

    /** end public methods */

    /**
     * start private methods
     */
    private void getContactList() {
        sendVMRequest(JPayEvent.VM_CONTACT, new Object[]{JPayEvent.EVENT_LOAD_CONTACT});
    }

    private void sendRefreshVideograms(Contact contact) {
        if (identityHashCode == 0) {
            identityHashCode = System.identityHashCode(this);
        }

        EventBusStation.getInstance().getControllerControllerEventBus().post(new InterControllerRequestEvent(JPayEvent.C_VIDEOGRAM_CONTROLLER,
                new Object[]{JPayEvent.C_EVENT_REFRESH_VIDEOGRAMS, contact}, identityHashCode));
    }

    private void handleLoadContactListSuccess(VMControllerResponseDataEvent event) {
        ContactList contactList = (ContactList) event.data;
        /*refresh contact UI list*/
        ((ContactFragmentView) view).refreshContactList(contactList.GetListOfContact());
    }

    /**
     * Load VG based on default contact at first time load of the app
     *
     * @param contact
     */
    public void OnLoadVGAtFirstTime(Contact contact) {
        /* send controller request to videogram controller to load default VG*/
        sendRefreshVideograms(contact);
        /* Auto select the default contact */
        ((ContactFragmentView) view).setSelectionForDefaultContact();
    }

    /** end private methods */
}
