package com.quesity.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.quesity.app.R;
import com.quesity.dialogs.QuesityDialog;

public class SimpleDialogs {
	public static Dialog getErrorDialog(String message, Context context) {
		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		};
		
		return getErrorDialog(message, context, listener);
	}
	
	public static Dialog getErrorDialog(String message, Context context, DialogInterface.OnClickListener click_listener){
		return getOKOnlyDialog(context.getString(R.string.error_title), message, context, click_listener);
	}
	
	public static Dialog getOKOnlyDialog(String title,String message, Context context) {
		return getOKOnlyDialog(title, message, context, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
	}
	
	public static Dialog getOKOnlyDialog(String title,String message, Context context, DialogInterface.OnClickListener what_to_do) {
		QuesityDialog d = new QuesityDialog(context);
		d.setTitle(title);
		d.setMessage(message);
		d.setButton(Dialog.BUTTON_POSITIVE, context.getString(android.R.string.ok), what_to_do);
		return d;
	}
	
	public static Dialog getConfirmationDialog(String title, String message, Context context, DialogInterface.OnClickListener yesAnswer, DialogInterface.OnClickListener noAnswer){
//		AlertDialog ad = new AlertDialog.Builder(context).create();
//		ad.setCancelable(false);
//		ad.setMessage(message);
//		ad.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.yes),yesAnswer);
//		ad.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(android.R.string.no),noAnswer);
		int[] answers = {android.R.string.yes,android.R.string.no};
		DialogInterface.OnClickListener[] listeners = { yesAnswer,noAnswer};
		return getGeneralQuestionDialog(title, message, context, answers, listeners);
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
	public static Dialog getGeneralQuestionDialog(String title, String message, Context context, int[] answers, DialogInterface.OnClickListener[] listeners) {
		
		QuesityDialog d = new QuesityDialog(context);
		d.setTitle(title);
		d.setMessage(message);
		for(int i=0; i<answers.length; i++){
			d.setButton(i, context.getString(answers[i]),listeners[i]);
		}

		return d;
		
	}
}
