package com.quesity.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.quesity.R;
import com.quesity.activities.QuestPageActivity;
import com.quesity.general.Constants;

public class InGameMenuFragment extends Fragment {
	
	private TransitionFragmentInvokation _transition;
	private InGameMenuPopup _menu_dialog;
	private InGameMenuPopup _tactics_dialog;
	private HintsFragment _hints_fragment;

	public InGameMenuFragment() {
		_menu_dialog = new InGameMenuPopup();
		_hints_fragment = new HintsFragment();

		_menu_dialog.setItemArray(R.array.game_menu_items);
		_menu_dialog.setTitle(R.string.lbl_game_menu_title);
		_tactics_dialog = new InGameMenuPopup();
		_tactics_dialog.setItemArray(R.array.tactics_menu_items);
		_tactics_dialog.setTitle(R.string.lbl_game_menu_tactics);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		final FragmentActivity factivity = (FragmentActivity) activity;
		final FragmentManager fragmentManager = factivity.getSupportFragmentManager();
		_transition = (TransitionFragmentInvokation) activity;
		_tactics_dialog.setClickListener( new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case Constants.HINTS_MENU_ITEM_INDEX:
					_hints_fragment.show(fragmentManager, "HintsFragment");
					break;
				default:
					dialog.dismiss();
				}				
			}
		});
		
		_menu_dialog.setClickListener(new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case Constants.EXIT_MENU_ITEM_INDEX:
					((QuestPageActivity)factivity).returnToMainPage();
					break;
				default:
					dialog.dismiss();
						
				}	
			}
		});
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
