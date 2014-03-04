package com.quesity.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.quesity.R;
import com.quesity.fragments.LoginFragment;
import com.quesity.general.Constants;

public class SplashScreen extends BaseActivity {
	
	public static final long SPLASH_DELAY = 1000;
	@Override
	public void onBackPressed() {
		super.onBackPressed();
        backToHome(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		hideFacebookLoginButton();
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
		String account_id = p.getString(Constants.CURRENT_ACCOUNT_ID,null);
		if ( account_id != null ) {
			Timer t = new Timer();
			t.schedule(new TimerTask() {
				
				@Override
				public void run() {
					startMainActivity();
					finish();
				}
			}, SPLASH_DELAY);
		}else {
			showFacebookLoginButton();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_splash_screen);	
		
	}
	
	private void startMainActivity() {
		Intent i = new Intent(this,QuesityMain.class);
		startActivity(i);
	}
	
	private void hideFacebookLoginButton() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment login_fragment = getSupportFragmentManager().findFragmentByTag(LoginFragment.TAG);
		if ( login_fragment == null )
			return;
		fragmentTransaction.remove(login_fragment);
		fragmentTransaction.commit();
	}
	
	private void showFacebookLoginButton() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		final LoginFragment loginFragment = new LoginFragment();
		fragmentTransaction.setCustomAnimations(R.anim.default_fade_in,R.anim.default_fade_out);
//		fragmentTransaction.add(R.id.facebook_login_fragment_container,loginFragment,LoginFragment.TAG);
		fragmentTransaction.commit();
	}
	

}
