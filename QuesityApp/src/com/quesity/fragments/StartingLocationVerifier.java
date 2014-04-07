package com.quesity.fragments;

import android.app.Dialog;
import android.app.Instrumentation.ActivityResult;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.quesity.app.R;
import com.quesity.models.StartingLocation;

public class StartingLocationVerifier extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks,
GooglePlayServicesClient.OnConnectionFailedListener {
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private LocationClient _location_client;
	private StartingLocation _starting_location;
	private static final int MAX_DISTANCE_FROM_CENTER = 2; //times the radius
	private StartingLocationListener _location_listener;
	public static StartingLocationVerifier getInstance(StartingLocation l, StartingLocationListener listener){
		StartingLocationVerifier verifier = new StartingLocationVerifier();
		verifier.setStartingLocation(l);
		verifier.setStartingLocationListener(listener);
		return verifier;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		_location_client = new LocationClient(this.getActivity(), this, this);
	}
	
	private void setStartingLocation(StartingLocation l) {
		_starting_location = l;
	}

	private void setStartingLocationListener(StartingLocationListener l) {
		_location_listener = l;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		_location_client.connect();
	}

	@Override
	public void onStop() {
		super.onStop();
		_location_client.disconnect();
	}
	
	
	private void showErrorDialog(String msg) {
		Dialog errorDialog = SimpleDialogs.getErrorDialog(msg, getActivity());
		errorDialog.show();
	}
	
	/**
	 * Google play services methods:
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
	    /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(
                        this.getActivity(),
                        CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            showErrorDialog(getActivity().getString(com.quesity.app.R.string.lbl_no_starting_location));
        }
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		if ( _starting_location == null ) {
			_location_listener.notOnStartingLocation(-1);
		}
		
		float start_lat = _starting_location.getLat();
		float start_lng = _starting_location.getLng();
		
		Location lastLocation = _location_client.getLastLocation();
		if ( lastLocation == null ){
			_location_listener.notOnStartingLocation(-1);
		}
		double cur_lat = lastLocation.getLatitude();
		double cur_lng = lastLocation.getLongitude();
		float results[] = new float[3];
		Location.distanceBetween(cur_lat, cur_lng, start_lat, start_lng, results);
		if ( results[0] > _starting_location.getRadius()*MAX_DISTANCE_FROM_CENTER ) {
			_location_listener.notOnStartingLocation(results[0]);
		}else {
			_location_listener.onStartingLocation();
		}
	}

	@Override
	public void onDisconnected() {
		
	}


	public interface StartingLocationListener {
		public void onStartingLocation();
		public void notOnStartingLocation(double distance);
	}



}
