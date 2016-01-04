package com.jpay.videograms.tasks;

import android.os.AsyncTask;

import com.google.gson.JsonSyntaxException;
import com.jpay.videograms.exceptions.Errors;
import com.jpay.mvcvm.utils.JLog;

import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class JBaseTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	private TaskNotifier mNotifier;

	public JBaseTask(TaskNotifier notifier) {
		this.mNotifier = notifier;
	}

	public void notifySuccessEvent(Object result) {
		if (mNotifier != null) {
			mNotifier.OnTaskSuccess(result);
		}
	}

	public void notifyErrorEvent(Errors error) {
		if (mNotifier != null) {
			mNotifier.OnTaskError(error);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Result doInBackground(Params... params) {
		
		if (performParameterValidation()) {
			if (params == null || params.length == 0) {
				return (Result) new Errors(Errors.eError.UNKNOWN_EXCEPTION, "Lost params in " + getClassName());
			}
		}

		try {
			return (Result) run(params);
		} 
		catch (FileNotFoundException e) {
			JLog.printStrackTrace(e);
			return (Result) new Errors(Errors.eError.FILE_NOT_FOUND_ERROR, getClassName() + " - Error: Either the file path is incorrect or the file does not exists!");
		} 
		catch (IOException e) {
			JLog.printStrackTrace(e);
			return (Result) new Errors(Errors.eError.IO_ERROR, getClassName() + " - " + e.getMessage());
		} 
		catch (JsonSyntaxException e) {
			JLog.printStrackTrace(e);
			return (Result) new Errors(Errors.eError.JsonSyntaxException, getClassName() + " - " + e.getMessage());
		}
		catch (Exception e) {
			JLog.printStrackTrace(e);
			return (Result) new Errors(Errors.eError.UNKNOWN_EXCEPTION, getClassName() + " - " + e.getMessage());
		}
	}

	@Override
	protected void onPostExecute(Result results) {
		super.onPostExecute(results);

		/* got exception? */
		if (results instanceof Errors) {
			notifyErrorEvent((Errors) results);
			return;
		}

		notifySuccessEvent(results);
	}

	protected abstract boolean performParameterValidation();

	protected abstract String getClassName();

	protected abstract Object run(Object... params) throws Exception;
}
