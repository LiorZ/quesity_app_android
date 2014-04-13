package com.quesity.fragments.login;

import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.quesity.activities.SplashScreen;
import com.quesity.app.R;
import com.quesity.general.Config;
import com.quesity.models.Account;
import com.quesity.network.FetchJSONTaskPost;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.JSONPostRequestTypeGetter;
import com.quesity.network.NetworkErrorHandler;
import com.quesity.network.NetworkInterface;
import com.throrinstudio.android.common.libs.validator.AbstractValidator;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.ValidatorException;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

public class RegisterFragment extends Fragment {

	private View _x_button;
	private View _v_button;
	
	private EditText _first_name;
	private EditText _last_name;
	private EditText _email;
	private EditText _password;
	
	private Form _form;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main_view = inflater.inflate(R.layout.fragment_register, null,false);
		
		_x_button = main_view.findViewById(R.id.register_no_btn);
		_v_button = main_view.findViewById(R.id.register_yes_btn);
		
		_first_name = (EditText) main_view.findViewById(R.id.txt_firstname_field);
		_last_name = (EditText) main_view.findViewById(R.id.txt_lastname_field);
		_email = (EditText) main_view.findViewById(R.id.txt_email_field);
		_password = (EditText) main_view.findViewById(R.id.txt_password_field);
		
		setValidators();
		setXButton();
		setVButton();
		return main_view;
	}
	
	private void setValidators(){
		
		_form = new Form();
		
		NotEmptyValidator notEmptyValidator = new NotEmptyValidator(getActivity(),R.string.err_empty_field);
		NameValidator nameValidator = new NameValidator(getActivity(),R.string.err_register_name);
		
		Validate email_field = new Validate(_email);
		Validate first_name_field = new Validate(_first_name);
		Validate last_name_field = new Validate(_last_name);
		Validate password_field = new Validate(_password);
		
		first_name_field.addValidator(nameValidator);
		email_field.addValidator(new EmailValidator(getActivity(),R.string.err_email_address));
		email_field.addValidator(notEmptyValidator);
		password_field.addValidator(notEmptyValidator);
		last_name_field.addValidator(nameValidator);
		
		_form.addValidates(email_field);
		_form.addValidates(first_name_field);
		_form.addValidates(last_name_field);
		_form.addValidates(password_field);
		
	}
	
	
	private void setXButton() {
		_x_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RegisterFragment.this.getFragmentManager().popBackStack();
			}
		});
	}
	
	private void setVButton() {
		_v_button.setOnClickListener(new View.OnClickListener() {
			private Account getAccount(){
				Account a  = new Account();
				a.setName(_first_name.getText().toString(), _last_name.getText().toString());
				a.setUsername(_email.getText().toString());
				a.setPassword(_password.getText().toString());
				return a;
			}
			
			private void registerAction() {
				FragmentActivity activity = getActivity();
				JSONPostRequestTypeGetter jsonpost = new JSONPostRequestTypeGetter(getActivity());
				try {
					jsonpost.setModel(getAccount());
					FetchJSONTaskPost<Account> task = new FetchJSONTaskPost<Account>(jsonpost,Account.class, activity);
					task.setNetworkErrorHandler(new ErrHandler())
					.setPostExecuteCallback(new RegistrationSuccess())
					.setNetworkInterface(NetworkInterface.getInstance())
					.execute(Config.SERVER_URL + activity.getString(R.string.email_registration));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onClick(View v) {
				if ( _form.validate() ) {
					registerAction();
				}
			}
			
		});
	}
	
	private class ErrHandler implements NetworkErrorHandler {

		@Override
		public void handle401() {
			String string = getActivity().getString(R.string.err_email_exists);
			_email.setError(string);
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					_email.requestFocus();
				}
			});
		}

		@Override
		public void handle500() {
			
		}
		
	}
	
	private class RegistrationSuccess implements IPostExecuteCallback {

		@Override
		public void apply(Object result) {
			if ( result != null && result instanceof Account ) {
				SplashScreen activity = (SplashScreen) getActivity();
				LoginProcessor processor = new LoginProcessor(activity,activity.getMainScreenLoader());
				processor.processLogin((Account) result);
			}else {
				Log.d("LIOR","Registration NOT Successfull");
			}
		}

		@Override
		public int get401ErrorMessage() {
			return 0;
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
