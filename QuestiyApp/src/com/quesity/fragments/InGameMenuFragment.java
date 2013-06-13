package com.quesity.fragments;


import com.quesity.R;
import com.quesity.models.QuestPage;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;

public class InGameMenuFragment extends Fragment {
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_transition = (TransitionFragmentInvokation) activity;
	}

	private TransitionFragmentInvokation _transition;
	public interface TransitionFragmentInvokation {
		public void transitToNextPage();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_ingame_menu,container);
		Button continue_button = (Button) v.findViewById(R.id.btn_continue);
		continue_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				_transition.transitToNextPage();
			}
		});
		return v;
	}

}
