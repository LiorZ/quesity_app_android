package com.quesity.fragments;

import android.support.v4.app.FragmentManager;

public interface OnDemandFragment {
	public void invokeFragment(FragmentManager manager);
	public int getButtonDrawable();
	public int getPressedButtonDrawable();
	public int getButtonStringId();
}