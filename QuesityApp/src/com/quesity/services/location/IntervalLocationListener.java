package com.quesity.services.location;

import java.util.ArrayList;
import java.util.List;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import com.quesity.controllers.LocationUser;

public class IntervalLocationListener implements LocationListener {

	protected float _minAccuracy;
	private List<LocationUser> _users;
	public IntervalLocationListener(float minAcc) {
		_minAccuracy = minAcc;
		_users = new ArrayList<LocationUser>();
	}
	
	public void addLocationUser(LocationUser u) {
		_users.add(u);
	}
	
	public void removeLocationUser(LocationUser u) {
		_users.remove(u);
	}
	public void clearLocationUsers() {
		_users.clear();
	}
	@Override
	public void onLocationChanged(Location location) {
		if ( location.getAccuracy() > _minAccuracy){
			for (LocationUser luser : _users) {
				luser.lowAccuracyLocation(location);
			}
			return;
		}
		
		Log.d("LocationListener" , "New location arrived with lat: " + location.getLatitude() + " And lng: " + location.getLongitude() +
				" And accuracy of " + location.getAccuracy());
		
		for (LocationUser luser : _users) {
			luser.useLocation(location);
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
