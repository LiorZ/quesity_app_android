package com.quesity.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import com.quesity.R;

public class BackgroundImageFragment extends Fragment {

	public static float ALPHA = 0.2f;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main_view = inflater.inflate(R.layout.fragment_background_image, container,false);
		ImageView logo = (ImageView) main_view.findViewById(R.id.logo);
		setAlphaForView(logo, ALPHA);
		return main_view;
	}
	
	private void setAlphaForView(View v, float alpha) {
		AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
		animation.setDuration(0);
		animation.setFillAfter(true);
		v.startAnimation(animation);
	}
	
}
