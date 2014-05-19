package com.quesity.fragments.in_game;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.quesity.activities.NextPageTransition;
import com.quesity.activities.QuestPageActivity;
import com.quesity.app.R;
import com.quesity.fragments.OnDemandFragment;
import com.quesity.models.ModelsFactory;
import com.quesity.models.QuestPage;

public class ContentPageFragment extends Fragment implements OnDemandFragment {

	@Override
	public void invokeFragment(FragmentManager manager) {
		NextPageTransition activity = (NextPageTransition) getActivity();
		QuestPage page = ((QuestPageActivity)getActivity()).getCurrentQuestPage();
	    activity.loadNextPage(page.getLinks()[0]);
	}

	@Override
	public int getButtonDrawable() {
		return R.drawable.continue_img;
	}

	@Override
	public int getPressedButtonDrawable() {
		return R.drawable.continue_img_pressed;
	}

	@Override
	public int getButtonStringId() {
		return R.string.ingame_btn_continue;
	}
	
	
}
