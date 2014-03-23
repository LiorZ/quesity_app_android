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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.quesity.R;


public abstract class CustomFontGeneralView extends FrameLayout {

	protected int _image;
	protected CharSequence _text;
	protected CharSequence _font_path;
	public CustomFontGeneralView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	protected void setFont(TextView text, Context context) {
		AssetManager assets = context.getAssets();
		if ( assets == null )
			return;
		Typeface tf = Typeface.createFromAsset(assets, _font_path.toString());
		text.setTypeface(tf);
	}
	
	protected void setImageView(View mainView,int view_id) {
		//if no button icon was defined, remove the imageview
		ImageView button_img = (ImageView) mainView.findViewById(view_id);
		if ( _image != 0 ){
			button_img.setImageResource(_image);
		}else{
			button_img.setLayoutParams(new LayoutParams(0, 0));
		}
	}
	
	protected void setTextView(View mainView, Context context,int view_id) {
		TextView button_text_view = (TextView) mainView.findViewById(view_id);
		setFont(button_text_view, context);
		button_text_view.setText(_text);
	}
	
	

}
