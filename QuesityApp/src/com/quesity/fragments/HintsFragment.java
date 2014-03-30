package com.quesity.fragments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

import com.quesity.R;
import com.quesity.activities.QuestPageActivity;
import com.quesity.models.Game;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageHint;

public class HintsFragment extends DialogFragment {

	private HintsListAdapter _hintsListAdapter;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final QuestPage page = ((QuestPageActivity) getActivity()).getCurrentQuestPage();
		final Game currentGame = ((QuestPageActivity) getActivity()).getCurrentGame();
		if ( currentGame != null && currentGame.getRemainingHints() == 0) {
			Dialog emptyHintsDlg = getNoHintsDialog(R.string.lbl_out_of_hints);
			return emptyHintsDlg;
		}
		if ( page.getHints().length == 0 ){
			Dialog emptyHintsDlg = getNoHintsDialog(R.string.lbl_no_hints);
			return emptyHintsDlg;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		_hintsListAdapter = new HintsListAdapter(page);
		builder.setTitle(R.string.title_hints_dialog).setAdapter(_hintsListAdapter, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				final QuestPageHint item = (QuestPageHint) _hintsListAdapter.getItem(which);
				
				currentGame.setRemainingHints(currentGame.getRemainingHints()-1);
				Dialog okOnlyDialog = SimpleDialogs.getOKOnlyDialog(getString(R.string.lbl_hint_dialog_title),item.getHintTxt(), getActivity(), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						removeHintFromPage(page, item);
						dialog.dismiss();
						
					}
				});
				okOnlyDialog.show();
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
		});
		return builder.create();
	}
	
	private Dialog getNoHintsDialog(int message){
		Dialog okOnlyDialog = SimpleDialogs.getErrorDialog(getString(message), getActivity(),new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		return okOnlyDialog;
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
