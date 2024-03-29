package com.quesity.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.quesity.app.R;
import com.quesity.controllers.QuestProvider;
import com.quesity.dialogs.AccessCodeDialog;
import com.quesity.fragments.QuesityButtonView;
import com.quesity.fragments.QuesityPageTitleView;
import com.quesity.fragments.QuestPropertiesFragment;
import com.quesity.fragments.QuestPropertiesItemsFragment;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.general.Constants;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.SavedGame;

public class QuestPropertiesActivity extends BaseActivity implements QuestProvider{

	private Quest _quest;
	private QuesityButtonView _start_button;
	private static final String PROPERTY_FRAGMENT_TAG = "PROPERTY_FRAGMENT";
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_quest_properties);
		String quest_str = getIntent().getExtras().getString(Constants.QUEST_OBJ);
		_quest = ModelsFactory.getInstance().getModelFromJSON(quest_str, Quest.class);
		_start_button = (QuesityButtonView) findViewById(R.id.quest_properties_play_button);
		_start_button.setOnClickListener(new StartQuestClickListener());
//		addFragments(); // Removed because we don't need the other fragments ... just one description page, that's all
		Fragment property_frag = getSupportFragmentManager().findFragmentByTag(PROPERTY_FRAGMENT_TAG);
		if ( property_frag == null )
			addPropertiesFragment();
		setTitleView();
	}
	
	private void addPropertiesFragment() {
		QuestPropertiesFragment frag = QuestPropertiesFragment.newInstance(_quest);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.add(R.id.properties_pager_fragment_container, frag,PROPERTY_FRAGMENT_TAG).commit();
	}
	
	@Override
	protected String getScreenViewPath() {
		String title = _quest.getTitle();
		return "Quest Properties " + title;
	}
	
	private void addFragments() {
		FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
		
		//Add properties fragment
		Fragment quest_p = QuestPropertiesItemsFragment.newInstance(_quest);
		trans.add(R.id.properties_pager_fragment_container, quest_p);
		trans.commit();
	}
	
	
	private void setTitleView() {
		QuesityPageTitleView title_view = (QuesityPageTitleView) findViewById(R.id.title_quest_properties);
		title_view.setTitle(_quest.getTitle());
	}

	@Override
	public Quest getQuest() {
		return _quest;
	}
	
	private boolean existsInCache( Quest q ) {
		SavedGame[] saved_games = ModelsFactory.getInstance().getFromPreferenceStore(this, Constants.SAVED_GAMES, SavedGame[].class);
		return SavedGame.questIsSaved(saved_games, q);
	}
	
	private void startQuestActivity(Intent i) {
		startActivity(i);
		finish();
	}
	
	private class StartQuestClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			final Intent intent = new Intent(QuestPropertiesActivity.this, QuestPageActivity.class);
	    	intent.putExtra(Constants.QUEST_OBJ, ModelsFactory.getInstance().getJSONFromModel(_quest));
	    	boolean existsInCache = existsInCache(_quest);
			if ( existsInCache ) {
	    		askStartOrResume(intent);
	    	}else {
	    		String accessRestriction = _quest.getAccessRestriction();
	    		if ( accessRestriction.equals(Constants.QUEST_ACCESS_RESTRICTION_CODE) ) {
	    			AccessCodeDialog accessCodeDialog = new AccessCodeDialog(QuestPropertiesActivity.this);
					accessCodeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
						
						@Override
						public void onDismiss(DialogInterface dialog) {
							boolean verified = ((AccessCodeDialog) dialog).isVerified();
							String currentCode = ((AccessCodeDialog) dialog).getCurrentCode();
							if ( verified ){
								Log.d("QuestPropertiesActivity", currentCode);
								intent.putExtra(Constants.QUEST_ACCESS_RESTRICTION_KEY, currentCode);
								
								startQuestActivity(intent);
							}
						}
					});
					accessCodeDialog.show();
	    		}else {
	    			startQuestActivity(intent);
	    		}
	    		
	    	}
		}

		private void askStartOrResume(final Intent i) {
			DialogInterface.OnClickListener resume = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					i.putExtra(Constants.QUEST_RESUME_KEY,true);
					dialog.dismiss();
					startQuestActivity(i);
				}
			};

			DialogInterface.OnClickListener start_over = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					startQuestActivity(i);
				}
			};
			int[] msgs = {R.string.lbl_resume, R.string.lbl_start_over};
			DialogInterface.OnClickListener[] listeners = {resume,start_over};
			SimpleDialogs.getGeneralQuestionDialog(getString(R.string.title_resume_game),getString(R.string.lbl_resume_start_over), QuestPropertiesActivity.this, 
					msgs , listeners).show();
		}
    }
	
	
}


