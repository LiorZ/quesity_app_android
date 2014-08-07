package com.quesity.fragments;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.quesity.activities.QuestPropertiesActivity;
import com.quesity.activities.QuestsListViewActivity;
import com.quesity.app.R;
import com.quesity.general.Constants;
import com.quesity.general.Utils;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.SavedGame;
import com.quesity.models.StartingLocation;

public class QuestListFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String string = savedInstanceState.getString(QUESTS_KEY);
			
			if (string != null )
				_quests = ModelsFactory.getInstance().getModelFromJSON(string, Quest[].class);
		}
		
		_listener = new ShowQuestPropertiesClickListener();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if ( outState != null ) {
			String jsonFromModel = ModelsFactory.getInstance().getJSONFromModel(_quests);
			outState.putString(QUESTS_KEY, jsonFromModel);
		}
	}


	private static final String QUESTS_KEY = "Quests_List";
	private Quest[] _quests;
	private OnItemClickListener _listener;
	private ListView _list;

	
	private void setQuests(Quest[] qs) {
		this._quests = qs;
	}

	public static QuestListFragment newInstance(Quest[] quests) {
		QuestListFragment frag = new QuestListFragment();
		frag.setQuests(quests);
		return frag;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View main_view = inflater.inflate(R.layout.fragment_quest_list, null, false);
		 _list = (ListView) main_view.findViewById(R.id.quest_list_fragment);
		_list.setAdapter(new QuestAdapter());
		 _list.setOnItemClickListener(_listener);
		 return main_view;
	}
	
	
	
	private class QuestAdapter extends BaseAdapter{

		private int _selected;
		private SavedGame[] _saved_quests;
		public QuestAdapter() {
			FragmentActivity activity = getActivity();
			_selected = -1;
			_saved_quests = ModelsFactory.getInstance().getFromPreferenceStore(activity, Constants.SAVED_GAMES, SavedGame[].class);
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
			        LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context
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
			TextView location_textView = (TextView) v.findViewById(R.id.quest_list_item_location_text);
			TextView time_textView = (TextView) v.findViewById(R.id.quest_list_item_time_text);
			
			StartingLocation startingLocation = q.getStartingLocation();
			String street = startingLocation.getStreet();
			int quest_time = (int) q.getTime();
			time_textView.setText(quest_time + " " + getString(R.string.lbl_minutes));
			
			location_textView.setText(street);
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
	
    private class ShowQuestPropertiesClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position,
				long id) {
			
			Object item = adapter.getItemAtPosition(position);
			if ( item == null || !(item instanceof Quest)){
				return;
			}
			
			FragmentActivity activity = getActivity();
			Intent intent = new Intent(activity, QuestPropertiesActivity.class);
	    	intent.putExtra(Constants.QUEST_OBJ, ModelsFactory.getInstance().getJSONFromModel(item));
	    	activity.startActivity(intent);
		}
    }


}
