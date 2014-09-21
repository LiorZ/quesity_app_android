package com.quesity.analytics;

import java.util.ArrayList;
import java.util.List;

import com.quesity.activities.QuestPageActivity;
import com.quesity.models.Game;
import com.quesity.models.Quest;


/**
 * Should I use a fragment here?
 * @author lior
 *
 */
public class QuestReporter {
	
	private QuestPageActivity _a;
	private List<GameEvent> _game_listeners;
	
	public QuestReporter(QuestPageActivity a){
		_a = a;
		_game_listeners = new ArrayList<GameEvent>();
		init();
	}
	
	private void init() {
		_game_listeners.add( new GameStartedEvent(_a));
		_game_listeners.add(new GameOverEvent(_a));
	}
	
	public void notifyGameEvent(Game g, Quest q) {
		for( GameEvent e: _game_listeners ){
			e.gameEventOccured(g, q);
		}
	}
	
	public void destroy() {
		for(GameEvent event : _game_listeners ){
			event.destroy();
		}
		
		_game_listeners.clear();
	}
}
