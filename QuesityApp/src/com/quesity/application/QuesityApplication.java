package com.quesity.application;

import java.io.File;

import android.app.Activity;
import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger.LogLevel;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.quesity.app.R;

public class QuesityApplication extends Application implements IQuesityApplication {

	private Tracker _tracker;
	
	@Override
	public void onCreate() {
		super.onCreate();
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//		analytics.setDryRun(false);
		analytics.getLogger().setLogLevel(LogLevel.VERBOSE);
		
		_tracker = analytics.newTracker(R.xml.global_tracker);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .build();
		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        										.discCacheSize(200 * 1024 * 1024)
        										.discCacheFileCount(1000)
        										.memoryCache(new LruMemoryCache(5 * 1024 * 1024))
        										.memoryCacheSize(5 * 1024 * 1024)
												.discCache(new UnlimitedDiscCache(cacheDir))
												.defaultDisplayImageOptions(defaultOptions)
												.build();
		ImageLoader.getInstance().init(config);
		
	}
	
	@Override
	public void inject(Activity a) {
	}
	
	public synchronized Tracker getTracker() {
		return _tracker;
	}
	
}
