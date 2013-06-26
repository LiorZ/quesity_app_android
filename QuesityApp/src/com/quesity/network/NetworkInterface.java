package com.quesity.network;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;

import com.quesity.network.AbstractFetchJSONTask.NetworkParameterGetter;
import com.quesity.network.exceptions.Status401Exception;

public class NetworkInterface {
	public static String getStringContent(String uri, NetworkParameterGetter getter) throws Exception {

	    try {
	        HttpClient client = getter.getHTTPClient();
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
		
	}
}
