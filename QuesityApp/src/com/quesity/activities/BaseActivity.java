package com.quesity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

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
	
	public void backToHome(int... flags) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        if ( flags != null )
        	for (int i : flags) {
        		intent.addFlags(i);
        	}
        
        startActivity(intent);
	}
	
}
