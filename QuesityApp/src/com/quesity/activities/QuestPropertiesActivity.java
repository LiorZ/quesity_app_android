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
import com.quesity.fragments.QuesityButtonView;
import com.quesity.fragments.QuesityPageTitleView;
import com.quesity.fragments.QuestPropertiesItemsFragment;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.fragments.StartingLocationVerifier;
import com.quesity.general.Constants;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.models.SavedGame;

public class QuestPropertiesActivity extends BaseActivity implements QuestProvider{

	private Quest _quest;
	private QuesityButtonView _start_button;
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_quest_properties);
		String quest_str = getIntent().getExtras().getString(Constants.QUEST_OBJ);
		_quest = ModelsFactory.getInstance().getModelFromJSON(quest_str, Quest.class);
		_start_button = (QuesityButtonView) findViewById(R.id.quest_properties_play_button);
		_start_button.setOnClickListener(new StartQuestClickListener());
		addFragments();
		setTitleView();
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
			Intent intent = new Intent(QuestPropertiesActivity.this, QuestPageActivity.class);
	    	intent.putExtra(Constants.QUEST_OBJ, ModelsFactory.getInstance().getJSONFromModel(_quest));
	    	boolean existsInCache = existsInCache(_quest);
			if ( existsInCache ) {
	    		askStartOrResume(intent);
	    	}else {
				startQuestActivity(intent);
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


