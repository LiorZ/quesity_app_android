package com.quesity.network;

import org.apache.http.HttpRequest;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.HttpHostConnectException;

import com.quesity.R;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.network.exceptions.Status401Exception;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public abstract class AbstractFetchJSONTask<Result> extends AsyncTask<String, Integer, Result> {
	

	private NetworkParameterGetter _getter;
	private Activity _activity;
	private LoadingProgressFragment _progress;
	public AbstractFetchJSONTask(NetworkParameterGetter getter) {
		_getter = getter;
		_activity = null;
	}
	
	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if ( _progress != null )
			_progress.dismiss();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if ( _progress != null && _activity != null && _activity instanceof FragmentActivity) {
			FragmentActivity activity;
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
	
	public AbstractFetchJSONTask<Result> setActivity(Activity v) {
		_activity = v;
		return this;
	}
		
	protected abstract Result resolveModel(String json);
	
	protected void handle401() {
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
		try {
			String json = NetworkInterface.getStringContent(url,_getter);
			Result model = resolveModel(json);
			return model;
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
		
		return null;
		
	}
	public interface NetworkParameterGetter {
		public HttpRequestBase getRequestObj();
		public HttpClient getHTTPClient();
	}
}
