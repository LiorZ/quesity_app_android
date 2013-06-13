package com.quesity.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class SimpleDialogs {
	public static AlertDialog getErrorDialog(String message, Context context) {
		AlertDialog ad = new AlertDialog.Builder(context).create();
		ad.setCancelable(false); // This blocks the 'BACK' button
		ad.setMessage(message);
		ad.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}); 
		return ad;

	}
}
