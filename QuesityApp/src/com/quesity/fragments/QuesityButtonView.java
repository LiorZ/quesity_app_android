package com.quesity.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.quesity.app.R;


public class QuesityButtonView extends CustomFontGeneralView{

	private View _main_view;
	public QuesityButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		build(context,attrs);
		 
	}
	
	public QuesityButtonView(Context context) {
		super(context);
		 _font_path = context.getString(R.string.button_font);
		 View view_inflated = View.inflate(context, R.layout.fragment_button, null);
		 _main_view = view_inflated;
		 addView(view_inflated);
	}
	
	private void build(Context context, AttributeSet attrs) {
		 TypedArray attr_set = context.obtainStyledAttributes(attrs,R.styleable.QuesityButtonView);
		    
		 _text = attr_set.getText(R.styleable.QuesityButtonView_button_text);
		 _image = attr_set.getResourceId(R.styleable.QuesityButtonView_button_icon, 0);
		 
		 int custom_selector = attr_set.getResourceId(R.styleable.QuesityButtonView_custom_selector, -1);
		 
		 float dimension = attr_set.getDimension(R.styleable.QuesityButtonView_button_text_size, -1) / getResources().getDisplayMetrics().density;
		 float padding_dimen = attr_set.getDimension(R.styleable.QuesityButtonView_button_padding, -1) / getResources().getDisplayMetrics().density;;
		 _image_view_width = attr_set.getDimension(R.styleable.QuesityButtonView_button_icon_width, getResources().getDimension(R.dimen.icons_size));
		 _image_view_height = attr_set.getDimension(R.styleable.QuesityButtonView_button_icon_height,getResources().getDimension(R.dimen.icons_size));
		 
		 View view_inflated = View.inflate(context, R.layout.fragment_button, null);
		 _main_view = view_inflated;
		 _font_path = context.getString(R.string.button_font);
		 _text_color = attr_set.getResourceId(R.styleable.QuesityButtonView_button_text_color, -1);

		 if ( _image != 0 )
			 setImageView(view_inflated, R.id.button_img);

		 setTextView(view_inflated, context, R.id.button_text);
		 if ( custom_selector > 0 ) { 
			 view_inflated.setBackgroundResource(custom_selector);
		 }
		 
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
	
	
	
	public void setButtonPadding(float padding) {
		setPadding(this, R.id.container, padding);
	}
	
	public void setButtonTextSize(float textSize) {
		 setTextSize(this, R.id.button_text, textSize);
	}
	
	public void setText(String text) {
		_text = text;
		setTextView(this, getContext(), R.id.button_text);
	}
	
	public void setTextSize(View v, int resid, float dimen) {
		TextView tv = (TextView) v.findViewById(resid);
		tv.setTextSize(dimen);
	}
	protected void setPadding(View v, int resid, float dimen) {
		RelativeLayout layout = (RelativeLayout) v.findViewById(resid);
		int dimenint = (int) dimen;
		layout.setPadding(dimenint, dimenint, dimenint, dimenint);
	}
	
	public void setButtonTextPadding(int left,int top,int right,int bottom) {
		RelativeLayout layout = (RelativeLayout) this.findViewById(R.id.container);
		layout.setPadding(left, top, right, bottom);
	}
	@Override
	public void setOnClickListener(OnClickListener l) {
		if ( _main_view != null )
			_main_view.setOnClickListener(l);
	}
	
}
