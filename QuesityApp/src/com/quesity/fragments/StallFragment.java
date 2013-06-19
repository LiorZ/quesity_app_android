package com.quesity.fragments;

import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Button;

import com.quesity.R;
import com.quesity.activities.NextPageTransition;
import com.quesity.activities.QuestPageActivity;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;
import com.quesity.models.QuestPageStall;

public class StallFragment extends DialogFragment implements OnDemandFragment {

	@Override
	public void onDetach() {
		super.onDetach();
		_okOnlyDialog.dismiss();
		_timer.cancel();
	}

	private AlertDialog _okOnlyDialog;
	private Timer _timer;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		QuestPageActivity activity = (QuestPageActivity) getActivity();
		QuestPage generalPage = activity.getCurrentQuestPage();
		if (!(generalPage instanceof QuestPageStall)) {
			return null; //TODO: Handle this kind of error..
		}
		final QuestPageStall stall_page = (QuestPageStall) generalPage;
		int stallTime = stall_page.getStallTime();

		String dialogMessage = getDialogMessage(getString(R.string.lbl_time_delay), stallTime*60);
		_timer = new Timer();

		_okOnlyDialog = SimpleDialogs.getOKOnlyDialog(dialogMessage, activity, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				_timer.cancel();
				NextPageTransition activity = (NextPageTransition) getActivity();
				QuestPageLink[] links = stall_page.getLinks();
				activity.loadNextPage(links[0].getLinksToPage());
			}
		});
		
		_okOnlyDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				_okOnlyDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
			}
		});
		
		_okOnlyDialog.setCanceledOnTouchOutside(false);
		_timer.schedule(new StallTimeTask(stallTime),0,1000);
		return _okOnlyDialog;
	}
	
	private String getDialogMessage(String raw_msg, int stall_time) {
		String msg = String.format(raw_msg, stall_time/60,stall_time%60);
		return msg;

	}

	@Override
	public void invokeFragment(FragmentManager manager) {
		this.show(manager, "DelayFragment");
	}
	
	private class StallTimeTask extends TimerTask {
		private int _stall_time;
		private String _raw_msg;

		public StallTimeTask(int stall) {
			_stall_time = stall*60;
			_raw_msg = getString(R.string.lbl_time_delay);
		}
		@Override
		public void run() {
			if ( _okOnlyDialog == null ){
				cancel();
				return;
			}
			getActivity().runOnUiThread(new Runnable() {
				

				@Override
				public void run() {
					String msg = getDialogMessage(_raw_msg, _stall_time);
					_okOnlyDialog.setMessage(msg);
				}
			});
			if ( _stall_time == 0 ){
				cancel();
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						_okOnlyDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
					}
				});
			}else {
				--_stall_time;
			}
		}
		
	}
}
