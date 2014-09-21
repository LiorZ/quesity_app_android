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
	private int _disabled_image;
	private int _enabled_image;
	private ImageView _image;
	private StyledTextView _text;
	private OnClickListener _listener;
	private View.OnTouchListener _enabled_touch_listener;
	private View _actual_btn;
	
	public QuesityIngameButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		_actual_btn = View.inflate(context, R.layout.fragment_ingame_button, null);
		_image = (ImageView) _actual_btn.findViewById(R.id.ingame_button_img);
		_text = (StyledTextView) _actual_btn.findViewById(R.id.ingame_button_text);
		addView(_actual_btn);
		
		_enabled_touch_listener = new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Resources resources = getResources();
				if ( event.getAction() == MotionEvent.ACTION_UP ){
					_image.setImageResource(_touchUpImage);
					_text.setTextColor(resources.getColor(R.color.quesity_title_color));
					_actual_btn.setBackgroundColor(resources.getColor(R.color.blue_btn_color));
					if ( _listener != null )
						_listener.onClick(_actual_btn);
				}else if (event.getAction() == MotionEvent.ACTION_DOWN) {
					_image.setImageResource(_touchDownImage);
					_text.setTextColor(resources.getColor(R.color.blue_btn_color));
					_actual_btn.setBackgroundColor(resources.getColor(R.color.quesity_title_color));
				}
				return true;
			}
		};
		_actual_btn.setOnTouchListener(_enabled_touch_listener);
	}
	
	public void setDisabledImage(int image) {
		_disabled_image = image;
	}
	
	public void setEnabled(boolean enabled) {
		if (!enabled) {
			_image.setImageResource(_disabled_image);
			_touchUpImage = _disabled_image;
			setBackgroundColor(getResources().getColor(R.color.DarkGray));
			setOnClickListener(null);
			_actual_btn.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					return true;
				}
			});
			_text.setTextColor(getResources().getColor(R.color.QuesityGray));
		}else{ 
			_touchUpImage = _enabled_image;
			_image.setImageResource(_touchUpImage);
			setOnClickListener(_listener);
			_actual_btn.setOnTouchListener(_enabled_touch_listener);
			int color = getResources().getColor(R.color.blue_btn_color);
			setBackgroundColor(color);
			_text.setTextColor(getResources().getColor(R.color.quesity_title_color));
		}
	}

	public void setButtonText(String text) {
		_text.setText(text);
	}
	
	public void setButtonImage(int res) {
		_touchUpImage = res;
		_enabled_image = res;
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
