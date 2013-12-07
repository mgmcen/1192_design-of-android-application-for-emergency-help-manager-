package com.ehm.ws;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.widget.TextView;

public class ViewLocation extends Activity {
	/** Called when the activity is first created. */
	int mcc, mnc, cid, lac;
	TextView textCID, textLAC,textMNC, textMCC;
	TelephonyManager telephonyManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_location);
		textCID = (TextView) findViewById(R.id.cid);
		textLAC = (TextView) findViewById(R.id.lac);
		textMNC = (TextView) findViewById(R.id.mnc);
		textMCC = (TextView) findViewById(R.id.mcc);
		textCID.setTextColor(Color.BLACK);
		textLAC.setTextColor(Color.BLACK);
		textMCC.setTextColor(Color.BLACK);
		textMNC.setTextColor(Color.BLACK);
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

				textCID.setText("gsm cell id: " + String.valueOf(cid));
				textLAC.setText("gsm location area code: "
						+ String.valueOf(lac));
				textMCC.setText("gsm MCC code: " + String.valueOf(mcc));
				textMNC.setText("gsm MNC code: " + String.valueOf(mnc));
			}
		};
		telephonyManager.listen(cellLocationListener,
				PhoneStateListener.LISTEN_CELL_LOCATION);

	}

}