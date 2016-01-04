package com.jpay.mvcvm.viewmodels;

import java.util.HashMap;

import com.jpay.mvcvm.EventBusStation;
import com.jpay.mvcvm.events.VMControllerRequestDataEvent;
import com.jpay.mvcvm.events.VMControllerResponseDataEvent;

public abstract class BaseViewModelManagerWithPool extends BaseViewModelManager {
	/* task pool names */
	HashMap<String, Integer> mTaskPools;

	protected BaseViewModelManagerWithPool() {
		mTaskPools = new HashMap<String, Integer>();
	}

	/* tasks pool management */
	/**
	 * Checking the specified task is still running in the pool or not
	 * 
	 * @param taskName
	 * @return
	 */
	public boolean isTaskRunningInPool(String taskName) {
		return mTaskPools.containsKey(taskName);
	}

	/**
	 * Add new task to pool
	 * 
	 * @param taskName
	 */
	public void addTaskToPool(String taskName, int hashCode) {
		mTaskPools.put(taskName, hashCode);
	}

	/**
	 * remove finished task out of pool
	 * 
	 * @param taskName
	 * @return - current hashCode belong to the task
	 */
	public Integer removeTaskFromPool(String taskName) {
		return mTaskPools.remove(taskName);
	}

	public void updateHashCodeForRunningTask(String taskName, int newHashCode) {
		if (mTaskPools.containsKey(taskName)) {
			mTaskPools.remove(taskName);
		}

		mTaskPools.put(taskName, newHashCode);
	}

	/**
	 * Push local data success to controller
	 * 
	 * @param eventType
	 * @param result
	 * @param data
	 * @param error
	 * @param backendResult
	 */
	public void pushEventLocalDataSuccessToController(VMControllerRequestDataEvent requestEvent, Object result) {
		EventBusStation.getInstance().getViewModelControllerEventBus().post(new VMControllerResponseDataEvent(requestEvent, result, null, true));
	}

	/**
	 * Push event : task is still running to view
	 * 
	 * @param eventType
	 * @param isStillRunning
	 * @param hashCode
	 */
	public void pushEventTaskIsRunningToController(VMControllerRequestDataEvent requestEvent) {
		EventBusStation.getInstance().getViewModelControllerEventBus().post(new VMControllerResponseDataEvent(requestEvent, true));
	}
}
