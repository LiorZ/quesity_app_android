package com.quesity.fragments;

import com.quesity.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

public class SimpleProgressFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB)
			return inflater.inflate(R.layout.fragment_simple_progress, null,false);
		else 
			return inflater.inflate(R.layout.fragment_simple_progress_new, null,false);
	}

}
