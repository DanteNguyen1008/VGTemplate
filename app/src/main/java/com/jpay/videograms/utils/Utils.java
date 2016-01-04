package com.jpay.videograms.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.os.Parcel;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.TextView;

import com.jpay.mvcvm.utils.JLog;
import com.jpay.videograms.Constants;
import com.jpay.videograms.R;
import com.jpay.videograms.models.Contact;
import com.jpay.videograms.ui.JRoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by anguyen on 12/14/2015.
 */
public class Utils extends com.jpay.mvcvm.utils.Utils {

    public static File GetVGRootDirectory() throws NullPointerException {
        if (IsExternalStorageReadable()) {
            File root = Environment.getExternalStorageDirectory();
            return new File(root, Constants.ROOT_VG_DIRECTORY);
        }

        return null;
    }

    public static File getVideogramDataFile() {
        File root = GetVGRootDirectory();

        if (root != null) {
            return new File(root, Constants.CONTACT_VG_FILE_NAME);
        }

        return null;
    }

    private static boolean IsExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    public static Contact generateAllDefaultContact() {
        Contact contact = new Contact();
        contact.id = "-1";
        contact.name = "ALL";

        return contact;
    }

    public static boolean isAllDefaultContact(Contact contact) {
        if (contact == null) {
            return false;
        }

        if (!isBlank(contact.id) && contact.id.equals("-1")) {
            return true;
        }

        return false;
    }

    public static String getKeyframePath(String keyframe) {
        if (!isBlank(keyframe)) {
            return "file:///" + GetVGRootDirectory().getAbsolutePath() + File.separator + Constants.KEYFRAME_DIRECTORY + File.separator + keyframe;
        }

        return "";
    }

    public static DisplayImageOptions getDisplayImageOptionWithRoundedImage(int mIAvatarCircleWidth) {
        return new DisplayImageOptions.Builder().resetViewBeforeLoading(true).cacheOnDisk(true).cacheInMemory(true)
                .displayer(new JRoundedBitmapDisplayer(mIAvatarCircleWidth))
                .showImageForEmptyUri(R.drawable.generic_thumb).showImageForEmptyUri(R.drawable.generic_thumb)
                .showImageOnLoading(R.drawable.generic_thumb).bitmapConfig(Bitmap.Config.ARGB_8888).build();
    }

    /**
     * @param rawDuration
     * @return
     */
    public static String getDurationByLong(long rawDuration) {
        rawDuration = rawDuration / 1000;
        if (rawDuration < 1) {
            return "0 secs";
        }

        if (rawDuration <= 1) {
            return "1 sec";
        }

        /*smaller than an minute*/
        if (rawDuration < 60) {
            return rawDuration + " secs";
        }

        /* smaller than an hour */
        if (rawDuration < (60 * 60)) {
            int minutes = (int) Math.floor(rawDuration / 60);
            int secs = (int) (rawDuration - (minutes * 60));
            return minutes + " mins " + secs + " secs";
        }

        /* smaller than a day */
        if (rawDuration < (60 * 60 * 60)) {
            int hours = (int) Math.floor(rawDuration / 60 / 60);
            int minutes = (int) ((rawDuration - (hours * 60 * 60)) / 60);
            int secs = (int) ((rawDuration - (hours * 60 * 60)) / 60 / 60);

            return hours + " hours " + minutes + " mins " + secs + " secs";
        }

        return "0 secs";
    }

    //output: MMM dd yyyy, HH:mm:ss

