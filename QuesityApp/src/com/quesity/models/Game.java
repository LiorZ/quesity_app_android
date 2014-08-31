package com.quesity.models;

import java.util.Date;
import java.util.List;


public class Game extends JSONModel {

	private Date date_started;
	private boolean is_over;
	private String account_id;
	private String quest_id;
	private List<Move> moves;
	private List<Location> locations;
	private int remaining_hints;
	private boolean is_at_starting_location;
	private int pages_passed = 0;
	
	public Date getDateStarted() {
		return date_started;
	}
	
	public int getPagesPassed() {
		return pages_passed;
	}
	
	public void setPagesPassed(int passed) {
		pages_passed = passed;
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
	
}
