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

import android.util.Log;

public class HTTPClients {

	private static DefaultHttpClient _defaultClient;
	private static String session_id;
	private static HTTPClients _me;
	private HTTPClients() {
		
	}
	public static DefaultHttpClient getDefaultHttpClient(){
		if ( _defaultClient == null ) {
			_defaultClient = new DefaultHttpClient();
			_me = new HTTPClients();
			_defaultClient.addResponseInterceptor(_me.new SessionKeeper());
			_defaultClient.addRequestInterceptor(_me.new SessionAdder());
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
