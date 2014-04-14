package com.quesity.network;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;

import com.quesity.models.JSONModel;
import com.quesity.models.ModelsFactory;

import android.content.Context;

public class JSONPostRequestTypeGetter extends SimplePostRequestTypeGetter {
	
	public JSONPostRequestTypeGetter(Context c) {
		super(c);
		setHeaders();
	}
	
	private void setHeaders() {
		_postObj.setHeader("Accept", "application/json");
	    _postObj.setHeader("Content-type", "application/json");
	}
	
	public JSONPostRequestTypeGetter(String json,Context c){
		super(c);
		try {
			_postObj.setEntity(new StringEntity(json));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		setHeaders();
	}
	
	public void setJSON(String json) throws UnsupportedEncodingException {
		_postObj.setEntity(new StringEntity(json));
	}
	
	public void setModel(JSONModel model) throws UnsupportedEncodingException{
		String json = ModelsFactory.getInstance().getJSONFromModel(model);
		setJSON(json);
	}
}
