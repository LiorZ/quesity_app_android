package com.quesity.test.mocks;

import android.app.Activity;
import android.os.AsyncTask;

import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.models.ModelsFactory;
import com.quesity.network.IFetchJSONTask;
import com.quesity.network.INetworkInteraction;
import com.quesity.network.INetworkInterface;
import com.quesity.network.IPostExecuteCallback;

public class FetchJSONTaskMock<T> implements IFetchJSONTask<T> {

	
	private IPostExecuteCallback _callback;
	private INetworkInterface _network_interface;
	private INetworkInteraction _network_interaction;
	private Class<T> _class;
	public FetchJSONTaskMock (Class<T> c){
		_class =c;
	}
	
	@Override
	public IFetchJSONTask<T> setPostExecuteCallback(IPostExecuteCallback post) {
		_callback = post;
		return this;
	}

	@Override
	public IFetchJSONTask<T> setNetworkInterface(INetworkInterface i) {
		_network_interface = i;
		return this;
	}

	@Override
	public IFetchJSONTask<T> setProgressBarHandler(LoadingProgressFragment p,
			String p_title, String p_msg) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public IFetchJSONTask<T> setActivity(Activity v) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public AsyncTask<String, Integer, T> execute(String... params) {
		try {
			String stringContent = _network_interface.getStringContent(params[0], null);
			T model = ModelsFactory.getInstance().getModelFromJSON(stringContent, _class);
			_callback.apply(model);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public IFetchJSONTask<T> setNetworkInteractionHandler(INetworkInteraction a) {
		if ( a != null ) {
			_network_interaction = a;
			setNetworkInterface(_network_interaction.getNetworkInterface());
			setPostExecuteCallback(_network_interaction.getPostExecuteCallback());
		}
		return this;
	}

}
