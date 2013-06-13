package com.quesity.fragments;

import com.quesity.R;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class LoadingProgressFragment extends DialogFragment{

	private String _message;
	private String _title;
	public LoadingProgressFragment(String message, String title) {
		_message = message;
		_title = title;
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

}