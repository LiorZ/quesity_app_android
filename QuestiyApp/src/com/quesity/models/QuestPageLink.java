package com.quesity.models;

public class QuestPageLink extends JSONModel {
	public String getLinksToPage() {
		return links_to_page;
	}
	public void setLinksToPage(String links_to_page) {
		this.links_to_page = links_to_page;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	private String links_to_page;
	private String type;
}
