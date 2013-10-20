package com.quesity.activities;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.quesity.application.IQuesityApplication;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.models.JSONModel;
import com.quesity.network.INetworkInterface;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.dagger_modules.NetworkInterfaceModule;

import dagger.ObjectGraph;

public abstract class BaseActivity extends FragmentActivity {
	@Inject
	INetworkInterface _network_interface;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
        ObjectGraph.create(new NetworkInterfaceModule()).inject(this);

	}
	
	public INetworkInterface getNetworkInterface() {
		return _network_interface;
	}
	
}
