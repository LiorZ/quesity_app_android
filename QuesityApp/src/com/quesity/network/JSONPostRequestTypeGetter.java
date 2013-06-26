package com.quesity.network;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;

public class JSONPostRequestTypeGetter extends SimplePostRequestTypeGetter {
	public JSONPostRequestTypeGetter(String json){
		super();
		try {
			_postObj.setEntity(new StringEntity(json));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		_postObj.setHeader("Accept", "application/json");
	    _postObj.setHeader("Content-type", "application/json");
	}
}
