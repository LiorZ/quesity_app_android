package com.quesity.controllers;

import android.content.DialogInterface;

public class TacticsMenuController implements DialogInterface.OnClickListener {

	private HintMenuActivator _hint_activator;
	public TacticsMenuController(HintMenuActivator activator){
		_hint_activator = activator;
	}
	@Override
	public void onClick(DialogInterface d, int sel) {
		switch(sel){
		case 0:
			_hint_activator.showHints();
			break;
		default:
			d.dismiss();
				
		}
	}

}
