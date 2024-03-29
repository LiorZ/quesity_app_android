package com.quesity.models;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quesity.general.Constants;

public class ModelsFactory {

	private static ModelsFactory _instance = null;
	private Gson _gson;
	protected ModelsFactory() {
		_gson = new GsonBuilder()
		   .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
		   .registerTypeAdapter(QuestPage.class, new QuestPageSerialization())
		   .setExclusionStrategies(new ExclusionStrategy() {
			
			@Override
			public boolean shouldSkipField(FieldAttributes field) {
				
				//Don't serialize to JSON fields that starts with "_"
				if ( field.getName().startsWith("_") && !field.getName().equals("_id") ){
					return true;
				}else {
					return false;
				}
				
			}
			
			@Override
			public boolean shouldSkipClass(Class<?> arg0) {
				return false;
			}
		})
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
	
	@Deprecated
	public Quest[] getQuestsFromJson(String json) {
		Quest[] quests = _gson.fromJson(json, Quest[].class);
		return quests;
	}
	
	public <M> M getModelFromJSON(String json, Class<M> c){
		M object = _gson.fromJson(json, c);
		return object;
	}
	
	public <M> String getJSONFromModel(M model){
		return _gson.toJson(model);
	}
	
	@Deprecated
	public QuestPage getQuestPageFromJson(String json) {
		QuestPage page = _gson.fromJson(json, QuestPage.class);
		return page;
	}
	
	@Deprecated
	public QuestPage[] getQuestPageArrayFromJson(String json) {
		QuestPage[] pages = _gson.fromJson(json, QuestPage[].class);
		return pages;
	}
	
	@Deprecated
	public Account getAccountFromJSON(String json) {
		return _gson.fromJson(json, Account.class);
	}
	
	@Deprecated
	public String getJSONFromAccount(Account a) {
		return _gson.toJson(a);
	}
	
	@Deprecated
	public String getJSONFromQuestPage(QuestPage page) {
		String s = _gson.toJson(page);
		return s;
	}
	
	@Deprecated
	public String getJSONFromEvent(Event e){ 
		String s = _gson.toJson(e);
		return s;
	}
	
	public <M> M getFromPreferenceStore(Context c, String key, Class<M> cl) {
		String json = PreferenceManager.getDefaultSharedPreferences(c).getString(key, null);
		if ( json == null ) {
			return null;
		}
		M model = ModelsFactory.getInstance().getModelFromJSON(json, cl);
		if ( model == null ){
			return null;
		}
		
		return model;
	}
	
	public  <M> void putInPreferenceStore(Context c, String key, M model){
		String json = getJSONFromModel(model);
		PreferenceManager.getDefaultSharedPreferences(c).edit().putString(key, json).commit();
	}
}
