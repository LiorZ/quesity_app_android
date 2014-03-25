package com.quesity.fragments;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MarkerOptionsCreator;
import com.quesity.R;
import com.quesity.controllers.QuestProvider;
import com.quesity.models.Quest;
import com.quesity.models.StartingLocation;

public class QuesityMapTabFragment extends SupportMapFragment {

	private Quest _quest;
	@Override
	public void onResume() {
		super.onResume();
		GoogleMap map = getMap();
		UiSettings uiSettings = map.getUiSettings();
		uiSettings.setScrollGesturesEnabled(false);
		FragmentActivity activity = getActivity();
		if ( activity != null && activity instanceof QuestProvider ){
			_quest = ((QuestProvider)activity).getQuest();
			setLocationFromQuest();
		}
	}
	
	private void setLocationFromQuest() {
		StartingLocation startingLocation = _quest.getStartingLocation();
		GoogleMap map = getMap();
		LatLng pos = new LatLng(startingLocation.getLat(), startingLocation.getLng());
		CameraUpdate cam = CameraUpdateFactory.newLatLng(pos);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(17);
		map.moveCamera(cam);
		map.animateCamera(zoom);
		map.addMarker(new MarkerOptions().position(pos).title(startingLocation.getStreet())).showInfoWindow();
		map.addCircle(new CircleOptions().center(pos).radius(startingLocation.getRadius()).
				fillColor(Color.argb(150,97, 162, 190))
				.strokeWidth(3)
				.strokeColor(Color.argb(150,16, 62, 65)));
	
	}

}
