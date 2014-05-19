package com.quesity.application;

import java.io.File;

import android.app.Activity;
import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class QuesityApplication extends Application implements IQuesityApplication {

	
	@Override
	public void onCreate() {
		super.onCreate();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisc(true)
        .build();
		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
        										.discCacheSize(200 * 1024 * 1024)
        										.discCacheFileCount(1000)
        										.memoryCache(new LruMemoryCache(2 * 1024 * 1024))
        										.memoryCacheSize(2 * 1024 * 1024)
												.discCache(new UnlimitedDiscCache(cacheDir))
												.defaultDisplayImageOptions(defaultOptions)
												.build();
		ImageLoader.getInstance().init(config);
		
	}
	
	@Override
	public void inject(Activity a) {
	}

}
