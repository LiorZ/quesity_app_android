package com.quesity.fragments;

public class ProgressBarHandler {
	
	private String _message;
	private String _title;
	private LoadingProgressFragment _fragment;
	
	public ProgressBarHandler(String _message, String _title,
			LoadingProgressFragment _fragment) {
		super();
		this._message = _message;
		this._title = _title;
		this._fragment = _fragment;
	}

	
	public String getMessage() {
		return _message;
	}
	public void setMessage(String _message) {
		this._message = _message;
	}
	public String getTitle() {
		return _title;
	}
	public void setTitle(String _title) {
		this._title = _title;
	}
	public LoadingProgressFragment getProgressBarFragment() {
		return _fragment;
	}
	public void setProgressBarFragment(LoadingProgressFragment _fragment) {
		this._fragment = _fragment;
	}

	
}
