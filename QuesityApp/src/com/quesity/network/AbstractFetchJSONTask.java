package com.quesity.network;

import javax.inject.Inject;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpHostConnectException;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.quesity.R;
import com.quesity.activities.BaseActivity;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.models.ModelsFactory;
import com.quesity.network.dagger_modules.NetworkInterfaceModule;
import com.quesity.network.exceptions.Status401Exception;

import dagger.ObjectGraph;

public abstract class AbstractFetchJSONTask<Result> extends AsyncTask<String, Integer, Result> implements IFetchJSONTask<Result>{
	

	private NetworkParameterGetter _getter;
	private Activity _activity;
	private LoadingProgressFragment _progress;
	protected boolean _login_success;
	private INetworkInterface _network_interface;
	
	private IPostExecuteCallback _post_execute;

	private Class<Result> _class_to_resolve;
	public AbstractFetchJSONTask(NetworkParameterGetter getter, Class<Result> c) {
		_getter = getter;
		_activity = null;
		_class_to_resolve = c;
	}
	
	public AbstractFetchJSONTask<Result> setPostExecuteCallback(IPostExecuteCallback post) {
		_post_execute = post;
		return this;
	}
	
	public AbstractFetchJSONTask<Result> setNetworkInterface(INetworkInterface i) {
		_network_interface = i;
		return this;
	}
	
	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if ( _progress != null )
			_progress.dismiss();
		if ( _post_execute != null)
			_post_execute.apply(result);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if ( _progress != null && _activity != null && _activity instanceof FragmentActivity) {
			_progress.show(((FragmentActivity)_activity).getSupportFragmentManager(), "tag");
		}else {
			Log.d("AbstractFetch","Progress is null!!!!");
		}
	}
	
	public AbstractFetchJSONTask<Result> setProgressBarHandler(LoadingProgressFragment p, String p_title, String p_msg){
		_progress = p;
		_progress.setTitle(p_title);
		_progress.setMessage(p_msg);
		return this;
	}
	
	public AbstractFetchJSONTask<Result> setNetworkInteractionHandler(INetworkInteraction n) {
		_post_execute = n.getPostExecuteCallback();
		_network_interface = n.getNetworkInterface();
		ProgressBarHandler progressBarHandler = n.getProgressBarHandler();
		if ( progressBarHandler != null ){
			_progress = progressBarHandler.getProgressBarFragment();
			_progress.setTitle(progressBarHandler.getTitle());
			_progress.setMessage(progressBarHandler.getMessage());
		}
		return this;
	}
	
	public AbstractFetchJSONTask<Result> setActivity(Activity v) {
		_activity = v;
		return this;
	}
		
	protected Result resolveModel(String json) {
		return ModelsFactory.getInstance().getModelFromJSON(json,_class_to_resolve);
		
	}
	
	protected void handle401() {
		if ( _post_execute != null && _post_execute.get401ErrorMessage() != -1) {
			showErrorMessage(_post_execute.get401ErrorMessage());
			return;
		}
		showErrorMessage(R.string.error_general_authentication);
	}
	
	protected void showErrorMessage(final int string_id) {
		if ( _activity != null && !_activity.isFinishing() ){
			_activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					final AlertDialog errorDialog = SimpleDialogs.getErrorDialog(_activity.getString(string_id), _activity);
					errorDialog.show();
				}
			});
		}
	}
	protected void handleConnectionException() {
		showErrorMessage(R.string.error_connecting);
	}
	
	@Override
	protected Result doInBackground(String... params) {
		String url = params[0];
		Result model = null;
		try {
			String json = _network_interface.getStringContent(url,_getter);
			model = resolveModel(json);
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			handleConnectionException();
		}
		catch (Status401Exception e_401) {
			e_401.printStackTrace();
			handle401();
		}
		catch (Exception e_general) {
			e_general.printStackTrace();
		}
		
		return model;
		
	}
	public interface NetworkParameterGetter {
		public HttpRequestBase getRequestObj();
		public HttpClient getHTTPClient();
	}
}
