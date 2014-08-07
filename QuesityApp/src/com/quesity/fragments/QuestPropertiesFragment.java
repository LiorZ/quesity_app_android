package com.quesity.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.quesity.app.R;
import com.quesity.controllers.TagView;
import com.quesity.general.Utils;
import com.quesity.layouts.FlowLayout;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.StartingLocation;


/**
 * This class represents just the quest properties fragment, no need for the other 2 or the pager..
 * @author lior
 *
 */
public class QuestPropertiesFragment extends Fragment {

	private Quest _quest;
	private static final String QUEST_DATA_KEY = "QUEST_DATA_KEY";
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if ( outState != null ) {
			String jsonFromModel = ModelsFactory.getInstance().getJSONFromModel(_quest);
			outState.putString(QUEST_DATA_KEY, jsonFromModel);
		}
	}

	public static QuestPropertiesFragment newInstance(Quest q) {
		QuestPropertiesFragment frag = new QuestPropertiesFragment();
		frag.setQuest(q);
		return frag;
	}
	
	private void setQuest(Quest q){
		_quest = q;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		restoreQuestFromInstanceState(savedInstanceState);
		setGallery();
	}
	
	private void restoreQuestFromInstanceState(Bundle b) {
		if ( b == null )
			return;
		String json = b.getString(QUEST_DATA_KEY);
		if ( json == null ) {
			return;
		}
		
		_quest = ModelsFactory.getInstance().getModelFromJSON(json, Quest.class);
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main_view = inflater.inflate(R.layout.accordion_description_content, null,false);
		setMainView(main_view);
		return main_view;
	}
	
	
	public void setMainView(View root) {
		setDescriptionText(root);
		setQuestProperties(root,_quest);
//		addTags(root);
	}
	
	private void setDescriptionText(View root) {
		TextView description_text = (TextView) root.findViewById(R.id.quest_description_text_view);
		Spanned desc_str = Html.fromHtml(_quest.getDescription());
		description_text.setText(desc_str);
	}
	
	private void addTags(View root) {
		/*FlowLayout tag_container_view = (FlowLayout) root.findViewById(R.id.tag_container);
		List<String> tag_list = _quest.getTags();
		for (String tag_text : tag_list) {
			TagView tag_view = new TagView(getActivity());
			tag_view.setTagText(tag_text);
			tag_container_view.addView(tag_view);
		}*/
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
	
	private void setGallery() {
		List<String> images = _quest.getImages();
		
		//Add gallery fragments:
		Fragment gallery = null;
		if ( images == null || images.size() < 2 ) {
			gallery = ImageGalleryViewPagerFragment.newInstance(new ArrayList<String>());
		}else {
			gallery = ImageGalleryViewPagerFragment.newInstance(images.subList(1, images.size()));	
		}
		
		FragmentTransaction trans = getChildFragmentManager().beginTransaction();
		trans.add(R.id.quest_properties_gallery_fragment_place_holder,gallery).commit();
	}

}
