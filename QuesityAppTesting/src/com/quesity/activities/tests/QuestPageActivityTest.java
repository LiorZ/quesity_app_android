package com.quesity.activities.tests;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;

import com.quesity.R;
import com.quesity.activities.QuestPageActivity;
import com.quesity.activities.QuestsListViewActivity;

public class QuestPageActivityTest extends ActivityUnitTestCase<QuestPageActivity>  {

	public QuestPageActivityTest(Class<QuestPageActivity> activityClass) {
		super(activityClass);
		// TODO Auto-generated constructor stub
	}

	public QuestPageActivityTest() {
		super(QuestPageActivity.class);
	}
	private QuestPageActivity _activity;
	
	
	  @Override
	  protected void setUp() throws Exception {
	    super.setUp();
	    Intent intent = new Intent(getInstrumentation().getTargetContext(),
	        QuestPageActivity.class);
	    intent.putExtra(QuestsListViewActivity.QUEST_ID, "524a88a00465ddf939000003");
	    startActivity(intent, null, null);
	    _activity = getActivity();
	  }
	  
	  public void testLayout() throws Exception {
		  WebView webView =(WebView) _activity.findViewById(R.id.webView);
		  View btn_continue = _activity.findViewById(R.id.btn_continue);
		  View btn_tactics = _activity.findViewById(R.id.btn_tactics);
		  View btn_menu = _activity.findViewById(R.id.btn_menu);
		  assertNotNull(btn_continue);
		  assertNotNull(btn_tactics);
		  assertNotNull(btn_menu);
		  assertNotNull(webView);
	  }
	

}
