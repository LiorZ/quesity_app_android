package com.quesity.fragments.in_game;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.quesity.activities.NextPageTransition;
import com.quesity.activities.QuestPageActivity;
import com.quesity.app.R;
import com.quesity.fragments.OnDemandFragment;
import com.quesity.models.Game;
import com.quesity.models.QuestPageLink;

public class ContentPageFragment extends Fragment implements OnDemandFragment {

	@Override
	public void invokeFragment(FragmentManager manager) {
		Game game = ((QuestPageActivity)getActivity()).getCurrentGame();
		QuestPageLink next_page_link = game.getNextPage();
		game.moveToPage(next_page_link);
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
