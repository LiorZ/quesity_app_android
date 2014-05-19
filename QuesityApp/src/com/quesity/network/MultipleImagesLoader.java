package com.quesity.network;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class MultipleImagesLoader {
	
	private int _loaded;
	private String[] _uris;
	private ImagesLoaded _loadingListener;
	public MultipleImagesLoader(String[] uris, ImagesLoaded listener){
		_uris = uris;
		_loadingListener = listener;
	}
	
	public void increaseLoaded(){
		_loaded++;
		if ( _loaded == _uris.length ){
			_loadingListener.done();
		}
	}
	
	public void load(){
		ImageLoadingListener l = new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				increaseLoaded();
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				
			}
		};
		for (int i = 0; i < _uris.length; i++) {
			ImageLoader.getInstance().loadImage(_uris[i],l );	
		}
		
	}
	
	public interface ImagesLoaded {
		public void done();
	}
}
