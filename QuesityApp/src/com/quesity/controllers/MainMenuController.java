package com.quesity.controllers;

import com.quesity.general.Constants;

import android.content.DialogInterface;

public class MainMenuController implements DialogInterface.OnClickListener {

	private HintsMenuActivator _activator;
	public MainMenuController(HintsMenuActivator activator) {
		_activator = activator;
	}
	@Override
	public void onClick(DialogInterface dialog, int which) {
		switch(which) {
		case Constants.EXIT_MENU_ITEM_INDEX:
			_activator.showHintsMenuItem();
		}
	}

}
