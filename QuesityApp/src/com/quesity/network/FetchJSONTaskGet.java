package com.quesity.network;

import android.content.Context;


public class FetchJSONTaskGet<Result> extends AbstractFetchJSONTask<Result> {
	
	public FetchJSONTaskGet(Class<Result> c, Context ctx) {
		super(new GetRequestTypeGetter(), c,ctx);
	}

}
