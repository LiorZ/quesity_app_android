package com.quesity.activities;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.quesity.app.R;
import com.quesity.fragments.login.EmailLoginFragment;
import com.quesity.fragments.login.EmailRegistrationLoginButtons;
import com.quesity.fragments.login.LoginFragment;
import com.quesity.fragments.login.LoginProcessor.ScreenLoader;
import com.quesity.fragments.login.EmailRegistrationFragment;
import com.quesity.general.Constants;

public class SplashScreen extends BaseActivity {
	
	public static final long SPLASH_DELAY = 1000;
	private LoginFragment _loginFragment;
	private EmailRegistrationLoginButtons _emailLoginFragment;
	
	@Override
	protected void onResume() {
		super.onResume();
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				startMainActivity();
				finish();
			}
		}, SPLASH_DELAY);
	}
	
	private String getAccountId() {
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(this);
		String account_id = p.getString(Constants.CURRENT_ACCOUNT_ID,null);
		return account_id;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_splash_screen);	
	}
	
	private void startMainActivity() {
		Intent i = new Intent(this,QuestsListViewActivity.class);
		startActivity(i);
	}
	
	
	public void removeLoginButtons() {
		View login_btns = findViewById(R.id.facebook_login_fragment_container);
		login_btns.setVisibility(View.INVISIBLE);
	}
	
	private class RegistrationScreenActivator implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
			add(R.id.layout_container, new EmailRegistrationFragment()).
			addToBackStack(null).
			remove(_emailLoginFragment).
			remove(_loginFragment).
			commit();
		}
		
	}
	
	private class LoginScreenActivator implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
			add(R.id.layout_container, new EmailLoginFragment()).
			addToBackStack(null).
			remove(_emailLoginFragment).
			remove(_loginFragment).
			commit();
		}
	}
	
	public ScreenLoader getMainScreenLoader() {
		ScreenLoader s = new ScreenLoader() {
			
			@Override
			public void loadScreen() {
		    	Intent intent = new Intent(SplashScreen.this, QuesityMain.class);
		    	startActivity(intent);
		    	finish();
			}
		};
		return s;
	}
	

}
