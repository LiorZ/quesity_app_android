package com.quesity.general;

public final class Constants {
	public static final String SERVER_URL = "http://10.0.0.6:8000";
	public static final String QUESTION_PAGE_TYPE = "question";
	public static final String OPEN_QUESTION_PAGE_TYPE = "open_question";
	public static final String STATIC_PAGE_TYPE = "static";
	public static final String LOCATION_PAGE_TYPE = "location";
	public static final String STALL_PAGE_TYPE="stall";
	
	public static final int HINTS_MENU_ITEM_INDEX = 0;
	public static final int EXIT_MENU_ITEM_INDEX = 1;
	public static final String PREFS_NAME = "com.quesity.preferences";
	public static final String PREF_USERNAME = "username";
	public static final String PREF_PASSWORD = "password";
	public static final String SELECTED_LIST_ITEM = "com.quesity.selected_list_item";
	public static final String PREF_USER_ACCOUNT_JSON = "com.quesity.user_account_json";
	public static final String EVENT_JSON = "com.quesity.event_json";
	public static final String FACEBOOK_ID_PREF_KEY = "com.quesity.facebook.id";
	public static final String FACEBOOK_FULL_NAME_KEY = "com.quesity.facebook.fullname";

	public static final String CURRENT_ACCOUNT_ID = "com.quesity.current_account_id";
	public static final String CURRENT_GAME_ID = "com.quesity.current_game_id";
	public static final String LOADED_QUESTS = "com.quesity.loaded_quests";

	public static final long GPS_UPDATE_INTERVAL = 10*1000; //milliseconds
	public static final float GPS_MIN_ACCURACY = 20; // meters
}
