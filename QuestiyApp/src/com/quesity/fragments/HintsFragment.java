package com.quesity.fragments;

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
import com.quesity.R.string;
import com.quesity.activities.QuestPageActivity;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageHint;

public class HintsFragment extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		QuestPage page = ((QuestPageActivity) getActivity()).getCurrentQuestPage();
		if ( page.getHints().length == 0 ){
			Dialog emptyHintsDlg = getEmptyHintsDialog();
			return emptyHintsDlg;
		}
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		final HintsListAdapter hintsListAdapter = new HintsListAdapter(page);
		builder.setTitle(string.title_hints_dialog).setAdapter(hintsListAdapter, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				QuestPageHint item = (QuestPageHint) hintsListAdapter.getItem(which);
				AlertDialog okOnlyDialog = SimpleDialogs.getOKOnlyDialog(item.getHintTxt(), getActivity(), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				okOnlyDialog.show();
			}
		});
		return builder.create();
	}
	
	private Dialog getEmptyHintsDialog(){
		AlertDialog okOnlyDialog = SimpleDialogs.getOKOnlyDialog(getString(R.string.lbl_no_hints), getActivity(),new DialogInterface.OnClickListener() {
			
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
