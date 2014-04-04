package com.quesity.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.quesity.app.R;
import com.quesity.general.Constants;

public class AsyncImageFragment extends Fragment{
	
	private String _image_src;
	private ImageView _image;
	private ProgressBar _progress;
	
	public static Fragment newInstance(String src){
		AsyncImageFragment img = new AsyncImageFragment();
		Bundle args = new Bundle();
		args.putString(Constants.QUEST_IMAGE_URL, src);
		img.setArguments(args);
		return img;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.async_image_view, container,false);
		_image = (ImageView) view.findViewById(R.id.image_loading_img);
		_progress = (ProgressBar) view.findViewById(R.id.image_loading_progress);
		_image_src = getArguments().getString(Constants.QUEST_IMAGE_URL);
		ImageLoader.getInstance().loadImage(_image_src, new InternalImageLoader());
		return view;
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
			_image.setVisibility(View.VISIBLE);
			_progress.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			// TODO Auto-generated method stub
			super.onLoadingFailed(imageUri, view, failReason);
		}
		
	}

}
