package com.quesity.network;


public class FetchJSONTaskGet<Result> extends AbstractFetchJSONTask<Result> {
	
	public FetchJSONTaskGet(Class<Result> c) {
		super(new GetRequestTypeGetter(), c);
	}

}
