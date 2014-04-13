package com.quesity.fragments.login;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.quesity.app.R;
import com.quesity.general.Config;
import com.quesity.models.Account;
import com.quesity.network.NetworkErrorHandler;
import com.throrinstudio.android.common.libs.validator.AbstractValidator;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.ValidatorException;

public class EmailRegistrationFragment extends GeneralAuthenticationFragment {

	
	private EditText _first_name;
	private EditText _last_name;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main_view = inflater.inflate(R.layout.fragment_register, null,false);
		
		setPasswordField(R.id.txt_password_field, main_view);
		setEmailField(R.id.txt_email_field,main_view);
		setXButton(R.id.register_no_btn,main_view);
		setVButton(getURI(),R.id.register_yes_btn,main_view);
		setProgress(R.id.register_spinner, main_view);
		
		_first_name = (EditText) main_view.findViewById(R.id.txt_firstname_field);
		_last_name = (EditText) main_view.findViewById(R.id.txt_lastname_field);
		
		setValidators();
		return main_view;
	}
	

	@Override
	protected String getURI() {
		String uri = Config.SERVER_URL + getActivity().getString(R.string.email_registration);
		return uri;
	}
	
	private void setValidators(){
		
		NameValidator nameValidator = new NameValidator(getActivity(),R.string.err_register_name);
		
		Validate first_name_field = new Validate(_first_name);
		Validate last_name_field = new Validate(_last_name);
		
		first_name_field.addValidator(nameValidator);
		last_name_field.addValidator(nameValidator);
		
		_form.addValidates(first_name_field);
		_form.addValidates(last_name_field);
		
	}
	
	@Override
	protected Account getAccount(){
		Account a  = new Account();
		a.setName(_first_name.getText().toString(), _last_name.getText().toString());
		a.setUsername(_email.getText().toString());
		a.setPassword(_password.getText().toString());
		return a;
	}

	
	@Override
	protected NetworkErrorHandler getErrHandler() {
		return new ErrHandler();
	}
	
	private class ErrHandler implements NetworkErrorHandler {

		private void handle() {
			FragmentActivity activity = getActivity();
			
			if ( activity == null ) 
				return;
			
			final String string = activity.getString(R.string.err_email_exists);

			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					_v_button.setOnClickListener(getOnClickListener(getURI()));
					_email.setError(string);
					_progress.setVisibility(View.INVISIBLE);
					_email.requestFocus();
				}
			});
		}
		@Override
		public void handle401() {
			handle();
		}

		@Override
		public void handle500() {
			handle();
		}
		@Override
		public void handleNoConnection() {
			
		}
		
	}
	
	private class NameValidator extends AbstractValidator {
		public NameValidator(Context c, int errorMessageRes) {
			super(c, errorMessageRes);
		}
		
		@Override
		public boolean isValid(String value) throws ValidatorException {
			if ( value == null || value.length() == 0)
				return false;
		    char[] chars = value.toCharArray();

		    for (char c : chars) {
		        if(!Character.isLetter(c)) {
		            return false;
		        }
		    }
		    
		    return true;
		}
		
	}


}
