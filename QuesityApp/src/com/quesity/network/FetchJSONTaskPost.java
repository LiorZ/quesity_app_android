package com.quesity.network;

public abstract class FetchJSONTaskPost<Result> extends AbstractFetchJSONTask<Result> {

	public FetchJSONTaskPost(NetworkParameterGetter getter){
		super(getter);
	}
	public FetchJSONTaskPost() {
		super(new SimplePostRequestTypeGetter());
	}


}
