package com.quesity.activities;

import java.util.List;

import pl.outofmemory.roboaccordion.RoboAccordionAdapter;
import pl.outofmemory.roboaccordion.RoboAccordionStateListener;
import pl.outofmemory.roboaccordion.RoboAccordionTogglePolicy;
import pl.outofmemory.roboaccordion.RoboAccordionView;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.quesity.R;
import com.quesity.fragments.ImageGalleryViewPagerFragment;
import com.quesity.fragments.QuesityPageTitleView;
import com.quesity.general.Constants;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;

public class QuestPropertiesActivity extends BaseActivity implements RoboAccordionAdapter,RoboAccordionStateListener {

	private Quest _quest;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_quest_properties);
		String quest_str = getIntent().getExtras().getString(Constants.QUEST_OBJ);
		_quest = ModelsFactory.getInstance().getModelFromJSON(quest_str, Quest.class);
		addGalleryFragment();
		setTitleView();
        RoboAccordionView accordionView = (RoboAccordionView) findViewById(R.id.accordion);
        accordionView.setAccordionAdapter(this);
        accordionView.setListener(this);
        accordionView.setTogglePolicy(new TogglePolicy());
        accordionView.setAnimDuration(300);
	}
	
	private void addGalleryFragment() {
		List<String> images = _quest.getImages();
		Fragment gallery = ImageGalleryViewPagerFragment.newInstance(images.subList(1, images.size()));
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		trans.add(R.id.quest_properties_gallery_fragment_place_holder,gallery);
		trans.commit();
	}
	
	private void setTitleView() {
		QuesityPageTitleView title_view = (QuesityPageTitleView) findViewById(R.id.title_quest_properties);
		title_view.setTitle(_quest.getTitle());
		
	}

	@Override
	public int getSegmentCount() {
		return 3;
	}
	
	private View setAccordion(String title, int icon) {
		View view = LayoutInflater.from(this).inflate(R.layout.accordion_description_view, null);
		ImageView img = (ImageView) view.findViewById(R.id.quest_accordion_image);
		img.setImageResource(icon);
		
		TextView text = (TextView) view.findViewById(R.id.quest_description_text_view);
		text.setText(title);
		return view;
	}
	@Override
	public View getHeaderView(int index) {
		switch (index) {
		case 0:
			return setAccordion(getString(R.string.lbl_accordion_starting_point), R.drawable.start_point);
		case 1:
			return setAccordion(getString(R.string.lbl_accordion_description), R.drawable.desc);

		case 2:
			return setAccordion(getString(R.string.lbl_accordion_reviews), R.drawable.reviews);
		default:
			break;
		}
		return null;
	}

	@Override
	public View getContentView(int index) {
		View view  = new View(this);
		switch(index) {
		case 0:
			view = LayoutInflater.from(this).inflate(R.layout.accordion_starting_location, null);
			break;
		case 1:
			view = LayoutInflater.from(this).inflate(R.layout.accordion_description_content, null);
			WebView webview = (WebView) view.findViewById(R.id.quest_description_text_view);
			webview.loadDataWithBaseURL(null, _quest.getDescription(), "text/html", "utf-8", null);
			webview.setBackgroundColor(0x00000000);
			break;
		default:
			break;
		}
		return view;
	}

	@Override
	public void onAccordionStateWillChange(int expandingSegmentIndex,
			int collapsingSegmentIndex) {
	}

	@Override
	public void onAccordionStateChanged(int expandedSegmentIndex,
			int collapsedSegmentIndex) {
	}

	private class TogglePolicy implements RoboAccordionTogglePolicy {
		@Override
		public int getFirstSegmentToExpandIndex() {
			return 2;
		}

		@Override
		public int getNextSegmentToExpandIndex(int collapsingIndex) {
			return -1;
		}
		
	}

}


