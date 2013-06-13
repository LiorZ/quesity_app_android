package com.quesity.network;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public abstract class FetchJSONTask<Result> extends AsyncTask<String, Integer, Result> {
	
	protected abstract Result resolveModel(String json);
	
	@Override
	protected Result doInBackground(String... params) {
		String url = params[0];
		try {
			String json = NetworkInterface.getStringContent(url);
			Result model = resolveModel(json);
			return model;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

}
