package com.quesity.activities;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.quesity.application.IQuesityApplication;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.models.JSONModel;
import com.quesity.network.INetworkInterface;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.NetworkInterface;
import com.quesity.network.dagger_modules.NetworkInterfaceModule;

import dagger.ObjectGraph;

public abstract class BaseActivity extends FragmentActivity {
	
	INetworkInterface _network_interface;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		_network_interface = NetworkInterface.getInstance();
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
