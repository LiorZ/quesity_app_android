package com.quesity.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ModelsFactory {

	private static ModelsFactory _instance = null;
	private Gson _gson;
	protected ModelsFactory() {
		_gson = new GsonBuilder()
		   .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
		   .registerTypeAdapter(QuestPage.class, new QuestPageSerialization())
		   .create();
	}
	
	private static void construct() {
		_instance = new ModelsFactory();
	}
	public static ModelsFactory getInstance(){
		if (_instance == null)
			construct();
		return _instance;
	}
	
	public Quest[] getQuestsFromJson(String json) {
		Quest[] quests = _gson.fromJson(json, Quest[].class);
		return quests;
	}
	
	public QuestPage getQuestPageFromJson(String json) {
		QuestPage page = _gson.fromJson(json, QuestPage.class);
		return page;
	}
	
	public QuestPage[] getQuestPageArrayFromJson(String json) {
		QuestPage[] pages = _gson.fromJson(json, QuestPage[].class);
		return pages;
	}
	
	public Account AccountFromJSON(String json) {
		return _gson.fromJson(json, Account.class);
	}
	
	public String JSONFromAccount(Account a) {
		return _gson.toJson(a);
	}
	
	public String QuestPageToJSON(QuestPage page) {
		String s = _gson.toJson(page);
		return s;
	}
}
