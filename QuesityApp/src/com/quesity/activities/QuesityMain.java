package com.quesity.activities;

import com.quesity.R;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.network.AbstractFetchJSONTask;
import com.quesity.network.FetchJSONTask;
import com.quesity.util.Constants;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;

public class QuesityMain extends FragmentActivity {

	private LoadingProgressFragment _progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _progress = new LoadingProgressFragment();
        setTitle(R.string.app_name);
        setContentView(R.layout.activity_quesity_main);
    }
    
    public void findQuestAction(View view){
    	Intent intent = new Intent(this, QuestsListViewActivity.class);
    	startActivity(intent);
    }
    
    public void performLogout(View view) {
    	SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_PRIVATE);
    	prefs.edit().putString(Constants.PREF_USERNAME, null).putString(Constants.PREF_PASSWORD, null).commit();
    	new LogoutAction().execute(Constants.SERVER_URL+"/logoff");
    }
    
    private class LogoutAction extends FetchJSONTask<String>{

    	@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.d("LogoutAction", "Result: " + result);
	    	Intent intent = new Intent(QuesityMain.this,SplashScreen.class);
	    	startActivity(intent);
		}
		public LogoutAction(){
    		String str_logout = getString(R.string.lbl_logout);
			setActivity(QuesityMain.this).setProgressBarHandler(_progress, str_logout, str_logout);
    	}
		@Override
		protected String resolveModel(String json) {
			Log.d("LogoutAction",json);
			return json;
		}

    	
    }
}
