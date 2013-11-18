package com.quesity.models;

import java.util.Date;

public class Move extends JSONModel {
	private Date date;
	private Location location;
	private String link_id;
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getLinkId() {
		return link_id;
	}
	public void setLinkId(String link_id) {
		this.link_id = link_id;
	}

}
