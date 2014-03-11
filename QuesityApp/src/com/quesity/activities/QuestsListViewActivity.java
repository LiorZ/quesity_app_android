package com.quesity.activities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.quesity.R;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.general.Constants;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.SavedGame;
import com.quesity.network.IPostExecuteCallback;

public class QuestsListViewActivity extends BaseActivity{
	private QuestAdapter array_adapter;
	private ListView _quest_list_view;
	private StartQuestClickListener _start_quest_listener;
	public static final String QUEST_ID = "com.quesity.QUEST_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quests_list);
		setTitle(R.string.app_name);
		_quest_list_view =  (ListView) findViewById(R.id.quest_list_fragment);
		_quest_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		_quest_list_view.setSelector(getResources().getDrawable(R.drawable.list_item_selectable));
		_start_quest_listener = new StartQuestClickListener();
		String quests_json = getIntent().getStringExtra(Constants.LOADED_QUESTS);
		if (quests_json != null && quests_json.length() > 0 )
			showLoadedQuests(quests_json);
	}
	
	
	private void showLoadedQuests(String json) {
		Quest[] quests = ModelsFactory.getInstance().getModelFromJSON(json, Quest[].class);
		List<Quest> quest_list = Arrays.asList(quests);
		Collections.sort(quest_list, new Comparator<Quest>() {

			@Override
			public int compare(Quest lhs, Quest rhs) {
				Float rating_lhs = new Float(Float.valueOf(lhs.getRating()));
				Float rating_rhs = new Float(Float.valueOf(rhs.getRating()));
				return rating_rhs.compareTo(rating_lhs);
			}
		});
		
		quests = (Quest[]) quest_list.toArray();
		array_adapter = new QuestAdapter(quests,QuestsListViewActivity.this);
		_quest_list_view.setAdapter(array_adapter);
		_quest_list_view.setOnItemClickListener(array_adapter);
	}
	
	private boolean existsInCache( Quest q ) {
		SavedGame[] saved_games = ModelsFactory.getInstance().getFromPreferenceStore(this, Constants.SAVED_GAMES, SavedGame[].class);
		if ( saved_games == null ) 
			return false;
		for(int i = 0; i<saved_games.length; ++i ) {
			if (saved_games[i].getQuest().getId().equals(q.getId())){
				return true;
			}
		}
		return false;
	}
	
	private void startQuestActivity(Intent i) {
		startActivity(i);
		finish();
	}
	
    private class StartQuestClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int selected_index = array_adapter.getSelectedIndex();
			if ( selected_index < 0 )
				return;
			Object item = array_adapter.getItem(selected_index);
			if ( item == null || !(item instanceof Quest)){
				return;
			}
			
			Intent intent = new Intent(QuestsListViewActivity.this, QuestPageActivity.class);
	    	intent.putExtra(Constants.QUEST_OBJ, ModelsFactory.getInstance().getJSONFromModel(item));
	    	if ( existsInCache((Quest)item)) {
	    		askStartOrResume(intent);
	    	}else{
				startQuestActivity(intent);
	    	}
		}

		private void askStartOrResume(final Intent i) {
			DialogInterface.OnClickListener resume = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					i.putExtra(Constants.QUEST_RESUME_KEY,true);
					dialog.dismiss();
					startQuestActivity(i);
				}
			};
			
			DialogInterface.OnClickListener start_over = new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					startQuestActivity(i);
				}
			};
			int[] msgs = {R.string.lbl_resume, R.string.lbl_start_over};
			DialogInterface.OnClickListener[] listeners = {resume,start_over};
			SimpleDialogs.getGeneralQuestionDialog(getString(R.string.lbl_resume_start_over), QuestsListViewActivity.this, 
					msgs , listeners).show();
		}
    }
    
    private class NewQuestsPostExecuteCallback implements IPostExecuteCallback {

		@Override
		public void apply(Object r) {
			Quest[] result = (Quest[])r;
			if ( result == null ) {
				AlertDialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.lbl_err_load_quest), QuestsListViewActivity.this);
				errorDialog.show();
				return;
			}
		}

		@Override
		public int get401ErrorMessage() {
			return -1;
		}
    	
    }
	
	private class QuestAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{

		private Quest[] _quests;
		private int _selected;
		public QuestAdapter(Quest[] quests, Activity activity) {
			_quests = quests;
			_selected = -1;
		}
		@Override
		public int getCount() {
			return _quests.length;
		}

		@Override
		public Object getItem(int position) {
			return _quests[position];
		}

		@Override
		public long getItemId(int position) {
			return Long.valueOf(position);
		}
		public int getSelectedIndex(){
			return _selected;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			   if(convertView == null) {
			        LayoutInflater vi = (LayoutInflater)getSystemService(Context
			        		.LAYOUT_INFLATER_SERVICE);
			        convertView = vi.inflate(R.layout.list_item_view, null);
			    }
			   Quest q =(Quest) getItem(position);
			   setMainTextView(convertView,q);
//			   setSubTextView(convertView, q);
			   setQuestProperties(convertView, q);
			   
			   setBackground(convertView, position);
			   return convertView;
		}
		
		
		private void setQuestProperties(View v, Quest q) {
			TextView distanceView = (TextView) v.findViewById(R.id.quest_list_distance);
			TextView timeView = (TextView) v.findViewById(R.id.quest_list_time);
			TextView playedView = (TextView) v.findViewById(R.id.quest_list_played);
			TextView ratingTextView = (TextView) v.findViewById(R.id.quest_list_rating_text);
			RatingBar ratingBar = (RatingBar) v.findViewById(R.id.quest_list_rating_bar);
			
			ratingBar.setRating(q.getRating());
			playedView.setText(q.getGamesPlayed() + "\n" + getString(R.string.lbl_games_played));
			timeView.setText(q.getTime()/60 + "\n" + getString(R.string.lbl_hours));
			distanceView.setText(q.getDistance() + "\n" + getString(R.string.lbl_distance_unit));
			ratingTextView.setText("("+q.getRating() + ")");
		}
		
		private void setBackground(View v,int position) {
			if ( position % 2 == 1)
				v.setBackgroundResource(R.color.list_color_odd);
			else
				v.setBackgroundResource(R.color.list_color_even);
		}
		private void setMainTextView(View convertView, Quest model) {
			   TextView text_view = (TextView) convertView.findViewById(R.id.quest_list_title);

			   text_view.setText(model.getTitle());
			   text_view.setTextColor(getResources().getColor(R.color.quesity_title_color));
		}
		
		private void setSubTextView(View convertView, Quest model) {
			TextView text_view = (TextView) convertView.findViewById(android.R.id.text2);
			String description = model.getDescription();
			if ( description != null ) {
				text_view.setText(description);
			}
		}
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,
				long arg3) {
			if (pos == _selected) {
				unHighlightItem(view);
			}else{
				if ( _selected >= 0  ){
					View someView = _quest_list_view.getChildAt(_selected);
					unHighlightItem(someView);
				}
				highlightItem(view);
				_selected = pos;
			}
			notifyDataSetChanged();
		}
		
		private void unHighlightItem(View view) {
			if ( view == null )
				return;
			view.setBackgroundColor(QuestsListViewActivity.this.getResources().getColor(R.color.splash_screen));
		}
		
		private void highlightItem(View view) {
			if (view == null)
				return;
			view.setBackgroundColor(QuestsListViewActivity.this.getResources().getColor(R.color.list_selected_color));
		}

		
	}


}
