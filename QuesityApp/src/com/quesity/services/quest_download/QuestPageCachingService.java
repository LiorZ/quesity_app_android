package com.quesity.services.quest_download;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.models.ModelsFactory;
import com.quesity.models.QuestPage;

public class QuestPageCachingService extends IntentService {

	private static QuestPageCachingService _instance;
	private QuestPage[] _pages;

	private static final String QUEST_PAGES_KEY = "com.quesity.QuestPageCachingService.QUEST_PAGES_KEY";
	public QuestPageCachingService getInstance(){ 
		return _instance;
	}
	
	public QuestPageCachingService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}
	
	private void startDownloadingPages() {
		ImageFinishedLoading loading_listener = new ImageFinishedLoading();
		for (QuestPage page : _pages) {
			
			String pageContent = page.getPageContent();
			Document doc = Jsoup.parse(pageContent);
			Elements imgs = doc.select("img");
			for (Element element : imgs) {
				String src = element.attr("src");
				ImageLoader.getInstance().loadImage(src, loading_listener);
			}
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		String pages = intent.getStringExtra(QUEST_PAGES_KEY);
		_pages = ModelsFactory.getInstance().getModelFromJSON(pages, QuestPage[].class);
		startDownloadingPages();
		return super.onStartCommand(intent, flags, startId);
	}
	
	private class ImageFinishedLoading extends SimpleImageLoadingListener {


		@Override
		public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		
		}

	}
	

}
