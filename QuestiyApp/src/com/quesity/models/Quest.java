package com.quesity.models;

import java.util.Date;

import org.json.JSONObject;

public class Quest extends JSONModel{
	private String title;
	private String accountId;
	private Date date_created;
	
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
