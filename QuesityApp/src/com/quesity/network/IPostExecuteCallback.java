package com.quesity.network;

public interface IPostExecuteCallback<R> {
	public void apply(R result);
	public int get401ErrorMessage();
}
