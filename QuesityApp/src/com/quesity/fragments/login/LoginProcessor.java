package com.quesity.fragments.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.quesity.activities.QuesityMain;
import com.quesity.app.R;
import com.quesity.general.Constants;
import com.quesity.models.Account;
import com.quesity.network.IPostExecuteCallback;

public class LoginProcessor implements IPostExecuteCallback{
	
	private Context _context;
	private ScreenLoader _loader;
	
	public LoginProcessor(Context c, ScreenLoader l) {
		_context = c;
		_loader = l;
	}
	
	public void processLogin(Account result) {
		if ( result != null ) {
			SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(_context);
			String current_account_id = p.getString(Constants.CURRENT_ACCOUNT_ID, null);
			p.edit().putString(Constants.CURRENT_ACCOUNT_ID, result.getId()).commit();
			if ( current_account_id == null ) //This is a fresh login, therefore load main screen
				_loader.loadScreen();
		}
	}

	@Override
	public void apply(Object result) {
		Account a = (Account) result;
		processLogin(a);
	}

	@Override
	public int get401ErrorMessage() {
		return R.string.error_general_authentication;
	}	
	
	public interface ScreenLoader {
		public void loadScreen();
	}
}
