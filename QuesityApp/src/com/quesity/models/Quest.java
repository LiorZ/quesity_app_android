package com.quesity.models;

import java.util.Date;
import java.util.List;

public class Quest extends JSONModel{
	private String title;
	private String accountId;
	private Date date_created;
	private List<QuestPage> pages;
	
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

}
