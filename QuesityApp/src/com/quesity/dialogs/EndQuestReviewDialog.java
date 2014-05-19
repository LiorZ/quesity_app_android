package com.quesity.dialogs;

import java.util.Date;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RatingBar;

import com.quesity.app.R;
import com.quesity.models.Review;


public class EndQuestReviewDialog extends QuesityDialog {

	private float _rating;
	private RatingBar _ratingbar;
	private EditText _review_edit_text_view;
	private String _game_id;
	
	
	public EndQuestReviewDialog(Context context,float rating, String game_id) {
		super(context);
		_rating = rating;
		_game_id = game_id;
	}

	public Review getReview() {
		Review r = new Review();
		r.setDateCreated(new Date());
		r.setRating(_ratingbar.getRating());
		r.setReviewText(_review_edit_text_view.getText().toString());
		r.setGameId(_game_id);
		return r;
	}
	
	@Override
	protected void build() {
		super.build();
		
		FrameLayout extra_content_view = (FrameLayout) findViewById(R.id.dialog_extra_content);
		View review_view = getLayoutInflater().inflate(R.layout.end_quest_review, null,false);
		findViewById(R.id.dialog_text).setVisibility(View.INVISIBLE);
		_ratingbar = (RatingBar) review_view.findViewById(R.id.end_quest_rating_bar_dialog);
		_review_edit_text_view = (EditText) review_view.findViewById(R.id.review_text);
		_ratingbar.setRating(_rating);
		
		extra_content_view.addView(review_view);
	}

}
