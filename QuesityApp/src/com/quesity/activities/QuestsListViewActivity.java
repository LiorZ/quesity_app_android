package com.quesity.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.quesity.R;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.fragments.ProgressBarHandler;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.general.Config;
import com.quesity.models.Quest;
import com.quesity.network.FetchJSONTaskGet;
import com.quesity.network.INetworkInteraction;
import com.quesity.network.IPostExecuteCallback;

public class QuestsListViewActivity extends BaseActivity implements INetworkInteraction{
	private QuestAdapter array_adapter;
	private ListView _quest_list_view;
	private Button _btn_start_quest; 
	private LoadingProgressFragment _progress;
	private StartQuestClickListener _start_quest_listener;
	public static final String QUEST_ID = "com.quesity.QUEST_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quests_list);
		setTitle(R.string.app_name);
		_quest_list_view =  (ListView) findViewById(R.id.quest_list_fragment);
		_btn_start_quest = (Button) findViewById(R.id.btn_start_quest);
		_quest_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		_quest_list_view.setSelector(getResources().getDrawable(R.drawable.list_item_selectable));
		_start_quest_listener = new StartQuestClickListener();
		_btn_start_quest.setOnClickListener(_start_quest_listener);
		_progress = new LoadingProgressFragment();
		new FetchJSONTaskGet<Quest[]>(Quest[].class).setNetworkInteractionHandler(this).setActivity(this).
		execute(Config.SERVER_URL + "/all_quests");
	}
	

    private class StartQuestClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int selected_index = array_adapter.getSelectedIndex();
			if ( selected_index < 0 )
				return;
			Object item = array_adapter.getItem(selected_index);
			if ( item == null || !(item instanceof Quest)){
				return;
			}
			Intent intent = new Intent(QuestsListViewActivity.this, QuestPageActivity.class);
	    	intent.putExtra(QUEST_ID, ((Quest)item).getId());
	    	startActivity(intent);
	    	finish();
		}

    }
    
    private class NewQuestsPostExecuteCallback implements IPostExecuteCallback {

		@Override
		public void apply(Object r) {
			Quest[] result = (Quest[])r;
			if ( result == null ) {
				AlertDialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.lbl_err_load_quest), QuestsListViewActivity.this);
				errorDialog.show();
				return;
			}
			array_adapter = new QuestAdapter(result,QuestsListViewActivity.this);
			_quest_list_view.setAdapter(array_adapter);
			_quest_list_view.setOnItemClickListener(array_adapter);
		}

		@Override
		public int get401ErrorMessage() {
			return -1;
		}
    	
    }
	
	private class QuestAdapter extends BaseAdapter implements AdapterView.OnItemClickListener{

		private Quest[] _quests;
		private int _selected;
		public QuestAdapter(Quest[] quests, Activity activity) {
			_quests = quests;
			_selected = -1;
		}
		@Override
		public int getCount() {
			return _quests.length;
		}

		@Override
		public Object getItem(int position) {
			return _quests[position];
		}

		@Override
		public long getItemId(int position) {
			return Long.valueOf(position);
		}
		public int getSelectedIndex(){
			return _selected;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			   if(convertView == null) {
			        LayoutInflater vi = (LayoutInflater)getSystemService(Context
			        		.LAYOUT_INFLATER_SERVICE);
			        convertView = vi.inflate(android.R.layout.simple_list_item_2, null);
			    }
			   Quest q =(Quest) getItem(position);
			   setMainTextView(convertView,q);
			   setSubTextView(convertView, q);
			   
			   if ( position == _selected ){
				   highlightItem(convertView);
			   }else {
				   unHighlightItem(convertView);
			   }
			   return convertView;
		}
		
		private void setMainTextView(View convertView, Quest model) {
			   TextView text_view = (TextView) convertView.findViewById(android.R.id.text1);

			   text_view.setText(model.getTitle());
			   text_view.setTextColor(getResources().getColor(R.color.quesity_title_color));
		}
		
		private void setSubTextView(View convertView, Quest model) {
			TextView text_view = (TextView) convertView.findViewById(android.R.id.text2);
			String description = model.getDescription();
			if ( description != null ) {
				text_view.setText(description);
			}
		}
		
		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos,
				long arg3) {
			if (pos == _selected) {
				unHighlightItem(view);
			}else{
				if ( _selected >= 0  ){
					View someView = _quest_list_view.getChildAt(_selected);
					unHighlightItem(someView);
				}
				highlightItem(view);
				_selected = pos;
			}
			notifyDataSetChanged();
		}
		
		private void unHighlightItem(View view) {
			if ( view == null )
				return;
			view.setBackgroundColor(QuestsListViewActivity.this.getResources().getColor(R.color.splash_screen));
		}
		
		private void highlightItem(View view) {
			if (view == null)
				return;
			view.setBackgroundColor(QuestsListViewActivity.this.getResources().getColor(R.color.list_selected_color));
		}

		
	}

	@Override
	public IPostExecuteCallback getPostExecuteCallback() {
		return new NewQuestsPostExecuteCallback();
	}

	@Override
	public ProgressBarHandler getProgressBarHandler() {
		return new ProgressBarHandler (getString(R.string.lbl_loading_quests), getString(R.string.lbl_loading),_progress);
	}


}
