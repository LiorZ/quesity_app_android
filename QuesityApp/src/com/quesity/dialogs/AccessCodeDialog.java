package com.quesity.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;

import com.quesity.app.R;
import com.quesity.controllers.AccessRestrictionVerification;
import com.quesity.controllers.CodeProvider;
import com.quesity.fragments.custom_views.EditTextWithErrorDisplay;
import com.quesity.general.Constants;
import com.quesity.models.UsageCode;
import com.quesity.network.IPostExecuteCallback;
import com.quesity.network.NetworkErrorHandler;

public class AccessCodeDialog extends Dialog {

	private EditTextWithErrorDisplay _txt_access_code;
	private View _btn_validate;
	private boolean _is_verified;
	public AccessCodeDialog(Context context) {
		super(context);
		_is_verified = false;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.fragment_code_restriction);
		_txt_access_code = (EditTextWithErrorDisplay) findViewById(R.id.txt_access_code);
		setValidateButton();
	}
	
	public boolean isVerified() {
		return _is_verified;
	}
	
	public String getCurrentCode() {
		return _txt_access_code.getText().toString();
	}
	
	private void setValidateButton() {
		_btn_validate = (View) findViewById(R.id.btn_validate_code);
		AccessRestrictionVerification listener = new AccessRestrictionVerification(getContext());
		listener.setCodeProvider(new CodeProvider() {
			
			@Override
			public UsageCode getCode() {
				UsageCode code = new UsageCode();
				code.setCode(getCurrentCode());
				return code;
			}
		});
		listener.setErrorHandler(new ErrHandler());
		listener.setPostExecuteCallback(new ActionSuccess());
		_btn_validate.setOnClickListener(listener);
	}
	

	private class ActionSuccess implements IPostExecuteCallback {

		@Override
		public void apply(Object result) {
			_is_verified = true;
			AccessCodeDialog.this.dismiss();
		}

		@Override
		public int get401ErrorMessage() {
			return 0;
		}
		
	}
	
	private class ErrHandler implements NetworkErrorHandler {

		private void handle(int msg) {
			
			Context context = getContext();
			
			final String string = context.getString(msg);
			
			Handler handler = new Handler(context.getMainLooper());
			handler.post(new Runnable() {
				
				@Override
				public void run() {
					_txt_access_code.setError(string);
					_txt_access_code.requestFocus();					
				}
			});
			
		}
		@Override
		public void handle401() {
			if ( getCurrentCode().equals(Constants.QUEST_ACCESS_RESTRICTION_MAGIC ) ) {
				_is_verified = true;
				AccessCodeDialog.this.dismiss();
				return;
			}
			
			handle(R.string.err_usage_code_exists);
		}

		@Override
		public void handle500() {
			handle(R.string.err_usage_server_error);
		}
		@Override
		public void handleNoConnection() {
			handle(R.string.error_connecting);
		}
		
	}

}
