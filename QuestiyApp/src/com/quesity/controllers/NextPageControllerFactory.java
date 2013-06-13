package com.quesity.controllers;

import android.support.v4.app.Fragment;

import com.quesity.fragments.MultipleChoiceFragment;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;
import com.quesity.util.Constants;

public class NextPageControllerFactory {
	private static NextPageControllerFactory _instance;
	private NextPageControllerFactory() {
		
	}
	
	public static NextPageControllerFactory getInstance() {
		if ( _instance == null ){
			_instance = new NextPageControllerFactory();
		}
		return _instance;
	}
	
	
	public Fragment getNextPageController(QuestPage page) {
		String pageType = page.getPageType();
		if ( pageType.equals(Constants.QUESTION_PAGE_TYPE )){
			return new MultipleChoiceFragment();
		}
		return null;
	}
}