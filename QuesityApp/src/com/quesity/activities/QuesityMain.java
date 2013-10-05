package com.quesity.activities;

import java.lang.reflect.Type;

import android.app.Activity;
import android.app.UiModeManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.widget.ProfilePictureView;
import com.quesity.R;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.fragments.LoginFragment;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.network.FetchJSONTaskGet;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.dagger_modules.NetworkInterfaceModule;

import dagger.ObjectGraph;

public class QuesityMain extends FragmentActivity {

	private LoadingProgressFragment _progress;
	private ProfilePictureView _profilePicture;
	private TextView _welcomeText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _progress = new LoadingProgressFragment();
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
    	_welcomeText.setText(getString(R.string.lbl_welcome) + user_fullname);
    }
    
    public void returnToSplashScreen() {
    	Intent intent = new Intent(this, SplashScreen.class);
    	startActivity(intent);
    }
    
    public void findQuestAction(View view){
    	Intent intent = new Intent(this, QuestsListViewActivity.class);
    	startActivity(intent);
    }
    
    public void performLogout(View view) {
    	SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
    	prefs.edit().putString(Constants.PREF_USERNAME, null).putString(Constants.PREF_PASSWORD, null).commit();
    	ObjectGraph graph = ObjectGraph.create(new NetworkInterfaceModule());
    	new FetchJSONTaskGet<String>(String.class).setPostExecuteCallback(new LogoutAction()).execute(Config.SERVER_URL+"/logoff");
    }
    
    private class LogoutAction implements IPostExecuteCallback<String> {

		@Override
		public void apply(String result) {
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
    
}
