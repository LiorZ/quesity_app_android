package com.quesity.activities;

import java.io.IOException;
import java.io.InputStream;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.quesity.R;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.fragments.TitleFragment;
import com.quesity.fragments.WebViewFragment;
import com.quesity.util.FileReader;

public class AboutActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_about);
		WebViewFragment web_fragment = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.about_webview);
		web_fragment.showLoading(false);
		web_fragment.getWebView().setBackgroundColor(0xCCFFFFFF);
		InputStream stream;
		
		try {
			stream = getAssets().open("htmls/about.html");
			String htmlFromFile = getHTMLFromFile(stream);
			web_fragment.loadHTMLDataRaw(htmlFromFile);
		} catch (IOException e) {
			SimpleDialogs.getErrorDialog(getString(R.string.err_display_about), this).show();
		}
	}
	
	private String getHTMLFromFile(InputStream stream) {
		try {
			return FileReader.readFromStream(stream);
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
	}
}
