package com.quesity.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.quesity.app.R;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.fragments.QuesityPageTitleView;
import com.quesity.fragments.QuestListFragment;
import com.quesity.fragments.SimpleProgressFragment;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.network.INetworkInteraction;
import com.quesity.network.INetworkInterface;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.NetworkInterface;
import com.quesity.network.QuestListTaskGet;

public class QuestsListViewActivity extends BaseActivity{
	public static final String QUEST_ID = "com.quesity.QUEST_ID";
	private static final String PROGRESS_FRAGMENT_TAG = "PROGRESS_TAG";
	private static final String LIST_FRAGMENT_TAG = "LIST_FRAGMENT_TAG";
	private QuestListFragment _list_fragment;
	
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
		Fragment list_frag = getSupportFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG);
		Log.d("QuestListActivity","QuestList Activity created");
		if ( list_frag == null )
			fetchQuests();
	}
	

	@Override
	protected String getScreenViewPath() {
		return "Quests List Screen";
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
	
	private void showLoadedQuests() {
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

		
		@Override
		public void apply(Object r) {
			Quest[] quests = (Quest[])r;
			_list_fragment = QuestListFragment.newInstance(quests);
			showLoadedQuests();
		}

		@Override
		public int get401ErrorMessage() {
			return -1;
		}

	}
	
}
