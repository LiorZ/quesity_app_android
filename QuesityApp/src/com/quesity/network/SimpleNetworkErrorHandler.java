package com.quesity.network;

import android.app.Dialog;
import android.content.Context;

import com.quesity.fragments.SimpleDialogs;

public class SimpleNetworkErrorHandler implements NetworkErrorHandler {

	private int _msg_401;
	private int _msg_500;
	private int _noConnection;
	private Context _context;
	public SimpleNetworkErrorHandler(Context c, int msg_401, int msg_500, int noConnection) {
		_msg_401 = msg_401;
		_msg_500 = msg_500;
		_noConnection = noConnection;
		_context = c;
	}
	
	public SimpleNetworkErrorHandler(Context c) {
		_context = c;
		_msg_401 = -1;
		_msg_500 = -1;
		_noConnection = -1;
	}
	
	private SimpleNetworkErrorHandler(){}
	
	@Override
	public void handle401() {
		if ( _msg_401 > 0 ){
			showMessage(_msg_401);
		}
	}

	protected void showMessage(int err) {
		if ( _context == null )
			return;
		Dialog errorDialog = SimpleDialogs.getErrorDialog(_context.getString(err),_context);
		errorDialog.show();
	}
	@Override
	public void handle500() {
		if ( _msg_500 > 0) {
			showMessage(_msg_500);
		}
	}

	@Override
	public void handleNoConnection() {
		if ( _noConnection > 0 ) {
			showMessage(_noConnection);
		}
	}

	public void setMessage401(int _msg_401) {
		this._msg_401 = _msg_401;
	}

	public void setMessage500(int _msg_500) {
		this._msg_500 = _msg_500;
	}

	public void setMessageNoConnection(int _noConnection) {
		this._noConnection = _noConnection;
	}

}
