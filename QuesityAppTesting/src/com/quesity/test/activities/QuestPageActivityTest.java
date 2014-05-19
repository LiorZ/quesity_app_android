package com.quesity.test.activities;

import android.app.Dialog;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.view.View;
import android.webkit.WebView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.quesity.app.R;
import com.quesity.activities.QuestPageActivity;
import com.quesity.activities.QuestsListViewActivity;
import com.quesity.fragments.MultipleChoiceFragment;
import com.quesity.fragments.OnDemandFragment;
import com.quesity.models.QuestPage;
import com.quesity.test.mocks.QuesityApplicationMock;

public class QuestPageActivityTest extends ActivityInstrumentationTestCase2<QuestPageActivity>  {

	public QuestPageActivityTest(Class<QuestPageActivity> activityClass) {
		super(activityClass);
	}

	public QuestPageActivityTest() {
		super(QuestPageActivity.class);
	}
	private QuestPageActivity _activity;
	
	
	  @Override
	  protected void setUp() throws Exception {
	    super.setUp();
//	    InputStream stream = getInstrumentation().getContext().getAssets().open("json/mock_quest_pages.json");
	    Context targetContext = getInstrumentation().getTargetContext();
		QuesityApplicationMock app =(QuesityApplicationMock) targetContext.getApplicationContext();
		app.setStream(getInstrumentation().getContext().getAssets().open("json/mock_quest_pages.json"));
	    Intent intent = new Intent(targetContext,
	        QuestPageActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.putExtra(QuestsListViewActivity.QUEST_ID, "524a88a00465ddf939000003");
	    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(QuestPageActivity.class.getName(), null, false);	    setActivityIntent(intent);
	    setActivityInitialTouchMode(true);
	    getInstrumentation().startActivitySync(intent);
	    _activity = (QuestPageActivity) getInstrumentation().waitForMonitor(monitor);
//	    ObjectGraph.create(new QuestPageModule(stream,targetContext)).inject(_activity);
	  }
	  
	  public void testFirstPage() throws Exception {
		  Instrumentation instrumentation = getInstrumentation();
		  WebView webView =(WebView) _activity.findViewById(R.id.webView);
		  final View btn_continue = _activity.findViewById(R.id.btn_continue);
		  View btn_tactics = _activity.findViewById(R.id.btn_tactics);
		  View btn_menu = _activity.findViewById(R.id.btn_menu);
		  assertNotNull(btn_continue);
		  assertNotNull(btn_tactics);
		  assertNotNull(btn_menu);
		  assertNotNull(webView);

		  QuestPage currentQuestPage = _activity.getCurrentQuestPage();
		  assertNotNull(currentQuestPage);
		  assertEquals(1,currentQuestPage.getPageNumber());
		  assertEquals("<p>Static page number 1</p>\n<p>&nbsp;</p>",currentQuestPage.getPageContent());
		  _activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				btn_continue.performClick();
			}
		  });
		  
		  instrumentation.waitForIdleSync();
		  
		  currentQuestPage = _activity.getCurrentQuestPage();
		  assertNotNull(currentQuestPage);
		  assertEquals(2,currentQuestPage.getPageNumber());
		  assertEquals("<p>Question page number 2</p>\n<p>&nbsp;</p>",currentQuestPage.getPageContent());

		  OnDemandFragment currentFragment = _activity.getCurrentFragment();
		  assertNotNull(currentFragment);

		  assertTrue(currentFragment instanceof MultipleChoiceFragment);
		  _activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				btn_continue.performClick();
			}
		  });
		  instrumentation.waitForIdleSync();
		  
		  
		  Dialog dialog = ((DialogFragment)currentFragment).getDialog();
		  assertNotNull(dialog);

		  assertTrue(dialog.isShowing());
		  final ListView list_view = (ListView) ((MultipleChoiceFragment)currentFragment).getView().findViewWithTag(MultipleChoiceFragment.DIALOG_VIEW_TAG);
		  final ListAdapter adapter = list_view.getAdapter();
		  
		  assertNotNull(list_view);
		  _activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				list_view.performItemClick(adapter.getView(1, null, null), 1, adapter.getItemId(1));
			}
		});
		  
		  instrumentation.waitForIdleSync();
		  currentQuestPage =  _activity.getCurrentQuestPage();
		  assertEquals(4,currentQuestPage.getPageNumber());
		  
	  }
	  

	

}
