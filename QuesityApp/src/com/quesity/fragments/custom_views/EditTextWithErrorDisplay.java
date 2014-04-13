package com.quesity.fragments.custom_views;

import com.throrinstudio.android.common.libs.validator.R;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.EditText;

public class EditTextWithErrorDisplay extends EditText{

	public EditTextWithErrorDisplay(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setError(CharSequence error) {
		if (error == null){
			super.setError(null);
			return;
		}
    	int ecolor = getResources().getColor(R.color.red);
    	ForegroundColorSpan fgcspan = new ForegroundColorSpan(ecolor);
    	SpannableStringBuilder ssbuilder = new SpannableStringBuilder(error);
    	ssbuilder.setSpan(fgcspan, 0, error.length(), 0);
    	super.setError(ssbuilder);
	}
	
	
	
	

}
