package com.quesity.general;

public final class Constants {
	public static final String QUESTION_PAGE_TYPE = "question";
	public static final String OPEN_QUESTION_PAGE_TYPE = "open_question";
	public static final String STATIC_PAGE_TYPE = "static";
	public static final String LOCATION_PAGE_TYPE = "location";
	public static final String STALL_PAGE_TYPE="stall";
	
	public static final int HINTS_MENU_ITEM_INDEX = 0;
	
	public static final int EXIT_MENU_ITEM_INDEX = 2;
	public static final int SHOW_FEEDBACK_ITEM_INDEX = 1;
	public static final int BACK_TO_PREVIOUS_PAGE = 0;
	public static final int EXIT_MENU_ITEM_INDEX_NO_BACK = 1;
	public static final int SHOW_FEEDBACK_ITEM_INDEX_NO_BACK = 0;
	
	public static final String PREFS_NAME = "com.quesity.preferences";
	public static final String PREF_USERNAME = "username";
	public static final String PREF_PASSWORD = "password";
	public static final String SELECTED_LIST_ITEM = "com.quesity.selected_list_item";
	public static final String PREF_USER_ACCOUNT_JSON = "com.quesity.user_account_json";
	public static final String EVENT_JSON = "com.quesity.event_json";

	public static final String CURRENT_ACCOUNT_ID = "com.quesity.current_account_id";
	public static final String CURRENT_GAME_ID = "com.quesity.current_game_id";
	public static final String LOADED_QUESTS = "com.quesity.loaded_quests";
	public static final String SAVED_GAMES = "com.quesity.saved_games";
	public static final String QUEST_OBJ = "com.quesity.quest_obj";
	public static final String QUEST_CURRENT_PAGE = "com.quesity.current_page";
	public static final String QUEST_RESUME_KEY = "com.quesity.resume_key";
	public static final String QUEST_LIST_ACTIVITY_TITLE = "com.quesity.quest_list_activity_title";
	public static final String QUEST_LIST_ACTIVITY_TITLE_IMG = "com.quesity.quest_list_activity_title_img";
	public static final String QUEST_IMAGE_ARRAY_KEY = "com.quesity.fragments.image_array_key";
	public static final String QUEST_IMAGE_URL = "com.quesity.fragments.image_URL";
	public static final String SESSION_ID_KEY = "com.quesity.session_id";
	public static final long GPS_UPDATE_INTERVAL = 10*1000; //milliseconds
	public static final float GPS_MIN_ACCURACY = 20; // meters
	public static final String QUEST_IS_IN_STARTING_LOC = "com.quesity.in_location";
	public static final String QUEST_ACCESS_RESTRICTION_CODE = "code";
	public static final String QUEST_ACCESS_RESTRICTION_KEY = "com.quesity.access_restriction.code";
	public static final String QUEST_ACCESS_RESTRICTION_MAGIC = "tomzion";
	public static final String ANALYTICS_FRAGMENT_TAG = "com.quesity.analytics.fragment";
	
	public static final String CURRENT_LOCATION_QUEST_LIST = "com.quesity.current_location_quest_list";
	
	public static final int ANALYTICS_IN_GAME_PAGE_VIEW = 1;
	public static final int ANALYTICS_QUEST_PAGES_PASSED = 2;
	public static final int ANALYTICS_QUEST_PAGE_NAME_HIT = 3;
}
