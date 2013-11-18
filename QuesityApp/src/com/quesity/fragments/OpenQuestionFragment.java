package com.quesity.fragments;

import com.quesity.R;
import com.quesity.activities.NextPageTransition;
import com.quesity.activities.QuestPageActivity;
import com.quesity.models.ModelsFactory;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;
import com.quesity.models.QuestPageQuestionLink;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.EditText;

public class OpenQuestionFragment extends DialogFragment implements OnDemandFragment{

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		final QuestPage page = ((QuestPageActivity)getActivity()).getCurrentQuestPage();
	    final AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
	    final EditText input = new EditText(getActivity());
	    alert.setView(input);
	    alert.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            String value = input.getText().toString().trim();
	            QuestPageLink nextPage = getNextPageByAnswer(value, page);
	            if ( nextPage == null ) {
	            	AlertDialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.lbl_err_wrong_answer), getActivity());
	            	errorDialog.show();
	            }else {
					NextPageTransition activity = (NextPageTransition) getActivity();
					activity.loadNextPage(nextPage);
	            }
	        }
	    });

	    alert.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int whichButton) {
	            dialog.cancel();
	        }
	    });
	    
	    return alert.create();
	}

	private QuestPageLink getNextPageByAnswer(String answer, QuestPage page) {
		QuestPageLink[] links = page.getLinks();
		for (int i = 0; i < links.length; i++) {
			QuestPageQuestionLink link =(QuestPageQuestionLink) links[i];
			if ( answer.equals(link.getAnswerTxt()) ) {
				return link;
			}
		}
		
		return null;
	}
	@Override
	public void invokeFragment(FragmentManager manager) {
		this.show(manager,"MultipleChoiceFragment");
	}
	
}
