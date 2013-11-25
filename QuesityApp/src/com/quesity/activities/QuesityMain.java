package com.quesity.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
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
import com.quesity.network.NetworkInterface;

public class QuesityMain extends BaseActivity implements INetworkInteraction {

	private ProfilePictureView _profilePicture;
	private TextView _welcomeText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		setContentView(R.layout.activity_quesity_main);
		_profilePicture = (ProfilePictureView) findViewById(R.id.quesity_main_profile_pic);
		_welcomeText = (TextView) findViewById(R.id.txt_welcome_msg);
		loadUserDetails();
	}

	private void loadUserDetails() {
		SharedPreferences p = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		String user_id = p.getString(Constants.FACEBOOK_ID_PREF_KEY, null);
		String user_fullname = p.getString(Constants.FACEBOOK_FULL_NAME_KEY,
				null);
		Log.d("QuesityMain", "user id is " + user_id);
		final Activity thisActivity = this;
		if (user_id == null || user_fullname == null) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					SimpleDialogs.getOKOnlyDialog(
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

		_profilePicture.setProfileId(user_id);
		_welcomeText.setText(getString(R.string.lbl_welcome) + " "
				+ user_fullname);
	}

	public void showMyQuests(View view) {
		String saved_games_json = PreferenceManager.getDefaultSharedPreferences(this).getString(Constants.SAVED_GAMES, "[]");
		SavedGame[] saved_games = ModelsFactory.getInstance().getModelFromJSON(saved_games_json, SavedGame[].class);
		Quest[] quests = new Quest[saved_games.length];
		for (int i=0; i<saved_games.length; ++i) {
			quests[i] = saved_games[i].getQuest();
		}
		showQuestList(quests);
		
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
		new FetchJSONTaskGet<Quest[]>(Quest[].class)
				.setNetworkInteractionHandler(interactor).setActivity(this)
				.execute(Config.SERVER_URL + "/all_quests");
	}

	public void returnToSplashScreen() {
		Intent intent = new Intent(this, SplashScreen.class);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		backToHome();
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
	public void showQuestList(Quest[] quests) {
		Intent i = new Intent(QuesityMain.this, QuestsListViewActivity.class);
		String json = ModelsFactory.getInstance().getJSONFromModel(quests);
		i.putExtra(Constants.LOADED_QUESTS, json);
		startActivity(i);
	}
	public void performLogout(View view) {
		SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME,
				MODE_PRIVATE);
		prefs.edit().putString(Constants.PREF_USERNAME, null)
				.putString(Constants.PREF_PASSWORD, null).commit();
		new FetchJSONTaskGet<String>(String.class).setActivity(this)
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
			Quest[] result = (Quest[])r;
			if ( result == null ) {
				AlertDialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.lbl_err_load_quest), QuesityMain.this);
				errorDialog.show();
				return;
			}
			showQuestList(result);
		}

		@Override
		public int get401ErrorMessage() {
			return -1;
		}

	}

}
