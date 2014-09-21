package com.quesity.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.location.Location;

import com.quesity.general.Constants;


public class Game extends JSONModel {

	private Date date_started;
	private boolean is_over;
	private String account_id;
	private String quest_id;
	private List<Move> moves;
	private List<Location> locations;
	private int remaining_hints;
	private boolean is_at_starting_location;
	private List<QuestPage> page_history;
	private QuestPage current_page;
	private Quest quest;
	private List<GamePageChangeListener> _page_change_listeners;
	private List<GameStartedListener> _game_started_listeners;
	
	public Game() {
		_page_change_listeners = new ArrayList<GamePageChangeListener>();
		_game_started_listeners = new ArrayList<GameStartedListener>();
		if ( page_history == null ) {
			page_history = new ArrayList<QuestPage>();
		}
	}
	
	public void addPageChangeListener(GamePageChangeListener l) {
		_page_change_listeners.add(l);
	}
	
	public void removePageChangeListener(GamePageChangeListener l ) {
		_page_change_listeners.remove(l);
	}
	
	private void notifyPageChange(QuestPage prev, QuestPage page, QuestPageLink link) {
		for(GamePageChangeListener l: _page_change_listeners) {
			l.pageChanged(prev,page, link);
		}
	}
	
	private void notifyPreviousPage(QuestPage page){
		for (GamePageChangeListener l : _page_change_listeners ){
			l.previousPage(page);
		}
	}
	
	private void notifyGameStarted() {
		for(GameStartedListener l: _game_started_listeners) {
			l.gameStarted(current_page);
		}
	}
	
	public void addGameStartedListener(GameStartedListener l) {
		_game_started_listeners.add(l);
	}
	
	public void removeGameStartedListener(GameStartedListener l ) {
		_game_started_listeners.remove(l);
	}
	
	public List<QuestPage> getPageHistory() {
		return page_history;
	}

	public Quest getQuest() {
		return quest;
	}

	public void startGame() {
		List<QuestPage> pages = quest.getPages();
		for (QuestPage page : pages){
			if ( page.getIsFirst() ) {
				current_page = page;
				page_history.add(page);
				notifyGameStarted();
				return;
			}
		}

	}
	
	public void setQuest(Quest quest) {
		this.quest = quest;
	}

	private void setCurrentPage(QuestPage p, QuestPageLink link) {
		QuestPage prev = current_page;
		current_page = p;
		if ( page_history == null ) {
			page_history = new ArrayList<QuestPage>();
		}
		page_history.add(p);
		notifyPageChange(prev, current_page, link);
	}
	
	public void goToPreviousPage(){
		if ( page_history == null || page_history.size() < 2 ) {
			return;
		}
		QuestPage questPage = page_history.get(page_history.size()-2);
		if (!questPage.getPageType().equals(Constants.STATIC_PAGE_TYPE)) {
			return;
		}
		current_page = questPage;
		page_history.remove(page_history.size()-1);
		notifyPreviousPage(questPage);
	}
	
	public QuestPage getCurrentPage() {
		return current_page;
	}
	
	public Date getDateStarted() {
		return date_started;
	}
	
	public int getPagesPassed() {
		if ( page_history == null ) {
			return 0;
		}
		
		return page_history.size();
	}
	
	public boolean getIsAtStartingLocation() {
		return is_at_starting_location;
	}
	
	public void setIsAtStartingLocation(boolean is) {
		is_at_starting_location = is;
	}
	
	public void setDateStarted(Date date_started) {
		this.date_started = date_started;
	}
	public boolean getIsover() {
		return is_over;
	}
	public void setIsOver(boolean is_over) {
		this.is_over = is_over;
	}
	public String getAccount_id() {
		return account_id;
	}
	public void setAccount_id(String account_id) {
		this.account_id = account_id;
	}
	public String getQuestId() {
		return quest_id;
	}
	public void setQuestId(String quest_id) {
		this.quest_id = quest_id;
	}
	public List<Move> getMoves() {
		return moves;
	}
	public void setMoves(List<Move> moves) {
		this.moves = moves;
	}
	public List<Location> getLocations() {
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public void setRemainingHints(int h) {
		remaining_hints = h;
	}
	
	public int getRemainingHints() {
		return remaining_hints;
	}
	
	/**
	 * For open question
	 * @param page
	 * @param answer
	 * @return
	 */
	
	public QuestPageLink getNextPage(String answer) {
		if ( current_page == null ) {
			return null;
		}
		QuestPageLink[] links = current_page.getLinks();
		if ( !(links[0] instanceof QuestPageQuestionLink) ) {
			return null;
		}
		
		for (int i = 0; i < links.length; i++) {
			
			QuestPageQuestionLink link =(QuestPageQuestionLink) links[i];
			if ( answer.equals(link.getAnswerTxt()) ) {
				return link;
			}
		}
		
		return null;
	}
	
	/**
	 * Get the next page - for multiple choice
	 * @param which
	 * @return
	 */
	public QuestPageLink getNextPage(int which){
		if ( current_page == null ) {
			return null;
		}
		QuestPageLink[] links = current_page.getLinks();
		if ( !(links[0] instanceof QuestPageQuestionLink) ) {
			return null;
		}
		return links[which];
	}
	
	public void moveToPage(QuestPageLink link) {
		
		String linksToPage = link.getLinksToPage();
		List<QuestPage> pages = quest.getPages();
		for(QuestPage p : pages) {
			if ( p.getId().equals(linksToPage) ){
				setCurrentPage(p,link);
				return;
			}
		}
		
		return;
	}
	
	/**
	 * Get the next page for location page
	 * @param location
	 * @return
	 */
	public QuestPageLink getNextPage(Location location) {
		if ( current_page == null ) {
			return null;
		}
		double lng = location.getLongitude();
		double lat = location.getLatitude();
		QuestPageLink[] links = current_page.getLinks();
		if ( !(links[0] instanceof QuestPageLocationLink )) {
			return null;
		}
		for (int i = 0; i < links.length; i++) {
			QuestPageLocationLink link = (QuestPageLocationLink) links[i];
			double link_lat = link.getLat();
			double link_lng = link.getLng();
			int radius = link.getRadius();
			float result[] = new float[3];
			Location.distanceBetween(link_lat, link_lng, lat, lng, result);
			if ( radius > result[0] ){
				return link;
			}
			
		}
		return null;
	}
	public QuestPageLink getNextPage() {
		if (current_page.getLinks().length > 1) {
			return null;
		}
		
		return current_page.getLinks()[0];
	}
	
}
