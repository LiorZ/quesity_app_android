package com.quesity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.quesity.R;
import com.quesity.util.ViewAlpha;

public class BackgroundImageFragment extends Fragment {

	public static float ALPHA = 0.2f;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main_view = inflater.inflate(R.layout.fragment_background_image, container,false);
		ImageView logo = (ImageView) main_view.findViewById(R.id.logo);
		ViewAlpha.setAlphaForView(logo, ALPHA,0);
		return main_view;
	}
	
	
}
