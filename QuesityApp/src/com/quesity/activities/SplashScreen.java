package com.quesity.activities;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.quesity.R;

public class SplashScreen extends FragmentActivity {
	
	private int _backButtonCount;
	private Timer _t;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
		_t = new Timer();
	}
	
	@Override
	public void onBackPressed() 
	{ 
	    if(_backButtonCount >= 1) 
	    { 
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_HOME);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	        _backButtonCount = 0;
	        startActivity(intent);
	    } 
	    else 
	    { 
	        Toast.makeText(this, getString(R.string.lbl_exit_notice), Toast.LENGTH_SHORT).show();
	        _backButtonCount++;
	        _t.schedule(new TimerTask() {
				
				@Override
				public void run() {
					_backButtonCount = 0;
				}
			}, 2000);
	    } 
	} 
	
}
