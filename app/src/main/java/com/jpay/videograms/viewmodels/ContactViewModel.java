package com.jpay.videograms.viewmodels;

import com.jpay.videograms.controllers.JPayEvent;
import com.jpay.videograms.exceptions.Errors;
import com.jpay.videograms.models.Contact;
import com.jpay.videograms.models.ContactList;
import com.jpay.videograms.models.Videograms;
import com.jpay.videograms.tasks.DeserializeContactFileTask;
import com.jpay.videograms.tasks.TaskNotifier;
import com.jpay.mvcvm.events.VMControllerRequestDataEvent;
import com.jpay.videograms.utils.SyncStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anguyen on 12/11/2015.
 */
public class ContactViewModel extends JBaseViewModel {

    private static ContactViewModel _instance;

    public static ContactViewModel getInstance() {
        if (_instance == null) {
            _instance = new ContactViewModel();
        }

        return _instance;
    }

    private ContactList mContactList;

    @Override
    public void onEvent(Object data) {
        super.onEvent(data);

        final VMControllerRequestDataEvent event = (VMControllerRequestDataEvent) data;

        JPayEvent eventType = (JPayEvent) event.params[0];

        switch (eventType) {
            case EVENT_LOAD_CONTACT:
                startDeserializationTask(event);
                break;

            case EVENT_LOAD_ALL_VIDEOGRAM:
                loadAllVideograms(event);
                break;

            case EVENT_GET_NEXT_VIDEOGRAM_BY_POS:
                loadVideogramByPos(event, true);
                break;
            case EVENT_GET_PREV_VIDEOGRAM_BY_POS:
                loadVideogramByPos(event, false);
                break;

            case EVENT_LOCALLY_REMOVE_VIDEOGRAM:
                locallyRemoveVideogram(event);
                break;
            default:
                break;
        }

    }

    private void locallyRemoveVideogram(VMControllerRequestDataEvent event) {
        if (event.params.length < 3) {
            ContactViewModel.this.onError(event, new Errors(Errors.eError.UNKNOWN_EXCEPTION, "Lost params in locallyRemoveVideogram method"));
        }

        Videograms videograms = (Videograms) event.params[1];
        if (mContactList != null) {
            ArrayList<Videograms> videogramsToUpdate = new ArrayList<>();
            videogramsToUpdate.add(videograms);
            updateVGsInContactList(videogramsToUpdate);
        }
    }

    private void updateVGsInContactList(List<Videograms> updatedVGs) {
        if (updatedVGs == null || updatedVGs.size() == 0) {
            return;
        }

        for (Videograms videograms : updatedVGs) {
            if (mContactList.updateVGInContactList(videograms)) {
                /*the videogram was found and updated*/
            }
        }
    }

    private void loadVideogramByPos(VMControllerRequestDataEvent event, boolean isNext) {
        if (event.params.length < 3) {
            ContactViewModel.this.onError(event, new Errors(Errors.eError.UNKNOWN_EXCEPTION, "Lost params in loadPrevVideogramByPos method"));
            return;
        }

        if (event.params[1] instanceof Integer && event.params[2] instanceof String) {
            int position = (Integer) event.params[1];
            String contactId = (String) event.params[2];

            ArrayList<Videograms> videogramses = (ArrayList) mContactList.GetListOfVideogramByContactId(contactId, SyncStatus.ON_DEVICE);
            Videograms videograms = null;
            if (!isNext) {
                if (position > 0 && videogramses != null && videogramses.size() > 0) {
                    videograms = videogramses.get(position - 1);
                } else {
                    ContactViewModel.this.onError(event, new Errors(Errors.eError.SHOW_ERROR, "No more previous videograms"));
                }
            } else {
                if (videogramses != null && videogramses.size() > 0 && videogramses.size() > (position + 1)) {
                    videograms = videogramses.get(position + 1);
                } else {
                    ContactViewModel.this.onError(event, new Errors(Errors.eError.SHOW_ERROR, "No more next videograms"));
                }
            }

            /*send back an empty item if something wrong happens*/
            ContactViewModel.this.onReturnDataSuccess(event, videograms);
        } else {
            ContactViewModel.this.onError(event, new Errors(Errors.eError.UNKNOWN_EXCEPTION, "Wrong param type in loadPrevVideogramByPos method"));
        }
    }

    /**
     * Load all videograms on all contact in contact list
     *
     * @param event
     */
    private void loadAllVideograms(VMControllerRequestDataEvent event) {
        if (mContactList != null) {
            ArrayList<Contact> contacts = mContactList.GetListOfContact();
            ArrayList<Videograms> videogramses = new ArrayList<>();
            for (Contact contact : contacts) {
                videogramses.addAll(contact.getVideogrames());
            }
            ContactViewModel.this.onReturnDataSuccess(event, videogramses);
        } else {
            ContactViewModel.this.onError(event, new Errors(Errors.eError.CONTACT_NOT_READY_OR_EMPTY, "The contact list was not loaded yet or empty"));
        }
    }

    /**
     * start the deserialization task for contact list from JSON
     *
     * @param event
     */
    private void startDeserializationTask(final VMControllerRequestDataEvent event) {
        /**
         * Use cache value for contact list
         */
        if (mContactList == null) {
            new DeserializeContactFileTask(new TaskNotifier() {
                @Override
                public void OnTaskSuccess(Object result) {
                    /** cache contacts list to memory*/
                    mContactList = (ContactList) result;
                    ContactViewModel.this.onReturnDataSuccess(event, result);
                }

                @Override
                public void OnTaskError(Errors error) {
                    ContactViewModel.this.onError(event, error);
                }
            }).execute();
        } else {
            ContactViewModel.this.onReturnDataSuccess(event, mContactList);
        }
    }
}
