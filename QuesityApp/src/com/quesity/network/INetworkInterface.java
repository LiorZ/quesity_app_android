package com.quesity.network;

import android.content.Context;

import com.quesity.network.AbstractFetchJSONTask.NetworkParameterGetter;

public interface INetworkInterface {
	public String getStringContent(String uri, NetworkParameterGetter getter,Context c) throws Exception;
}