    /**
     * @param _createdDate
     * @return
     */
    public static String parseCreatedDate(Date _createdDate) {
        if (_createdDate == null) {
            return "";
        }

        SimpleDateFormat wantDateFormatter = new SimpleDateFormat("MMM dd");
        Calendar createdDateCal = Calendar.getInstance();
        createdDateCal.setTime(_createdDate);
        String AM_PM_String = "";
        switch (createdDateCal.get(Calendar.AM_PM)) {
            case Calendar.AM:
                AM_PM_String = "AM";
                break;
            case Calendar.PM:
                AM_PM_String = "PM";
                break;
        }

        int hourBase12 = createdDateCal.get(Calendar.HOUR);
        if (hourBase12 == 0)
            hourBase12 = 12;

        StringBuilder time = new StringBuilder();
        time.append(hourBase12).append(":");
        if (createdDateCal.get(Calendar.MINUTE) >= 10) {
            time.append(createdDateCal.get(Calendar.MINUTE));
        } else {
            time.append("0").append(createdDateCal.get(Calendar.MINUTE));
        }
        time.append(" ").append(AM_PM_String);

        String displayDateStr;

        if (DateUtils.isToday(createdDateCal.getTimeInMillis())) {
            // show today
            displayDateStr = "Today";
        } else if (Utils.isYesterday(createdDateCal)) {
            // show yesterday
            displayDateStr = "Yesterday";
        } else {
            // show normal date
            displayDateStr = wantDateFormatter.format(_createdDate);
        }

        int year = createdDateCal.get(Calendar.YEAR);
        displayDateStr += year != Calendar.getInstance().get(Calendar.YEAR) ? " " + year : "";
        return displayDateStr + ", " + time;
    }

    /**
     * @return
     */
    public static boolean isYesterday(Calendar checkDate) {
        Calendar today = Calendar.getInstance();

        if (today.get(Calendar.DAY_OF_YEAR) - 1 == checkDate.get(Calendar.DAY_OF_YEAR)) {
            return true;
        }

        return false;
    }

    /**
     * Convert date string from WS with multiple date-time patterns
     *
     * @param strDate
     * @return
     */
    public static java.util.Date ConvertDateFromWebService(String strDate) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.US);
            return format.parse(strDate);
        } catch (ParseException e) {
            JLog.printStrackTrace(e);
        }

        return new Date();
    }

    /**
     * Looking for video file based on given path
     *
     * @param videoPath
     * @return
     * @throws NullPointerException
     * @throws FileNotFoundException
     */
    public static File getVideogramsFile(String videoPath) throws NullPointerException, FileNotFoundException {
        File root = GetVGRootDirectory();

        if (root != null) {
            File videoFile = new File(root, Constants.VIDEO_DIRECTORY + "/" + videoPath);
            if (videoFile.exists()) {
                return videoFile;
            }

            throw new FileNotFoundException("Unable to find the video file");
        }

        return null;
    }

    /**
     * Show error style snackbar
     *
     * @param activity
     * @param snackbarLayout
     * @param msg
     */
    public static void showErrorStyleSnackbar(Activity activity, final CoordinatorLayout snackbarLayout, final String msg) {
        if (activity != null && !isBlank(msg)) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    final Snackbar snackbar = Snackbar.make(snackbarLayout, msg, Snackbar.LENGTH_LONG);
                    snackbar.getView().setBackgroundColor(Color.RED);
                    snackbar.getView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
                    /*set more lines for snackbar*/
                    ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setMaxLines(Constants.SNACKBAR_CONFIG_MAX_LINES);
                    snackbar.show();
                }
            });
        }
    }

    public static void showAlertMessage(final Activity activity,final  String message,final  String possibleButton,final  DialogInterface.OnClickListener possibleButtonOnClick,final  String negativeButton,final  DialogInterface.OnClickListener negativeButtonOnClick) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(activity).setMessage(message).setPositiveButton(possibleButton, possibleButtonOnClick).setNegativeButton(negativeButton, negativeButtonOnClick).show();
            }
        });
    }

    public static void showAlertMessage(final Activity activity, final String message, final String possibleButton, final DialogInterface.OnClickListener possibleButtonOnClick) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(activity).setMessage(message).setPositiveButton(possibleButton, possibleButtonOnClick).show();
            }
        });
    }

    public static void showAlertMessage(final Activity activity, final String message, final String possibleButton) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(activity).setMessage(message).setPositiveButton(possibleButton, null).show();
            }
        });
    }
}
