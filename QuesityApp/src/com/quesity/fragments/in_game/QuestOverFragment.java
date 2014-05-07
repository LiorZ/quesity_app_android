package com.quesity.fragments.in_game;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.quesity.activities.QuestPageActivity;
import com.quesity.app.R;
import com.quesity.dialogs.EndQuestReviewDialog;
import com.quesity.fragments.AsyncImageFragment;
import com.quesity.general.Config;
import com.quesity.models.Game;
import com.quesity.models.Quest;
import com.quesity.models.Review;
import com.quesity.network.reporting.ModelReport;

public class QuestOverFragment extends Fragment {

	private String _img_uri;
	private String _quest_title;
	private String _game_id;
	private String _quest_id;
	private RatingBar _rating_bar;
	
	public static QuestOverFragment newInstance( Quest q, String game_id ) {
		QuestOverFragment frag = new QuestOverFragment();
		String img_uri = null;
		if ( q.getImages() != null && q.getImages().size() > 0)
			img_uri = q.getImages().get(0);
		
		frag.setImageURI(img_uri);
		frag.setQuestTitle(q.getTitle());
		frag.setQuestId(q.getId());
		frag.setGameId(game_id);
		
		return frag;
	}
	
	public void setGameId(String id) {
		_game_id = id;
	}
	
	public void setQuestId(String id) {
		_quest_id = id;
	}
	
	private void setRatingBar(View main_view) {
		_rating_bar = (RatingBar) main_view.findViewById(R.id.quest_over_rating);
		_rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				
				final EndQuestReviewDialog review_dialog = new EndQuestReviewDialog(getActivity(), rating,_game_id);
				review_dialog.setTitle(getString(R.string.review_dialog_title));
//				review_dialog.setMessage(getString(R.string.review_dialog_message));
				
				review_dialog.setButton(Dialog.BUTTON_POSITIVE, getString(R.string.button_ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						String submit_review_raw = getString(R.string.submit_review);
						String uri = String.format(submit_review_raw, _quest_id);
						
						Review review = review_dialog.getReview();
						FragmentActivity calling_activity = getActivity();
						if ( calling_activity == null ){
							return;
						}
						ModelReport report = new ModelReport(review, calling_activity);
						Log.d("QUESTOVERFRAGMENT", "Submitting review " + review.toString());
						Log.d("QUESTOVERFRAGMENT", "to URI " + Config.SERVER_URL + uri);
						report.send(Config.SERVER_URL + uri);
						
						dialog.dismiss();
						
						FragmentActivity activity = calling_activity;
						if (activity!=null && activity instanceof QuestPageActivity){
							((QuestPageActivity) activity).returnToMainPage();
						}
						
					}
				});
				
				review_dialog.show();
				
			}
		});
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View main_view = inflater.inflate(R.layout.fragment_end_quest, null,false);
		main_view.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
		});
		
		TextView quest_name_view = (TextView) main_view.findViewById(R.id.end_quest_name);
		quest_name_view.setText(_quest_title);


		setRatingBar(main_view);
		setFinishButton(main_view);
		return main_view;
	}
	
	private void setFinishButton(View main_view) {
		View finish_button = main_view.findViewById(R.id.end_quest_back_button);
		finish_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentActivity activity = getActivity();
				if (activity!=null && activity instanceof QuestPageActivity){
					((QuestPageActivity) activity).returnToMainPage();
				}
			}
		});
	}
	
	private void reportGameOver() {
		if ( getActivity() == null ){
			return; 
		}
		
		ModelReport report = new ModelReport(new Game(), getActivity());
		String game_over_raw = getActivity().getString(R.string.game_over);
		String uri = Config.SERVER_URL + String.format(game_over_raw, _game_id);
		report.send(uri);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		reportGameOver();
		addEndQuestImage();
	}
	
	private void addEndQuestImage() {
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.add(R.id.end_quest_image_container, AsyncImageFragment.newInstance(_img_uri, R.layout.fragment_circular_async_imageview));
		ft.commit();
	}

	public void setQuestTitle(String name){
		_quest_title= name;
	}
	
	public void setImageURI(String uri) {
		_img_uri = uri;
	}

}
