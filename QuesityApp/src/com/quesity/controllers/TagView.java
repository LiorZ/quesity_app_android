package com.quesity.controllers;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.quesity.app.R;

public class TagView extends FrameLayout {
	
	public TagView(Context context) {
		super(context);
		View tag_view = View.inflate(context, R.layout.fragment_tag, null);
		_text_view = (TextView) tag_view.findViewById(R.id.tag_text);
		addView(tag_view);
	}


	private TextView _text_view;
	
	
	public void setTagText(String text) {
		_text_view.setText(text);
	}
	

}
