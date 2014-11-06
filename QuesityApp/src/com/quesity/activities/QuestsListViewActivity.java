package com.quesity.activities;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.quesity.app.R;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.fragments.QuesityPageTitleView;
import com.quesity.fragments.QuestListFragment;
import com.quesity.fragments.SimpleProgressFragment;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.models.Location;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.StartingLocation;
import com.quesity.network.INetworkInteraction;
import com.quesity.network.INetworkInterface;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.NetworkInterface;
import com.quesity.network.QuestListTaskGet;

public class QuestsListViewActivity extends BaseActivity{

	public static final String QUEST_ID = "com.quesity.QUEST_ID";
	private static final String PROGRESS_FRAGMENT_TAG = "PROGRESS_TAG";
	private static final String LIST_FRAGMENT_TAG = "LIST_FRAGMENT_TAG";
	private static final String CURRENT_LOCATION_KEY = "com.quesity.cuurent_location";
	private static final String SAVED_QUESTS_KEY = "com.quesity.saved_quests";
	private static final String SAVED_QUESTS_KEY_DATE = "com.quesity.saved_quests_date";
	private static long MILLISECONDS_IN_DAY = 3600*1000*24;
	private QuestListFragment _list_fragment;
	private Location _current_location;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quests_list);
		setTitle(R.string.app_name);
		String title = getString(R.string.lbl_find_quest);
		int title_img_resource = R.drawable.search;
		QuesityPageTitleView title_view = (QuesityPageTitleView) findViewById(R.id.title_find_quest);
		title_view.setTitle(title);
		title_view.setTitleImage(title_img_resource);
		
		//Check whether we recreate the activity, if the fragment already inside the layout, don't readd it.
		String cur_location_str = getIntent().getStringExtra(Constants.CURRENT_LOCATION_QUEST_LIST);
		if ( cur_location_str != null ) {
			Log.d("QuestListActivity",cur_location_str);
			_current_location = ModelsFactory.getInstance().getModelFromJSON(cur_location_str, Location.class);
		}else if (savedInstanceState != null ) {
			String string = savedInstanceState.getString(CURRENT_LOCATION_KEY);
			if ( string != null ){
				_current_location = ModelsFactory.getInstance().getModelFromJSON(string, Location.class);
			}
		}
		Log.d("QuestListActivity","QuestList Activity created");
		loadQuestsToList();
			
	}
	
	private void loadQuestsToList() {
		Fragment list_frag = getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG);
		if ( list_frag == null ){
			boolean shouldILoadQuests = shouldILoadQuests();
			if (shouldILoadQuests) {
				Log.d("QuestsListViewActivity", "Loading Quests from network");
				fetchQuests();
			}else {
				String quests_json = PreferenceManager.getDefaultSharedPreferences(this).getString(SAVED_QUESTS_KEY, null);
				if ( quests_json == null ) {
					Log.d("QuestsListViewActivity", "Quest json from pref is null. Loading Quests from network");
					fetchQuests();
				}else {
					Log.d("QuestsListViewActivity", "Getting quests from prefs");
					Quest[] quests = ModelsFactory.getInstance().getModelFromJSON(quests_json, Quest[].class);
					showLoadedQuests(quests);
				}
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if ( outState == null ){
			outState = new Bundle();
		}
		outState.putString(CURRENT_LOCATION_KEY, ModelsFactory.getInstance().getJSONFromModel(_current_location));
	}
	
	@Override
	protected String getScreenViewPath() {
		return "Quests List Screen";
	}
	
	private boolean shouldILoadQuests() {
		long saved_date = PreferenceManager.getDefaultSharedPreferences(QuestsListViewActivity.this).getLong(SAVED_QUESTS_KEY_DATE, 0);
		if ( saved_date == 0 ) {
			return true;
		}
		
		long curtime = System.currentTimeMillis();
		if ( curtime - saved_date > MILLISECONDS_IN_DAY ) {
			return true;
		}
		
		return false;
		

	}
	private void fetchQuests() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.quest_list_loading_progress_container, new SimpleProgressFragment(),PROGRESS_FRAGMENT_TAG);
		ft.commit();
		INetworkInteraction interactor = new INetworkInteraction() {

			@Override
			public ProgressBarHandler getProgressBarHandler() {
				return null;
			}

			@Override
			public IPostExecuteCallback getPostExecuteCallback() {
				return new NewQuestsPostExecuteCallback(); 
			}

			@Override
			public INetworkInterface getNetworkInterface() {
				return NetworkInterface.getInstance();
			}
		};
		
		new QuestListTaskGet<Quest[]>(Quest[].class,this)
				.setNetworkInteractionHandler(interactor).setActivity(this)
				.setPostExecuteCallback(new NewQuestsPostExecuteCallback() )
				.execute(Config.SERVER_URL + "/all_quests");
	}
	
	private void showLoadedQuests(Quest[] quests) {
		_list_fragment = QuestListFragment.newInstance(quests);

		getSupportFragmentManager().executePendingTransactions();
		Fragment progress_frag = getSupportFragmentManager().findFragmentByTag(PROGRESS_FRAGMENT_TAG);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		
		if ( progress_frag != null ){
			ft.remove(progress_frag);
		}
		
		ft.add(R.id.quest_list_container, _list_fragment, LIST_FRAGMENT_TAG);
		ft.commit();
	}
	
	
	private class NewQuestsPostExecuteCallback implements IPostExecuteCallback {

		
		private void saveQuestsToPerfs(Quest[] quests) {
			String quests_json = ModelsFactory.getInstance().getJSONFromModel(quests);
			PreferenceManager.getDefaultSharedPreferences(QuestsListViewActivity.this).edit().
			putString(SAVED_QUESTS_KEY, quests_json).
			putLong(SAVED_QUESTS_KEY_DATE, System.currentTimeMillis()).commit();
		}
		
		@Override
		public void apply(Object r) {
			Quest[] quests = (Quest[])r;
			if ( _current_location != null )
				sortQuests(quests);
			saveQuestsToPerfs(quests);
			
			showLoadedQuests(quests);
		}
		
		private void sortQuests(Quest[] quests) {
			Arrays.sort(quests, new Comparator<Quest>() {

				@Override
				public int compare(Quest lhs, Quest rhs) {
					StartingLocation lhs_starting_location = lhs.getStartingLocation();
					StartingLocation rhs_starting_location = rhs.getStartingLocation();
					float[] result_lhs = new float[3];
					float[] result_rhs = new float[3];
					android.location.Location.distanceBetween(lhs_starting_location.getLat(), lhs_starting_location.getLng(), _current_location.getLat(), _current_location.getLng(), result_lhs);
					android.location.Location.distanceBetween(rhs_starting_location.getLat(), rhs_starting_location.getLng(), _current_location.getLat(), _current_location.getLng(), result_rhs);
					
					return Float.compare(result_lhs[0], result_rhs[0]);
				}
			
			});
		}
		
		@Override
		public int get401ErrorMessage() {
			return -1;
		}

	}
	
}
