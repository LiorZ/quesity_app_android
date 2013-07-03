package com.quesity.activities;

import com.quesity.R;
import com.quesity.models.Account;
import com.quesity.models.Event;
import com.quesity.models.ModelsFactory;
import com.quesity.models.Quest;
import com.quesity.util.Constants;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class EventLobby extends Activity {

	private Event _event_details;
	private ListView _people;
	private AccountListAdapter _people_adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quesity_lobby);
		_event_details = getEventFromExtras();
		setListView();
	}
	
	private void setListView() {
		_people = (ListView) findViewById(R.id.list_participants);
		Account[] participants = _event_details.getParticipants();
		_people_adapter = new AccountListAdapter(participants);
		_people.setAdapter(_people_adapter);
	}
	
	private Event getEventFromExtras(){
		Bundle extras = getIntent().getExtras();
		String event_json = extras.getString(Constants.EVENT_JSON);
		return ModelsFactory.getInstance().getEventFromJSON(event_json);
	}
	
	private class AccountListAdapter extends BaseAdapter {
		private Account[] _accounts;
		public AccountListAdapter(Account[] accounts) {
			_accounts = accounts;
		}
		@Override
		public int getCount() {
			return _accounts.length;
		}

		@Override
		public Object getItem(int position) {
			return _accounts[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			   if(convertView == null) {
			        LayoutInflater vi = (LayoutInflater)getSystemService(Context
			        		.LAYOUT_INFLATER_SERVICE);
			        convertView = vi.inflate(android.R.layout.simple_list_item_1, null);
			    }
			   Account a = (Account) getItem(position);
			   
			   TextView text_view = (TextView) convertView.findViewById(android.R.id.text1);

			   text_view.setText(a.getFirstName() + " " + a.getLastName());
			   return convertView;
		}
		
	}

}
