package com.quesity.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quesity.R;
import com.quesity.general.Constants;
import com.viewpagerindicator.CirclePageIndicator;

public class ImageGalleryViewPagerFragment extends Fragment {

	private ViewPager _image_gallery;
	private List<String> _images_url;
	
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
		
		CirclePageIndicator indicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
		indicator.setViewPager(_image_gallery);
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

		public ImageViewPager(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return AsyncImageFragment.newInstance(_images_url.get(position));
		}

		@Override
		public int getCount() {
			return _images_url.size();
		}
		
	}
	
}
