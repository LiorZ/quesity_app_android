package com.quesity.network;

import android.app.Activity;
import android.os.AsyncTask;

import com.quesity.fragments.LoadingProgressFragment;

public interface IFetchJSONTask<T> {
	public IFetchJSONTask<T> setPostExecuteCallback(IPostExecuteCallback post);
	public IFetchJSONTask<T> setNetworkInterface(INetworkInterface i);
	public IFetchJSONTask<T> setProgressBarHandler(LoadingProgressFragment p, String p_title, String p_msg);
	public IFetchJSONTask<T> setActivity(Activity v);
	public AsyncTask<String, Integer,T> execute(String ... params);
	public IFetchJSONTask<T> setNetworkInteractionHandler(INetworkInteraction a);
}
