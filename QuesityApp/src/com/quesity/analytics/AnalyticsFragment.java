package com.quesity.analytics;

import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.quesity.activities.QuestPageActivity;
import com.quesity.app.R;
import com.quesity.application.QuesityApplication;
import com.quesity.general.Constants;
import com.quesity.models.Game;
import com.quesity.models.GamePageChangeListener;
import com.quesity.models.GameStartedListener;
import com.quesity.models.Quest;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;

public class AnalyticsFragment extends Fragment {

	@Override
	public void onPause() {
		super.onPause();
	}


	private Game _game;
	protected Tracker _tracker;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		QuestPageActivity activity = (QuestPageActivity) getActivity();
		_game = activity.getCurrentGame();
		QuesityApplication application = (QuesityApplication) activity.getApplication();
		_tracker = application.getTracker();
		_tracker.set("&uid", activity.getAccountId());
		initListeners();
	}
	
	
	private void initListeners() {
		_game.addGameStartedListener(new GameStartedEvent());
		_game.addPageChangeListener(new PageChangeEvent());
		_game.addPageChangeListener(new GameOverEvent());
	}
	
	public EventBuilder getDefaultEventBuilder(int category,int action, String title) {
		EventBuilder event = new HitBuilders.EventBuilder()
	    .setCategory(getString(category))
	    .setAction(getString(action))
	    .setLabel(title);
	    
	    return event;
		
	}
	
	
	private class GameStartedEvent implements GameStartedListener  {

		@Override
		public void gameStarted(QuestPage first_page) {
			Quest q = _game.getQuest();
			Map<String, String> event = getDefaultEventBuilder(R.string.analytics_category_quest_event, R.string.analytics_action_start_quest, q.getTitle()).build();
			_tracker.send(event);
		}
		
	}
	
	private class GameOverEvent implements GamePageChangeListener {

		
		@Override
		public void pageChanged(QuestPage prev_page, QuestPage currentPage,
				QuestPageLink link) {
			if ( currentPage.getLinks().length == 0  || currentPage.getLinks() == null){
				Quest q = _game.getQuest();
				Map<String, String> event = getDefaultEventBuilder(R.string.analytics_category_quest_event, R.string.analytics_action_finish_quest, q.getTitle()).build();
				_tracker.send(event);
			}
		}
		
		@Override
		public void previousPage(QuestPage page) {
			
		}
		
	}
	
	private class PageChangeEvent implements GamePageChangeListener {

		@Override
		public void pageChanged(QuestPage prev_page, QuestPage currentPage,
				QuestPageLink link) {
			Quest q = _game.getQuest();
			EventBuilder event_builder = getDefaultEventBuilder(R.string.analytics_category_quest_event, R.string.analytics_action_page_change, q.getTitle());
			event_builder.setCustomDimension(Constants.ANALYTICS_QUEST_PAGE_NAME_HIT, currentPage.getPageName());
			
			int pages_passed = _game.getPagesPassed();
			event_builder.setValue(pages_passed);
			Map<String, String> event = event_builder.build();
			_tracker.send(event);
		}
		
		private void sendPagesPassed(QuestPage page){

		}

		@Override
		public void previousPage(QuestPage page) {
			
		}
		
	}
}
