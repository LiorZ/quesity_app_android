package com.quesity.fragments;

import com.quesity.app.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class InGameMenuPopup extends DialogFragment{

	private int _title_id;
	private ItemProvider _provider;
	private DialogInterface.OnClickListener _listener;
	public void setTitle(int id) {
		_title_id = id;
	}
	
	public void setItemProvider(ItemProvider provider) {
		_provider = provider;
	}
	
	public void setClickListener(DialogInterface.OnClickListener listener){
		_listener = listener;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		String[] items = _provider.getItems();
		builder.setTitle(_title_id).setItems(items, _listener);
		AlertDialog dialog = builder.create();
		return dialog;
	}
	
}
