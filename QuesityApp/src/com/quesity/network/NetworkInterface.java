package com.quesity.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;

import android.content.Context;
import android.util.Log;

import com.quesity.network.AbstractFetchJSONTask.NetworkParameterGetter;
import com.quesity.network.exceptions.GeneralConnectionException;
import com.quesity.network.exceptions.Status401Exception;
import com.quesity.network.exceptions.Status500Exception;

public class NetworkInterface implements INetworkInterface{
	
	private static INetworkInterface _instance;
	private NetworkInterface(){
		
	}
	
	public static INetworkInterface getInstance(){
		if ( _instance == null ){
			_instance = new NetworkInterface();
		}
		return _instance;
	}
	@Override
	public String getStringContent(String uri, NetworkParameterGetter getter,Context c) throws Exception {

		HttpClient client = getter.getHTTPClient(c);
	    try {
	        HttpRequestBase request = getter.getRequestObj();
	        
	        request.setURI(new URI(uri));
	        HttpResponse response = client.execute(request);
	        checkStatusLine(response);
	        InputStream ips  = response.getEntity().getContent();
	        BufferedReader buf = new BufferedReader(new InputStreamReader(ips,"UTF-8"));

	        StringBuilder sb = new StringBuilder();
	        String s;
	        while(true )
	        {
	            s = buf.readLine();
	            if(s==null || s.length()==0)
	                break;
	            sb.append(s);

	        }
	        buf.close();
	        ips.close();
	        return sb.toString();

	        } 
	    finally {
	    	client.getConnectionManager().closeExpiredConnections();
	    }
	} 
	
	private static void checkStatusLine(HttpResponse response) throws Exception{
		StatusLine statusLine = response.getStatusLine();
		if (statusLine == null ){
			return;
		}
		int statusCode = statusLine.getStatusCode();
		if (statusCode == 401) {
			throw new Status401Exception();
		}
		if ( statusCode == 500 ){
			throw new Status500Exception();
		}
		if (statusCode != 200){
			throw new GeneralConnectionException();
		}
		
	}
}
