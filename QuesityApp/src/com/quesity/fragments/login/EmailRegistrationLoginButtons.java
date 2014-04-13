package com.quesity.fragments.login;

import com.quesity.app.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class EmailRegistrationLoginButtons extends Fragment {


	private View.OnClickListener _register_listener;
	private View.OnClickListener _login_listener;
	
	public void setRegisterButtonListener(View.OnClickListener listener) {
		_register_listener = listener;
	}
	
	public void setLoginButtonClickListener( View.OnClickListener listener ) {
		_login_listener = listener;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main_view = inflater.inflate(R.layout.fragment_login_email, null,false);
		
		View reg_email = main_view.findViewById(R.id.register_email_button);
		reg_email.setOnClickListener(_register_listener);
		
		View login_email = main_view.findViewById(R.id.login_email_btn);
		login_email.setOnClickListener(_login_listener);
		
		return main_view;
	}
	
	

}
