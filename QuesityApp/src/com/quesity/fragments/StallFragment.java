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
import android.view.KeyEvent;

import com.quesity.R;
import com.quesity.activities.NextPageTransition;
import com.quesity.activities.QuestPageActivity;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;
import com.quesity.models.QuestPageStall;

public class StallFragment extends DialogFragment implements OnDemandFragment {

	@Override
	public void invokeFragment(FragmentManager manager) {
		// TODO Auto-generated method stub
		
	}

//	private Dialog _okOnlyDialog;
//	private Timer _timer;
//	private TimerTask _timer_task;
//	private long _time_started_global=0;
//	@Override
//	public void onPause() {
//		stopTask();
//		super.onPause();
//	}
//
//	private void stopTask() {
//		if ( _timer_task != null )
//			_timer_task.cancel();
//	}
//	
//	private void startTask(int stallTime) {
//		if ( _time_started_global == 0 ){
//			_timer_task = new StallTimeTask(stallTime);
//		}else {
//			_timer_task = new StallTimeTask(stallTime,_time_started_global);
//		}
//		_timer.schedule(_timer_task,0,1000);
//	}
//	@Override
//	public void onResume() {
//		super.onResume();
//		if (  _time_started_global > 0 ){
//			Log.d("StallFragment", "Restarting timer");
//			final QuestPageStall stall_page = extractQuestPage();
//			int stallTime = stall_page.getStallTime();
//			startTask(stallTime);
//		}
//	}
//
//	@Override
//	public void onDetach() {
//		super.onDetach();
//		_okOnlyDialog.dismiss();
//		stopTask();
//	}
//
//	public QuestPageStall extractQuestPage() {
//		QuestPageActivity activity = (QuestPageActivity) getActivity();
//		QuestPage generalPage = activity.getCurrentQuestPage();
//		if (!(generalPage instanceof QuestPageStall)) {
//			return null; //TODO: Handle this kind of error..
//		}
//		final QuestPageStall stall_page = (QuestPageStall) generalPage;
//		return stall_page;
//	}
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		QuestPageActivity activity = (QuestPageActivity) getActivity();
//		final QuestPageStall stall_page = extractQuestPage();
//		int stallTime = stall_page.getStallTime();
//
//		String dialogMessage = getDialogMessage(getString(R.string.lbl_time_delay), stallTime*60);
//		_timer = new Timer();
//
//		_okOnlyDialog = SimpleDialogs.getOKOnlyDialog("",dialogMessage, activity, new DialogInterface.OnClickListener() {
//			
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
//				stopTask();
//				NextPageTransition activity = (NextPageTransition) getActivity();
//				QuestPageLink[] links = stall_page.getLinks();
//				activity.loadNextPage(links[0]);
//			}
//		});
//		
//		_okOnlyDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//			
//			@Override
//			public void onShow(DialogInterface dialog) {
//				_okOnlyDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
//			}
//		});
//		
//		_okOnlyDialog.setCanceledOnTouchOutside(false);
//		_okOnlyDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//			
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//				return true;
//			}
//		});
//		startTask(stallTime);
//		return _okOnlyDialog;
//	}
//	
//	private String getDialogMessage(String raw_msg, long stall_time) {
//		String msg = String.format(raw_msg, stall_time/60,stall_time%60);
//		return msg;
//
//	}
//
//	@Override
//	public void invokeFragment(FragmentManager manager) {
//		this.show(manager, "DelayFragment");
//	}
//	
//	private class StallTimeTask extends TimerTask {
//		@Override
//		public boolean cancel() {
//			_time_started_global = _time_started;
//			Log.d("StallTimer","Timer cancelled");
//			return super.cancel();
//		}
//		
//		private int _stall_time;
//		private String _raw_msg;
//		private long _time_started;
//		private boolean _timer_initialized;
//		public StallTimeTask(int stall) {
//			_stall_time = stall*60;
//			_raw_msg = getString(R.string.lbl_time_delay);
//			_timer_initialized = false;
//		}
//		public StallTimeTask(int stall, long time_started) {
//			this(stall);
//			_timer_initialized = true;
//			_time_started = time_started;
//					
//		}
//		private long timePassed() {
//			long now = System.currentTimeMillis();
//			long time_passed_sec = (now - _time_started)/1000;
//			return time_passed_sec;
//		}
//		@Override
//		public void run() {
//			if ( !_timer_initialized ){
//				_time_started = System.currentTimeMillis();
//				Log.d("TimerTask","Setting the time started to " + _time_started);
//				_timer_initialized = true;
//			}
//			if ( _okOnlyDialog == null ){
//				cancel();
//				return;
//			}
//			final long time_passed = _stall_time - timePassed();
//			if (time_passed <= 0 ){
//				cancel();
//				getActivity().runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						_okOnlyDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
//					}
//				});
//			}
//			getActivity().runOnUiThread(new Runnable() {
//				
//
//				@Override
//				public void run() {
//					String msg = getDialogMessage(_raw_msg, Math.max(time_passed,0));
//					_okOnlyDialog.setMessage(msg);
//				}
//			});
//			
//		}
//		
//	}
}
