package com.quesity.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.quesity.app.R;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.models.Account;
import com.quesity.network.FetchJSONTaskGet;
import com.quesity.network.INetworkInteraction;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.reporting.ModelReport;

public class QuesityMain extends BaseActivity implements INetworkInteraction {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.app_name);
		setContentView(R.layout.activity_quesity_main);
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
		}else {
			//sending a login report...
			Account ac = new Account();
			ModelReport login_report = new ModelReport(ac, this);
			login_report.send(Config.SERVER_URL + getString(R.string.report_login));
		}
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

	
}
