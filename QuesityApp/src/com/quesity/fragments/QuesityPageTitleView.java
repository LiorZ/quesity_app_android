package com.quesity.fragments;

import com.quesity.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class QuesityPageTitleView extends CustomFontGeneralView {


	public QuesityPageTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		build(context, attrs);
	}

	private void build(Context context, AttributeSet attrs) {
		 TypedArray attr_set = context.obtainStyledAttributes(attrs,R.styleable.QuesityPageTitleView);
		    
		 _text = attr_set.getText(R.styleable.QuesityPageTitleView_page_title_text);
		 _image = attr_set.getResourceId(R.styleable.QuesityPageTitleView_title_icon, 0);
		 View view_inflated = View.inflate(context, R.layout.fragment_title, null);
		 
		 _font_path = "fonts/Andada-Regular.ttf";
		 if ( _image != 0 )
			 setImageView(view_inflated, R.id.title_page_img);

		 setTextView(view_inflated, context, R.id.title_main_screen);
		 
		 addView(view_inflated);
		 
		 attr_set.recycle();
	}
}
