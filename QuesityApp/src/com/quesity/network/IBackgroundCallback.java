package com.quesity.network;

public interface IBackgroundCallback<Model> {
	public Model apply(String ... urls) throws Exception;
}
