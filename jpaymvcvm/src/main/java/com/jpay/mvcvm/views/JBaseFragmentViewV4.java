package com.jpay.mvcvm.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.jpay.mvcvm.controllers.JBaseController;
import com.jpay.mvcvm.exceptions.MissingControllerException;
import com.jpay.mvcvm.utils.JLog;

import java.lang.reflect.Field;

/**
 * Created by anguyen on 12/10/2015.
 */
public abstract class JBaseFragmentViewV4 extends Fragment implements JView {
    private static final String TAG = JBaseFragmentView.class.getSimpleName();
    /***
     * this controller will be use for handling all app's logistics: Send and
     * Receive data with VMM. Controller life Cycle will be tight to VIEW Life
     * Cycle
     */
    protected JBaseController controller;
    private String tag;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            controller = setUpController();
        } catch (MissingControllerException e) {
            JLog.printStrackTrace(e);
        }
    }

    protected abstract JBaseController setUpController() throws MissingControllerException;

    @Override
    public void onResume() {
        super.onResume();

        if (controller != null) {
            controller.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (controller != null) {
            controller.onPause();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (controller != null) {
            controller.onStop();
        }
    }

    @Override
    public void onDestroy() {
        releaseController();

        super.onDestroy();
    }

    public String getFragmentTag() {
        return tag;
    }

    public void setFragmentTag(String tag) {
        this.tag = tag;
    }

    public boolean onBackPressed() {
        return true;
    }

    /**
     * Work around to handling : No activity bug of Android
     * <p>
     * More info: https://code.google.com/p/android/issues/detail?id=42601
     */
    private static final Field sChildFragmentManagerField;

    static {
        Field f = null;
        try {
            f = Fragment.class.getDeclaredField("mChildFragmentManager");
            f.setAccessible(true);
        } catch (NoSuchFieldException e) {
            JLog.d(TAG, "Error getting mChildFragmentManager field");
            JLog.printStrackTrace(e);
        }
        sChildFragmentManagerField = f;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if (sChildFragmentManagerField != null) {
            try {
                sChildFragmentManagerField.set(this, null);
            } catch (Exception e) {
                JLog.printStrackTrace(e);
                JLog.d(TAG, "Error setting mChildFragmentManager field");
            }
        }
    }

    /**
     * Release current controller
     */
    public void releaseController() {
        if (controller != null) {
            controller.onDestroy();
        }

        controller = null;
    }

    protected void onRequestDataOnload() {
        controller.onRequestDataOnLoad();
    }
}
