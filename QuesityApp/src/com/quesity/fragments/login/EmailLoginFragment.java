package com.quesity.fragments.login;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;

import com.quesity.app.R;
import com.quesity.general.Config;
import com.quesity.network.NetworkErrorHandler;

public class EmailLoginFragment extends GeneralAuthenticationFragment{

	private TextView _err_msg;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main_view = inflater.inflate(R.layout.fragment_email_login, null,false);
		_err_msg = (TextView) main_view.findViewById(R.id.email_login_err_msg);
		setPasswordField(R.id.txt_login_password_field, main_view);
		setEmailField(R.id.txt_login_email_field,main_view);
		setXButton(R.id.login_no_btn,main_view);
		setVButton(getURI(),R.id.login_yes_btn, main_view);
		setProgress(R.id.login_spinner, main_view);
		
		return main_view;
	}
	
	@Override
	protected void setPasswordField(int id, View main_view) {
		super.setPasswordField(id, main_view);
		setFocusHideErrMsg(main_view.findViewById(id));
	}

	
	private void setFocusHideErrMsg(View view){
		if ( view == null )
			return; 
		
		view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				_err_msg.setVisibility(View.INVISIBLE);
			}
		});
	}

	@Override
	protected void setEmailField(int id, View main_view) {
		super.setEmailField(id, main_view);
		setFocusHideErrMsg(main_view.findViewById(id));
		
	}
	@Override
	protected String getURI() {
		return Config.SERVER_URL + getActivity().getString(R.string.email_login);
	}
	
	@Override
	protected NetworkErrorHandler getErrHandler() {
		NetworkErrorHandler handler = new NetworkErrorHandler() {
			
			private void handle(final int strid) {
				final FragmentActivity activity = getActivity();
				if ( activity == null ) 
					return;
				activity.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						_err_msg.setText(activity.getString(strid));
						AlphaAnimation anim = new AlphaAnimation(0, 1);
						anim.setDuration(1000);
						anim.setAnimationListener(new Animation.AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
								_err_msg.setVisibility(View.VISIBLE);
							}
							
							@Override
							public void onAnimationRepeat(Animation animation) {
								
							}
							
							@Override
							public void onAnimationEnd(Animation animation) {
								
							}
						});
						_err_msg.startAnimation(anim);
						_v_button.setOnClickListener(getOnClickListener(getURI()));
						_progress.setVisibility(View.INVISIBLE);
					}
				});
			}
			@Override
			public void handle500() {
				handle(R.string.err_wrong_username);
			}
			
			@Override
			public void handle401() {
				handle(R.string.err_wrong_username);
			}
			@Override
			public void handleNoConnection() {
				handle(R.string.err_connection);
			}
		};
		
		return handler;
	}


}
