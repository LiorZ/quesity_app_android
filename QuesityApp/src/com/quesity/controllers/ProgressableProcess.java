package com.quesity.controllers;

public interface ProgressableProcess {
	public void startProgressBar(String title,String msg);
	public void stopProgressBar();
}
