package com.quesity.fragments;

import com.quesity.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class InGameMenuPopup extends DialogFragment{

	private int _title_id;
	private int _item_array;

	private DialogInterface.OnClickListener _listener;
	public void setTitle(int id) {
		_title_id = id;
	}
	
	public void setItemArray(int id) {
		_item_array = id;
	}
	
	public void setClickListener(DialogInterface.OnClickListener listener){
		_listener = listener;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(_title_id).setItems(_item_array, _listener);
		return builder.create();
	}
	
}
