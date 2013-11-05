package com.quesity.models;

import java.util.List;

public class QuestPage extends JSONModel {
	
	public String getPageContent() {
		return page_content;
	}
	public String getPageName() {
		return page_name;
	}
	public int getPageNumber() {
		return page_number;
	}
	public String getPageType() {
		return page_type;
	}
	public QuestPageLink[] getLinks() {
		return links;
	}
	public QuestPageHint[] getHints() {
		return hints;
	}
	public boolean getIsFirst(){
		return is_first;
	}
	private boolean is_first;
	private String page_content;
	private String page_name;
	private int page_number;
	private String page_type;
	private QuestPageLink[] links;
	private QuestPageHint[] hints;
}
