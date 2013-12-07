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
import android.os.IBinder;
import android.os.Vibrator;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.Toast;

public class StartPowerService extends Service {
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

	public int onStartCommand(Intent intent, int flags, int startId) {

		Log.v("Screen off", "1");
		onCreate();
		this.stopSelf();
		return START_STICKY;
	}

	public void onCreate() {
		super.onCreate();
		databaseAdapter = new DatabaseAdapter(this);
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
		};
		telephonyManager.listen(cellLocationListener,
				PhoneStateListener.LISTEN_CELL_LOCATION);

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

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	// @Override
	public void onDestroy() {
		// unregisterReceiver(mReceiver);
		super.onDestroy();
		Log.v("destroy", "power");
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	}
}