package com.quesity.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class SimpleDialogs {
	public static AlertDialog getErrorDialog(String message, Context context) {
		AlertDialog ad = new AlertDialog.Builder(context).create();
		ad.setCancelable(false); // This blocks the 'BACK' button
		ad.setMessage(message);
		ad.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}); 
		return ad;
	}
	
	public static AlertDialog getOKOnlyDialog(String message, Context context, DialogInterface.OnClickListener what_to_do) {
		AlertDialog ad = new AlertDialog.Builder(context).create();
		ad.setCancelable(false);
		ad.setMessage(message);
		ad.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok), what_to_do);
		return ad;
	}
	
	public static AlertDialog getConfirmationDialog(String message, Context context, DialogInterface.OnClickListener yesAnswer, DialogInterface.OnClickListener noAnswer){
		AlertDialog ad = new AlertDialog.Builder(context).create();
		ad.setCancelable(false);
		ad.setMessage(message);
		ad.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.yes),yesAnswer);
		ad.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(android.R.string.no),noAnswer);
		
		return ad;
	}
}
