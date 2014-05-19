package com.quesity.network;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HttpContext;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.quesity.general.Constants;

public class HTTPClients {

	private static DefaultHttpClient _defaultClient;
	private static String session_id = null;
	private static HTTPClients _me;
	private HTTPClients() {
		
	}
	public static DefaultHttpClient getDefaultHttpClient(Context c){
		if ( _defaultClient == null ) {
			_defaultClient = new DefaultHttpClient();
			_me = new HTTPClients();
			_defaultClient.addResponseInterceptor(_me.new SessionKeeper());
			_defaultClient.addRequestInterceptor(_me.new SessionAdder());
		}
		if ( c != null ) {
			SharedPreferences shared_pref = PreferenceManager.getDefaultSharedPreferences(c);
			String session_from_store = shared_pref.getString(Constants.SESSION_ID_KEY, null);
			if ( session_id == null ) {
				session_id = session_from_store;
			}else if ( session_id != null && session_from_store == null) {
				shared_pref.edit().putString(Constants.SESSION_ID_KEY, session_id).commit();
			}
		}

		return _defaultClient;
	}
	
	private class SessionAdder implements HttpRequestInterceptor {
		
		@Override
		public void process(HttpRequest request, HttpContext context)
				throws HttpException, IOException {
			Log.d("SessionKeeper", "Adding session with the following string: " + session_id);
			if ( session_id != null ) {
				request.setHeader("Cookie", session_id);
			}else {
			}
		}
		
	}
	
	private class SessionKeeper implements HttpResponseInterceptor {

		@Override
		public void process(HttpResponse response, HttpContext context)
				throws HttpException, IOException {
			Header[] headers = response.getHeaders("Set-Cookie");
			if ( headers != null && headers.length == 1 ){
				Log.d("SessionKeeper", "Keeping session with the following string: " + headers[0].getValue());
				session_id = headers[0].getValue();
			}
		}
		
	}
}
