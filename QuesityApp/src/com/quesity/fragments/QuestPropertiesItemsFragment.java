package com.quesity.fragments;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.Spanned;
import android.text.format.Formatter;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.quesity.R;
import com.quesity.controllers.TagView;
import com.quesity.fragments.QuestPropertiesItemContainerFrag.FragmentManipulator;
import com.quesity.layouts.FlowLayout;
import com.quesity.models.Quest;
import com.viewpagerindicator.TabPageIndicator;

public class QuestPropertiesItemsFragment extends Fragment {
	
	private ViewPager _pager;
	private TabPageIndicator _indicator;
	private Quest _quest;
	
	
	public static Fragment newInstance(Quest q) {
		QuestPropertiesItemsFragment f = new QuestPropertiesItemsFragment();
		f.setQuest(q);
		return f;
	}
	private void setQuest(Quest q) {
		_quest =q;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Context themeWrapper = new ContextThemeWrapper(getActivity(),R.style.CustomTabPageIndicator);
		LayoutInflater context_inflater = inflater.cloneInContext(themeWrapper);
		View view = context_inflater.inflate(R.layout.fragment_quest_properties_items, container,false);
		_pager = (ViewPager) view.findViewById(R.id.items_pager);
		_pager.setOffscreenPageLimit(2);
		_indicator = (TabPageIndicator) view.findViewById(R.id.items_indicator);
		
		_pager.setAdapter(new QuestPropertiesItems(getFragmentManager()));
		_indicator.setViewPager(_pager);
		return view;
	}


	private class QuestPropertiesItems extends FragmentPagerAdapter {

		private int[] TITLES = { R.string.lbl_tab_description, R.string.lbl_tab_starting_point, R.string.lbl_tab_reviews };
		private int[] VIEWS = {R.layout.accordion_description_content,R.layout.accordion_starting_location, R.layout.accordion_description_content};
		@Override
		public CharSequence getPageTitle(int position) {
			return getString(TITLES[position % TITLES.length]);
		}

		public QuestPropertiesItems(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int num) {
			switch (num) {
			case 0:
				FragmentManipulator desc_manip = getDescriptionMainView();
				Fragment frag = QuestPropertiesItemContainerFrag.newInstance(VIEWS[num%VIEWS.length],desc_manip);
				return frag;
			case 1:
				Fragment f = QuestPropertiesItemContainerFrag.newInstance(VIEWS[num%VIEWS.length],getStartingLocationView());
				return f;
			case 2: 
				Fragment frag_rev = QuestPropertiesItemContainerFrag.newInstance(VIEWS[num%VIEWS.length],null);
				return frag_rev;
			default:
				break;
			}
			return null;
		}

		@Override
		public int getCount() {
			return VIEWS.length;
		}
		
		private FragmentManipulator getDescriptionMainView() {
			
			FragmentManipulator m = new FragmentManipulator() {
				
				@Override
				public void manipulateView(View root) {
					setDescriptionText(root);
					setQuestData(root);
					addTags(root);
				}
				
				private void setDescriptionText(View root) {
					TextView description_text = (TextView) root.findViewById(R.id.quest_description_text_view);
					Spanned desc_str = Html.fromHtml(_quest.getDescription());
					description_text.setText(desc_str);
				}
				
				private void addTags(View root) {
					FlowLayout tag_container_view = (FlowLayout) root.findViewById(R.id.tag_container);
					List<String> tag_list = _quest.getTags();
					for (String tag_text : tag_list) {
						TagView tag_view = new TagView(getActivity());
						tag_view.setTagText(tag_text);
						tag_container_view.addView(tag_view);
					}
				}

				private void setQuestData(View root) {
					//Set rating:
					RatingBar rating_view = (RatingBar) root.findViewById(R.id.quest_properties_rating_bar);
					rating_view.setRating(_quest.getRating());
					TextView rating_text = (TextView) root.findViewById(R.id.quest_properties_rating_bar_text);
					rating_text.setText("(" + _quest.getRating() + ")");
					
					//set distance and time:
					TextView distance_time_text = (TextView ) root.findViewById(R.id.quest_properties_distance_time_text);
					distance_time_text.setText(_quest.getDistance() + "Km/" + _quest.getTime()/60 + "h");
					
					
					//set number of games played:
					String formatted_games_played = String.format(getString(R.string.lbl_games_played), _quest.getGamesPlayed());
					TextView games_played = (TextView) root.findViewById(R.id.quest_properties_participants);
					games_played.setText(formatted_games_played);
				}
				
				@Override
				public void manipulateFragment(Fragment frag) {
					List<String> images = _quest.getImages();
					
					//Add gallery fragments:
					Fragment gallery = ImageGalleryViewPagerFragment.newInstance(images.subList(1, images.size()));
					FragmentTransaction trans = frag.getChildFragmentManager().beginTransaction();
					trans.add(R.id.quest_properties_gallery_fragment_place_holder,gallery).commit();
				}
			};

			return m;
		}
		
		private FragmentManipulator getStartingLocationView() {
			FragmentManipulator m = new FragmentManipulator() {
				
				@Override
				public void manipulateView(View root) {

				}

				@Override
				public void manipulateFragment(Fragment frag) {

				}
			};
			
			return m;
		}
		
	}
}
