package com.quesity.models;

import java.util.Date;

public class Feedback extends JSONModel {

	private String feedback_text;
	private Date date_created;
	private String account_id;
	public String getFeedback() {
		return feedback_text;
	}
	public void setFeedback(String feedback) {
		this.feedback_text = feedback;
	}
	public Date getDateCreated() {
		return date_created;
	}
	public void setDateCreated(Date date_created) {
		this.date_created = date_created;
	}
	public String getAccountId() {
		return account_id;
	}
	public void setAccountId(String account_id) {
		this.account_id = account_id;
	}
	
}
