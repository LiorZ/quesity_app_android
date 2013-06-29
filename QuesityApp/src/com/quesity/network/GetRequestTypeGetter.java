package com.quesity.network;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.quesity.network.AbstractFetchJSONTask.NetworkParameterGetter;

public class GetRequestTypeGetter implements NetworkParameterGetter {

	@Override
	public HttpRequestBase getRequestObj() {
		return new HttpGet();
	}

	@Override
	public HttpClient getHTTPClient() {
		return HTTPClients.getDefaultHttpClient();
	}
	
}