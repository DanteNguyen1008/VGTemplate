package com.jpay.videograms.exceptions;

import com.jpay.mvcvm.exceptions.IBaseError;

public class Errors implements IBaseError {

	public enum eError {
		UNKNOWN_EXCEPTION,
		PARSING_ERROR,
		IO_ERROR,
		LOGIC_ERROR,
		VIEW_ERROR, 
		MODEL_ERROR, 
		CONTROLLER_ERROR,
		FILE_NOT_FOUND_ERROR,
		JsonSyntaxException,
        CONTACT_NOT_READY_OR_EMPTY,
        SHOW_ERROR,
	}

	public eError error;
	public String errorMessage;

	public Errors(eError error, String errorMessage) {
		this.error = error;
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return "Errors: " + error.name() + " - message: " + errorMessage;
	}
}
