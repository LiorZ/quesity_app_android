package com.quesity.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.quesity.R;
import com.quesity.activities.QuesityMapActivity;
import com.quesity.activities.QuestPageActivity;
import com.quesity.general.Constants;
import com.quesity.models.Game;

public class InGameMenuFragment extends Fragment {
	
	private TransitionFragmentInvokation _transition;
	private InGameMenuPopup _menu_dialog;
	private InGameMenuPopup _tactics_dialog;
	private HintsFragment _hints_fragment;

	public InGameMenuFragment() {
		_menu_dialog = new InGameMenuPopup();
		_hints_fragment = new HintsFragment();

	}
	
	/**
	 * Ugly!!!
	 * @return
	 */
	private String[] getTacticsMenuItems() {
		String string_hint = getString(R.string.menu_get_a_hint);
		int hints = 0;
		Game currentGame = ((QuestPageActivity)getActivity()).getCurrentGame();
		if ( currentGame != null ) {
			hints = currentGame.getRemainingHints();
		}
		
		String formatted_string_hint = String.format(string_hint, hints);
		String[] as_array = {formatted_string_hint};
		return as_array;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		_menu_dialog.setItemProvider(new ItemProvider() {
			
			@Override
			public String[] getItems() {
				return getResources().getStringArray(R.array.game_menu_items);
			}
		});
		_menu_dialog.setTitle(R.string.lbl_game_menu_title);
		_tactics_dialog = new InGameMenuPopup();
		_tactics_dialog.setTitle(R.string.lbl_game_menu_tactics);
		_tactics_dialog.setItemProvider(new ItemProvider() {
			
			@Override
			public String[] getItems() {
				return getTacticsMenuItems();
			}
		});
		
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
				case Constants.SHOW_MAP_MENU_ITEM_INDEX:
					startShowMapActivity();
					break;
				case Constants.EXIT_MENU_ITEM_INDEX:
					((QuestPageActivity)factivity).returnToMainPage();
					break;
				default:
					dialog.dismiss();
						
				}	
			}
		});
	}

	private void startShowMapActivity() {
		Intent i = new Intent(this.getActivity(), QuesityMapActivity.class);
		startActivity(i);
	}
	
	public interface TransitionFragmentInvokation {
		public void transitToNextPage();
	}

	private void setupMenuButton(View v){
		QuesityIngameButtonView btn = (QuesityIngameButtonView)v;
		btn.setButtonImage(R.drawable.menu);
		btn.setOnTouchButtonImage(R.drawable.menu_pressed);
		btn.setButtonText(getString(R.string.ingame_btn_menu));
		btn.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				_menu_dialog.show(getFragmentManager(), "ingame_menu");
			}
		});
		
	}
	
	private void setupPlayButton(View v) {
		QuesityIngameButtonView btn = (QuesityIngameButtonView)v;
		btn.setButtonImage(R.drawable.continue_img);
		btn.setOnTouchButtonImage(R.drawable.continue_img_pressed);
		btn.setButtonText(getString(R.string.ingame_btn_continue));
		
		v.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				_transition.transitToNextPage();
			}
		});
	}
	
	private void setupTacticsButton(View v) {
		QuesityIngameButtonView btn = (QuesityIngameButtonView)v;
		btn.setButtonImage(R.drawable.tactics);
		btn.setButtonText(getString(R.string.ingame_btn_tactics));
		btn.setOnTouchButtonImage(R.drawable.tactics_pressed);
		v.setOnClickListener( new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				_tactics_dialog.show(getFragmentManager(), "ingame_tactics_menu");
			}
		});
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root_view = inflater.inflate(R.layout.fragment_ingame_menu, container,false);
		
		View menu_btn_view = root_view.findViewById(R.id.btn_menu);
		setupMenuButton(menu_btn_view);
		
		View btn_play_view = root_view.findViewById(R.id.btn_continue);
		setupPlayButton(btn_play_view);
		
		View btn_tactics_view = root_view.findViewById(R.id.btn_tactics);
		setupTacticsButton(btn_tactics_view);
		
		return root_view;
		
		
	}

}
