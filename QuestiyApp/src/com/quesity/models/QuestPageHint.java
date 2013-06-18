package com.quesity.models;

public class QuestPageHint extends JSONModel {
	public String getHintTitle() {
		return hint_title;
	}
	public void setHintTitle(String hint_title) {
		this.hint_title = hint_title;
	}
	public String getHintTxt() {
		return hint_txt;
	}
	public void setHintTxt(String hint_txt) {
		this.hint_txt = hint_txt;
	}
	private String hint_title;
	private String hint_txt;
}
