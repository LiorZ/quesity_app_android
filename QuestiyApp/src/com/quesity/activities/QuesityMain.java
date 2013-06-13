package com.quesity.activities;

import com.quesity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class QuesityMain extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_quesity_main);
    }
    
    public void showQuests(View view){
    	Intent intent = new Intent(this, QuestsListViewActivity.class);
    	startActivity(intent);
    }
}
