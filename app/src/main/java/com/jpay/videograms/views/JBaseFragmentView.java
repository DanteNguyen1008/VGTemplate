package com.jpay.videograms.views;

import com.jpay.videograms.JPayMainActivity;
import com.jpay.videograms.R;


/**
 * Base VIEW class for all view
 * 
 * @author anguyen
 * 
 */
public abstract class JBaseFragmentView extends com.jpay.mvcvm.views.JBaseFragmentViewV4 {
	public void showLoadingDialog(String title, String message, boolean isIndeterminate) {
		if (getActivity() != null) {
			((JPayMainActivity) getActivity()).showLoadingDialog(title, message, isIndeterminate);
		}
	}

	public void showLoadingDialog() {
		if (getActivity() != null) {
			((JPayMainActivity) getActivity()).showLoadingDialog("", getString(R.string.loading) + "...", true);
		}
	}

	public void hideLoadingDialog() {
		if (getActivity() != null) {
			((JPayMainActivity) getActivity()).hideLoadingDialog();
		}
	}

    @Override
    public void onStart() {
        super.onStart();
        onRequestDataOnload();
    }
}
