package com.quesity.fragments.login;

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
import com.facebook.android.Facebook;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.quesity.app.R;
import com.quesity.activities.BaseActivity;
import com.quesity.activities.QuesityMain;
import com.quesity.activities.SplashScreen;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.models.Account;
import com.quesity.network.FetchJSONTaskPost;
import com.quesity.network.INetworkInteraction;
import com.quesity.network.INetworkInterface;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.JSONPostRequestTypeGetter;

public class LoginFragment extends Fragment {

	//	private LoadingProgressFragment _progress;
	private UiLifecycleHelper _uiHelper;
	private LoginButton _loginBtn;
	private boolean _isResumed = false;
	public static final String TAG = "com.quesity.fragment.facebook_login_fragment";
	private LoginStateChangeListener _listener;
	
	private Session.StatusCallback _sessionCallback = 
		    new Session.StatusCallback() {
		    @Override
		    public void call(Session session, 
		            SessionState state, Exception exception) {
		        onSessionStateChange(session, state, exception);
		    }
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View frag = inflater.inflate(R.layout.fragment_login, container,false);
		 _loginBtn = (LoginButton) frag.findViewById(R.id.facebook_login_button);
		_loginBtn.setReadPermissions(Arrays.asList("user_birthday", "email", "user_location"));
		_loginBtn.setFragment(this);
//		_progress = new LoadingProgressFragment();
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
		_listener = null;
		if ( _uiHelper != null )
			_uiHelper.onStop();
		super.onStop();
	}
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		_listener = null;
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
	        	if ( getActivity() == null )
	        		return;
	        	SplashScreen splash = (SplashScreen) getActivity();
	        	splash.removeLoginButtons();
	        	
	        	Log.d("LoginFragment","Session and state are open!");
	            // If the session state is open:
	            // Show the authenticated fragment
//	        	Intent intent = new Intent(this, QuesityMain.class);
//	        	startActivity(intent);
	        	if ( _listener != null ) {
	        		_listener.stateChanged(true);
	        	}
	        	Request newMeRequest = Request.newMeRequest(session, new Request.GraphUserCallback() {

					@Override
					public void onCompleted(GraphUser user, Response response) {
						if ( response.getError() == null  ){
							Log.d("LoginFragment", "Login Completed and was successful");
							String json_to_send = "{\"facebook_id\":\""+user.getId()+"\", \"access_token\":\""+session.getAccessToken()+"\"}";
							final BaseActivity activity = (BaseActivity) getActivity();
							
							INetworkInteraction net_handler = new INetworkInteraction() {
								
								@Override
								public ProgressBarHandler getProgressBarHandler() {
//									return new ProgressBarHandler(getString(R.string.lbl_logging_in), getString(R.string.lbl_loading), _progress);
									return null;
								}
								
								@Override
								public IPostExecuteCallback getPostExecuteCallback() {
									SplashScreen activity = (SplashScreen) getActivity();
									return new LoginProcessor(activity,activity.getMainScreenLoader());
								}
								
								@Override
								public INetworkInterface getNetworkInterface() {
									return activity.getNetworkInterface();
								}
							};
							new FetchJSONTaskPost<Account>(new JSONPostRequestTypeGetter(json_to_send,getActivity()), Account.class,getActivity()).
							setActivity(activity).setNetworkInteractionHandler(net_handler).execute(Config.SERVER_URL+getString(R.string.register_facebook_user));
						}
					}
	        	});
	        	newMeRequest.executeAsync();
	        } else if (state.isClosed() || session.isClosed()) {
	        	_loginBtn.setVisibility(View.VISIBLE);
	        	if (_listener != null)
	        		_listener.stateChanged(false);
	        	
	        	Log.d("LoginFragment","Session and state are closed!");
				SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(getActivity());
				p.edit().putString(Constants.CURRENT_ACCOUNT_ID, null).commit();
	        	((BaseActivity) getActivity()).onBackPressed();
	        }
	        
	    }else {
	    	Log.d("LoginFragment","IsResumed is false");
	    }
	}
	
	public void setLoginStateChangeListener(LoginStateChangeListener listener) {
		_listener = listener;
	}
	
	public interface LoginStateChangeListener {
		public void stateChanged(boolean loggedin);
	}
	
	}
	
