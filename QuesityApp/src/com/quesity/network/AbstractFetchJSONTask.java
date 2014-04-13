package com.quesity.network;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpHostConnectException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.quesity.app.R;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.models.ModelsFactory;
import com.quesity.network.exceptions.Status401Exception;
import com.quesity.network.exceptions.Status500Exception;

public abstract class AbstractFetchJSONTask<Result> extends AsyncTask<String, Integer, Result> implements IFetchJSONTask<Result>{
	

	private NetworkParameterGetter _getter;
	private Activity _activity;
	protected LoadingProgressFragment _progress;
	protected boolean _login_success;
	private INetworkInterface _network_interface;
	private NetworkErrorHandler _handler;
	protected IPostExecuteCallback _post_execute;
	private IBackgroundCallback<Result> _background_callback;

	private Class<Result> _class_to_resolve;
	private Context _context;
	public AbstractFetchJSONTask(NetworkParameterGetter getter, Class<Result> c,Context context) {
		_getter = getter;
		_activity = null;
		_class_to_resolve = c;
		_context = context;
	}
	
	public AbstractFetchJSONTask<Result> setNetworkErrorHandler(NetworkErrorHandler h) {
		_handler = h;
		return this;
	}
	
	public AbstractFetchJSONTask<Result> setBackgroundCallback(IBackgroundCallback<Result> c) {
		_background_callback = c;
		return this;
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
		if (_handler != null) {
			_handler.handle401();
			return;
		}
		if ( _post_execute != null && _post_execute.get401ErrorMessage() != -1) {
			showErrorMessage(_post_execute.get401ErrorMessage());
			return;
		}
		showErrorMessage(R.string.error_general_authentication);
	}
	
	protected void handle500() {
		if ( _handler != null ) {
			_handler.handle500();
		}
	}
	
	protected void showErrorMessage(final int string_id) {
		if ( _activity != null && !_activity.isFinishing() ){
			_activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					final Dialog errorDialog = SimpleDialogs.getErrorDialog(_activity.getString(string_id), _activity);
					errorDialog.show();
				}
			});
		}
	}
	protected void handleConnectionException() {
		if (_handler != null){
			_handler.handleNoConnection();
			return;
		}
		showErrorMessage(R.string.error_connecting);
	}
	
	@Override
	protected Result doInBackground(String... params) {
		Result model = null;
		try {
			if ( _background_callback != null ) {
				model = _background_callback.apply(params);
			}else {
				String url = params[0];
				String json = _network_interface.getStringContent(url,_getter,_context);
				model = resolveModel(json);
			}
		} catch (HttpHostConnectException e) {
			e.printStackTrace();
			handleConnectionException();
		}
		catch (Status401Exception e_401) {
			e_401.printStackTrace();
			handle401();
		}
		catch(Status500Exception e_500) {
			e_500.printStackTrace();
			handle500();
		}
		catch (Exception e_general) {
			e_general.printStackTrace();
		}
		
		return model;
	}
	public interface NetworkParameterGetter {
		public HttpRequestBase getRequestObj();
		public HttpClient getHTTPClient(Context c);
	}
}
