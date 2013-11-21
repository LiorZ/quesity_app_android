package com.quesity.activities;

import android.app.Activity;
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
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.network.FetchJSONTaskGet;
import com.quesity.network.INetworkInteraction;
import com.quesity.network.IPostExecuteCallback;

public class QuesityMain extends BaseActivity  implements INetworkInteraction{
	
	

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
    	SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
    	String user_id = p.getString(Constants.FACEBOOK_ID_PREF_KEY, null);
    	String user_fullname = p.getString(Constants.FACEBOOK_FULL_NAME_KEY, null);
    	Log.d("QuesityMain","user id is " + user_id);
    	final Activity thisActivity = this;
    	if (user_id == null || user_fullname == null){
    		runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
		    		SimpleDialogs.getOKOnlyDialog(getString(R.string.error_display_facebook_data), thisActivity, new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							returnToSplashScreen();
						}
					}).show();
				}
			});
    	}
    	
    	_profilePicture.setProfileId(user_id);
    	_welcomeText.setText(getString(R.string.lbl_welcome) + " "+ user_fullname);
    }
    
    public void returnToSplashScreen() {
    	Intent intent = new Intent(this, SplashScreen.class);
    	startActivity(intent);
    }
    
    public void findQuestAction(View view){
    	Intent intent = new Intent(this, QuestsListViewActivity.class);
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
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
		String current_account_id = p.getString(Constants.CURRENT_ACCOUNT_ID, null);
		if ( current_account_id == null ) {
			Intent i = new Intent(this,SplashScreen.class);
			startActivity(i);
			finish();
		}
	}
    
    public void performLogout(View view) {
    	SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
    	prefs.edit().putString(Constants.PREF_USERNAME, null).putString(Constants.PREF_PASSWORD, null).commit();
    	new FetchJSONTaskGet<String>(String.class).
    	setActivity(this).setNetworkInteractionHandler(this).
    	execute(Config.SERVER_URL+"/logoff");
    }
    
    private class LogoutAction implements IPostExecuteCallback {

		@Override
		public void apply(Object r) {
			String result = (String) r;
			Log.d("LogoutAction", "Result: " + result);
	    	Intent intent = new Intent(QuesityMain.this,SplashScreen.class);
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
//		return new ProgressBarHandler(getString(R.string.lbl_logging_out),getString(R.string.lbl_logout),_progress);
		return null;
	}
    
}
