package com.jpay.videograms.viewmodels;

import com.jpay.videograms.controllers.JPayEvent;
import com.jpay.mvcvm.events.VMControllerRequestDataEvent;
import com.jpay.mvcvm.viewmodels.BaseViewModelManager;

/**
 * Manages all requests sent on the Controller to ViewModel bus, rerouting them to the proper
 * ViewModel.
 * @see BaseViewModelManager
 */
public class ViewModelManager extends BaseViewModelManager {

	/** singleton */
	private static ViewModelManager _instance = null;

	public static ViewModelManager getInstance() {
		if (_instance == null)
			_instance = new ViewModelManager();

		return _instance;
	}

	private ViewModelManager() {
		super();
	}

	@Override
	public void onEventAsync(VMControllerRequestDataEvent event) {
		switch ((JPayEvent) event.eventType) {

            case VM_CONTACT:
                ContactViewModel.getInstance().onEvent(event);
                break;

			default:
				break;
			}
	}
}
