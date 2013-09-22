package com.quesity.models;

import java.util.Date;

public class Event extends JSONModel {
	private String title;
	private Account creator;
	private Date date_created;
	private Quest quest;
	private Account[] participants;
	private boolean active;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Account getCreator() {
		return creator;
	}
	public void setCreator(Account creator) {
		this.creator = creator;
	}
	public Date getDate_created() {
		return date_created;
	}
	public void setDateCreated(Date date_created) {
		this.date_created = date_created;
	}
	public Quest getQuest() {
		return quest;
	}
	public void setQuest(Quest quest) {
		this.quest = quest;
	}
	public Account[] getParticipants() {
		return participants;
	}
	public void setParticipants(Account[] participants) {
		this.participants = participants;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}

}
