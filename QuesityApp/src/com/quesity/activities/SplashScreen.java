package com.quesity.activities;

import java.util.Arrays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.quesity.R;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.general.Constants;
import com.quesity.models.Account;
import com.quesity.models.ModelsFactory;
import com.quesity.network.FetchJSONTaskPost;
import com.quesity.network.JSONPostRequestTypeGetter;

public class SplashScreen extends FragmentActivity {

	private boolean isResumed = false;

	private UiLifecycleHelper uiHelper;
	private LoginButton _loginBtn;
	private Session.StatusCallback _sessionCallback = 
		    new Session.StatusCallback() {
		    @Override
		    public void call(Session session, 
		            SessionState state, Exception exception) {
		        onSessionStateChange(session, state, exception);
		    }
	};
	private LoadingProgressFragment _progress;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		_progress = new LoadingProgressFragment();
		
		setContentView(R.layout.activity_splash_screen);
		_loginBtn = (LoginButton) findViewById(R.id.login_button);
		
		_loginBtn.setReadPermissions(Arrays.asList("user_birthday", "email", "user_location"));
		uiHelper = new UiLifecycleHelper(this, _sessionCallback);
	    uiHelper.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onResume() {
	    super.onResume();
	    isResumed = true;
	    uiHelper.onResume();
	}

	@Override
	public void onPause() {
	    super.onPause();
	    isResumed = true;
	    uiHelper.onPause();
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
	    // Only make changes if the activity is visible
	    if (isResumed) {
	        if (state.isOpened()) {
	            // If the session state is open:
	            // Show the authenticated fragment
//	        	Intent intent = new Intent(this, QuesityMain.class);
//	        	startActivity(intent);
	        	Request newMeRequest = Request.newMeRequest(session, new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						if ( response.getError() == null  ){
							Log.d("MainActivity", user.getId());
							Log.d("MainActivity", session.getAccessToken());
						}
					}
					
					
	        	});
	        	newMeRequest.executeAsync();
	        } else if (state.isClosed()) {
	            // If the session state is closed:
	            // Show the login fragment
	        	Intent intent = new Intent(this, SplashScreen.class);
	        	startActivity(intent);
	        }
	    }
	}
	
	private void loadMainScreen() {
    	Intent intent = new Intent(this, QuesityMain.class);
    	startActivity(intent);
	}

	
	private class LoginTask extends FetchJSONTaskPost<Account>{


		@Override
		protected void handle401() {
			showErrorMessage(R.string.error_username_password);
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
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
