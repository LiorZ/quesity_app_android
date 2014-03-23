package com.quesity.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
		 float dimension = attr_set.getDimension(R.styleable.QuesityButtonView_button_text_size, -1);
		 float padding_dimen = attr_set.getDimension(R.styleable.QuesityButtonView_button_padding, -1);
		 View view_inflated = View.inflate(context, R.layout.fragment_button, null);
		 _font_path = "fonts/Andada-Regular.ttf";
		 if ( _image != 0 )
			 setImageView(view_inflated, R.id.button_img);

		 setTextView(view_inflated, context, R.id.button_text);
		 
		 if ( dimension >= 0 ) {
			 setTextSize(view_inflated, R.id.button_text, dimension);
		 }
		 if ( padding_dimen >= 0 ){
			 setPadding(view_inflated, R.id.container, padding_dimen);
		 }
		 addView(view_inflated);
		 
		 setClickable(true);
		 
		 attr_set.recycle();
	}
	
	private void setTextSize(View v, int resid, float dimen) {
		TextView tv = (TextView) v.findViewById(resid);
		tv.setTextSize(dimen);
	}
	private void setPadding(View v, int resid, float dimen) {
		RelativeLayout layout = (RelativeLayout) v.findViewById(resid);
		int dimenint = (int) dimen;
		layout.setPadding(dimenint, dimenint, dimenint, dimenint);
	}
	
}
