package com.quesity.activities;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.quesity.app.R;
import com.quesity.fragments.login.EmailLoginFragment;
import com.quesity.fragments.login.EmailRegistrationFragment;
import com.quesity.fragments.login.EmailRegistrationLoginButtons;
import com.quesity.fragments.login.LoginFragment;
import com.quesity.fragments.login.LoginProcessor.ScreenLoader;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.models.Account;
import com.quesity.models.ModelsFactory;
import com.quesity.network.FetchJSONTaskPost;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.JSONPostRequestTypeGetter;
import com.quesity.network.NetworkInterface;

public class SplashScreen extends BaseActivity {
	
	public static final int SPLASH_DELAY = 1000;
	private LoginFragment _loginFragment;
	private EmailRegistrationLoginButtons _emailLoginFragment;
	
	@Override
	protected void onResume() {
		super.onResume();
		String accountId = getAccountId();
		if ( accountId == null ) {
			registerTempUser();
		}else {
			delayMainActivity(SPLASH_DELAY);
		}
	}
	
	@Override
	protected String getScreenViewPath() {
		return "Splash Screen";
	}
	
	private void registerTempUser() {
		SecureRandom random = new SecureRandom();
		String email = new BigInteger(130, random).toString(32) + "@email.com";
		Account a = new Account();
		a.setUsername(email);
		a.setName("Temp", "User");
		a.setPassword("1");
		
		String uri = Config.SERVER_URL + getString(R.string.email_registration);
		
		JSONPostRequestTypeGetter jsonpost = new JSONPostRequestTypeGetter(this);		
		try {
			jsonpost.setModel(a);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		FetchJSONTaskPost<Account> task = new FetchJSONTaskPost<Account>(jsonpost,Account.class, this);
		task.setPostExecuteCallback(new IPostExecuteCallback() {
			
			@Override
			public int get401ErrorMessage() {
				return 0;
			}
			
			@Override
			public void apply(Object obj) {
				Account result = (Account) obj; 
				SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
				p.edit().putString(Constants.CURRENT_ACCOUNT_ID, result.getId()).commit();
				delayMainActivity(1000);
			}
		});
		task.setNetworkInterface(NetworkInterface.getInstance());
		task.execute(uri);
	}
	
	private void delayMainActivity(int delay) {
		Timer t = new Timer();
		t.schedule(new TimerTask() {
			@Override
			public void run() {
				startMainActivity();
				finish();
			}
		}, delay);
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
