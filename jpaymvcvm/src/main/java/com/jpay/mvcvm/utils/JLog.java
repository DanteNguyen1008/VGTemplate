package com.jpay.mvcvm.utils;

import android.util.Log;

import com.jpay.mvcvm.BuildConfig;

public class JLog {
	
	public static int d(String TAG, String msg, Throwable tr) {
		if(BuildConfig.DEBUG) {
			return Log.d(TAG, msg, tr);
		}
		return 0;
	}
	
	public static int d(String TAG, String msg) {
		return d(TAG, msg, null);
	}
	
	public static int e(String TAG, String msg, Throwable tr) {
		if(BuildConfig.DEBUG) {
			return Log.e(TAG, msg, tr);
		}
		return 0;
	}
	
	public static int e(String TAG, String msg) {
		return e(TAG, msg, null);
	}
	
	public static int i(String TAG, String msg, Throwable tr) {
		if(BuildConfig.DEBUG) {
			return Log.i(TAG, msg, tr);
		}
		return 0;
	}
	
	public static int i(String TAG, String msg) {
		return i(TAG, msg, null);
	}
	
	public static int w(String TAG, String msg, Throwable tr) {
		if(BuildConfig.DEBUG) {
			return Log.w(TAG, msg, tr);
		}
		return 0;
	}
	
	public static int w(String TAG, String msg) {
		return w(TAG, msg, null);
	}
	
	public static int w(String TAG, Throwable tr) {
		return w(TAG, null, tr);
	}
	
	public static int wtf(String TAG, String msg, Throwable tr) {
		if(BuildConfig.DEBUG) {
			return Log.wtf(TAG, msg, tr);
		}
		return 0;
	}
	
	public static int wtf(String TAG, String msg) {
		return wtf(TAG, msg, null);
	}
	
	public static int wtf(String TAG, Throwable tr) {
		return wtf(TAG, null, tr);
	}

	public static int v(String TAG, String msg, Throwable tr) {
		if(BuildConfig.DEBUG) {
			return Log.v(TAG, msg, tr);
		}
		return 0;
	}
	
	public static int v(String TAG, String msg) {
		return v(TAG, msg, null);
	}
	
	public static void printStrackTrace(Exception e) {
		if(BuildConfig.DEBUG)
			e.printStackTrace();
	}
	
	public static void printStrackTrace(Error e) {
		if(BuildConfig.DEBUG)
			e.printStackTrace();
	}
}
