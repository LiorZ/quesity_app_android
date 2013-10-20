package com.quesity.network;

import com.quesity.fragments.ProgressBarHandler;

public interface INetworkInteraction {
	public IPostExecuteCallback getPostExecuteCallback();
	public INetworkInterface getNetworkInterface();
	public ProgressBarHandler getProgressBarHandler();
}
