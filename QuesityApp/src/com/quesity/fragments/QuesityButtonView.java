package com.quesity.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.quesity.R;


public class QuesityButtonView extends CustomFontGeneralView {

	public QuesityButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		build(context,attrs);
		 
	}
	
	private void build(Context context, AttributeSet attrs) {
		 TypedArray attr_set = context.obtainStyledAttributes(attrs,R.styleable.QuesityButtonView);
		    
		 _text = attr_set.getText(R.styleable.QuesityButtonView_button_text);
		 _image = attr_set.getResourceId(R.styleable.QuesityButtonView_button_icon, 0);
		 View view_inflated = View.inflate(context, R.layout.fragment_button, null);
		 _font_path = "fonts/Andada-Regular.ttf";
		 if ( _image != 0 )
			 setImageView(view_inflated, R.id.button_img);

		 setTextView(view_inflated, context, R.id.button_text);
		 
		 addView(view_inflated);
		 
		 setClickable(true);
		 
		 attr_set.recycle();
	}

}
