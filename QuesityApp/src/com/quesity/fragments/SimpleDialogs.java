package com.quesity.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class SimpleDialogs {
	public static AlertDialog getErrorDialog(String message, Context context) {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		
		return getErrorDialog(message, context, listener);
	}
	
	public static AlertDialog getErrorDialog(String message, Context context, DialogInterface.OnClickListener click_listener){
		AlertDialog ad = new AlertDialog.Builder(context).create();
		ad.setCancelable(false); // This blocks the 'BACK' button
		ad.setMessage(message);
		ad.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok), click_listener); 
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
	
	/**
	 * Asks a certain question with two custom answers.
	 * answers[0] = positive, answers[1] = negative
	 * @param message
	 * @param context
	 * @param answers
	 * @param listeners
	 * @return
	 */
	public static AlertDialog getGeneralQuestionDialog(String message, Context context, int[] answers, DialogInterface.OnClickListener[] listeners) {
		AlertDialog ad = new AlertDialog.Builder(context).create();
		ad.setCancelable(true);
		ad.setMessage(message);
		ad.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(answers[0]), listeners[0]);
		ad.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(answers[1]), listeners[1]);
		return ad;
	}
}
