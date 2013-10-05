package com.quesity.fragments;

import java.util.Arrays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.quesity.R;
import com.quesity.activities.QuesityMain;
import com.quesity.activities.SplashScreen;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.models.Account;
import com.quesity.models.ModelsFactory;
import com.quesity.network.FetchJSONTaskPost;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.JSONPostRequestTypeGetter;

public class LoginFragment extends Fragment {

	private LoadingProgressFragment _progress;
	private UiLifecycleHelper _uiHelper;
	private LoginButton _loginBtn;
	private Session.StatusCallback _sessionCallback = 
		    new Session.StatusCallback() {
		    @Override
		    public void call(Session session, 
		            SessionState state, Exception exception) {
		        onSessionStateChange(session, state, exception);
		    }
	};

	private boolean _isResumed = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View frag = inflater.inflate(R.layout.fragment_login, container);
		 _loginBtn = (LoginButton) frag.findViewById(R.id.facebook_login_button);
		_loginBtn.setReadPermissions(Arrays.asList("user_birthday", "email", "user_location"));
		_loginBtn.setFragment(this);
		_progress = new LoadingProgressFragment();
		return frag;
	}
	@Override
	public void onResume() {
	    super.onResume();
	    if ( _uiHelper != null ){
		    _uiHelper.onResume();
	    }
	    _isResumed = true;
	    Log.d("LoginFragment","Setting isresumed to be true");
	}

	@Override
	public void onPause() {
	    super.onPause();
	    if ( _uiHelper != null ) {
		    _uiHelper.onPause();
	    }
	    _isResumed = false;
	    Log.d("LoginFragment","Setting isresumed to be false");
	}
	
	@Override
	public void onStop() {
		if ( _uiHelper != null )
			_uiHelper.onStop();
		super.onStop();
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if ( _uiHelper != null )
			_uiHelper.onDestroy();
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    _uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    if ( _uiHelper != null )
	    	_uiHelper.onDestroy();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_uiHelper = new UiLifecycleHelper(getActivity(), _sessionCallback);
		if (_uiHelper != null )
			_uiHelper.onCreate(savedInstanceState);
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (_uiHelper != null )
			_uiHelper.onSaveInstanceState(outState);
	}
	
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
	    // Only make changes if the activity is visible
	    if (_isResumed) {
	    	Log.d("LoginFragmnet","isResumed is true");
	        if (state.isOpened() && session.isOpened()) {
	        	Log.d("LoginFragment","Session and state are open!");
	            // If the session state is open:
	            // Show the authenticated fragment
//	        	Intent intent = new Intent(this, QuesityMain.class);
//	        	startActivity(intent);
	        	Request newMeRequest = Request.newMeRequest(session, new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						if ( response.getError() == null  ){
							Log.d("LoginFragment", "Login Completed and was successful");
							String json_to_send = "{\"facebook_id\":\""+user.getId()+"\", \"access_token\":\""+session.getAccessToken()+"\"}";
							saveUserToPreferences(user);
							
							new FetchJSONTaskPost<Account>(new JSONPostRequestTypeGetter(json_to_send), Account.class).
							setPostExecuteCallback(new SendLoginToServerTask()).execute(Config.SERVER_URL+getString(R.string.register_facebook_user));
//							new SendLoginToServerTask(json_to_send).execute(Config.SERVER_URL+getString(R.string.register_facebook_user));
						}
					}
	        	});
	        	newMeRequest.executeAsync();
	        } else if (state.isClosed() || session.isClosed()) {
	            // If the session state is closed:
	            // Show the login fragment
	        	Log.d("LoginFragment","Session and state are closed!");
	        	Intent intent = new Intent(getActivity(), SplashScreen.class);
	        	startActivity(intent);
	        }
	        
	    }else {
	    	Log.d("LoginFragment","IsResumed is false");
	    }
	}
	
	private void saveUserToPreferences(GraphUser user) {
		SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getActivity());
		p.edit().putString(Constants.FACEBOOK_ID_PREF_KEY,user.getId()).putString(Constants.FACEBOOK_FULL_NAME_KEY, user.getName()).commit();

	}
	private void loadMainScreen() {
    	Intent intent = new Intent(getActivity(), QuesityMain.class);
    	startActivity(intent);
	}
	
	private class SendLoginToServerTask implements IPostExecuteCallback<Account> {

		@Override
		public void apply(Account result) {
			if ( result != null ) {
				loadMainScreen();	
			}
		}

		@Override
		public int get401ErrorMessage() {
			return R.string.error_general_authentication;
		}	
		
		
	
	}
	
//	private class SendLoginToServerTask extends FetchJSONTaskPost<Account>{
//
//
//		@Override
//		protected void handle401() {
//			showErrorMessage(R.string.error_general_authentication);
//		}
//
//
//
//		public SendLoginToServerTask(String json) {
//			super(new JSONPostRequestTypeGetter(json));
//			setActivity(getActivity()).setProgressBarHandler(_progress,getString(R.string.lbl_logging_in_title), getString(R.string.lbl_logging_in));
//		}
//		
//		
//		
//		@Override
//		protected void onPostExecute(Account result) {
//			super.onPostExecute(result);
//			if ( result != null ) {
//				loadMainScreen();	
//			}
//		}
//		
//		@Override
//		protected Account resolveModel(String json) {
//			Account account = ModelsFactory.getInstance().getModelFromJSON(json, Account.class);
//			return account;
//		}
		
	}
	
