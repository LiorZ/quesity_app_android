package com.quesity.activities;

import java.util.HashMap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;

import com.quesity.R;
import com.quesity.controllers.ProgressableProcess;
import com.quesity.fragments.ContentPageFragment;
import com.quesity.fragments.InGameMenuFragment.TransitionFragmentInvokation;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.fragments.LocationPageFragment;
import com.quesity.fragments.MultipleChoiceFragment;
import com.quesity.fragments.OnDemandFragment;
import com.quesity.fragments.OpenQuestionFragment;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.fragments.StallFragment;
import com.quesity.fragments.WebViewFragment;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.models.ModelsFactory;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;
import com.quesity.network.FetchJSONTask;

public class QuestPageActivity extends FragmentActivity implements TransitionFragmentInvokation, NextPageTransition, ProgressableProcess {

	public static final String QUEST_PAGE_KEY = "com.quesity.QUEST_PAGE_KEY";
	
	private LoadingProgressFragment _progress;
	private WebViewFragment _webViewFragment;
	private String _quest_id;
	private OnDemandFragment _transitionFragment;
	private MultipleChoiceFragment _multiple_choice_fragment;
	private OpenQuestionFragment _open_question_fragment;
	private ContentPageFragment _content_page_fragment;
	private LocationPageFragment _location_page_fragment;
	private StallFragment _stall_fragment;
	private HashMap<String,Fragment> _fragmentMapper;
	private QuestPage _currentPage;
	
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("QuestPageActivity","Activity Paused");
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		String page_json = ModelsFactory.getInstance().getJSONFromQuestPage(_currentPage);
		Log.d("QuestPageActivity", "Saving instance state with json");
		outState.putString(QUEST_PAGE_KEY,page_json);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("QuestPageActivity", "OnCreate - QuestPageActivity");
		setContentView(R.layout.activity_quest_page);
		_quest_id = getIntent().getStringExtra(QuestsListViewActivity.QUEST_ID);
		_progress = new LoadingProgressFragment();
		_progress.setCancelable(false);
		_webViewFragment = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webview_fragment);
		
		constructFragmentMapper();
		
		restoreSavedPage(savedInstanceState);
	}
	
	
	private void restoreSavedPage(Bundle savedInstanceState) {
		String page = null;
		if ( savedInstanceState != null ){
			page = savedInstanceState.getString(QUEST_PAGE_KEY);
		}
		Log.d("QuestPageActivity", "Restoring instance state");
		if ( page != null ) {
			_currentPage = ModelsFactory.getInstance().getQuestPageFromJson(page);
			refreshQuestPage(_currentPage);
		}else {
			Log.d("QuesityPageActivity","Got a null page from the saved instance");
			Log.d("QuestPageActivity","Current page is null, downloading the first page");
			String url = Config.SERVER_URL + "/app/"+_quest_id+"/first_page";
			new FetchQuestPageTask().execute(url);
		}
	}
	
	@Override
	public void transitToNextPage() {
		QuestPageLink[] links = _currentPage.getLinks();
		if ( links.length == 0 ){
			finishQuest();
			return;
		}
		
		_transitionFragment.invokeFragment(getSupportFragmentManager());
	}
	
	public void returnToMainPage() {
    	Intent intent = new Intent(QuestPageActivity.this, QuestsListViewActivity.class);
    	startActivity(intent);
	}
	
	public void finishQuest(){
		AlertDialog okOnlyDialog = SimpleDialogs.getOKOnlyDialog(getString(R.string.lbl_quest_over), this, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				returnToMainPage();
			}
		});
		okOnlyDialog.show();
	}
	
	
	private void constructFragmentMapper() {
		addMultipleChoiceFragment();
		addOpenQuestionFragment();
		addContentPageFragment();
		addLocationPageFragment();
		addStallFragment();
		
		_fragmentMapper = new HashMap<String, Fragment>();
		_fragmentMapper.put(Constants.OPEN_QUESTION_PAGE_TYPE,_open_question_fragment);
		_fragmentMapper.put(Constants.QUESTION_PAGE_TYPE, _multiple_choice_fragment);
		_fragmentMapper.put(Constants.STATIC_PAGE_TYPE,_content_page_fragment);
		_fragmentMapper.put(Constants.LOCATION_PAGE_TYPE,_location_page_fragment);
		_fragmentMapper.put(Constants.STALL_PAGE_TYPE, _stall_fragment);
	}

	private void addMultipleChoiceFragment() {
		_multiple_choice_fragment = new MultipleChoiceFragment();
	}
	
	private void addStallFragment() {
		_stall_fragment = new StallFragment();
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
		transaction.add(_location_page_fragment, "location fragment");
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
		String address = Config.SERVER_URL + "/app/"+_quest_id+"/page/"+page_id;
		Log.d(this.getClass().getName(),address);
		new FetchQuestPageTask().execute(address);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quest_page, menu);
		return true;
	}
	
	public void refreshQuestPage(QuestPage page) {
		setTitle(page.getPageName());
		Fragment fragment = _fragmentMapper.get(page.getPageType());
		_transitionFragment = (OnDemandFragment)fragment;
		_webViewFragment.loadHTMLData(page.getPageContent());
	}
	
	private class FetchQuestPageTask extends FetchJSONTask<QuestPage> {

		public FetchQuestPageTask(){
			super();
			setActivity(QuestPageActivity.this).
			setProgressBarHandler(_progress, getString(R.string.lbl_loading_page), getString(R.string.lbl_loading));
		}
		
		@Override
		protected void onPostExecute(QuestPage result) {
			super.onPostExecute(result);
			if ( result == null ) {
				Log.w("QuestPageActivity", "result page is null");
				SimpleDialogs.getErrorDialog(getString(R.string.lbl_err_wrong_answer),QuestPageActivity.this);
				return;
			}
			_currentPage = result;
			refreshQuestPage(result);
		}

		@Override
		protected void onPreExecute() {
			Log.d(this.getClass().getName(),"preparing to load page ..");
			super.onPreExecute();
		}

		@Override
		protected QuestPage resolveModel(String json) {
			QuestPage questPage = ModelsFactory.getInstance().getQuestPageFromJson(json);
			return questPage;
		}
		
	}

	@Override
	public void startProgressBar(String title, String msg) {
	    _progress.setTitle(title);
	    _progress.setMessage(msg);
	    
		_progress.show(getSupportFragmentManager(), "progress");
	}
	@Override
	public void stopProgressBar() {
		_progress.dismiss();
	}

}
