
package com.quesity.fragments;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.quesity.activities.QuestPropertiesActivity;
import com.quesity.app.R;
import com.quesity.general.Constants;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.StartingLocation;

public class QuestListFragment extends Fragment {



	private static final String QUESTS_KEY = "Quests_List";
	private Quest[] _quests;
	private OnItemClickListener _listener;
	private ListView _list;
	private DisplayImageOptions _display_options;
	private static final int DISPLAY_FADE_IN_DURATION = 500;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			String string = savedInstanceState.getString(QUESTS_KEY);
			if (string != null ){
				_quests = ModelsFactory.getInstance().getModelFromJSON(string, Quest[].class);
			}
		}
		
		_display_options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(DISPLAY_FADE_IN_DURATION, true, true, false)).
															 showImageOnLoading(R.drawable.no_image).
															 cacheInMemory(true).
															 cacheOnDisc(true).
															 build();
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
		public QuestAdapter() {
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
			   ViewHolder holder;
			   if(convertView == null) {
			        LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context
			        		.LAYOUT_INFLATER_SERVICE);
			        convertView = vi.inflate(R.layout.list_item_view, null);
			        holder = new ViewHolder();
			        setHolder(holder, convertView);
			        convertView.setTag(holder);
			    }else {
			    	holder = (ViewHolder) convertView.getTag();
			    }
			   
			   Quest q =(Quest) getItem(position);
			   setMainTextView(holder,q);
//			   setSubTextView(convertView, q);
			   setQuestProperties(holder, q);
			   setQuestImage(holder, q);
			   setBackground(convertView, position);
			   
			   return convertView;
		}
		
		private void setHolder(ViewHolder h, View v){
			h.image_view = (ImageView) v.findViewById(R.id.quest_img);
			h.main_text = (TextView) v.findViewById(R.id.quest_list_title);
			h.location_text = (TextView) v.findViewById(R.id.quest_list_item_location_text);
			h.time_text = (TextView) v.findViewById(R.id.quest_list_item_time_text); 
		}
		
		private void setQuestImage(ViewHolder h, Quest q) {
			List<String> images = q.getImages();
			if ( images.size() == 0 ) {
				return;
			}
			ImageLoader.getInstance().displayImage(images.get(0), h.image_view,_display_options);
		}
		
		
		private void setQuestProperties(ViewHolder h, Quest q) {
			
			StartingLocation startingLocation = q.getStartingLocation();
			String street = startingLocation.getStreet();
			int quest_time = (int) q.getTime();
			h.time_text.setText(quest_time + " " + getString(R.string.lbl_minutes));
			
			h.location_text.setText(street);
		}
		
		
		private void setBackground(View v,int position) {
			if ( position % 2 == 1)
				v.setBackgroundResource(R.color.list_color_odd);
			else
				v.setBackgroundResource(R.color.list_color_even);
		}
		private void setMainTextView(ViewHolder h, Quest model) {
			h.main_text.setText(model.getTitle());
			h.main_text.setTextColor(getResources().getColor(R.color.quesity_title_color));
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
	static class ViewHolder {
		public TextView main_text;
		public TextView location_text;
		public TextView time_text;
		public ImageView image_view;
	}

}
