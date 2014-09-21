package com.quesity.analytics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;
import com.quesity.activities.QuestPageActivity;
import com.quesity.application.QuesityApplication;

public abstract class AbstractGameEvent implements GameEvent {

	protected QuestPageActivity _a;
	protected Tracker _tracker;
	protected String _account_id;
	
	@Override
	public void destroy() {
		_a = null;
	}

	public AbstractGameEvent(QuestPageActivity a) {
		a = _a;
		QuesityApplication application = (QuesityApplication) _a.getApplication();
		_tracker = application.getTracker();
		_tracker.set("&uid", _a.getAccountId());
	}
	
	protected EventBuilder getDefaultEventBuilder(int category,int action, String title) {
		EventBuilder event = new HitBuilders.EventBuilder()
	    .setCategory(_a.getString(category))
	    .setAction(_a.getString(action))
	    .setLabel(title);
	    
	    return event;
		
	}
	
}
