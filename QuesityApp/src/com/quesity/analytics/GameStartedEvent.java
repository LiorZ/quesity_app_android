package com.quesity.analytics;

import java.util.Map;

import com.google.android.gms.analytics.HitBuilders;
import com.quesity.activities.QuestPageActivity;
import com.quesity.app.R;
import com.quesity.models.Game;
import com.quesity.models.Quest;
import com.quesity.models.QuestPage;

public class GameStartedEvent extends AbstractGameEvent{

	public GameStartedEvent(QuestPageActivity a) {
		super(a);
	}

	@Override
	public void gameEventOccured(Game g, Quest q) {
		
		QuestPage page = _a.getCurrentQuestPage();
		if ( page.getIsFirst() ) {
			Map<String, String> event = getDefaultEventBuilder(R.string.analytics_quest_event, R.string.analytics_start_quest, q.getTitle()).build();
			_tracker.send(event);
		}
		
	}

}
