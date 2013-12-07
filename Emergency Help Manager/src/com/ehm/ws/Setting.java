package com.ehm.ws;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;


public class Setting extends Activity {

	CheckBox cb1, cb2;
	// TextView tv1, tv2;
	Intent intent;
	BroadcastReceiver mReceiver;
	private SharedPreferences prefs;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		cb1 = (CheckBox) findViewById(R.id.checkBox1);
		cb2 = (CheckBox) findViewById(R.id.checkBox2);
		cb1.setTextColor(Color.BLACK);
		cb2.setTextColor(Color.BLACK);
		// cb1.setChecked(false);
		// cb2.setChecked(false);

		// cbp=(CheckBoxPreference)
		// tv1 = (TextView) findViewById(R.id.textView1);
		// String str = (String) cb1.getText();
		// String str1 = (String) cb2.getText();
		// Toast.makeText(getApplicationContext(), str + " started",
		// Toast.LENGTH_LONG).show();
		cb1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (arg1 == true) {
					intent = new Intent(getBaseContext(), ShakeService.class);
					startService(intent);

				} else {
					//String str = (String) cb1.getText();
					intent = new Intent(getBaseContext(), ShakeService.class);
					stopService(intent);

				}

			}
		});
		cb2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if (cb2.isChecked() == true) {
					// String str = (String) cb2.getText();

					intent = new Intent(getBaseContext(),
							MainPowerService.class);
					startService(intent);

				} else {

					intent = new Intent(getBaseContext(),
							MainPowerService.class);
					//unregisterReceiver(mReceiver);
					stopService(intent);

				}
			}
		});

	}

	@Override
	public void onPause() {
		super.onPause();
		save(cb1.isChecked(), cb2.isChecked());

	}

	@Override
	public void onResume() {
		super.onResume();

		cb1.setChecked(load("check1"));
		cb2.setChecked(load("check2"));

	}

	private void save(final boolean ischecked1, final boolean ischecked2) {
		// TODO Auto-generated method stub
		prefs = getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("check1", ischecked1);
		editor.putBoolean("check2", ischecked2);
		editor.commit();
	}

	private boolean load(String check) {
		// TODO Auto-generated method stub
		prefs = getPreferences(Context.MODE_PRIVATE);
		if (check.equals("check1"))
			return prefs.getBoolean("check1", false);
		if (check.equals("check2"))
			return prefs.getBoolean("check2", false);
		else
			return false;
	}

}