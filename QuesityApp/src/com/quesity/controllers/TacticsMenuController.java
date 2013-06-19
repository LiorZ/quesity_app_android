package com.quesity.controllers;

import com.quesity.util.Constants;

import android.content.DialogInterface;

public class TacticsMenuController implements DialogInterface.OnClickListener {

	private HintsMenuActivator _hint_activator;
	public TacticsMenuController(HintsMenuActivator activator){
		_hint_activator = activator;
	}
	@Override
	public void onClick(DialogInterface d, int sel) {
		switch(sel){
		case Constants.HINTS_MENU_ITEM_INDEX:
			_hint_activator.showHintsMenuItem();
			break;
		default:
			d.dismiss();
				
		}
	}

}
