package com.quesity.fragments;

import com.quesity.R;
import com.quesity.controllers.ProgressableProcess;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.quesity.controllers.ProgressableProcess;

public class LoadingProgressFragment extends DialogFragment implements ProgressableProcess{
	
	
	private static final String TAG = "progress_bar_tag";
	@Override
	public void show(FragmentManager manager, String tag) {
		super.show(manager, TAG);
	}

	private String _message;
	private String _title;
	
	public void setMessage(String msg) {
		_message = msg;
	}
	
	public void setTitle(String t) {
		_title = t;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

	    final ProgressDialog dialog = new ProgressDialog(getActivity());
	    dialog.setTitle(_title);
	    dialog.setMessage(_message);
	    dialog.setIndeterminate(true);
	    dialog.setCancelable(false);
	    return dialog;
	}

	@Override
	public void startProgressBar(String title, String message) {
		this.setTitle(title);
		this.setMessage(message);
		FragmentActivity a = getActivity();
		if ( a!= null )
			this.show(a.getSupportFragmentManager(),TAG);
	}

	@Override
	public void stopProgressBar() {
		this.dismiss();
	}

}
