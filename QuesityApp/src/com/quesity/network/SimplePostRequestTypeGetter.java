package com.quesity.network;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.quesity.network.AbstractFetchJSONTask.NetworkParameterGetter;

public class SimplePostRequestTypeGetter implements NetworkParameterGetter {

	protected HttpPost _postObj;
	protected DefaultHttpClient _client;
	
	public SimplePostRequestTypeGetter(){
		_postObj = new HttpPost();
		_client = HTTPClients.getDefaultHttpClient();
	}
	@Override
	public HttpRequestBase getRequestObj() {
		return _postObj;
	}

	@Override
	public HttpClient getHTTPClient() {
		return _client;
	}

}
