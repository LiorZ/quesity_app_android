package com.quesity.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.quesity.R;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.models.Account;
import com.quesity.models.ModelsFactory;
import com.quesity.network.FetchJSONTaskPost;
import com.quesity.network.JSONPostRequestTypeGetter;
import com.quesity.util.Constants;

public class SplashScreen extends FragmentActivity {

	private static final int MAIN_LAYOUT_FADE_DURATION = 3000;
	private static final int FADE_WAIT_DURATION = 2000;
	private static final int LOGIN_BOX_FADE_DURATION = 1000;
	
	private LoadingProgressFragment _progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		_progress = new LoadingProgressFragment();

		setContentView(R.layout.activity_splash_screen);
		animateMain();
		
		if ( isAlreadyLoggedIn() ) {
			SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE);
			String username = prefs.getString(Constants.PREF_USERNAME, null);
			String password = prefs.getString(Constants.PREF_PASSWORD, null);
			loginAction(username, password);
		}
		setLoginAction();		
		
	}
	
	private boolean isAlreadyLoggedIn(){
		SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE);
		String username = prefs.getString(Constants.PREF_USERNAME, null);
		String password = prefs.getString(Constants.PREF_PASSWORD, null);
		
		if (username != null && password != null ){
			return true;
		}
		return false;
	}
	private void loginAction(String username, String password) {
		Account account = new Account();
		account.setPassword(password);
		account.setUsername(username);
		
		final String json = ModelsFactory.getInstance().getJSONFromAccount(account);
		new LoginTask(json).execute(Constants.SERVER_URL + "/login");
	}
	
	private void saveCredentials(String username, String password) {
		SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE);
		prefs.edit().putString(Constants.PREF_USERNAME, username)
		.putString(Constants.PREF_PASSWORD, password).commit();
	}
	
	private void setLoginAction() {
		final EditText txt_username = (EditText) findViewById(R.id.txt_username);
		final EditText txt_password = (EditText) findViewById(R.id.txt_password);
		final Button btn_login = (Button) findViewById(R.id.btn_login);
		
		btn_login.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final String username = txt_username.getText().toString();
				final String password = txt_password.getText().toString();
				loginAction(username,password);
				saveCredentials(username, password);
			}
		});
		
	}
	
	private void showLoginBox() {
		final View loginBox = findViewById(R.id.login_box);
		loginBox.setVisibility(View.VISIBLE);
	}
	
	private void animateLoginBox() {
		final View loginBox = findViewById(R.id.login_box);

		final Animation animation = getAnimationWithDuration(LOGIN_BOX_FADE_DURATION);
		animation.setStartOffset(FADE_WAIT_DURATION + MAIN_LAYOUT_FADE_DURATION);
		animation.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				loginBox.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				
			}
		});
		loginBox.setAnimation(animation);
		
	}
	
	private void loadMainScreen() {
    	Intent intent = new Intent(this, QuesityMain.class);
    	startActivity(intent);
	}
	private Animation getAnimationWithDuration(int duration) {
		Animation animation = new AlphaAnimation(0, 1);
		animation.setInterpolator(new DecelerateInterpolator());
		animation.setDuration(duration);
		return animation;
	}

	private void animateMain() {
		final View main_layout = findViewById(R.id.splash_main_layout);
		Animation fadeIn_main = getAnimationWithDuration(MAIN_LAYOUT_FADE_DURATION);
		
		fadeIn_main.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				showLoginBox();
			}
		});
		main_layout.setAnimation(fadeIn_main);
	}
	
	private class LoginTask extends FetchJSONTaskPost<Account>{


		@Override
		protected void handle401() {
			showErrorMessage(R.string.error_username_password);
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					showLoginBox();
				}
			});
			
		}



		public LoginTask(String json) {
			super(new JSONPostRequestTypeGetter(json));
			setActivity(SplashScreen.this).setProgressBarHandler(_progress,getString(R.string.lbl_logging_in_title), getString(R.string.lbl_logging_in));
		}
		
		
		
		@Override
		protected void onPostExecute(Account result) {
			super.onPostExecute(result);
			if ( result != null ) {
				SharedPreferences prefs = getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE);
				prefs.edit().putString(Constants.PREF_USER_ACCOUNT_JSON,ModelsFactory.getInstance().getJSONFromAccount(result)).commit();
				loadMainScreen();	
			}
		}
		
		@Override
		protected Account resolveModel(String json) {
			final Account account = ModelsFactory.getInstance().getAccountFromJSON(json);
			return account;
		}
		
	}
}
