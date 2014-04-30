package com.quesity.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.quesity.app.R;
import com.quesity.general.Constants;
import com.viewpagerindicator.CirclePageIndicator;

public class ImageGalleryViewPagerFragment extends Fragment {

	private ViewPager _image_gallery;
	private List<String> _images_url;
	private int _current_item;
	
	private static final int PICTURE_ROTATION_DELAY = 4000;
	private Timer _timer;
	private TimerTask _rotation_task;
	
	public static ImageGalleryViewPagerFragment newInstance(List<String> urls) {
		ImageGalleryViewPagerFragment gallery = new ImageGalleryViewPagerFragment();
		Bundle bundle = new Bundle();
		ArrayList<String> arr = new ArrayList<String>(urls);
				
		bundle.putStringArrayList(Constants.QUEST_IMAGE_ARRAY_KEY, arr);
		gallery.setArguments(bundle);
		return gallery;
	}
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_image_gallery, container,false);
		setArguments();
		_image_gallery = (ViewPager) view.findViewById(R.id.image_gallery_pager);
		_image_gallery.setAdapter(new ImageViewPagerAdapter(getFragmentManager()));
		_current_item = 0;
		_image_gallery.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if ( event.getAction() == MotionEvent.ACTION_UP ){
					if ( _timer != null ){
						stopTimer();
					}
					nextImage();
				}
				return true;
			}
		});
		return view;
	}
	

	@Override
	public void onResume() {
		super.onResume();
		
		_timer = new Timer();
		_rotation_task = new TimerTask() {
			
			@Override
			public void run() {
				if (getActivity() != null) {
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							nextImage();							
						}
					});
				}
			}
		};
		
		_timer.schedule(_rotation_task, PICTURE_ROTATION_DELAY,PICTURE_ROTATION_DELAY);
		
	}
	
	private void stopTimer() {
		_timer.cancel();
		_timer.purge();
		_timer = null;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (_timer != null ){
			stopTimer();
		}
	}
	
	
	private void nextImage() {
		_image_gallery.setCurrentItem(++_current_item % _images_url.size(),true);
	}
	
	private void setArguments() {
		Bundle arguments = getArguments();
		if ( arguments == null ) {
			_images_url = new ArrayList<String>(); 
			return;
		}
			
		_images_url = arguments.getStringArrayList(Constants.QUEST_IMAGE_ARRAY_KEY);
		if (_images_url == null ){
			_images_url = new ArrayList<String>();
			return;
		}
	}
	
	private class ImageViewPagerAdapter extends FragmentStatePagerAdapter {
		
		private List<Fragment> _fragments;

		public ImageViewPagerAdapter(FragmentManager fm) {
			super(fm);
			_fragments = new ArrayList<Fragment>();
			for (String url : _images_url) {
				_fragments.add(AsyncImageFragment.newInstance(url));
			}
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = _fragments.get(position);
			return fragment;
		}

		@Override
		public int getCount() {
			return _fragments.size();
		}
		
	}

}
