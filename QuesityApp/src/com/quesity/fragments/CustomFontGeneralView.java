package com.quesity.fragments;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


public abstract class CustomFontGeneralView extends FrameLayout {

	protected int _image;
	protected CharSequence _text;
	protected CharSequence _font_path;
	protected float _image_view_width = -1;
	protected float _image_view_height = -1;
	protected int _text_color = -1;
	public CustomFontGeneralView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public CustomFontGeneralView(Context context) {
		super(context);
	}

	protected void setFont(TextView text, Context context) {
		AssetManager assets = context.getAssets();
		if ( assets == null )
			return;
		Typeface tf = Typeface.createFromAsset(assets, _font_path.toString());
		text.setTypeface(tf);
	}
	
	protected void setImageViewSize(ImageView view){
		if ( _image_view_height > 0 && _image_view_width > 0 ){
			android.view.ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
			layoutParams.width = (int)_image_view_width;
			layoutParams.height = (int)_image_view_height;
			view.setLayoutParams(layoutParams);
		}
	}
	
	protected void setImageView(View mainView,int view_id) {
		//if no button icon was defined, remove the imageview
		ImageView button_img = (ImageView) mainView.findViewById(view_id);
		if ( _image != 0 ){
			button_img.setImageResource(_image);
			setImageViewSize(button_img);
		}else{
			button_img.setLayoutParams(new LayoutParams(0, 0));
		}
	}
	
	protected void setTextView(View mainView, Context context,int view_id) {
		TextView button_text_view = (TextView) mainView.findViewById(view_id);
		setFont(button_text_view, context);
		button_text_view.setText(_text);
		if ( _text_color > 0 ) {
			int color = getResources().getColor(_text_color);
			button_text_view.setTextColor(color);
		}
	}
	
	

}
