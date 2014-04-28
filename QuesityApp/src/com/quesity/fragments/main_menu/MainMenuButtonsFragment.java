package com.quesity.fragments.main_menu;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.quesity.activities.AboutActivity;
import com.quesity.activities.BaseActivity;
import com.quesity.activities.QuesityMain;
import com.quesity.activities.QuestsListViewActivity;
import com.quesity.app.R;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.SavedGame;
import com.quesity.network.INetworkInteraction;
import com.quesity.network.INetworkInterface;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.NetworkInterface;
import com.quesity.network.QuestListTaskGet;

public class MainMenuButtonsFragment extends Fragment {

	private View _main_view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		_main_view = inflater.inflate(R.layout.main_menu_buttons, null,false);
		View btn_find_quest = _main_view.findViewById(R.id.btn_find_quest);
		btn_find_quest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				findQuestAction(v);
			}
		});
		
		View btn_my_quests = _main_view.findViewById(R.id.btn_my_quests);
		btn_my_quests.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showMyQuests(v);
			}
		});
		
		View btn_about = _main_view.findViewById(R.id.btn_about);
		btn_about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAbout(v);
			}
		});
		return _main_view;
	}
	
	public void showAbout(View view) {
		Intent i = new Intent(getActivity(),AboutActivity.class);
		startActivity(i);
	}
	
	public void showMyQuests(View view) {
		String saved_games_json = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(Constants.SAVED_GAMES, "[]");
		SavedGame[] saved_games = ModelsFactory.getInstance().getModelFromJSON(saved_games_json, SavedGame[].class);
		Quest[] quests = new Quest[saved_games.length];
		for (int i=0; i<saved_games.length; ++i) {
			quests[i] = saved_games[i].getQuest();
		}
		showQuestList(quests,getString(R.string.lbl_my_quests),R.drawable.favorites);
	}
	
	public void showQuestList(Quest[] quests, String title, int img_resource) {
		Intent i = new Intent(getActivity(), QuestsListViewActivity.class);
		String json = ModelsFactory.getInstance().getJSONFromModel(quests);
		i.putExtra(Constants.LOADED_QUESTS, json);
		i.putExtra(Constants.QUEST_LIST_ACTIVITY_TITLE, title);
		i.putExtra(Constants.QUEST_LIST_ACTIVITY_TITLE_IMG,img_resource);
		startActivity(i);
	}
	
	public void findQuestAction(View view){
		INetworkInteraction interactor = new INetworkInteraction() {

			@Override
			public ProgressBarHandler getProgressBarHandler() {
				return new ProgressBarHandler(
						getString(R.string.lbl_loading_quests),
						getString(R.string.lbl_loading), ((BaseActivity)getActivity()).getProgressFragment());
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
		
		new QuestListTaskGet<Quest[]>(Quest[].class,getActivity())
				.setNetworkInteractionHandler(interactor).setActivity(getActivity())
				.setPostExecuteCallback(new NewQuestsPostExecuteCallback() )
				.execute(Config.SERVER_URL + "/all_quests");
	}
	
	private class NewQuestsPostExecuteCallback implements IPostExecuteCallback {

		
		@Override
		public void apply(Object r) {
			showQuestList((Quest[])r, getString(R.string.lbl_find_quest),R.drawable.search);
		}

		@Override
		public int get401ErrorMessage() {
			return -1;
		}

	}
	

}
