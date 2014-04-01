package com.quesity.fragments;

import com.quesity.app.R;
import com.quesity.activities.NextPageTransition;
import com.quesity.activities.QuestPageActivity;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;
import com.quesity.models.QuestPageQuestionLink;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class MultipleChoiceFragment extends DialogFragment implements OnDemandFragment{
	public static final String DIALOG_VIEW_TAG = "com.quesity.fragments.MultipleChoiceDialog";

	@Override
	public void invokeFragment(FragmentManager manager) {
		this.show(manager,"MultipleChoiceFragment");
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    final QuestPage quest_page = ((QuestPageActivity)getActivity()).getCurrentQuestPage();
	    builder.setTitle(R.string.lbl_choose_answer)
	    		.setAdapter(new AnswersListAdapter(quest_page), new OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						Log.d("MultipleChoiceFragment", "Item " + which + "Clicked");
						NextPageTransition activity = (NextPageTransition) getActivity();
						QuestPageLink questPageLink = quest_page.getLinks()[which];
						activity.loadNextPage(questPageLink);
					}
				});
	    return builder.create();
	}

	private class AnswersListAdapter extends BaseAdapter implements ListAdapter {
		
		private QuestPage _page;
		private QuestPageLink[] _links;
		public AnswersListAdapter(QuestPage page) {
			_page = page;
			_links = page.getLinks();
		}
		@Override
		public int getCount() {
			return _links.length;
		}
		@Override
		public Object getItem(int position) {
			return _links[position];
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
			   QuestPageLink link =(QuestPageLink) getItem(position);
			   
			   TextView text_view = (TextView) convertView.findViewById(android.R.id.text1);
			   text_view.setText(((QuestPageQuestionLink)link).getAnswerTxt());
			   convertView.setTag(DIALOG_VIEW_TAG);
			   return convertView;
		}
		
	}
}
