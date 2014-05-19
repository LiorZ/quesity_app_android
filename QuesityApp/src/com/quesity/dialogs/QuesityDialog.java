package com.quesity.dialogs;

import java.util.HashMap;
import java.util.Map;

import android.R.dimen;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.quesity.app.R;
import com.quesity.fragments.QuesityButtonView;
import com.quesity.network.GetRequestTypeGetter;

public class QuesityDialog extends Dialog {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		build();
	}

	private TextView _title;
	private String _title_text;
	private String _msg_text;
	private TextView _text;
	private Map<Integer, QuesityButtonView> _buttons;
	private LinearLayout _button_container;
	public QuesityDialog(Context context) {
		super(context);
		_buttons = new HashMap<Integer, QuesityButtonView>();
		_title_text = "";
		_msg_text = "";
	}
	
	protected void build() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_general);
		_title = (TextView) findViewById(R.id.dialog_title);
		_title.setText(_title_text);
		_text = (TextView) findViewById(R.id.dialog_text);
		_text.setText(_msg_text);
		_button_container = (LinearLayout) findViewById(R.id.dialog_button_container);
		int btns = _buttons.values().size();
		_button_container.setWeightSum(btns);
		boolean is_first = true;
		for (View btn : _buttons.values()) {
			LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btn.getLayoutParams();
			layoutParams.weight = 1;
			if (!is_first) {
				layoutParams.leftMargin = (int) getContext().getResources().getDimension(R.dimen.dialog_buttons_margin_side);
			}
			is_first = false;
			_button_container.addView(btn);
			
		}
	}
	
	public void setButton(final int button,String text,final DialogInterface.OnClickListener listener) {
		Resources resources = getContext().getResources();
		QuesityButtonView btn = new QuesityButtonView(getContext());
		LinearLayout.LayoutParams l = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
		btn.setLayoutParams(l);
		float btn_text_size = resources.getDimension(R.dimen.dialog_button_text_size) / resources.getDisplayMetrics().density;
		int padding_top_bottom = (int) (resources.getDimension(R.dimen.dialog_buttons_padding_top_bottom) / resources.getDisplayMetrics().density);
		int padding_left_right = (int) (resources.getDimension(R.dimen.dialog_buttons_padding_left_right) / resources.getDisplayMetrics().density);
		btn.setButtonTextSize(btn_text_size);
		btn.setText(text);
		btn.setButtonTextPadding(padding_left_right, padding_top_bottom, padding_left_right, padding_top_bottom);
		btn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onClick(QuesityDialog.this, button);
				if ( QuesityDialog.this.isShowing() ) {
					QuesityDialog.this.dismiss();
				}
			}
		});
		_buttons.put(button, btn);
	}
	
	@Override
	public void setTitle(CharSequence title){
		_title_text = title.toString();
	}


	@Override
	public void setTitle(int titleId) {
		_title_text = getContext().getString(titleId);
	}

	public void setMessage(CharSequence msg) {
		_msg_text = msg.toString();
	}

}
