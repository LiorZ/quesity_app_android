package com.quesity.fragments.in_game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.quesity.activities.NextPageTransition;
import com.quesity.activities.QuestPageActivity;
import com.quesity.app.R;
import com.quesity.dialogs.OpenQuestionDialog;
import com.quesity.dialogs.QuesityDialog;
import com.quesity.fragments.OnDemandFragment;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.models.Game;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;
import com.quesity.models.QuestPageQuestionLink;

public class OpenQuestionFragment extends DialogFragment implements OnDemandFragment{

	private static final long VERIFYING_ANSWER_DELAY = 1900;
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final OpenQuestionDialog alert = new OpenQuestionDialog(getActivity());
		alert.setTitle(getString(R.string.lbl_open_question_title));
		alert.setMessage(getString(R.string.lbl_open_question_message));
    	final String wrong_answer_title = getString(R.string.lbl_err_wrong_answer_title);
		final String wrong_answer_text = getString(R.string.lbl_err_wrong_answer);
		
		final Game game = ((QuestPageActivity)getActivity()).getCurrentGame();
		
	    alert.setButtonDontDismiss(Dialog.BUTTON_POSITIVE,getString(R.string.button_ok), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	        	alert.setProgressVisibility(View.VISIBLE);
	        	Handler handler = new Handler();
	        	Runnable task  = new Runnable() {
					
					@Override
					public void run() {
						if ( getActivity() == null ) {
							return;
						}
						String value = alert.getAnswer().trim();
			            QuestPageLink nextPage = game.getNextPage(value);
			            if ( nextPage == null ) {
							Dialog okOnlyDialog = SimpleDialogs.getOKOnlyDialog(wrong_answer_title, wrong_answer_text, getActivity());
			            	okOnlyDialog.show();
			            }else {
							game.moveToPage(nextPage);
			            }
			            alert.dismiss();
					}
				};
				handler.postDelayed(task, VERIFYING_ANSWER_DELAY);
				
				
	        }
	    });

	    alert.setButton(Dialog.BUTTON_NEGATIVE,getString(R.string.button_cancel), new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            dialog.dismiss();
	        }
	    });
	    
	    return alert;
	}
	
	@Override
	public void invokeFragment(FragmentManager manager) {
		this.show(manager,"MultipleChoiceFragment");
	}

	@Override
	public int getButtonDrawable() {
		return R.drawable.enter_text;
	}

	@Override
	public int getPressedButtonDrawable() {
		return R.drawable.enter_text_pressed;
	}

	@Override
	public int getButtonStringId() {
		return R.string.ingame_btn_enter_text;
	}
	
}
