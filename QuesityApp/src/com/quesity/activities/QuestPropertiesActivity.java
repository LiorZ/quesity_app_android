package com.quesity.activities;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quesity.R;
import com.quesity.controllers.QuestProvider;
import com.quesity.fragments.ImageGalleryViewPagerFragment;
import com.quesity.fragments.QuesityPageTitleView;
import com.quesity.fragments.QuestPropertiesItemsFragment;
import com.quesity.general.Constants;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.viewpagerindicator.IconPagerAdapter;

public class QuestPropertiesActivity extends BaseActivity implements QuestProvider{

	private Quest _quest;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_quest_properties);
		String quest_str = getIntent().getExtras().getString(Constants.QUEST_OBJ);
		_quest = ModelsFactory.getInstance().getModelFromJSON(quest_str, Quest.class);
		addFragments();
		setTitleView();
		
	}
	
	private void addFragments() {
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		//Add properties fragment
		Fragment quest_p = QuestPropertiesItemsFragment.newInstance(_quest);
		trans.add(R.id.properties_pager_fragment_container, quest_p);
		trans.commit();
	}
	
	
	private void setTitleView() {
		QuesityPageTitleView title_view = (QuesityPageTitleView) findViewById(R.id.title_quest_properties);
		title_view.setTitle(_quest.getTitle());
		
	}

	@Override
	public Quest getQuest() {
		return _quest;
	}

//	private View setAccordion(String title, int icon) {
//		View view = LayoutInflater.from(this).inflate(R.layout.accordion_description_view, null);
//		ImageView img = (ImageView) view.findViewById(R.id.quest_accordion_image);
//		img.setImageResource(icon);
//		
//		TextView text = (TextView) view.findViewById(R.id.quest_description_text_view);
//		text.setText(title);
//		return view;
//	}

	

//	@Override
//	public View getContentView(int index) {
//		View view  = new View(this);
//		switch(index) {
//		case 0:
//			view = LayoutInflater.from(this).inflate(R.layout.accordion_starting_location, null);
//			break;
//		case 1:
//			view = LayoutInflater.from(this).inflate(R.layout.accordion_description_content, null);
//			WebView webview = (WebView) view.findViewById(R.id.quest_description_text_view);
//			webview.loadDataWithBaseURL(null, _quest.getDescription(), "text/html", "utf-8", null);
//			webview.setBackgroundColor(0x00000000);
//			break;
//		default:
//			break;
//		}
//		return view;
//	}

//	@Override
//	public void onAccordionStateWillChange(int expandingSegmentIndex,
//			int collapsingSegmentIndex) {
//	}
//
//	@Override
//	public void onAccordionStateChanged(int expandedSegmentIndex,
//			int collapsedSegmentIndex) {
//	}
//
//	private class TogglePolicy implements RoboAccordionTogglePolicy {
//		@Override
//		public int getFirstSegmentToExpandIndex() {
//			return 2;
//		}
//
//		@Override
//		public int getNextSegmentToExpandIndex(int collapsingIndex) {
//			return -1;
//		}
//		
//	}

}


