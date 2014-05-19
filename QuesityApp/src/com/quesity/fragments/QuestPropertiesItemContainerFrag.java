package com.quesity.fragments;

import com.quesity.app.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class QuestPropertiesItemContainerFrag extends Fragment {
	

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ( savedInstanceState != null)
			_layout = savedInstanceState.getInt(LAYOUT_KEY,0);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (outState == null)
			outState = new Bundle();
		outState.putInt(LAYOUT_KEY, _layout);
	}

	@Override
	public void onResume() {
		super.onResume();
		if ( _manip != null )
			_manip.manipulateFragment(this);
	}
	private static String LAYOUT_KEY = "LAYOUT_ID";
	private int _layout;
	private FragmentManipulator _manip;
	public static Fragment newInstance(int layout_id, FragmentManipulator m) {
		QuestPropertiesItemContainerFrag frag = new QuestPropertiesItemContainerFrag();
		frag.setLayoutId(layout_id);
		if ( m != null ) {
			frag.setViewManipulator(m);
		}
		return frag;
	}
	
	private void setLayoutId(int layout) {
		_layout = layout;
	}
	
	private void setViewManipulator(FragmentManipulator m) {
		_manip = m;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(_layout, container,false);
		if ( _manip != null )
			_manip.manipulateView(view);
//		WebView webview = (WebView) view.findViewById(R.id.quest_description_text_view);
//		webview.loadDataWithBaseURL(null, "<html><body><h1>Hello!</h1></body></html>", "text/html", "utf-8", null);
//		webview.setBackgroundColor(0x00000000);
//		return view;
		return view;
	}
	
	public interface FragmentManipulator {
		public void manipulateView(View root);
		public void manipulateFragment(Fragment frag);
	}

}