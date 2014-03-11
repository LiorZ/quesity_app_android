package com.quesity.fragments;

import com.quesity.R;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;


public class StyledTextView extends android.widget.TextView {
	
	private CharSequence _font_path;
	public StyledTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		build(context, attrs);
	}
	
	private void build(Context context, AttributeSet attrs) {
		 TypedArray attr_set = context.obtainStyledAttributes(attrs,R.styleable.StyledTextView);
		 _font_path = attr_set.getText(R.styleable.StyledTextView_font_path);
		AssetManager assets = context.getAssets();
		if ( assets == null )
			return;
		Typeface tf = Typeface.createFromAsset(assets, _font_path.toString());
		setTypeface(tf);
		attr_set.recycle();
	}

}
