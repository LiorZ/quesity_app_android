package com.quesity.models;

import com.quesity.general.Constants;

import android.content.Context;
import android.preference.PreferenceManager;

public class SavedGame extends JSONModel {

	private Game game;
	private QuestPage currentPage;
	private QuestPage[] pages;
	private Quest quest;
	
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
	public QuestPage getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(QuestPage currentPage) {
		this.currentPage = currentPage;
	}
	public QuestPage[] getPages() {
		return pages;
	}
	public void setPages(QuestPage[] pages) {
		this.pages = pages;
	}
	
	public Quest getQuest() {
		return quest;
	}
	public void setQuest(Quest quest) {
		this.quest = quest;
	}

}
