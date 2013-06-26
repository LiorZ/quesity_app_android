package com.quesity.network;


public abstract class FetchJSONTask<Result> extends AbstractFetchJSONTask<Result> {
	
	public FetchJSONTask() {
		super(new GetRequestTypeGetter());
	}

}
