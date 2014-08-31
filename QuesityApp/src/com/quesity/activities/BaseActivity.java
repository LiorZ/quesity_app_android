package com.quesity.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.quesity.app.R;
import com.quesity.application.QuesityApplication;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.network.INetworkInterface;
import com.quesity.network.NetworkInterface;

public abstract class BaseActivity extends FragmentActivity {
	
	@Override
	protected void onStart() {
		super.onStart();
		sendScreenView();
	}

	INetworkInterface _network_interface;
	protected LoadingProgressFragment _progress;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		_network_interface = NetworkInterface.getInstance();
		_progress = new LoadingProgressFragment();
	}
	
	public INetworkInterface getNetworkInterface() {
		return _network_interface;
	}
	
	public LoadingProgressFragment getProgressFragment() {
		return _progress;
	}

	@Override
	protected void onResume() {
		super.onResume();
		//facebook reporter:
		com.facebook.AppEventsLogger.activateApp(this, getString(R.string.applicationId));
	}
	
	protected abstract String getScreenViewPath();
	
	protected void sendScreenView() {
        // Get tracker.
        Tracker t = ((QuesityApplication) getApplication()).getTracker();

        // Set screen name.
        // Where path is a String representing the screen name.
        String path = getScreenViewPath();
        t.setScreenName(path);

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	
}
