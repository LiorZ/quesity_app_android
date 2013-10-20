package com.quesity.test.mocks;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.app.Application;

import com.quesity.application.IQuesityApplication;

import dagger.ObjectGraph;

public class QuesityApplicationMock extends Application implements IQuesityApplication{


	private InputStream _stream;
	private ObjectGraph _object_graph;

	public void setStream(InputStream s) {
		_stream = s;
	}
	
	@Override
	public void inject(Activity a) {
		_object_graph = ObjectGraph.create(new QuestPageModule(_stream, a));
		_object_graph.inject(a);
	}
	
	
}
