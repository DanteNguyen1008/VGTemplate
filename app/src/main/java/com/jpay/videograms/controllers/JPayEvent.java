package com.jpay.videograms.controllers;

import com.jpay.mvcvm.viewmodels.IEventENum;

/**
 * This enum represents all the event bus communication requests.
 *
 * <p>
 *     Each time you add a Controller or a ViewModel, please add to this enum list
 * </p>
 *
 * As a matter of formatting please follow existing format:
 *      <ul>
 *          ViewModel Requests [1...n]
 *              <ul>Events for ViewModel Requests [0...n]</ul>
 *          Controller Requests [1...n]
 *              <ul>Events for Controller Requests [0...n]</ul>
 *      </ul>
 *
 *
 */
public enum JPayEvent implements IEventENum {
    /** Contact requests */
    VM_CONTACT,
        EVENT_LOAD_CONTACT,
        EVENT_LOAD_ALL_VIDEOGRAM,
        EVENT_GET_NEXT_VIDEOGRAM_BY_POS,
        EVENT_GET_PREV_VIDEOGRAM_BY_POS,
        EVENT_LOCALLY_REMOVE_VIDEOGRAM,
        EVENT_UNDO_LOCALLY_REMOVED_VIDEOGRAM,
        EVENT_SET_VIDEOGRAM_TO_BE_DOWNLOADED,

    /** controller events*/
    C_VIDEOGRAM_CONTROLLER,
        C_EVENT_REFRESH_VIDEOGRAMS,
    C_MAINACTIVITY_CONTROLLER,
        C_EVENT_PLAY_VIDEO
}
