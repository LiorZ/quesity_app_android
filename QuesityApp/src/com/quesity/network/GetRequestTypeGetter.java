package com.quesity.network;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;

import android.content.Context;

import com.quesity.network.AbstractFetchJSONTask.NetworkParameterGetter;

public class GetRequestTypeGetter implements NetworkParameterGetter {

	@Override
	public HttpRequestBase getRequestObj() {
		return new HttpGet();
	}

	@Override
	public HttpClient getHTTPClient(Context c) {
		return HTTPClients.getDefaultHttpClient(c);
	}
	
}