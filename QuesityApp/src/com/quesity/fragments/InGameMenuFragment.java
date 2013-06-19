package com.quesity.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.quesity.R;
import com.quesity.activities.QuestPageActivity;
import com.quesity.controllers.HintsMenuActivator;
import com.quesity.controllers.TacticsMenuController;

public class InGameMenuFragment extends Fragment {
	
	private TransitionFragmentInvokation _transition;
	private InGameMenuPopup _menu_dialog;
	private InGameMenuPopup _tactics_dialog;
	public InGameMenuFragment() {
		_menu_dialog = new InGameMenuPopup();
		_menu_dialog.setItemArray(R.array.game_menu_items);
		_menu_dialog.setTitle(R.string.lbl_game_menu_title);
		_menu_dialog.setClickListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		_tactics_dialog = new InGameMenuPopup();
		_tactics_dialog.setItemArray(R.array.tactics_menu_items);
		_tactics_dialog.setTitle(R.string.lbl_game_menu_tactics);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		_transition = (TransitionFragmentInvokation) activity;
		_tactics_dialog.setClickListener( new TacticsMenuController((HintsMenuActivator)activity));
	}

	public interface TransitionFragmentInvokation {
		public void transitToNextPage();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_ingame_menu,container);
		Button continue_button = (Button) v.findViewById(R.id.btn_continue);
		continue_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				_transition.transitToNextPage();
			}
		});
		
		View btn_menu = v.findViewById(R.id.btn_menu);
		btn_menu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				_menu_dialog.show(getFragmentManager(), "ingame_menu");
			}
		});
		
		View btn_tactics = v.findViewById(R.id.btn_tactics);
		btn_tactics.setOnClickListener( new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				_tactics_dialog.show(getFragmentManager(), "ingame_tactics_menu");
			}
		});
		return v;
		
		
	}

}
