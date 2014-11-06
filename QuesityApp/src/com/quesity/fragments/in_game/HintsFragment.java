package com.quesity.fragments.in_game;

import java.util.Map;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.quesity.activities.QuestPageActivity;
import com.quesity.app.R;
import com.quesity.application.QuesityApplication;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.general.Constants;
import com.quesity.models.Game;
import com.quesity.models.Quest;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageHint;

public class HintsFragment extends DialogFragment {

	private Game _currentGame;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final QuestPage page = ((QuestPageActivity) getActivity()).getCurrentQuestPage();
		_currentGame = ((QuestPageActivity) getActivity()).getCurrentGame();
		QuestPageHint[] hints = page.getHints();
		if ( _currentGame != null && _currentGame.getRemainingHints() == 0) {
			Dialog emptyHintsDlg = getNoHintsDialog(R.string.lbl_out_of_hints);
			return emptyHintsDlg;
		}
		if ( hints.length == 0 ){
			Dialog emptyHintsDlg = getNoHintsDialog(R.string.lbl_no_hints);
			return emptyHintsDlg;
		}
		analyticsReportHintTaken(R.string.analytics_hint_taken_action);
		
		final QuestPageHint hint = hints[0];
		_currentGame.setRemainingHints(_currentGame.getRemainingHints()-1);
		String title = getHintDialogTitle();
		Dialog okOnlyDialog = SimpleDialogs.getOKOnlyDialog(title,hint.getHintTxt(), getActivity(), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removeHintFromPage(page, hint);
				dialog.dismiss();
				
			}
		});
		return okOnlyDialog;
	}
	
	private void removeHintFromPage(QuestPage page,QuestPageHint hint) {
		
		QuestPageHint[] hints = page.getHints();
		if ( hints == null )
			return;
		
		if ( hints.length == 0  || hints.length == 1){
			QuestPageHint[] n = new QuestPageHint[0];
			page.setQuestPageHints(n);
			return;
		}
		QuestPageHint[] new_hints = new QuestPageHint[hints.length-1]; 
		
		int i_src = 0;
		int i_tgt = 0;
		while ( i_src < hints.length ){
			if ( !hints[i_src].getId().equals(hint.getId()) ) {
				new_hints[i_tgt] = hints[i_src];
				i_tgt++;
			}
			i_src++;
		}
		page.setQuestPageHints(new_hints);
	}
	
	private void analyticsReportHintTaken(int action) {
		QuestPageActivity activity = (QuestPageActivity) getActivity();
		Quest quest = activity.getQuest();
		QuestPage currentQuestPage = activity.getCurrentQuestPage();
		if (quest == null) {
			return;
		}
		
		QuesityApplication application = (QuesityApplication) activity.getApplication();
		Tracker tracker = application.getTracker();
		
		Map<String, String> event = new HitBuilders.EventBuilder()
	    .setCategory(getString(R.string.analytics_category_quest_event))
	    .setAction(getString(action))
	    .setCustomDimension(Constants.ANALYTICS_QUEST_PAGE_NAME_HIT, currentQuestPage.getPageName())
	    .setLabel(quest.getTitle())
	    .build();
		
		tracker.send(event);
	}
	
	private String getHintDialogTitle() {
		String str = getString(R.string.title_hints_dialog, _currentGame.getRemainingHints());
		return str;
	}
	
	private Dialog getNoHintsDialog(int message){
		String title = getHintDialogTitle();
		return SimpleDialogs.getOKOnlyDialog(title, getString(message), getActivity(), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}

	private class HintsListAdapter extends BaseAdapter {

		private QuestPageHint[] _hints;
		public HintsListAdapter(QuestPage q){
			_hints = q.getHints();
		}
		
		@Override
		public int getCount() {
			return _hints.length;
		}

		@Override
		public Object getItem(int position) {
			return _hints[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			   if(convertView == null) {
			        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context
			        		.LAYOUT_INFLATER_SERVICE);
			        convertView = vi.inflate(android.R.layout.simple_list_item_1, null);
			    }
			   QuestPageHint hint =(QuestPageHint) getItem(position);
			   
			   TextView text_view = (TextView) convertView.findViewById(android.R.id.text1);
			   text_view.setText(hint.getHintTitle());
			   return convertView;
		}
		
	}
}
