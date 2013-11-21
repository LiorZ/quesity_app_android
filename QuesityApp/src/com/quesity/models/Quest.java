package com.quesity.models;

import java.util.Date;
import java.util.List;
public class Quest extends JSONModel{
	private String title;
	private String accountId;
	private Date date_created;
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
	
	public void setDescription(String d) {
		description  = d;
	}

}
