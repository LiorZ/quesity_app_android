package com.quesity.activities;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quesity.R;
import com.quesity.general.Constants;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.SavedGame;

public class QuestsListViewActivity extends BaseActivity{
	private QuestAdapter array_adapter;
	private ListView _quest_list_view;
	private ShowQuestPropertiesClickListener _start_quest_listener;
	public static final String QUEST_ID = "com.quesity.QUEST_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quests_list);
		setTitle(R.string.app_name);
		_quest_list_view =  (ListView) findViewById(R.id.quest_list_fragment);
		_start_quest_listener = new ShowQuestPropertiesClickListener();
		_quest_list_view.setOnItemClickListener(_start_quest_listener);
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
	}
	
	
	private void startQuestActivity(Intent i) {
		startActivity(i);
	}
	
    private class ShowQuestPropertiesClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position,
				long id) {
			Object item = array_adapter.getItem(position);
			if ( item == null || !(item instanceof Quest)){
				return;
			}
			
			Intent intent = new Intent(QuestsListViewActivity.this, QuestPropertiesActivity.class);
	    	intent.putExtra(Constants.QUEST_OBJ, ModelsFactory.getInstance().getJSONFromModel(item));
	    	startQuestActivity(intent);
		}
    }
    
	
	private class QuestAdapter extends BaseAdapter{

		private Quest[] _quests;
		private int _selected;
		private SavedGame[] _saved_quests;
		public QuestAdapter(Quest[] quests, Activity activity) {
			_quests = quests;
			_selected = -1;
			_saved_quests = ModelsFactory.getInstance().getFromPreferenceStore(QuestsListViewActivity.this, Constants.SAVED_GAMES, SavedGame[].class);
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
			   setQuestImage(convertView, q);
			   setBackground(convertView, position);
			   return convertView;
		}
		
		
		private void setQuestImage(View v, Quest q) {
			ImageView img_view = (ImageView) v.findViewById(R.id.quest_img);
			List<String> images = q.getImages();
			if ( images.size() == 0 ) {
				return;
			}
			ImageLoader.getInstance().displayImage(images.get(0), img_view);
		}
		
		private void setQuestProperties(View v, Quest q) {
			TextView distanceView = (TextView) v.findViewById(R.id.quest_list_distance);
			TextView timeView = (TextView) v.findViewById(R.id.quest_list_time);
			TextView playedView = (TextView) v.findViewById(R.id.quest_list_played);
			TextView ratingTextView = (TextView) v.findViewById(R.id.quest_list_rating_text);
			RatingBar ratingBar = (RatingBar) v.findViewById(R.id.quest_list_rating_bar);
			
			ratingBar.setRating(q.getRating());
			playedView.setText(q.getGamesPlayed() + "\n" + getString(R.string.lbl_games_played_raw));
			timeView.setText(q.getTime()/60 + "\n" + getString(R.string.lbl_hours));
			distanceView.setText(q.getDistance() + "\n" + getString(R.string.lbl_distance_unit));
			ratingTextView.setText("("+q.getRating() + ")");
			
			//set the bookmark button:
			ImageView img_btn = (ImageView) v.findViewById(R.id.quest_list_bookmark_btn);
			if ( SavedGame.questIsSaved(_saved_quests, q) ){
				img_btn.setBackgroundResource(R.drawable.book_full_list);
			}else {
				img_btn.setBackgroundResource(R.drawable.book_empty_list);
			}
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

		
	}


}
