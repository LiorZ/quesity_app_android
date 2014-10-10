package com.quesity.controllers;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;

import com.quesity.app.R;
import com.quesity.general.Config;
import com.quesity.models.UsageCode;
import com.quesity.network.FetchJSONTaskPost;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.JSONPostRequestTypeGetter;
import com.quesity.network.NetworkErrorHandler;
import com.quesity.network.NetworkInterface;

public class AccessRestrictionVerification implements OnClickListener {

	private Context _context;
	private CodeProvider _code_provider;
	private NetworkErrorHandler _error_handler;
	private IPostExecuteCallback _action_success;
	
	public AccessRestrictionVerification(Context a) {
			_context = a;
	}
	
	public void setCodeProvider(CodeProvider p) {
		_code_provider = p;
	}
	
	public void setErrorHandler(NetworkErrorHandler handler) {
		_error_handler = handler;
	}
	
	public void setPostExecuteCallback(IPostExecuteCallback s) {
		_action_success = s;
	}
	
	@Override
	public void onClick(View v) {
		if ( _code_provider.getCode() != null && !_code_provider.getCode().getCode().isEmpty() )
			submitTask();
	}
	
	private void submitTask() {
		JSONPostRequestTypeGetter jsonpost = new JSONPostRequestTypeGetter(_context);
		String uri = Config.SERVER_URL + _context.getString(R.string.validate_code);
		try {
			jsonpost.setModel(_code_provider.getCode());
			FetchJSONTaskPost<UsageCode> task = new FetchJSONTaskPost<UsageCode>(jsonpost,UsageCode.class, _context);
			task.setNetworkErrorHandler(_error_handler)
			.setPostExecuteCallback(_action_success)
			.setNetworkInterface(NetworkInterface.getInstance())
			.execute(uri);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	
	
	
}
