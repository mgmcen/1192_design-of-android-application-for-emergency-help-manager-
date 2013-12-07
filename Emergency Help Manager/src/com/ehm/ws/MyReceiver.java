package com.ehm.ws;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {
	private Service activity = null;
	static int i = 0;
	IntentFilter intentFilter;
	BroadcastReceiver mReceiver;
	Vibrator vibrate;
	long pattern[];
	int mcc, mnc, cid, lac;
	TelephonyManager telephonyManager;
	static final String DATABASE_NAME = "PhoneNo.db";
	String ID = null;
	String str2, str3;
	public SQLiteDatabase db;
	BroadcastReceiver broadcastReceiver, broadcastReceiver1;
	DatabaseAdapter databaseAdapter;

	public MyReceiver(Service activity) {

		this.activity = activity;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		i++;

		Log.v("onReceive", "Power button is pressed.");
		Log.d("in OnRecive inside ", "" + i);

		if (i == 1) {
			new CountDownTimer(5000, 1000) {

				@Override
				public void onTick(long arg0) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					Log.d("in Onfinish outside if", "" + i);
					Intent intent1;
					 intent1 = new Intent(activity,
								StartPowerService.class);
					if (i >= 4) {
						
						// Do something you want
						Log.d("in Onfinish outside if", "" + i);
			
						activity.startService(intent1);
						

					}
					i = 0;
				}

			}.start();

		}

	}

}
