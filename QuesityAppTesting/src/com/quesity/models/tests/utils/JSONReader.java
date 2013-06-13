package com.quesity.models.tests.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;

public class JSONReader {
	public static String getJSONString(InputStream stream) throws Exception{
		String json = "";
		InputStreamReader reader =new InputStreamReader(stream);
		BufferedReader r = new BufferedReader(reader);
		String l="";
		while ((l = r.readLine()) != null) {
			json = json + l;
		}
		if ( json == null || json.isEmpty() ){
			throw new JSONException("JSON Is empty or null");
		}
		return json;
			
	}
}
