package com.quesity.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.quesity.R;
import com.quesity.fragments.LoadingProgressFragment;
import com.quesity.fragments.OnDemandFragment;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.network.FetchJSONTask;
import com.quesity.util.Constants;

public class QuestsListViewActivity extends FragmentActivity {
	private QuestAdapter array_adapter;
	private DialogFragment _progress_dialog;
	private ListFragment _quest_list_fragment;
	public static final String QUEST_ID = "com.quesity.QUEST_ID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quests_list);
		FragmentManager manager = getSupportFragmentManager();
		_progress_dialog = new LoadingProgressFragment(getString(R.string.lbl_loading),getString(R.string.lbl_loading_quests));

		_quest_list_fragment =  (ListFragment) manager.findFragmentById(R.id.quest_list_fragment);
		ListView listView = _quest_list_fragment.getListView();
		listView.setOnItemClickListener(new StartQuestClickListener());
		
		new FetchNewQuestsTask().execute(Constants.SERVER_URL + "/all_quests");
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

		@Override
		protected void onPostExecute(Quest[] result) {
			_progress_dialog.dismiss();
			if ( result == null ) {
				AlertDialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.lbl_err_load_quest), QuestsListViewActivity.this);
				errorDialog.show();
				return;
			}
			array_adapter = new QuestAdapter(result,QuestsListViewActivity.this);
			_quest_list_fragment.setListAdapter(array_adapter);
		}

		@Override
		protected void onPreExecute() {
			_progress_dialog.show(getSupportFragmentManager(), "loading_progress_dialog");
		}

		@Override
		protected Quest[] resolveModel(String json) {
			Quest[] quests = ModelsFactory.getInstance().getQuestsFromJson(json);
			return quests;
		}
		
	}
	
	private class QuestAdapter extends BaseAdapter {

		private Quest[] _quests;
		public QuestAdapter(Quest[] quests, Activity activity) {
			_quests = quests;
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
			   return convertView;
		}
		
	}

}
