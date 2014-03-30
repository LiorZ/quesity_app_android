package com.quesity.activities;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;

import com.quesity.R;
import com.quesity.application.IQuesityApplication;
import com.quesity.controllers.ProgressableProcess;
import com.quesity.fragments.ContentPageFragment;
import com.quesity.fragments.InGameMenuFragment.TransitionFragmentInvokation;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.fragments.LocationPageFragment;
import com.quesity.fragments.MultipleChoiceFragment;
import com.quesity.fragments.OnDemandFragment;
import com.quesity.fragments.OpenQuestionFragment;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.fragments.StallFragment;
import com.quesity.fragments.WebViewFragment;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.models.Game;
import com.quesity.models.Location;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Move;
import com.quesity.models.Quest;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;
import com.quesity.models.SavedGame;
import com.quesity.network.FetchJSONTaskGet;
import com.quesity.network.GetRequestTypeGetter;
import com.quesity.network.IBackgroundCallback;
import com.quesity.network.INetworkInteraction;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.JSONPostRequestTypeGetter;
import com.quesity.network.reporting.ModelReport;
import com.quesity.services.location.LocationService;

public class QuestPageActivity extends BaseActivity implements INetworkInteraction,TransitionFragmentInvokation, NextPageTransition, ProgressableProcess {

	
	@Override
	public ProgressBarHandler getProgressBarHandler() {
		
		return new ProgressBarHandler(getString(R.string.lbl_loading_page), getString(R.string.lbl_loading), _progress);
	}
	
	public static final String QUEST_PAGE_KEY = "com.quesity.QUEST_PAGE_KEY";
	public static final String TAG = "com.quesity.activities.QuestPageActivity";
	private LoadingProgressFragment _progress;
	private WebViewFragment _webViewFragment;
	private String _quest_id;
	private Quest _quest_obj;
	private OnDemandFragment _transitionFragment;
	private MultipleChoiceFragment _multiple_choice_fragment;
	private OpenQuestionFragment _open_question_fragment;
	private ContentPageFragment _content_page_fragment;
	private LocationPageFragment _location_page_fragment;
	private StallFragment _stall_fragment;
	private HashMap<String,Fragment> _fragmentMapper;
	private QuestPage _currentPage;
	private IPostExecuteCallback _post_callback;
	private QuestPage[] _all_pages;
	private Game _current_game;
	
	@Override
	protected void onPause() {
		super.onPause();
		Log.d("QuestPageActivity","Activity Paused");
	}
	
	private Bundle getInstanceState(Bundle existing) {
		Bundle b = existing;
		if ( existing == null ) {
			b = new Bundle();
		}
		
		
		String page_json = ModelsFactory.getInstance().getJSONFromModel(_currentPage);
		Log.d("QuestPageActivity", "Saving instance state with json");
		b.putString(QUEST_PAGE_KEY,page_json);
		return b;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState = getInstanceState(outState);
	}

