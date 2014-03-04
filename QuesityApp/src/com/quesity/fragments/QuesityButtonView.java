package com.quesity.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.quesity.R;


public class QuesityButtonView extends FrameLayout {

	private int _button_icon;
	private CharSequence _button_text;

	public QuesityButtonView(Context context, AttributeSet attrs) {
		super(context, attrs);
		build(context,attrs);
		 
	}
	
	private void build(Context context, AttributeSet attrs) {
		 TypedArray attr_set = context.obtainStyledAttributes(attrs,R.styleable.QuesityButtonView);
		    
		 _button_text = attr_set.getText(R.styleable.QuesityButtonView_button_text);
		 _button_icon = attr_set.getResourceId(R.styleable.QuesityButtonView_button_icon, 0);
		 
		 View view_inflated = View.inflate(context, R.layout.fragment_button, null);

		 setImageView(view_inflated);
		 setButtonText(view_inflated,context);
		 
		 addView(view_inflated);
		 
		 setClickable(true);
		 
		 
		 attr_set.recycle();
	}
	
	private void setFont(TextView text, Context context) {
		AssetManager assets = context.getAssets();
		if ( assets == null )
			return;
		Typeface tf = Typeface.createFromAsset(assets, "fonts/Andada-Regular.ttf");
		text.setTypeface(tf);
	}

	private void setImageView(View mainView) {
		//if no button icon was defined, remove the imageview
		ImageView button_img = (ImageView) mainView.findViewById(R.id.button_img);
		if ( _button_icon != 0 ){
			button_img.setImageResource(_button_icon);
		}else{
			button_img.setLayoutParams(new LayoutParams(0, 0));
		}
	}
	
	private void setButtonText(View mainView, Context context) {
		TextView button_text_view = (TextView) mainView.findViewById(R.id.button_text);
		setFont(button_text_view, context);
		button_text_view.setText(_button_text);
	}

}
