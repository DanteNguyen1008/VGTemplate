package com.jpay.videograms.tasks;

import com.jpay.videograms.exceptions.Errors;

/**
 * Interface responsible for alerting that a task has finished successfully or not.
 */
public interface TaskNotifier {
	void OnTaskSuccess(Object result);
	void OnTaskError(Errors error);
}
