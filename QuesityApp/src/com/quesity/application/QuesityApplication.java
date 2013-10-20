package com.quesity.application;

import android.app.Activity;
import android.app.Application;

import com.quesity.network.dagger_modules.NetworkInterfaceModule;

import dagger.ObjectGraph;

public class QuesityApplication extends Application implements IQuesityApplication {

	
	private ObjectGraph _object_graph;
	@Override
	public void onCreate() {
		super.onCreate();
		
		_object_graph = ObjectGraph.create(new NetworkInterfaceModule());
		
	}
	
	@Override
	public void inject(Activity a) {
		_object_graph.inject(a);
	}

}
