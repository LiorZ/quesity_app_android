package com.quesity.util;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.animation.AlphaAnimation;

public class ViewAlpha {
	/**
	 * Should be exactly like View.setAlpha, only it works for earlier APIs too.
	 * @param v
	 * @param alpha
	 */
	
	@SuppressLint("NewApi")
	public static void setAlphaForView(View v, float alpha, long duration) {
		if ( android.os.Build.VERSION.SDK_INT  >= 12 ) {
			v.setAlpha(alpha);
		}else {
			AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
			animation.setDuration(duration);
			animation.setFillAfter(true);
			v.startAnimation(animation);	
		}
	}
}
