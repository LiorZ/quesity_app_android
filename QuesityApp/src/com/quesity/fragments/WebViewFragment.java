package com.quesity.fragments;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.quesity.app.R;
import com.quesity.util.ViewAlpha;

public class WebViewFragment extends Fragment {

	private WebView _w;
	private TextView _loadingView;
	private Animation _animation;
	private boolean _showLoading = true;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_showLoading = true;
	}
	
	public void showLoading (boolean s) {
		_showLoading = s;
	}
	
	public WebView getWebView() {
		return _w;
	}
	
	public void loadHTMLData(String raw_data) {
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
		 if ( _showLoading )
			 setUpLoading(frag);
		 _w.getSettings().setJavaScriptEnabled(false);
		 _w.getSettings().setSupportZoom(true);
		 return frag;
	}

	private void setUpLoading(View frag) {
		_loadingView = (TextView) frag.findViewById(R.id.loading_lbl);
		 if ( _animation == null )
			 _animation = AnimationUtils.loadAnimation(getActivity(), R.anim.tween);
		 _w.setWebViewClient(new WebViewClient(){
			 private TimerTask task;
			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
				if ( !_showLoading )
					return;
				
				ViewAlpha.setAlphaForView(view, 0.0f,500);
				_loadingView.setVisibility(View.VISIBLE);
				_loadingView.startAnimation(_animation);
			}

			@Override
			public void onPageFinished(final WebView view, String url) {
				super.onPageFinished(view, url);
				if ( !_showLoading )
					return;
				task = new TimerTask() {
					
					@Override
					public void run() {
						FragmentActivity activity = getActivity();
						if ( activity == null ) 
							return;
						
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								_loadingView.clearAnimation();
								_loadingView.setVisibility(View.INVISIBLE);
//								view.setVisibility(View.VISIBLE);	
								ViewAlpha.setAlphaForView(view, 1.0f,500);
							}
						});
					
					}
				};
				
				Timer t = new Timer();
				t.schedule(task, 2000);

			}
			 
		 });
	}

}
