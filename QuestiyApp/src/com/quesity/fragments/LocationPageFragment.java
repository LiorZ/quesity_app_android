package com.quesity.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.quesity.R;
import com.quesity.activities.NextPageTransition;
import com.quesity.activities.QuestPageActivity;
import com.quesity.models.QuestPage;
import com.quesity.models.QuestPageLink;
import com.quesity.models.QuestPageLocationLink;

public class LocationPageFragment extends Fragment implements OnDemandFragment {

		@Override
		public void invokeFragment(FragmentManager manager) {
			NextPageTransition activity = (NextPageTransition) getActivity();
			QuestPage page = ((QuestPageActivity)getActivity()).getCurrentQuestPage();
		    
			LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE); 
			Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
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
					if ( radius < result[0] ){
						activity.loadNextPage(link.getLinksToPage());
						return;
					}
					
				}
			}
			AlertDialog errorDialog = SimpleDialogs.getErrorDialog(getString(R.string.lbl_wrong_location), getActivity());
			errorDialog.show();
			
		}
}
