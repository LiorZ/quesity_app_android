package com.quesity.activities;

import java.util.HashMap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.webkit.WebView;

import com.quesity.R;
import com.quesity.controllers.NextPageControllerFactory;
import com.quesity.fragments.ContentPageFragment;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.fragments.LocationPageFragment;
import com.quesity.fragments.MultipleChoiceFragment;
import com.quesity.fragments.OnDemandFragment;
import com.quesity.fragments.InGameMenuFragment.TransitionFragmentInvokation;
import com.quesity.fragments.OpenQuestionFragment;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.models.ModelsFactory;
import com.quesity.models.QuestPage;
import com.quesity.network.FetchJSONTask;
import com.quesity.util.Constants;

public class QuestPageActivity extends FragmentActivity implements TransitionFragmentInvokation, NextPageTransition {

	@Override
	public void transitToNextPage() {
		_transitionFragment.invokeFragment(getSupportFragmentManager());
	}

	private LoadingProgressFragment _progress;
	private WebView _webView;
	private String _quest_id;
	private OnDemandFragment _transitionFragment;
	private MultipleChoiceFragment _multiple_choice_fragment;
	private OpenQuestionFragment _open_question_fragment;
	private ContentPageFragment _content_page_fragment;
	private LocationPageFragment _location_page_fragment;
	private HashMap<String,Fragment> _fragmentMapper;
	public static final String QUEST_PAGE_KEY = "com.quesity.QUEST_PAGE_KEY";
	private QuestPage _currentPage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quest_page);
		_quest_id = getIntent().getStringExtra(QuestsListViewActivity.QUEST_ID);
		_progress = new LoadingProgressFragment(getString(R.string.lbl_loading_page), getString(R.string.lbl_loading));
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.webview_fragment);
		_webView = (WebView) fragment.getView().findViewById(R.id.webView);
		_webView.getSettings().setJavaScriptEnabled(false);
		constructFragmentMapper();
		new FetchQuestPageTask().execute(Constants.SERVER_URL + "/app/"+_quest_id+"/first_page");
	}
	
	private void constructFragmentMapper() {
		addMultipleChoiceFragment();
		addOpenQuestionFragment();
		addContentPageFragment();
		addLocationPageFragment();
		
		_fragmentMapper = new HashMap<String, Fragment>();
		_fragmentMapper.put(Constants.OPEN_QUESTION_PAGE_TYPE,_open_question_fragment);
		_fragmentMapper.put(Constants.QUESTION_PAGE_TYPE, _multiple_choice_fragment);
		_fragmentMapper.put(Constants.STATIC_PAGE_TYPE,_content_page_fragment);
		_fragmentMapper.put(Constants.LOCATION_PAGE_TYPE,_location_page_fragment);
		
	}

	private void addMultipleChoiceFragment() {
		_multiple_choice_fragment = new MultipleChoiceFragment();
	}
	
	
	private void addContentPageFragment() {
		_content_page_fragment = new ContentPageFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(_content_page_fragment, "content fragment");
		transaction.commit();
	}
	
	private void addLocationPageFragment() {
		_location_page_fragment = new LocationPageFragment();
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.add(_location_page_fragment, "locatio fragment");
		transaction.commit();
	}
	
	private void addOpenQuestionFragment() {
		_open_question_fragment = new OpenQuestionFragment();
	}
	
	public QuestPage getCurrentQuestPage() {
		return _currentPage;
	}
	@Override
	public void loadNextPage(String page_id) {
		String address = Constants.SERVER_URL + "/app/"+_quest_id+"/page/"+page_id;
		Log.d(this.getClass().getName(),address);
		new FetchQuestPageTask().execute(address);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quest_page, menu);
		return true;
	}

	
	private class FetchQuestPageTask extends FetchJSONTask<QuestPage> {

		
		private void loadNextFragment(QuestPage page) {
			Fragment fragment = _fragmentMapper.get(page.getPageType());
			_transitionFragment = (OnDemandFragment)fragment;
		}
		@Override
		protected void onPostExecute(QuestPage result) {
			_progress.dismiss();
			if ( result == null ) {
				Log.w("QuestPageActivity", "result page is null");
				SimpleDialogs.getErrorDialog(getString(R.string.lbl_err_wrong_answer),QuestPageActivity.this);
				return;
			}
			_currentPage = result;
			loadNextFragment(result);
			_webView.loadDataWithBaseURL(null, result.getPageContent(), "text/html", "utf-8", null);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			Log.d(this.getClass().getName(),"preparing to load page ..");
			_progress.show(getSupportFragmentManager(), "loading_page_dialog");
		}

		@Override
		protected QuestPage resolveModel(String json) {
			QuestPage questPage = ModelsFactory.getInstance().getQuestPageFromJson(json);
			return questPage;
		}
		
	}

}
