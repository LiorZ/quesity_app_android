package com.quesity.network;

import com.quesity.network.AbstractFetchJSONTask.NetworkParameterGetter;

public interface INetworkInterface {
	public String getStringContent(String uri, NetworkParameterGetter getter) throws Exception;
}
