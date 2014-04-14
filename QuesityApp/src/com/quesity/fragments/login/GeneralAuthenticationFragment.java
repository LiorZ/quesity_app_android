package com.quesity.fragments.login;

import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;

import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.quesity.activities.SplashScreen;
import com.quesity.app.R;
import com.quesity.models.Account;
import com.quesity.network.FetchJSONTaskPost;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.JSONPostRequestTypeGetter;
import com.quesity.network.NetworkErrorHandler;
import com.quesity.network.NetworkInterface;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.EmailValidator;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

public abstract class GeneralAuthenticationFragment extends Fragment {

	protected View _x_button;
	protected View _v_button;
	
	protected EditText _password;
	protected EditText _email;
	protected ProgressBar _progress; 
	protected Form _form;
	
	protected void setPasswordField(int id, View main_view){
		_password = (EditText) main_view.findViewById(id);
		Validate password_field = new Validate(_password);
		password_field.addValidator(new NotEmptyValidator(getActivity()));
		_form.addValidates(password_field);
	}

	private void setEmailFromDevice() {
		Pattern emailPattern = Patterns.EMAIL_ADDRESS; 
		FragmentActivity activity = getActivity();
		if ( activity == null )
			return;
		
		android.accounts.Account[] accounts = AccountManager.get(activity).getAccounts();
		if ( accounts == null || accounts.length == 0 )
			return; 
		
		for (android.accounts.Account account : accounts) {
			if ( account.name == null )
				continue;
			
		    if (emailPattern.matcher(account.name).matches()) {
		        String possibleEmail = account.name;
		        _email.setText(possibleEmail);
		    }
		}
	}
	
	protected void setEmailField(int id, View main_view) {
		_email = (EditText) main_view.findViewById(id);
		
		setEmailFromDevice();
		
		Validate email_field = new Validate(_email);
		email_field.addValidator(new EmailValidator(getActivity(),R.string.err_email_address));
		email_field.addValidator(new NotEmptyValidator(getActivity()));
		_form.addValidates(email_field);

	}
	
	protected void setXButton(int id, View main_view) {
		_x_button = main_view.findViewById(id);

		_x_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GeneralAuthenticationFragment.this.getFragmentManager().popBackStack();
			}
		});
	}
	
	protected Account getAccount() {
		Account a = new Account();
		a.setUsername(_email.getText().toString());
		a.setPassword(_password.getText().toString());
		return a;
	}
	
	protected void setProgress(int id, View main_view) {
		_progress = (ProgressBar) main_view.findViewById(id);
	}
	
	protected void setVButton(final String uri,int id, View main_view) {
		_v_button = main_view.findViewById(id);
		_v_button.setOnClickListener(getOnClickListener(uri));
	}
	
	protected abstract String getURI();
	
	protected View.OnClickListener getOnClickListener(final String uri) {
		return new View.OnClickListener() {
			
			private void action() {
				_progress.setVisibility(View.VISIBLE);
				_v_button.setOnClickListener(null);
				FragmentActivity activity = getActivity();
				if ( activity == null ) 
					return;
				JSONPostRequestTypeGetter jsonpost = new JSONPostRequestTypeGetter(getActivity());
				try {
					jsonpost.setModel(getAccount());
					FetchJSONTaskPost<Account> task = new FetchJSONTaskPost<Account>(jsonpost,Account.class, activity);
					task.setNetworkErrorHandler(getErrHandler())
					.setPostExecuteCallback(new ActionSuccess())
					.setNetworkInterface(NetworkInterface.getInstance())
					.execute(uri);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onClick(View v) {
				if ( _form.validate() ) {
					action();
				}
			}
			
		};
	}
	
	private class ActionSuccess implements IPostExecuteCallback {

		@Override
		public void apply(Object result) {
			if ( result != null && result instanceof Account ) {
				SplashScreen activity = (SplashScreen) getActivity();
				if (activity == null)
					return;
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
	
	protected abstract NetworkErrorHandler getErrHandler();
	
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_form = new Form();
	}
}
