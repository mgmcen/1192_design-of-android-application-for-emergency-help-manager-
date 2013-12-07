package com.ehm.ws;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AddContacts extends Activity {
	/** Called when the activity is first created. */
	Button b1, b2, b3;
	private SharedPreferences prefs;
	private String prefName = "MyPref";
	private static final String TEXT_VALUE_KEY = "textvalue";
	DatabaseAdapter databaseAdapter;
	SQLiteDatabase db = null;
	public int PICK_CONTACT;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_contacts);
		databaseAdapter = new DatabaseAdapter(this);
		b1 = (Button) findViewById(R.id.button1);
		b1.setOnClickListener(new OnClickListener() {
			public int PICK_CONTACT;

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				startActivityForResult(new Intent("android.intent.action.PICK",
						ContactsContract.Contacts.CONTENT_URI),
						this.PICK_CONTACT);

				return;
			}
		});

	}

	@Override
	protected void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		if ((reqCode == this.PICK_CONTACT) && (resultCode == -1)) {
			try {
				Uri localUri = data.getData();
				Cursor c1 = getContentResolver().query(localUri, null, null,
						null, null);
				if (c1.moveToFirst()) {
					String str1 = c1.getString(c1.getColumnIndex("_id"));
					Cursor c2 = getContentResolver()
							.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
									null, "contact_id = ?",
									new String[] { str1 }, null);

					boolean bool = c2.moveToFirst();
					String str2 = null;
					String str3 = null;
					if (bool) {
						str2 = c2.getString(c2.getColumnIndex("display_name"));
						str3 = c2.getString(c2.getColumnIndex("data1"));
					}

					if ((str3 == null) || (str3.length() <= 0)) {
						Toast.makeText(getApplicationContext(),
								"Unable to add this contact", 0).show();

						return;
					}
					c1.close();
					c2.close();
					addContact1(str2, str3);
					return;

				}
			} catch (IllegalArgumentException localIllegalArgumentException) {
				localIllegalArgumentException.printStackTrace();
				return;
			} catch (Exception localException) {
				localException.printStackTrace();
			}

		}
	}

	private void addContact1(String str11, String str22) {
		// TODO Auto-generated method stub
		if (isContactAlreadyPresent(str11, str22)) {
			Toast.makeText(this, "Contact " + str11 + " already added.", 0)
					.show();
			return;
		}
		if(!contactLimit())
		{
			Toast.makeText(getApplicationContext(), "limit is over..", Toast.LENGTH_SHORT).show();
	return;
		}
		add2DB(str11, str22);

	}

	private boolean contactLimit() {
		// TODO Auto-generated method stub
		   databaseAdapter.open();
		    Cursor localCursor = databaseAdapter.limitContact();
		    int i = 0;
		    if (localCursor != null)
		    {
		      boolean bool2 = localCursor.moveToFirst();
		      i = 0;
		      if (bool2)
		        i = localCursor.getInt(0);
		    }
		    localCursor.close();
		    databaseAdapter.close();
		    boolean bool1 = false;
		    if (i < 5)
		      bool1 = true;
		    return bool1;
		  }


	private boolean isContactAlreadyPresent(String str11, String str22) {
		// TODO Auto-generated method stub
		databaseAdapter.open();
		Cursor c = databaseAdapter.selectContact1(str11, str22);
				int i = 0;
		if (c != null) {
			boolean bool = c.moveToFirst();
			i = 0;
			if (bool)
				i = c.getInt(0);
		}
		c.close();
		databaseAdapter.close();
		return i > 0;

	}

	private boolean add2DB(String paramString1, String paramString2) {

		databaseAdapter.open();
		prefs = getSharedPreferences(prefName, MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(TEXT_VALUE_KEY, paramString1);
		
		editor.putString(TEXT_VALUE_KEY, paramString2);
		String name = paramString1;
		String number = paramString2.trim();
		editor.commit();
		editor.commit();
		

		databaseAdapter.insertEntry(name, number.trim());

		Toast.makeText(getApplicationContext(),
				"Phone No successfully Entered...", Toast.LENGTH_SHORT).show();

		databaseAdapter.close();

		return true;
	}



}