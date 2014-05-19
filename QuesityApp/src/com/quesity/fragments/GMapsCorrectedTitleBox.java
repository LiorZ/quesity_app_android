package com.quesity.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.quesity.app.R;


/**
 * Correcting the case in which hebrew text in the infobox isn't correctly displayed.
 * @author lior
 *
 */
public class GMapsCorrectedTitleBox implements InfoWindowAdapter {

	private LayoutInflater _inflater;
	public GMapsCorrectedTitleBox(LayoutInflater inflater) {
		_inflater = inflater;
	}
	
	@Override
	public View getInfoContents(Marker m) {
		View title_view = _inflater.inflate(R.layout.fragment_gmaps_titlebox, null);
		TextView title_text = (TextView) title_view.findViewById(R.id.gmaps_title_text);
		title_text.setText(m.getTitle());
		return title_view;
	}

	@Override
	public View getInfoWindow(Marker arg0) {
		return null;
	}

}
