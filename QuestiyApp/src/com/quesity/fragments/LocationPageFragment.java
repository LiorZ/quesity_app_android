package com.quesity.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.quesity.R;
import com.quesity.activities.NextPageTransition;
import com.quesity.activities.QuestPageActivity;
import com.quesity.controllers.IntervalLocationListener;
import com.quesity.controllers.LocationUser;
import com.quesity.controllers.ProgressableProcess;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;
import com.quesity.models.QuestPageLocationLink;

public class LocationPageFragment extends Fragment implements OnDemandFragment, LocationUser {

		@Override
		public void onDetach() {
				LocationManager systemService = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
				systemService.removeUpdates(_inter_listener);
				super.onDetach();
		}
		@Override
		public void onAttach(Activity activity) {
			super.onAttach(activity);
			LocationManager systemService = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
			String locationProvider = LocationManager.GPS_PROVIDER;
			systemService.requestLocationUpdates(locationProvider,10, 0, _inter_listener);
		}

		private IntervalLocationListener _inter_listener;
		public LocationPageFragment() {
			_inter_listener = new IntervalLocationListener();
		}
		@Override
		public void useLocation(Location location){
			
			if ( location == null ){
				AlertDialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.lbl_cant_find_location), getActivity());
				errorDialog.show();
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
						activity.loadNextPage(link.getLinksToPage());
						return;
					}
					
				}
			}
			AlertDialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.lbl_wrong_location), getActivity());
			errorDialog.show();
		}
		
		@Override
		public void stopProgressBar() {
			if (getActivity() instanceof ProgressableProcess) {
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
			Location currentLocation = _inter_listener.getCurrentLocation();
			useLocation(currentLocation);
		}
		
		private class LocationDeterminant extends AsyncTask<Void, Integer, Location> {
			
			private LocationUser _user;
			private IntervalLocationListener _listener;
			private LocationManager _manager;
			public LocationDeterminant(LocationUser user, LocationManager manager) {
				_user = user;
				_listener = new IntervalLocationListener();
				_manager = manager;
			}
			@Override
			protected void onPostExecute(Location result) {
				_user.stopProgressBar();
				_manager.removeUpdates(_listener);
				_user.useLocation(result);
			}

			@Override
			protected void onPreExecute() {
				_user.startProgressBar(getString(R.string.lbl_location_fix_title), getString(R.string.lbl_location_fix_message));
				String locationProvider = LocationManager.GPS_PROVIDER;
				_manager.requestLocationUpdates(locationProvider, 0, 0, _listener);
			}

			@Override
			protected Location doInBackground(Void... params) {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return _listener.getCurrentLocation();
			}
			
		}
}
