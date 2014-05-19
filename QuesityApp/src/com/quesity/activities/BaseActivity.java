package com.quesity.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.quesity.app.R;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.network.INetworkInterface;
import com.quesity.network.NetworkInterface;

public abstract class BaseActivity extends FragmentActivity {
	
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
	
	
}
