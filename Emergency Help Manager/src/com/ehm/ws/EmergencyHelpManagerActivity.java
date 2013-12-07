package com.ehm.ws;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;

public class EmergencyHelpManagerActivity extends Activity {
	TextView tv;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    tv=(TextView)findViewById(R.id.textView1);
    tv.setTextColor(Color.BLACK);
    new CountDownTimer(3000,1000) {
		
		@Override
		public void onTick(long arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getBaseContext(),
					EmergencyHelpManager1Activity.class);
			startActivity(intent);
		}
	}.start();
	
    
    }
}