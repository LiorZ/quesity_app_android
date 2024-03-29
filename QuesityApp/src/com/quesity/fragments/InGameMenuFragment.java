package com.quesity.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.quesity.activities.QuesityMapActivity;
import com.quesity.activities.QuestPageActivity;
import com.quesity.app.R;
import com.quesity.fragments.in_game.HintsFragment;
import com.quesity.general.Constants;
import com.quesity.models.Game;

public class InGameMenuFragment extends Fragment {
	
	private TransitionFragmentInvokation _transition;
	private InGameMenuPopup _menu_dialog;
	private InGameMenuPopup _tactics_dialog;
	private HintsFragment _hints_fragment;
	private QuesityIngameButtonView _btn_play_view;
	private QuesityIngameButtonView _btn_tactics_view;
	private OnClickListener _play_button_listener;
	private OnClickListener _tactics_button_listener;
	private DialogInterface.OnClickListener _listener_with_back;
	private DialogInterface.OnClickListener _listener_no_back;

	public InGameMenuFragment() {
		_menu_dialog = new InGameMenuPopup();
		_hints_fragment = new HintsFragment();
		_listener_with_back = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case Constants.BACK_TO_PREVIOUS_PAGE:
					Game currentGame = ((QuestPageActivity) getActivity()).getCurrentGame();
					currentGame.goToPreviousPage();
					break;
				case Constants.EXIT_MENU_ITEM_INDEX:
					((QuestPageActivity) getActivity()).returnToMainPage();
					break;
				case Constants.SHOW_FEEDBACK_ITEM_INDEX:
					((QuestPageActivity) getActivity()).showFeedbackFragment();
					break;
				default:
					dialog.dismiss();
				}
			}
		};
		
		_listener_no_back = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which){
				case Constants.EXIT_MENU_ITEM_INDEX_NO_BACK:
					((QuestPageActivity)getActivity()).returnToMainPage();
					break;
				case Constants.SHOW_FEEDBACK_ITEM_INDEX_NO_BACK:
					((QuestPageActivity)getActivity()).showFeedbackFragment();
					break;
				default:
					dialog.dismiss();
				}	
			}
		};
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
	
	public void setBackOption(boolean back) {
		final int items;
		_menu_dialog.setClickListener(_listener_no_back);
		
		if (back) {
			items = R.array.game_menu_items;
			_menu_dialog.setClickListener(_listener_with_back);
			
		}else {
			 items = R.array.game_menu_items_no_back;
		}
		
		_menu_dialog.setItemProvider(new ItemProvider() {
			
			@Override
			public String[] getItems() {
				// TODO Auto-generated method stub
				return getResources().getStringArray(items);
			}
		});
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		_menu_dialog.setItemProvider(new ItemProvider() {
			
			@Override
			public String[] getItems() {
				return getResources().getStringArray(R.array.game_menu_items_no_back);
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
		
		_menu_dialog.setClickListener(_listener_no_back);
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
				_menu_dialog.show(getFragmentManager(), "MenuFragment");
			}
		});
		
	}
	
	public void setPlayButtonText(String text) {
		_btn_play_view.setButtonText(text);
	}
	
	private void setupPlayButton(View v) {
		QuesityIngameButtonView btn = (QuesityIngameButtonView)v;
		btn.setButtonImage(R.drawable.continue_img);
		btn.setOnTouchButtonImage(R.drawable.continue_img_pressed);
		btn.setButtonText(getString(R.string.ingame_btn_continue));

		v.setOnClickListener(_play_button_listener);
	}
	
	private void setupTacticsButton(View v) {
		QuesityIngameButtonView btn = (QuesityIngameButtonView)v;
		btn.setButtonImage(R.drawable.tactics);
		btn.setButtonText(getString(R.string.ingame_btn_tactics));
		btn.setOnTouchButtonImage(R.drawable.tactics_pressed);
		btn.setBackgroundColor(getResources().getColor(R.color.blue_btn_color));
		btn.setDisabledImage(R.drawable.tactics_disabled);
	}
	
	public void setGameButtonsEnabledState(boolean enabled) {
		if ( enabled ) {
			_btn_play_view.setOnClickListener(_play_button_listener);
			_btn_tactics_view.setOnClickListener(_tactics_button_listener);
		}else {
			_btn_play_view.setOnClickListener(null);
			_btn_tactics_view.setOnClickListener(null);
		}
	}
	
	public void setPlayButtonDrawable(int drawable, int drawable_pressed) {
		_btn_play_view.setButtonImage(drawable);
		_btn_play_view.setOnTouchButtonImage(drawable_pressed);
	}
		
	public void setHintButtonEnabled(boolean enabled) {
//		_btn_tactics_view.setEnabled(enabled);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View root_view = inflater.inflate(R.layout.fragment_ingame_menu, container,false);
		
		_play_button_listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				_transition.transitToNextPage();
			}
		};
		
		_tactics_button_listener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				_hints_fragment.show(getFragmentManager(), "hints");
			}
		};
		
		View menu_btn_view = root_view.findViewById(R.id.btn_menu);
		setupMenuButton(menu_btn_view);
		
		_btn_play_view = (QuesityIngameButtonView) root_view.findViewById(R.id.btn_continue);
		
		setupPlayButton(_btn_play_view);
		
		_btn_tactics_view = (QuesityIngameButtonView)root_view.findViewById(R.id.btn_tactics);
		setupTacticsButton(_btn_tactics_view);
		
		
		return root_view;
		
		
	}

}
