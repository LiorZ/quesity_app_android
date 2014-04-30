package com.quesity.fragments;

import java.util.Date;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.quesity.app.R;
import com.quesity.general.Config;
import com.quesity.general.Constants;
import com.quesity.models.Feedback;
import com.quesity.network.reporting.ModelReport;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

public class FeedbackFragment extends Fragment {

	private EditText _feedback_text;
	private Form _form;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main_view = inflater.inflate(R.layout.fragment_feedback, null,false);
		_feedback_text = (EditText) main_view.findViewById(R.id.feedback_text);
		setNoButton(main_view);
		setYesButton(main_view);
		main_view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		setValidation();
		return main_view;
	}
	
	private void setValidation() { 
		_form = new Form();
		Validate v = new Validate(_feedback_text);
		v.addValidator(new NotEmptyValidator(getActivity(), R.string.feedback_text_empty));
		_form.addValidates(v);
	}
	
	private void removeMe() {
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.remove(FeedbackFragment.this);
		ft.addToBackStack(null);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
		ft.commit();
	}
	
	private void setNoButton(View main_view) {
		View nobtn = main_view.findViewById(R.id.feedback_no_btn);
		nobtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removeMe();
			}
		});
	}
	
	private void setYesButton(View main_view) {
		View yes_btn = main_view.findViewById(R.id.feedback_yes_btn);
		yes_btn.setOnClickListener(new View.OnClickListener() {
			
			private String getAccountId(){
				FragmentActivity activity = getActivity();
				if ( activity == null )
					return null;
				
				SharedPreferences p = PreferenceManager.getDefaultSharedPreferences(activity);
				return p.getString(Constants.CURRENT_ACCOUNT_ID, null);
			}
			private String getFeedback() {
				return _feedback_text.getText().toString();
			}
			
			private void setRemoveListener() {
				final FragmentActivity activity = getActivity();
				if (activity == null)
					return;
				
				activity.getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
					
					@Override
					public void onBackStackChanged() {
						Toast.makeText(activity, activity.getString(R.string.after_feedback_text),2000).show();
						activity.getSupportFragmentManager().removeOnBackStackChangedListener(this);
					}
				});
			}
			
			@Override
			public void onClick(View v) {
				FragmentActivity activity = getActivity();
				if (activity == null)
					return;
				
				if ( !_form.validate() ) { 
					return;
				}
				setRemoveListener();
				Feedback feedback = new Feedback();
				
				feedback.setAccountId(getAccountId());
				feedback.setDateCreated(new Date());
				feedback.setFeedback(getFeedback());
				ModelReport feedback_report = new ModelReport(feedback, activity);
				feedback_report.send(Config.SERVER_URL + activity.getString(R.string.send_feedback));
				removeMe();
			}
		});
	}
	
	
}
