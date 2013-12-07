package com.ehm.ws;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.Toast;

public class ShakeService extends Service implements SensorEventListener {
	Vibrator vibrate;
	long pattern[];
	int mcc, mnc, cid, lac;
	TelephonyManager telephonyManager;
	static final String DATABASE_NAME = "PhoneNo.db";
	String ID = null;
	String str2, str3;
	public SQLiteDatabase db;
	DatabaseAdapter databaseAdapter;
	private SensorManager sensorMgr;
	BroadcastReceiver broadcastReceiver, broadcastReceiver1;
	private long lastUpdate = -1;

	// measure the x y and z value change
	private float x, y, z;
	private float last_x, last_y, last_z;

	// amount of shake required to perform the given action
	private static final int SHAKE_THRESHOLD = 500;

	// private static final String TEXT_VALUE_KEY = null;

	public void onAccuracyChanged(Sensor paramSensor, int paramInt) {
	}

	public IBinder onBind(Intent paramIntent) {
		return null;
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(getApplicationContext(), "Shake Service Started...",
				Toast.LENGTH_SHORT).show();
		// onCreate();
		return START_STICKY;
	}

	public void onCreate() {
		super.onCreate();
		vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		PhoneStateListener cellLocationListener = new PhoneStateListener() {
			public void onCellLocationChanged(CellLocation location) {
				GsmCellLocation gsmLocation = (GsmCellLocation) location;
				cid = gsmLocation.getCid();
				lac = gsmLocation.getLac();
				String networkOperator = telephonyManager.getNetworkOperator();
				if (networkOperator != null && networkOperator.length() > 0) {
					try {
						mcc = Integer.parseInt(networkOperator.substring(0, 3));
						mnc = Integer.parseInt(networkOperator.substring(3));
					} catch (NumberFormatException e) {
					}
				}

			}
		};
		telephonyManager.listen(cellLocationListener,
				PhoneStateListener.LISTEN_CELL_LOCATION);

		/*
		 * Start without a delay Vibrate for 100 milliseconds Sleep for 1000
		 * milliseconds
		 */
		pattern = new long[] { 0, 200, 1000, 300, 200, 500 };

		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		// check whether the sensor is supported by device
		boolean accelSupported = sensorMgr.registerListener(this,
				sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
		if (!accelSupported) {
			Toast.makeText(this, "Sensor not supported in your device !!!",
					Toast.LENGTH_SHORT).show();
			// unregister the sensor
			sensorMgr.unregisterListener(this);
		}
	}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
			long curTime = System.currentTimeMillis();

			// only allow one update every 100ms.
			if ((curTime - lastUpdate) > 100) {
				long diffTime = (curTime - lastUpdate);
				lastUpdate = curTime;

				// get the value of x y and z co-ordinates
				x = event.values[0];
				y = event.values[1];
				z = event.values[2];

				// convert values to speed
				float speed = Math.abs(x + y + z - last_x - last_y - last_z)
						/ diffTime * 10000;

				// compare with the threshold and perform action as given
				if (speed > SHAKE_THRESHOLD) {
					// vibrate action
					vibrate.vibrate(pattern, -1);
					databaseAdapter = new DatabaseAdapter(this);
					databaseAdapter.open();
					Cursor c = databaseAdapter.getAllContacts();
					if (c.moveToFirst()) {
						do {
							c.getString(2);

							str2 = "CID:"
									+ cid
									+ ",LAC:"
									+ lac
									+ ",MCC:"
									+ mcc
									+ ",MNC:"
									+ mnc
									+ "please refer this link to locate me:www.cellphonetrackers.org/gsm/gsm-tracker.php";

							str3 = "Help Me I am in Trouble\nLocation:" + str2;
							sendSMS(c.getString(2), str3);
						} while (c.moveToNext());
					}
					c.close();
					databaseAdapter.close();
				}

			}
			last_x = x;
			last_y = y;
			last_z = z;

			// used when need to consider accuracy

		}
	}

	private void sendSMS(String phoneNumber, String message) {
		String SENT = "SMS_SENT";
		String DELIVERED = "SMS_DELIVERED";

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(
				SENT), 0);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
				new Intent(DELIVERED), 0);

		// ---when the SMS has been sent---
		registerReceiver(broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS sent",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Generic failure",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "No service",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_NULL_PDU:
					Toast.makeText(getBaseContext(), "Null PDU",
							Toast.LENGTH_SHORT).show();
					break;
				case SmsManager.RESULT_ERROR_RADIO_OFF:
					Toast.makeText(getBaseContext(), "Radio off",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}

		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(broadcastReceiver1 = new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				switch (getResultCode()) {
				case Activity.RESULT_OK:
					Toast.makeText(getBaseContext(), "SMS delivered",
							Toast.LENGTH_SHORT).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(getBaseContext(), "SMS not delivered",
							Toast.LENGTH_SHORT).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
		unregisterReceiver(broadcastReceiver);
		unregisterReceiver(broadcastReceiver1);

	}

	public void onDestroy() {
		super.onDestroy();

		Toast.makeText(this, " Shake Service Destroyed...", Toast.LENGTH_SHORT)
				.show();
	}
}
