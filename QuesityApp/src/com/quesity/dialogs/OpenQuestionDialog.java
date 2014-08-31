package com.quesity.dialogs;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.quesity.app.R;

public class OpenQuestionDialog extends QuesityDialog{

	private EditText _answer_text;
	private ProgressBar _progress;
	private TextView _verifying_lbl;
	
	public OpenQuestionDialog(Context context) {
		super(context);
	}
	
	@Override
	protected void build() {
		super.build();
		FrameLayout extra_content_view = (FrameLayout) findViewById(R.id.dialog_extra_content);
		View edittext = getLayoutInflater().inflate(R.layout.open_question_dialog, null,false);
		_answer_text = (EditText) edittext.findViewById(R.id.open_question_answer);
		_progress = (ProgressBar) edittext.findViewById(R.id.open_question_loading_progress);
		_verifying_lbl = (TextView) edittext.findViewById(R.id.open_question_verifying_answer);
		extra_content_view.addView(edittext);
	}
	public void setProgressVisibility(int visibility) {
		_progress.setVisibility(visibility);
		_verifying_lbl.setVisibility(visibility);
	}
	
	public String getAnswer() {
		return _answer_text.getText().toString();
	}

	
	public interface CheckIsOver {
		public void checkOver();
	}
}
