package com.ehm.ws;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MainPowerService extends Service {
	int counter = 0;
	int i = 0;
	static final int UPDATE_INTERVAL = 1000;
	private BroadcastReceiver mReceiver;
	IntentFilter intentFilter;

	public void onCreate() {
		super.onCreate();

	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		Toast.makeText(this, "Power Button Service Started...",
				Toast.LENGTH_LONG).show();
		Log.d("Screen off", "1");
		Log.v("Screen off", "1");

		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver = new MyReceiver(this);
		registerReceiver(mReceiver, filter);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
		unregisterReceiver(mReceiver);
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}

}