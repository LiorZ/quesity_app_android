package com.quesity.activities;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.quesity.app.R;

public class QuesityMapActivity extends BaseActivity {

	
	
	@Override
	protected String getScreenViewPath() {
		// TODO Auto-generated method stub
		return "Quesity Map";
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_game_map);
		setBackButton();
		setupMap();
	}
	
	private void setupMap() {
		SupportMapFragment map_fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.ingame_map);
		GoogleMap map = map_fragment.getMap();
		map.setMyLocationEnabled(true);
		
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location myLocation = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if ( myLocation != null ) { 
			LatLng pos = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
			map.moveCamera(CameraUpdateFactory.newLatLng(pos));
			map.animateCamera(CameraUpdateFactory.zoomTo(17f));
        }
	}
	
	private void setBackButton(){
		View back_button = findViewById(R.id.ingame_map_back_button);
		back_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				QuesityMapActivity.this.onBackPressed();
			}
		});
	}

}
