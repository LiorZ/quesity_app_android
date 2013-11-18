package com.quesity.fragments;

import com.quesity.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class WebViewFragment extends Fragment {

	private WebView _w;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	public void loadHTMLData(String raw_data) {
		String data  = "<html><body style='padding:0; margin:0'>"+raw_data+"</body></html>";
		_w.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
		_w.scrollTo(0, 0);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("Quesity","Creating View...");
		 View frag = inflater.inflate(R.layout.fragment_webview, container);
		 _w = (WebView) frag.findViewById(R.id.webView);
		 _w.getSettings().setJavaScriptEnabled(false);
		 _w.getSettings().setSupportZoom(true);
		 return frag;
	}

}