	private boolean  restoreFromPreferences() {
		SavedGame[] saved_games = ModelsFactory.getInstance().getFromPreferenceStore(this, Constants.SAVED_GAMES, SavedGame[].class);
		
		if ( saved_games == null || saved_games.length == 0 )
			return false;
		Bundle b = new Bundle();
		for(int i = 0; i<saved_games.length; ++i) {
			if (saved_games[i].getQuest().getId().equals(_quest_id) ){
				_current_game = saved_games[i].getGame();
				_currentPage = saved_games[i].getCurrentPage();
				_all_pages = saved_games[i].getPages();
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("QuestPageActivity", "OnCreate - QuestPageActivity");
		setContentView(R.layout.activity_quest_page);
		String quest_json = getIntent().getStringExtra(Constants.QUEST_OBJ);

		_quest_obj = ModelsFactory.getInstance().getModelFromJSON(quest_json, Quest.class);
		_quest_id = _quest_obj.getId();
		
		_progress = new LoadingProgressFragment();
		_progress.setCancelable(false);
		_webViewFragment = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webview_fragment);
		((IQuesityApplication)getApplication()).inject(this);
		
		constructFragmentMapper();
		
		boolean shouldResume = getIntent().getBooleanExtra(Constants.QUEST_RESUME_KEY, false);
		if ( shouldResume ) {
			restoreFromPreferences();
			startLocationService();
			refreshQuestPage(_currentPage);
		}else {
			restoreSavedPage(savedInstanceState);	
		}
		
	}

	private void startLocationService() {
		Intent i = new Intent(this,LocationService.class);
		i.putExtra(LocationService.KEY_UPDATE_INTERVAL, Constants.GPS_UPDATE_INTERVAL);
		i.putExtra(LocationService.KEY_GPS_ACCURACY, Constants.GPS_MIN_ACCURACY);
		String report_url = Config.SERVER_URL + String.format(getString(R.string.new_location), _quest_id,_current_game.getId());
		i.putExtra(LocationService.KEY_URL_TO_REPORT, report_url);
		startService(i);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopLocationService();
	}
	
	private void stopLocationService() {
		Intent i = new Intent(this,LocationService.class);
		stopService(i);
	}
	
	private void restoreSavedPage(Bundle savedInstanceState) {
		String page = null;
		if ( savedInstanceState != null ){
			page = savedInstanceState.getString(QUEST_PAGE_KEY);
		}
		Log.d("QuestPageActivity", "Restoring instance state");
		if ( page != null ) {
			_currentPage = ModelsFactory.getInstance().getModelFromJSON(page, QuestPage.class);
			refreshQuestPage(_currentPage);
		}else {
			Log.d("QuesityPageActivity","Got a null page from the saved instance");
			Log.d("QuestPageActivity","Current page is null, downloading the first page");
			String game_url = Config.SERVER_URL + String.format(getString(R.string.new_game),_quest_id);
			String quest_pages_url = Config.SERVER_URL +  String.format(getString(R.string.quest_pages),_quest_id);
			new FetchAllQuestPagesTask(QuestPage[].class).execute(game_url,quest_pages_url);
		}
	}
	
	public Game getCurrentGame() {
		return _current_game;
	}
	@Override
	public void transitToNextPage() {
		if ( _currentPage == null )
			return;
		QuestPageLink[] links = _currentPage.getLinks();
		if ( links.length == 0 ){
			finishQuest();
			return;
		}
		
		FragmentManager supportFragmentManager = getSupportFragmentManager();
		supportFragmentManager.executePendingTransactions();
		_transitionFragment.invokeFragment(supportFragmentManager);

	}
	
	public void returnToMainPage() {
    	stopLocationService();
    	Intent intent = new Intent(QuestPageActivity.this, QuesityMain.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);	
    	startActivity(intent);
    	finish();
	}
	
	public void finishQuest(){
		Dialog okOnlyDialog = SimpleDialogs.getOKOnlyDialog(getString(R.string.lbl_title_dialog_quest_over),getString(R.string.lbl_quest_over), this, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				returnToMainPage();
				finish();
			}
		});
		removeGameFromSaved();
		stopLocationService();
		okOnlyDialog.show();
	}
	
	private void removeGameFromSaved() {
		SavedGame[] saved_games = ModelsFactory.getInstance().getFromPreferenceStore(this, Constants.SAVED_GAMES, SavedGame[].class);
		if ( saved_games == null || saved_games.length == 0 )
			return;
		boolean has = false;
		for(int i=0; i<saved_games.length; ++i) {
			if (saved_games[i].getGame().getQuestId().equals(_quest_id)) {
				has = true;
			}
		}
		if ( ! has ){
			return;
		}
		
		SavedGame[] removed = new SavedGame[saved_games.length-1];
		int i_src  = 0;
		int i_tgt = 0;
		while ( i_src < saved_games.length ){
			if ( !saved_games[i_src].getGame().getQuestId().equals(_quest_id) ){
				removed[i_tgt] =saved_games[i_src];
				i_tgt++;
			}
			i_src++;
		}
		
		ModelsFactory.getInstance().putInPreferenceStore(this, Constants.SAVED_GAMES, removed);
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
	public void loadNextPage(QuestPageLink link) {
		String page_id = link.getLinksToPage();
		for (int i=0; i<_all_pages.length; ++i) {
			if ( _all_pages[i].getId().equals(page_id) ) {
				reportMove(link);
				savePage(_all_pages[i]);
				refreshQuestPage(_all_pages[i]);
				return;
			}
		}
	}
	
	private void savePage(QuestPage page) {
		final Activity a = this;
		AsyncTask<QuestPage, Void, Integer> task = new AsyncTask<QuestPage, Void, Integer>() {
			
			@Override
			protected Integer doInBackground(QuestPage... params) {
				QuestPage page = params[0];
				String savedGamesJson = PreferenceManager.getDefaultSharedPreferences(a).getString(Constants.SAVED_GAMES, null);
				SavedGame[] games;
				SavedGame g = null;
				int index = 0;
				if (savedGamesJson == null){
					Log.d(TAG,"Creating new array for games");
					games = new SavedGame[0];
				}else {
					Log.d(TAG,"Creating a new entry in an existing array of saved games");
					games = ModelsFactory.getInstance().getModelFromJSON(savedGamesJson, SavedGame[].class);
				}
				
				for(int i=0; i<games.length; ++i) {
					if ( games[i] == null || games[i].getGame() == null )
						continue;
					
					if ( games[i].getGame().getQuestId().equals( _current_game.getQuestId() ) ){
						Log.d(TAG,"Retrieved existing game");
						g = games[i];
						break;
					}
				}
				
				if ( g == null ) {
					games = Arrays.copyOf(games, games.length+1);
					index = games.length-1;
					Log.d(TAG,"Could not find existing game, so I'm creating a new entry in index " + index);
					games[index] = new SavedGame();
					putProgressInSavedGame(games[index], page);
				}else {
					Log.d(TAG,"Found existing game, updating the page");
					putProgressInSavedGame(g, page);
				}
				Editor edit = PreferenceManager.getDefaultSharedPreferences(a).edit();
				String games_json = ModelsFactory.getInstance().getJSONFromModel(games);
				edit.putString(Constants.SAVED_GAMES, games_json).commit();
				return 0;
			}
		};
		task.execute(page);
		
	}
	
	private void putProgressInSavedGame(SavedGame game, QuestPage page) {
		game.setCurrentPage(page);
		game.setGame(_current_game);
		game.setPages(_all_pages);
		game.setQuest(_quest_obj);
	}
	
	private void reportMove(QuestPageLink l){ 
		final Move m = new Move();
		m.setDate(new Date());
		m.setLinkId(l.getId());
		
		final ModelReport report = new ModelReport(m, this);
		final String report_url = Config.SERVER_URL + String.format(getString(R.string.new_move),_quest_id,_current_game.getId());
		LocationService instance = LocationService.getInstance();
		if ( instance == null ) {
			report.send(report_url);
			return;
		}
		android.location.Location lastKnownLocation = instance.getLastKnownLocation();
		if ( lastKnownLocation == null ) {
			report.send(report_url);
			return;
		}
		Location loc = new Location();
		loc.setDate(new Date());
		loc.setLat(lastKnownLocation.getLatitude());
		loc.setLng(lastKnownLocation.getLongitude());
		m.setLocation(loc);
		report.send(report_url);
	}
	
	public Activity getActivity() {
		return this;
	}
	
	public OnDemandFragment getCurrentFragment() {
		return _transitionFragment;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.quest_page, menu);
		return true;
	}
	
	public void refreshQuestPage(QuestPage page) {
		setTitle(page.getPageName());
		_currentPage = page;
		Fragment fragment = _fragmentMapper.get(page.getPageType());
		_transitionFragment = (OnDemandFragment)fragment;
		getSupportFragmentManager().executePendingTransactions();
		_webViewFragment.loadHTMLData(page.getPageContent());
		
	}
	
	
	private class AfterLoadingAllQuestPages implements IPostExecuteCallback {

		@Override
		public void apply(Object result) {
			_all_pages = (QuestPage[])result;
			if ( _all_pages == null ){
				DialogInterface.OnClickListener click_listener = new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						returnToMainPage();
						finish();						
					}
				};
				SimpleDialogs.getErrorDialog(getString(R.string.error_starting_quest), getActivity(), click_listener ).show();

				return;
			}
			for (int i = 0; i < _all_pages.length; i++) {
				if ( _all_pages[i].getIsFirst() ){
					refreshQuestPage(_all_pages[i]);
					return;
				}
			}
			
		}

		@Override
		public int get401ErrorMessage() {
			return -1;
		}
		
	}
	
	@Override
	public void onBackPressed() {
		DialogInterface.OnClickListener yesAnswer = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				returnToMainPage();
			}
		};
		
		DialogInterface.OnClickListener noAnswer = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		
		SimpleDialogs.getConfirmationDialog(getString(R.string.title_exit),getString(R.string.lbl_sure_leave_quest), this, yesAnswer, noAnswer).show();
	}
	
	private class GameCreationTaskBackgroundHandler implements IBackgroundCallback<QuestPage[]> {

		private void startNewGame(String uri) throws Exception{
			String account_id = PreferenceManager.getDefaultSharedPreferences(QuestPageActivity.this).getString(Constants.CURRENT_ACCOUNT_ID, null);
			if ( account_id == null ) {
				throw new Exception("No account id");
			}
			if ( _quest_id == null ){
				throw new Exception("No quest id was found");
			}
			
			Game game = new Game();
			game.setDateStarted(new Date());
			game.setAccount_id(account_id);
			game.setQuestId(_quest_id);
			String game_json_input = ModelsFactory.getInstance().getJSONFromModel(game);
			String game_json = _network_interface.getStringContent(uri, new JSONPostRequestTypeGetter(game_json_input,QuestPageActivity.this),QuestPageActivity.this);
			_current_game = ModelsFactory.getInstance().getModelFromJSON(game_json, Game.class);
			
			removeGameFromSaved(); //If we started a new game, remove the old one.
			
			Log.d(TAG, "Started Game with " + _current_game.getRemainingHints() + " available hints");
			startLocationService();
		}
		@Override
		public QuestPage[] apply(String... urls) throws Exception {
			startNewGame(urls[0]);
			String uri = urls[1];
			
			String pages = _network_interface.getStringContent(uri, new GetRequestTypeGetter(),QuestPageActivity.this);
			QuestPage[] pages_model = ModelsFactory.getInstance().getModelFromJSON(pages, QuestPage[].class);
			return pages_model;
		}
		
	}
	private class FetchAllQuestPagesTask extends FetchJSONTaskGet<QuestPage[]> {


		public FetchAllQuestPagesTask(Class<QuestPage[]> c) {
			super(c,QuestPageActivity.this);
			setNetworkInterface(_network_interface);
			setPostExecuteCallback(new AfterLoadingAllQuestPages());
			setBackgroundCallback(new GameCreationTaskBackgroundHandler());
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
	@Override
	public IPostExecuteCallback getPostExecuteCallback() {
		return _post_callback;
	}

}
