package com.quesity.models;

import java.util.Date;

public class Review extends JSONModel {

	
	private String account_id;
	private String review_text;
	private float rating;
	private String game_id;
	private Date date_created;
	
	public String getAccountId() {
		return account_id;
	}
	public void setAccountId(String account_id) {
		this.account_id = account_id;
	}
	public String getReviewText() {
		return review_text;
	}
	public void setReviewText(String review_text) {
		this.review_text = review_text;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public String getGameId() {
		return game_id;
	}
	public void setGameId(String game_id) {
		this.game_id = game_id;
	}
	public Date getDateCreated() {
		return date_created;
	}
	public void setDateCreated(Date date_created) {
		this.date_created = date_created;
	}
	
}
