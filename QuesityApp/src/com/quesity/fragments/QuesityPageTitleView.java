package com.quesity.fragments;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.quesity.app.R;

public class QuesityPageTitleView extends CustomFontGeneralView {


	private float _title_text_size;
	public QuesityPageTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		build(context, attrs);
	}

	private void build(Context context, AttributeSet attrs) {
		 TypedArray attr_set = context.obtainStyledAttributes(attrs,R.styleable.QuesityPageTitleView);
		    
		 _text = attr_set.getText(R.styleable.QuesityPageTitleView_page_title_text);
		 _image = attr_set.getResourceId(R.styleable.QuesityPageTitleView_title_icon, 0);
		 _title_text_size = attr_set.getDimension(R.styleable.QuesityPageTitleView_title_text_size, 0) / getResources().getDisplayMetrics().density;
		 View view_inflated = View.inflate(context, R.layout.fragment_title, null);
		 
		 
		 _font_path = context.getString(R.string.quesity_title_font);
		 if ( _image != 0 )
			 setImageView(view_inflated, R.id.title_page_img);

		 setTextView(view_inflated, context, R.id.title_main_screen);
		 
		 addView(view_inflated);
		 
		 attr_set.recycle();
	}
	
	@Override
	protected void setTextView(View mainView, Context context, int view_id) {
		super.setTextView(mainView, context, view_id);
		if ( _title_text_size > 0 ) {
			TextView t = (TextView) mainView.findViewById(R.id.title_main_screen);
			t.setTextSize(_title_text_size);
		}
			
	}

	
	public void setTitle(String title) {
		_text = title;
		TextView textView = (TextView) findViewById(R.id.title_main_screen);
		textView.setTextSize(_title_text_size);
		textView.setText(_text);
	}
	
	public void setTitleImage(int rid) {
		ImageView img = (ImageView) findViewById(R.id.title_page_img);
		img.setImageResource(rid);
	}
}
