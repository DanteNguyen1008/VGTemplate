package com.jpay.mvcvm.utils;

import android.os.Environment;
import android.provider.SyncStateContract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	public static boolean isBlank(String name) {
		return !(name != null && name.trim().length() > 0);
	}

	/**
	 * Check whether the given array contains the given value or not
	 * 
	 * @param array
	 *            - array to check
	 * @param value
	 *            - object value to check
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> boolean contains(T[] array, T value) {
		if (value == null)
			return false;

		if (array.length == 0)
			return false;

		if (value instanceof Number) {
			for (T t : array) {
				if (t == value)
					return true;
			}
		}
		if (value instanceof Comparable<?>) {
			for (T t : array) {
				if (((Comparable<T>) t).compareTo(value) == 0)
					return true;
			}
		} else {
			for (T t : array) {
				if (t.equals(value))
					return true;
			}
		}

		return false;
	}

	public static List<String> getListFiles(File parentDir) {
		ArrayList<String> inFilesPath = new ArrayList<String>();
		File[] files = parentDir.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				inFilesPath.addAll(getListFiles(file));
			} else {
				if (file.getName().endsWith(".jpg") || file.getName().endsWith(".png") || file.getName().endsWith(".jpeg")) {
					inFilesPath.add(file.getAbsolutePath());
				}
			}
		}
		return inFilesPath;
	}
}
