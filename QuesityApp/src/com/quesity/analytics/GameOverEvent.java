package com.quesity.analytics;

import java.util.Map;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.quesity.activities.QuestPageActivity;
import com.quesity.app.R;
import com.quesity.application.QuesityApplication;
import com.quesity.models.Game;
import com.quesity.models.Quest;
import com.quesity.models.QuestPage;

public class GameOverEvent extends AbstractGameEvent {

	public GameOverEvent(QuestPageActivity a) {
		super(a);
	}

	@Override
	public void gameEventOccured(Game g, Quest q) {
		QuestPage current_page = _a.getCurrentQuestPage();
		
		if ( current_page.getLinks() == null || current_page.getLinks().length == 0 ) {
			Map<String, String> event = getDefaultEventBuilder(R.string.analytics_quest_event, R.string.analytics_finish_quest, q.getTitle()).build();
			_tracker.send(event);
		}
	}

}
