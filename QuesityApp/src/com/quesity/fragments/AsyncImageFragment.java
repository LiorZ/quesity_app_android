package com.quesity.fragments;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.quesity.app.R;

public class AsyncImageFragment extends Fragment{
	
	private String _image_src;
	private ImageView _image;
	private ProgressBar _progress;
	private int _layout_res;
	
	public static Fragment newInstance(String src){
		return newInstance(src,R.layout.async_image_view);
	}
	
	public static Fragment newInstance(String src,int layout_res) {
		AsyncImageFragment img = new AsyncImageFragment();
		img.setLayoutRes(layout_res);
		img.setImageSrc(src);
		return img;
	}
	
	public void setLayoutRes(int res) {
		_layout_res = res;
	}
	
	public void setImageSrc(String src){
		_image_src = src;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("ASYNC_IMAGE_VIEW","CREATING ASYNC IMAGE VIEW");
		View view = inflater.inflate(_layout_res, container,false);
		_image = (ImageView) view.findViewById(R.id.image_loading_img);
		_progress = (ProgressBar) view.findViewById(R.id.image_loading_progress);
		if ( _image_src != null ){
			ImageLoader.getInstance().loadImage(_image_src, new InternalImageLoader());
		}else {
			_image.setImageResource(R.drawable.no_image_found);
			showImage();
		}
		return view;
	}
	
	private void showImage() {
		_image.setVisibility(View.VISIBLE);
		_progress.setVisibility(View.INVISIBLE);
	}
	
	private class InternalImageLoader extends SimpleImageLoadingListener{

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			super.onLoadingComplete(imageUri, view, loadedImage);
			_image.setImageBitmap(loadedImage);
			showImage();
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			super.onLoadingFailed(imageUri, view, failReason);
		}
		
	}

}
