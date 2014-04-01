package com.quesity.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.quesity.R;
import com.quesity.general.Constants;
import com.viewpagerindicator.CirclePageIndicator;

public class ImageGalleryViewPagerFragment extends Fragment {

	private ViewPager _image_gallery;
	private List<String> _images_url;
	private int _current_item;
	
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
		_image_gallery.setAdapter(new ImageViewPager(getFragmentManager()));
		_current_item = 0;
		_image_gallery.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if ( event.getAction() == MotionEvent.ACTION_UP ){
					_image_gallery.setCurrentItem(++_current_item % _images_url.size(),true);
				}
				return true;
			}
		});
		return view;
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
	
	private class ImageViewPager extends FragmentPagerAdapter {
		
		private List<Fragment> _fragments;

		public ImageViewPager(FragmentManager fm) {
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
