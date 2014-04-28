package com.quesity.fragments;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.quesity.app.R;

public class QuesityIngameButtonView extends FrameLayout {

	private int _touchUpImage;
	private int _touchDownImage;
	private ImageView _image;
	private StyledTextView _text;
	private OnClickListener _listener;
	
	public QuesityIngameButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final View btn = View.inflate(context, R.layout.fragment_ingame_button, null);
		_image = (ImageView) btn.findViewById(R.id.ingame_button_img);
		_text = (StyledTextView) btn.findViewById(R.id.ingame_button_text);
		addView(btn);
		
		btn.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Resources resources = getResources();
				if ( event.getAction() == MotionEvent.ACTION_UP ){
					_image.setImageResource(_touchUpImage);
					_text.setTextColor(resources.getColor(R.color.quesity_title_color));
					btn.setBackgroundColor(resources.getColor(R.color.blue_btn_color));
					if ( _listener != null )
						_listener.onClick(btn);
				}else if (event.getAction() == MotionEvent.ACTION_DOWN) {
					_image.setImageResource(_touchDownImage);
					_text.setTextColor(resources.getColor(R.color.blue_btn_color));
					btn.setBackgroundColor(resources.getColor(R.color.quesity_title_color));
				}
				return true;
			}
		});
	}

	public void setButtonText(String text) {
		_text.setText(text);
	}
	
	public void setButtonImage(int res) {
		_touchUpImage = res;
		_image.setImageResource(res);
	}
	
	public void setOnTouchButtonImage(int res) {
		_touchDownImage = res;
	}
	
	@Override
	public void setOnClickListener(OnClickListener l) {
		_listener = l;
	}
	
}
