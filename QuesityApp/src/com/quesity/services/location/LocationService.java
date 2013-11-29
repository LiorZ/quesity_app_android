package com.quesity.services.location;

import java.util.Date;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.quesity.R;
import com.quesity.activities.QuestPageActivity;
import com.quesity.controllers.LocationUser;
import com.quesity.general.Constants;
import com.quesity.network.reporting.ModelReport;

public class LocationService extends Service implements LocationUser{

	
	
	private static final int SERVICE_ID = 6661119;
	private LocationManager _systemService;
	private long _updateInterval;
	private IntervalLocationListener _location_listener;
	private IntervalLocationListener _one_time_location_listener;
	private float _minAccuracy;
	private Location _last_known_location;
	
	private String _report_url;
	
	private static final float DEFAULT_ACCURACY = 20;
	
	public static final String KEY_UPDATE_INTERVAL = "com.quesity.location_service.update_interval";
	public static final String KEY_GPS_ACCURACY = "com.quesity.location_service.gps_accuracy";
	public static final String KEY_URL_TO_REPORT = "com.quesity.location_service.report_url";
	private static final String TAG = "com.quesity.services.LocationService";
	private static LocationService _instance;
	
	public static LocationService getInstance() {
		return _instance;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		_last_known_location = null;
		_instance = this;
		Notification notification = buildNotification();
		startForeground(SERVICE_ID, notification);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		_updateInterval = intent.getLongExtra(KEY_UPDATE_INTERVAL, Constants.GPS_UPDATE_INTERVAL);
		_minAccuracy = intent.getFloatExtra(KEY_GPS_ACCURACY, DEFAULT_ACCURACY);
		_report_url = intent.getStringExtra(KEY_URL_TO_REPORT);
		startLocationService();
		Log.d(TAG, "LocationService - onStartCommand with update interval of " + _updateInterval + " and min accuracy of " + _minAccuracy);
		return START_STICKY;
	}
	
	public void requestImmediateLocation(LocationUser user) {
		_one_time_location_listener.clearLocationUsers();
		_one_time_location_listener.addLocationUser(new OneTimeLocationUser(user, _one_time_location_listener));
		_systemService.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, _one_time_location_listener);
	}
	
	public void cancelImmediateLocationRequest() {
		_systemService.removeUpdates(_one_time_location_listener);
	}
	
	private void startLocationService() {
		String service = Context.LOCATION_SERVICE;
		_one_time_location_listener = new IntervalLocationListener(_minAccuracy);
		_location_listener = new IntervalLocationListener(_minAccuracy);
		_location_listener.addLocationUser(this);
		_systemService = (LocationManager) getSystemService(service);
		_systemService.requestLocationUpdates(LocationManager.GPS_PROVIDER, _updateInterval, 0, _location_listener);
	}
	
	private Notification buildNotification() {
		Intent intent = new Intent(this,QuestPageActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
		return builder.setSmallIcon(R.drawable.logo_temp).
				setContentIntent(pendingIntent).
				setContentTitle("Quesity").
				setContentText(getString(R.string.lbl_resume_quest)).
				build();
		
	}
	
	public Location getLastKnownLocation() {
		return _last_known_location;
	}
	private void stopLocationService() {
		_location_listener.clearLocationUsers();
		_one_time_location_listener.clearLocationUsers();
		
		_systemService.removeUpdates(_location_listener);
		_systemService.removeUpdates(_one_time_location_listener);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		stopLocationService();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void startProgressBar(String title, String msg) {
		
	}

	@Override
	public void stopProgressBar() {
		
	}

	@Override
	public void useLocation(Location location) {
		Log.d(TAG,"use location - "+ location);
		_last_known_location = location;
		com.quesity.models.Location model_location = new com.quesity.models.Location();
		model_location.setDate(new Date());
		model_location.setLat(location.getLatitude());
		model_location.setLng(location.getLongitude());
		ModelReport report = new ModelReport(model_location, this);
		report.send(_report_url);
	}

	@Override
	public void lowAccuracyLocation(Location loc) {
		Log.d(TAG,"Low accuracy location! - "+ loc);

	}
	
	
	private class OneTimeLocationUser implements LocationUser {

		private LocationUser _delegator;
		private LocationListener _listener;
		public OneTimeLocationUser(LocationUser u, LocationListener l) {
			_delegator = u;
			_listener = l;
		}
		
		@Override
		public void startProgressBar(String title, String msg) {
			
		}

		@Override
		public void stopProgressBar() {
			
		}

		@Override
		public void useLocation(Location location) {
			if ( _delegator != null ) {
				_last_known_location = location;
				_delegator.useLocation(location);
			}
			_systemService.removeUpdates(_listener);
		}

		@Override
		public void lowAccuracyLocation(Location loc) {
			
		}
		
	}
}
