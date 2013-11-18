package com.quesity.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.quesity.R;
import com.quesity.general.Constants;

public class SplashScreen extends BaseActivity {
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
        backToHome(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
		String account_id = p.getString(Constants.CURRENT_ACCOUNT_ID,null);
		if ( account_id != null ) {
			Intent i = new Intent(this,QuesityMain.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(i);
			finish();
			return;
		}
		setContentView(R.layout.activity_splash_screen);
	}
	

}
