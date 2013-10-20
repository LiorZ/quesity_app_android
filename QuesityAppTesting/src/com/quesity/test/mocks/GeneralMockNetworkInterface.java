package com.quesity.test.mocks;

import java.io.InputStream;
import java.util.HashMap;

import android.content.Context;
import android.util.Log;

import com.quesity.models.JSONModel;
import com.quesity.models.ModelsFactory;
import com.quesity.network.AbstractFetchJSONTask.NetworkParameterGetter;
import com.quesity.network.INetworkInterface;
import com.quesity.test.utils.JSONReader;

public abstract class GeneralMockNetworkInterface implements INetworkInterface{
	
	private HashMap<String, JSONModel> _url_to_model;
	protected InputStream _json_stream;
	private Context _context;
	
	public GeneralMockNetworkInterface(InputStream jsonStream, Context c){
		_url_to_model = new HashMap<String, JSONModel>();
		_context = c;
		_json_stream = jsonStream;
		try {
			readModelsFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public String getStringContent(String uri, NetworkParameterGetter getter)
			throws Exception {
		JSONModel jsonModel = _url_to_model.get(uri);
		Log.d("QuesityTestApp", "Sending page with id " + jsonModel.getId());
		String jsonFromModel = ModelsFactory.getInstance().getJSONFromModel(jsonModel);
		return jsonFromModel;
	}
	
	protected Context getContext() {
		return _context;
	}
	
	protected void readModelsFromFile() throws Exception{
		String jsonString = JSONReader.getJSONString(_json_stream);
		JSONModel[] json = (JSONModel[]) ModelsFactory.getInstance().getModelFromJSON(jsonString, getModelClass());
		for (JSONModel jsonModel : json) {
			constructURL(jsonModel);
		}
	}
	
	protected abstract Class<?> getModelClass();
	
	protected void pointURLToModel(String url,JSONModel m) {
		_url_to_model.put(url, m);
	}
	
	protected abstract void constructURL(JSONModel m);
	
	

}
