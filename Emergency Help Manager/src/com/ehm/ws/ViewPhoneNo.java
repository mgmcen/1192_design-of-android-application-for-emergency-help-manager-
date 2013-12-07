package com.ehm.ws;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewPhoneNo extends Activity {
	List<String> name1 = new ArrayList<String>();
	List<String> phno1 = new ArrayList<String>();
	MyAdapter ma;
	TextView tv1, tv;
	ListView lv;
	Button b;
	DatabaseAdapter databaseAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_phone_no);
		lv = (ListView) findViewById(R.id.lv1);
		ma = new MyAdapter();
		lv.setAdapter(ma);
		lv.setItemsCanFocus(false);
		b = (Button) findViewById(R.id.button1);
		databaseAdapter = new DatabaseAdapter(this);
		displayContactList();
	}

	public void displayContactList() {
		databaseAdapter.open();
		Cursor c = databaseAdapter.getAllContacts();

		if (c.moveToFirst()) {
			do {
				DisplayContact(c);

			} while (c.moveToNext());
			ma = new MyAdapter();
			lv.setAdapter(ma);
			ma.notifyDataSetChanged();

		}
		c.close();
		databaseAdapter.close();

	}

	public void DisplayContact(Cursor c) {
		String str1 = c.getString(1);
		Log.i("Contacts", "Name: " + str1);
		String str2 = c.getString(2);
		Log.i("Contacts", "No: " + str1);
		name1.add(str1);
		phno1.add(str2);
	}

	public class MyAdapter extends BaseAdapter implements
			OnItemLongClickListener {
		LayoutInflater mInflater;

		MyAdapter() {
			mInflater = (LayoutInflater) ViewPhoneNo.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return name1.size();
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View vi = convertView;
			if (convertView == null) {
				vi = mInflater.inflate(R.layout.phone_row, null);
				tv = (TextView) vi.findViewById(R.id.contact_name);
				tv1 = (TextView) vi.findViewById(R.id.phone_number);
				tv.setTextColor(Color.BLACK);
				tv1.setTextColor(Color.BLACK);
				tv.setText("Name:" + name1.get(position));
				tv1.setText("Phone No :" + phno1.get(position).trim());
				lv.setOnItemLongClickListener(this);

			}
			return vi;
		}

		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				final int position, long id) {
			// TODO Auto-generated method stub
			final Dialog dialog = new Dialog(ViewPhoneNo.this);
			dialog.setContentView(R.layout.delete_contacts);
			dialog.setTitle("DELETE NUMBER");
			dialog.show();
			// get the References of views
			Button btndelete = (Button) dialog.findViewById(R.id.button2);
			Button btnCancel = (Button) dialog.findViewById(R.id.button1);
			btndelete.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					databaseAdapter.open();
					String str5 = name1.get(position);
					if (databaseAdapter.deleteContact(str5)) {
						Log.i("Contacts", "Deleted: ");

						dialog.dismiss();
						Toast.makeText(getApplicationContext(),
								"Delete successful." + phno1.get(position),
								Toast.LENGTH_LONG).show();
						lv.setAdapter(ma);
						ma.notifyDataSetChanged();
						databaseAdapter.close();
						createContactList();
					}

					else {
						Toast.makeText(getApplicationContext(),
								"Delete failed.", Toast.LENGTH_LONG).show();
						databaseAdapter.close();
					}
				}

				private void createContactList() {
					// TODO Auto-generated method stub
					databaseAdapter.open();

					lv = (ListView) findViewById(R.id.lv1);
					name1.clear();
					phno1.clear();
					Cursor localCursor = databaseAdapter.getAllContacts();
					if ((localCursor != null) && (localCursor.moveToFirst())) {
						do {
							String str1 = localCursor.getString(1);
							Log.i("Contacts", "Name: " + str1);
							String str2 = localCursor.getString(2);
							Log.i("Contacts", "No: " + str1);
							name1.add(str1);
							phno1.add(str2);

						} while (localCursor.moveToNext());
						ma = new MyAdapter();
						lv.setAdapter(ma);
						ma.notifyDataSetChanged();
					}

					else {
						Toast.makeText(getApplicationContext(),
								"You have no Contacts.", 0).show();

					}
					localCursor.close();
					databaseAdapter.close();
				}

			});
			btnCancel.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					Toast.makeText(getApplicationContext(), "cancelled",
							Toast.LENGTH_LONG).show();

				}
			});
			return false;
		}
	}
}
