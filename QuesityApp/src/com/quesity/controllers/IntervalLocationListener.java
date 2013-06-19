package com.quesity.controllers;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class IntervalLocationListener implements LocationListener {

	private Location _bestLocation;
	private static long LOCATION_OBSOLETE_INTERVAL = 60*2*1000;
	
	public IntervalLocationListener() {
		_bestLocation = null;
	}
	
	public Location getCurrentLocation() {
		return _bestLocation;
	}
	@Override
	public void onLocationChanged(Location location) {
		Log.d("LocationListener" , "New location arrived with lat: " + location.getLatitude() + " And lng: " + location.getLongitude() +
				" And accuracy of " + location.getAccuracy());
		if (_bestLocation == null || location.getTime() - _bestLocation.getTime() > LOCATION_OBSOLETE_INTERVAL ){
			_bestLocation = location;
			return;
		}
		if ( _bestLocation.getAccuracy() >= location.getAccuracy() ){
			_bestLocation = location;
			return;
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}

}
