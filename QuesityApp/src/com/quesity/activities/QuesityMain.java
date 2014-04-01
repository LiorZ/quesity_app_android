package com.quesity.activities;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.quesity.R;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.SavedGame;
import com.quesity.network.FetchJSONTaskGet;
import com.quesity.network.INetworkInteraction;
import com.quesity.network.INetworkInterface;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.MultipleImagesLoader;
import com.quesity.network.MultipleImagesLoader.ImagesLoaded;
import com.quesity.network.NetworkInterface;
import com.quesity.network.QuestListTaskGet;

public class QuesityMain extends BaseActivity implements INetworkInteraction {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		setContentView(R.layout.activity_quesity_main);
		View btn_find_quest = findViewById(R.id.btn_find_quest);
		btn_find_quest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				findQuestAction(v);
			}
		});
		
		View btn_my_quests = findViewById(R.id.btn_my_quests);
		btn_my_quests.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showMyQuests(v);
			}
		});
		
		View btn_about = findViewById(R.id.btn_about);
		btn_about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showAbout(v);
			}
		});
		loadUserDetails();
	}

	private void loadUserDetails() {
		SharedPreferences p = PreferenceManager
				.getDefaultSharedPreferences(this);
		String user_id = p.getString(Constants.CURRENT_ACCOUNT_ID, null);
		Log.d("QuesityMain", "user id is " + user_id);
		final Activity thisActivity = this;
		if (user_id == null) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					SimpleDialogs.getErrorDialog(
							getString(R.string.error_display_facebook_data),
							thisActivity,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									returnToSplashScreen();
								}
							}).show();
				}
			});
		}

	}

	public void showMyQuests(View view) {
		String saved_games_json = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.SAVED_GAMES, "[]");
		SavedGame[] saved_games = ModelsFactory.getInstance().getModelFromJSON(saved_games_json, SavedGame[].class);
		Quest[] quests = new Quest[saved_games.length];
		for (int i=0; i<saved_games.length; ++i) {
			quests[i] = saved_games[i].getQuest();
		}
		showQuestList(quests,getString(R.string.lbl_my_quests),R.drawable.favorites);
		
	}
	
	public void showAbout(View view) {
		Intent i = new Intent(this,AboutActivity.class);
		startActivity(i);
	}
	
	public void findQuestAction(View view){
		INetworkInteraction interactor = new INetworkInteraction() {

			@Override
			public ProgressBarHandler getProgressBarHandler() {
				return new ProgressBarHandler(
						getString(R.string.lbl_loading_quests),
						getString(R.string.lbl_loading), _progress);
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

	public void returnToSplashScreen() {
		Intent intent = new Intent(this, SplashScreen.class);
		startActivity(intent);
	}


	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferences p = PreferenceManager
				.getDefaultSharedPreferences(this);
		String current_account_id = p.getString(Constants.CURRENT_ACCOUNT_ID,
				null);
		if (current_account_id == null) {
			Intent i = new Intent(this, SplashScreen.class);
			startActivity(i);
			finish();
		}
	}
	public void showQuestList(Quest[] quests, String title, int img_resource) {
		Intent i = new Intent(QuesityMain.this, QuestsListViewActivity.class);
		String json = ModelsFactory.getInstance().getJSONFromModel(quests);
		i.putExtra(Constants.LOADED_QUESTS, json);
		i.putExtra(Constants.QUEST_LIST_ACTIVITY_TITLE, title);
		i.putExtra(Constants.QUEST_LIST_ACTIVITY_TITLE_IMG,img_resource);
		startActivity(i);
	}
	public void performLogout(View view) {
		SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME,
				MODE_PRIVATE);
		prefs.edit().putString(Constants.PREF_USERNAME, null)
				.putString(Constants.PREF_PASSWORD, null).commit();
		new FetchJSONTaskGet<String>(String.class,this).setActivity(this)
				.setNetworkInteractionHandler(this)
				.execute(Config.SERVER_URL + "/logoff");
	}

	private class LogoutAction implements IPostExecuteCallback {

		@Override
		public void apply(Object r) {
			String result = (String) r;
			Log.d("LogoutAction", "Result: " + result);
			Intent intent = new Intent(QuesityMain.this, SplashScreen.class);
			startActivity(intent);
		}

		@Override
		public int get401ErrorMessage() {
			// TODO Auto-generated method stub
			return -1;
		}

	}

	@Override
	public IPostExecuteCallback getPostExecuteCallback() {
		return new LogoutAction();
	}

	@Override
	public ProgressBarHandler getProgressBarHandler() {
		// return new
		// ProgressBarHandler(getString(R.string.lbl_logging_out),getString(R.string.lbl_logout),_progress);
		return null;
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
