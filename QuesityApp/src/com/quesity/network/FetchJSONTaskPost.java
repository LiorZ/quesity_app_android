package com.quesity.network;

public class FetchJSONTaskPost<Result> extends AbstractFetchJSONTask<Result> {

	public FetchJSONTaskPost(NetworkParameterGetter getter, Class<Result> c){
		super(getter,c);
	}
	public FetchJSONTaskPost(Class<Result> c) {
		super(new SimplePostRequestTypeGetter(),c);
	}


}
