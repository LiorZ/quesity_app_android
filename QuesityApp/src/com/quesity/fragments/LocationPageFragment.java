package com.quesity.fragments;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.location.Location;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.quesity.app.R;
import com.quesity.activities.NextPageTransition;
import com.quesity.activities.QuestPageActivity;
import com.quesity.controllers.LocationUser;
import com.quesity.controllers.ProgressableProcess;
import com.quesity.models.QuestPage;
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
					Dialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.lbl_cant_find_location), getActivity());
					errorDialog.show();
				}
			});
		}
		
		@Override
		public void useLocation(Location location){
			
			if ( location == null ){
				handleLocationError();
				return;
			}
			
			NextPageTransition activity = (NextPageTransition) getActivity();
			QuestPage page = ((QuestPageActivity)getActivity()).getCurrentQuestPage();
			
			double lng = location.getLongitude();
			double lat = location.getLatitude();
			
			QuestPageLink[] links = page.getLinks();
			if (links.length > 0) {
				for (int i = 0; i < links.length; i++) {
					QuestPageLocationLink link = (QuestPageLocationLink) links[i];
					double link_lat = link.getLat();
					double link_lng = link.getLng();
					int radius = link.getRadius();
					float result[] = new float[3];
					Location.distanceBetween(link_lat, link_lng, lat, lng, result);
					Log.d("LocationPageFragment","Distance between points is " + result[0] + " while radius is " + radius);
					if ( radius > result[0] ){
						stopProgressBar();
						activity.loadNextPage(link);
						return;
					}
					
				}
			}
			Log.d("LocationFragment", location.toString());
			Dialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.lbl_wrong_location), getActivity());
			stopProgressBar();
			errorDialog.show();
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
}
