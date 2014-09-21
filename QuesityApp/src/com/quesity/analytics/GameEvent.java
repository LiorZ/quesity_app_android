package com.quesity.analytics;

import com.quesity.models.Game;
import com.quesity.models.Quest;

public interface GameEvent {
	public void gameEventOccured(Game g,  Quest q);
	public void destroy();
}
