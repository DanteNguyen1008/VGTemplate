package com.jpay.videograms.controllers;

import com.jpay.mvcvm.events.VMControllerRequestDataEvent;
import com.jpay.mvcvm.events.VMControllerResponseDataEvent;
import com.jpay.mvcvm.utils.JLog;
import com.jpay.mvcvm.viewmodels.IEventENum;
import com.jpay.videograms.VideoPlayerActivity;
import com.jpay.videograms.exceptions.Errors;
import com.jpay.videograms.models.Videograms;
import com.jpay.videograms.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by anguyen on 12/16/2015.
 */
public class VideoPlayerController extends JBaseController {
    private static final String TAG = VideoPlayerController.class.getSimpleName();
    private int stopPosition = 0;
    private Videograms mPlayingVideo;
    private int mCurrentVideogramsPos;

    @Override
    public String getClassName() {
        return null;
    }

    @Override
    public void onRequestDataFail(VMControllerResponseDataEvent event) {

    }

    @Override
    public IEventENum[] getEventFilter() {
        return new IEventENum[]{JPayEvent.VM_CONTACT};
    }

    @Override
    public void onRequestDataOnLoad() {
        /*Load videofile*/
        try {
            File videoFile = Utils.getVideogramsFile(mPlayingVideo.videoPath);
            videoFile.setReadable(true, false);

            ((VideoPlayerActivity) view).loadVideoViewData(videoFile);
        } catch (FileNotFoundException e) {
            JLog.printStrackTrace(e);
            ((VideoPlayerActivity) view).showVideoNotFoundError();
        } catch (NullPointerException e) {
            JLog.printStrackTrace(e);
        }
    }

    public void initVariable(Videograms videograms, int currentVideogramsPos) {
        this.mPlayingVideo = videograms;
        this.mCurrentVideogramsPos = currentVideogramsPos;

        if (mPlayingVideo == null || Utils.isBlank(mPlayingVideo.videoPath)) {
            /*Show error*/
            ((VideoPlayerActivity) view).showEmptyVideoError();
        }
    }

    public boolean onPlayingVideoError(int what, int extra) {
        ((VideoPlayerActivity) view).showPlayVideoError();
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        stopPosition = ((VideoPlayerActivity) view).getStopPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((VideoPlayerActivity) view).resumeVideo(stopPosition);
    }

    @Override
    public void onRequestDataSuccess(VMControllerResponseDataEvent event) {
        super.onRequestDataSuccess(event);

        VMControllerRequestDataEvent request = event.requestEvent;
        JPayEvent requestEventType = (JPayEvent) request.params[0];

        switch (requestEventType) {
            case EVENT_GET_NEXT_VIDEOGRAM_BY_POS:
                mCurrentVideogramsPos++;
                handleGetVideogramsSuccess(event);
                break;
            case EVENT_GET_PREV_VIDEOGRAM_BY_POS:
                mCurrentVideogramsPos--;
                handleGetVideogramsSuccess(event);
                break;
        }
    }

    @Override
    public void onRequestDataError(VMControllerResponseDataEvent event) {
        super.onRequestDataError(event);
        JLog.e(TAG, event.error.toString());
        if (((Errors) event.error).error.equals(Errors.eError.SHOW_ERROR)) {
            VMControllerRequestDataEvent request = event.requestEvent;
            JPayEvent requestEventType = (JPayEvent) request.params[0];

            switch (requestEventType) {
                case EVENT_GET_NEXT_VIDEOGRAM_BY_POS:
                    ((VideoPlayerActivity) view).displayNoMoreNextVideoError();
                    break;
                case EVENT_GET_PREV_VIDEOGRAM_BY_POS:
                    ((VideoPlayerActivity) view).displayNoMorePreviousVideoError();
                    break;
            }
        }
    }

    public void onNextButtonClicked() {
        /*stop the playing video*/
        ((VideoPlayerActivity) view).stopVideoView();
        getNextVideograms(mCurrentVideogramsPos, mPlayingVideo.contact.id);
    }

    public void onPrevButtonClicked() {
        if (mCurrentVideogramsPos == 0) {
            /* No more video */
            ((VideoPlayerActivity) view).displayNoMorePreviousVideoError();
            return;
        }
        /*stop the playing video*/
        ((VideoPlayerActivity) view).stopVideoView();
        getPrevVideograms(mCurrentVideogramsPos, mPlayingVideo.contact.id);
    }

    private void getPrevVideograms(int currentVideogramsPos, String contactId) {
        sendVMRequest(JPayEvent.VM_CONTACT, new Object[]{JPayEvent.EVENT_GET_PREV_VIDEOGRAM_BY_POS, currentVideogramsPos, contactId});
    }

    private void getNextVideograms(int currentVideogramsPos, String contactId) {
        sendVMRequest(JPayEvent.VM_CONTACT, new Object[]{JPayEvent.EVENT_GET_NEXT_VIDEOGRAM_BY_POS, currentVideogramsPos, contactId});
    }

    private void handleGetVideogramsSuccess(VMControllerResponseDataEvent event) {
        Videograms videograms = (Videograms) event.data;
        mPlayingVideo = videograms;
        onRequestDataOnLoad();
    }
}
