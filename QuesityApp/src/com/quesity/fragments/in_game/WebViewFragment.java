package com.quesity.fragments.in_game;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.quesity.app.R;
import com.quesity.util.ViewAlpha;

public class WebViewFragment extends Fragment {

	private WebView _w;
	private TextView _loadingView;
	private Animation _animation;
	private PageLoadingListener _page_change_listener;
	private boolean _showProgress = true;
	private static final int CACHE_INITIAL_SIZE = 8*1024*1024;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		stopAudio();
	}
		
	private void stopAudio() {
		AudioManager systemService = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
		systemService.requestAudioFocus(new OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {}
        }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
	}
	
	public WebView getWebView() {
		return _w;
	}
	
	public void loadHTMLData(final String raw_data) {

		String data  = "<html><body style='padding:0; margin:0'>"+raw_data+"</body></html>";
		loadHTMLDataRaw(data);
	}
	
	public void loadHTMLDataRaw(String raw_data) {
		raw_data = raw_data.replaceFirst("<p>", "").replaceFirst("</p>", "");
		_w.loadDataWithBaseURL(null, raw_data, "text/html", "utf-8", null);
		_w.scrollTo(0, 0);
	}

	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Quesity","Creating View...");
		 View frag = inflater.inflate(R.layout.fragment_webview, container);
		 _w = (WebView) frag.findViewById(R.id.webView);
		 WebSettings settings = _w.getSettings();
		 
		 settings.setJavaScriptEnabled(false);
		 settings.setDomStorageEnabled(true);
		 settings.setAppCacheMaxSize(CACHE_INITIAL_SIZE);
		 settings.setSupportZoom(true);
		 String appCachePath = getActivity().getApplicationContext().getCacheDir().getAbsolutePath();
		 settings.setAppCachePath(appCachePath);
		 settings.setAllowFileAccess(true);
		 settings.setAppCacheEnabled(true);
		 settings.setCacheMode(WebSettings.LOAD_DEFAULT);
		 
		 return frag;
	}

	public void setup(String data, final PageLoadingListener load_finished_listener, final PageLoadingListener page_changed_listener) {
		_loadingView = (TextView) getView().findViewById(R.id.loading_lbl);
		_page_change_listener = page_changed_listener;
		 _w.setWebChromeClient( new WebChromeClient() {

			@Override
			public void onReachedMaxAppCacheSize(long requiredStorage,
					long quota, QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(requiredStorage * 2);
				super.onReachedMaxAppCacheSize(requiredStorage, quota, quotaUpdater);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				if (!_showProgress){
					Log.d("WEBVIEW" ,"Not Showing Progress");
					return;
				}
				if ( newProgress >= 100 ) {
					Log.d("WEBVIEW" ,"Completed prefetching");
					_showProgress = false;
					_loadingView.setVisibility(View.INVISIBLE);
					_w.setWebViewClient(new PageChangeWebClient());
					_w.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ONLY);
					load_finished_listener.pageFinishedLoading();
				}else {
					Log.d("WEBVIEW" ,"Progress: " + newProgress);
					_loadingView.setVisibility(View.VISIBLE);
					_loadingView.setText(newProgress + "%");
				}
				
				super.onProgressChanged(view, newProgress);
			}
			 
		 });
		 
		 loadHTMLData(data);
	}

	public void setPageLoadingListener(PageLoadingListener listener) {
		_page_change_listener = listener;
	}
	
	public interface PageLoadingListener { 
		public void pageStartedLoading();
		public void pageFinishedLoading();
	}
	
	private class PageChangeWebClient extends WebViewClient {
		
		@Override
		public void onLoadResource(WebView view, String url) {
			super.onLoadResource(view, url);
			if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB)
				pageLoadEvent(view);
		}

		 
		private void pageLoadEvent(WebView view) {
			if ( _page_change_listener != null ) {
				_page_change_listener.pageStartedLoading();
			}
		}
		 
		@Override
		public void onPageStarted(WebView view, String url,Bitmap b) {
			super.onPageStarted(view, url,b);
			if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB)
				pageLoadEvent(view);
		}

		@Override
		public void onPageFinished(final WebView view, String url) {
			super.onPageFinished(view, url);
			if ( _page_change_listener != null )
				_page_change_listener.pageFinishedLoading();
		}
	}
	
	
}
