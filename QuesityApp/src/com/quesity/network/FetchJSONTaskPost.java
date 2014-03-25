package com.quesity.network;

import android.content.Context;

public class FetchJSONTaskPost<Result> extends AbstractFetchJSONTask<Result> {

	public FetchJSONTaskPost(NetworkParameterGetter getter, Class<Result> c,Context ct){
		super(getter,c,ct);
	}
	public FetchJSONTaskPost(Class<Result> c, Context co) {
		super(new SimplePostRequestTypeGetter(co),c,co);
	}


}
