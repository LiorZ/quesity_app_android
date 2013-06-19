package com.quesity.models;

public class QuestPageLocationLink extends QuestPageLink {
	public int getRadius() {
		return radius;
	}
	public void setRadius(int radius) {
		this.radius = radius;
	}
	public String getTxtStreet() {
		return txt_street;
	}
	public void setTxtStreet(String txt_street) {
		this.txt_street = txt_street;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	private double lng;
	private double lat;
	private int radius;
	private String txt_street;
}
