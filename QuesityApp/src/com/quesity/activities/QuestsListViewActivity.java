package com.quesity.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.quesity.R;
import com.quesity.controllers.ProgressableProcess;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.network.FetchJSONTask;
import com.quesity.util.Constants;

public class QuestsListViewActivity extends FragmentActivity {
	@Override
	protected void onPause() {
		Log.d("Activity","Activity Paused");
		super.onPause();
	}

	private QuestAdapter array_adapter;
	private LoadingProgressFragment _progress_dialog;
	private ListView _quest_list_view;
	private Button _create_event;
	private EditText _event_title;
	public static final String QUEST_ID = "com.quesity.QUEST_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quests_list);
		setTitle(R.string.app_name);
		_progress_dialog = new LoadingProgressFragment();
		_quest_list_view =  (ListView) findViewById(R.id.quest_list_fragment);
		_quest_list_view.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		_quest_list_view.setSelector(getResources().getDrawable(R.drawable.list_item_selectable));
		_create_event = (Button) findViewById(R.id.btn_create_event);
		_event_title = (EditText) findViewById(R.id.event_title);
		_create_event.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if ( !isReadyToCreate() ) {
					return;
				}
				int selectedIndex = array_adapter.getSelectedIndex();
				Quest selectedItem = (Quest) _quest_list_view.getItemAtPosition(selectedIndex);
				if (selectedItem != null) {
					Log.d("Dta",selectedItem.getTitle());
				}else {
					Log.d("Dta","Selected item is null!!");
				}
			}
			
		});
		
		new FetchNewQuestsTask().setActivity(this).execute(Constants.SERVER_URL + "/all_quests");
	}
	
	private boolean isReadyToCreate(){
		if ( array_adapter == null ){
			return false;
		}
		
		int selectedIndex = array_adapter.getSelectedIndex();
		if ( selectedIndex < 0 ) {
			AlertDialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.err_choose_quest), this);
			errorDialog.show();
			return false;
		}
		
		String event_title_string = _event_title.getText().toString();
		if ( event_title_string == null || event_title_string.isEmpty() ) {
			AlertDialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.err_fill_event_title),this);
			errorDialog.show();
			return false;
		}
		return true;
	}

    private class StartQuestClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
	    	Intent intent = new Intent(QuestsListViewActivity.this, QuestPageActivity.class);
	    	Object obj = arg0.getItemAtPosition(arg2);
	    	intent.putExtra(QUEST_ID, ((Quest)obj).getId());
	    	startActivity(intent);
		}

    }
	private class FetchNewQuestsTask extends FetchJSONTask<Quest[]>{

		public FetchNewQuestsTask(){
			super();
			setActivity(QuestsListViewActivity.this).
			setProgressBarHandler(_progress_dialog, getString(R.string.lbl_loading),getString(R.string.lbl_loading_quests));
		}
		@Override
		protected void onPostExecute(Quest[] result) {
			super.onPostExecute(result);
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
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Quest[] resolveModel(String json) {
			Quest[] quests = ModelsFactory.getInstance().getQuestsFromJson(json);
			return quests;
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
			        convertView = vi.inflate(android.R.layout.simple_list_item_1, null);
			    }
			   Quest q =(Quest) getItem(position);
			   
			   TextView text_view = (TextView) convertView.findViewById(android.R.id.text1);

			   text_view.setText(q.getTitle());
			   if ( position == _selected ){
				   highlightItem(convertView);
			   }else {
				   unHighlightItem(convertView);
			   }
			   return convertView;
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
			view.setBackgroundColor(QuestsListViewActivity.this.getResources().getColor(android.R.color.white));
		}
		
		private void highlightItem(View view) {
			if (view == null)
				return;
			
			view.setBackgroundColor(QuestsListViewActivity.this.getResources().getColor(R.color.list_selected_color));
		}

		
	}


}
