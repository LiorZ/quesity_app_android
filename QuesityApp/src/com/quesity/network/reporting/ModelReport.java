package com.quesity.network.reporting;

import java.net.URI;
import java.net.URISyntaxException;

import android.content.Context;

import com.b2msolutions.reyna.Header;
import com.b2msolutions.reyna.Message;
import com.b2msolutions.reyna.services.StoreService;
import com.quesity.models.JSONModel;
import com.quesity.models.ModelsFactory;

public class ModelReport {
	
	private JSONModel _model;
	private Context _context;
	public ModelReport(JSONModel model, Context c) {
		_model = model;
		_context = c;
	}
	
	public void send(String uri) {
	    String json_string = ModelsFactory.getInstance().getJSONFromModel(_model);
	    Header[] headers = {};
		Message message = new Message(
		        uri,
		        json_string,
		        headers
		);
		StoreService.start(_context, message);
			
	}
}
