package com.quesity.fragments;

import com.quesity.R;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TitleFragment extends Fragment{

	private TextView _titleView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_title, container,false);
		Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Andada-Regular.ttf");
		
		_titleView = (TextView) view.findViewById(R.id.title_main_screen);
		_titleView.setTypeface(tf);
		return view;
	}

	public void SetAppearance(int resid) {
		FragmentActivity activity = getActivity();
		if ( activity != null )
			_titleView.setTextAppearance(activity, resid);
	}
	
	public void setText(String txt) {
		FragmentActivity activity = getActivity();
		if ( activity != null )
			_titleView.setText(txt);
			
	}
}
