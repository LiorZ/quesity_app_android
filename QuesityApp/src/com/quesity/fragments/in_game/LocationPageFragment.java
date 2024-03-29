package com.quesity.fragments.in_game;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.quesity.activities.NextPageTransition;
import com.quesity.activities.QuestPageActivity;
import com.quesity.app.R;
import com.quesity.controllers.LocationUser;
import com.quesity.controllers.ProgressableProcess;
import com.quesity.fragments.OnDemandFragment;
import com.quesity.fragments.SimpleDialogs;
import com.quesity.models.Game;
import com.quesity.models.QuestPageLink;
import com.quesity.models.QuestPageLocationLink;
import com.quesity.services.location.LocationService;

public class LocationPageFragment extends Fragment implements OnDemandFragment, LocationUser {
	private static final long GPS_TIMEOUT = 10*1000;
	private Timer _timeoutTimer;
	private TimerTask _task;
	public LocationPageFragment() {
	}
		@Override
		public void onPause() {
			super.onPause();
			Log.d("LocationPageFragment","Location Fragment paused");
		}

		@Override
		public void onResume() {
			super.onResume();
			Log.d("LocationPageFragment","Location Fragment resumed");
//			startListening(getActivity());
		}
		
		public void stopTimer() {
			if ( _task != null ) {
				_task.cancel();
			}
			if ( _timeoutTimer != null ){
				_timeoutTimer.cancel();
				_timeoutTimer.purge();
			}
		}
		
		@Override
		public void onStop() {
//			stopListening();
			super.onStop();
		}
		@Override
		public void onDetach() {
				Log.d("LocationPageFragment", "LocationPageFragment - OnDetach");
				super.onDetach();
		}
		
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
//			startListening(activity);
		}

		private void startListening(Activity activity) {
			LocationService service = LocationService.getInstance();
			if ( service == null) {
				return; // TODO: Handle the case where the service is dead.
			}
			service.requestImmediateLocation(this);
			_task = new TimerTask() {
				
				@Override
				public void run() {
					handleLocationError();
				}
			};
			_timeoutTimer = new Timer();
			_timeoutTimer.schedule(_task, GPS_TIMEOUT);
		}
		
		private void handleLocationError() {
			LocationService instance = LocationService.getInstance();
			if ( instance != null ){
				instance.cancelImmediateLocationRequest();
			}
			stopTimer();
			stopProgressBar();
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					SimpleDialogs.getOKOnlyDialog(getString(R.string.lbl_cant_find_location_title), getString(R.string.lbl_cant_find_location),
							getActivity(),new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).show();
				}
			});
		}
		
		@Override
		public void useLocation(Location location){
			
			if ( location == null ){
				handleLocationError();
				return;
			}
			
//			QuestPage page = ((QuestPageActivity)getActivity()).getCurrentQuestPage();
			Game game = ((QuestPageActivity)getActivity()).getCurrentGame();
			QuestPageLink next_page_link = game.getNextPage(location);
			stopProgressBar();
			if ( next_page_link == null ) {
				Dialog wrong_location_dialog = SimpleDialogs.getOKOnlyDialog(getString(R.string.lbl_cant_find_location_title), getString(R.string.lbl_wrong_location), getActivity());
				wrong_location_dialog.show();
			}else{
				game.moveToPage(next_page_link);
			}
			Log.d("LocationFragment", location.toString());
		}
		
		@Override
		public void stopProgressBar() {
			if (getActivity() instanceof ProgressableProcess) {
				stopTimer();
				ProgressableProcess activity = (ProgressableProcess) getActivity();
				activity.stopProgressBar();
			}
		}
		@Override
		public void startProgressBar(String title, String message) {
			if (getActivity() instanceof ProgressableProcess) {
				ProgressableProcess activity = (ProgressableProcess) getActivity();
				activity.startProgressBar(title, message);
			}
		}
		@Override
		public void invokeFragment(FragmentManager manager) {
//			LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
//			new LocationDeterminant(this, locationManager).execute();
//			Location currentLocation = _inter_listener.getCurrentLocation();
			startProgressBar(getString(R.string.lbl_location_fix_title), getString(R.string.lbl_location_fix_message));
			startListening(getActivity());
		}
		

		@Override
		public void lowAccuracyLocation(Location loc) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public int getButtonDrawable() {
			return R.drawable.arrived;
		}
		@Override
		public int getPressedButtonDrawable() {
			return R.drawable.arrived_pressed;
		}
		@Override
		public int getButtonStringId() {
			return R.string.ingame_btn_arrived;
		}
}
