package com.quesity.models;

import java.util.Date;
import java.util.List;
public class Quest extends JSONModel{
	private String title;
	private String accountId;
	private Date date_created;
	private float rating;
	private float distance;
	private float time;
	private int games_played;
	private List<QuestPage> pages;
	private String description;
	
	public List<QuestPage> getPages() {
		return pages;
	}
	public String getTitle() {
		return title;
	}
	public String getAccountId() {
		return accountId;
	}
	public Date getDateCreated() {
		return date_created;
	}
	
	public String getDescription() {
		return description;
	}
	
	public float getDistance(){
		return distance;
	}
	
	public float getTime() {
		return time;
	}
	
	public float getRating() {
		return rating;
	}
	
	public int getGamesPlayed() {
		return games_played;
	}
	
	public void setDescription(String d) {
		description  = d;
	}

}
