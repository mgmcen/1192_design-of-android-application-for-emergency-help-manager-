package com.ehm.ws;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EmergencyHelpManager1Activity extends Activity {
	/** Called when the activity is first created. */
	static List<String> name = new ArrayList<String>();
	static List<ImageView> icon = new ArrayList<ImageView>();
	MyAdapter ma;
	ListView lv;
	TextView tv1;
	Integer[] imageIDs = { R.drawable.contacts, R.drawable.location,
			R.drawable.viewphone, R.drawable.settings, R.drawable.help,
			R.drawable.about };

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainpage);
		lv = (ListView) findViewById(R.id.lv);
		ma = new MyAdapter();
		lv.setAdapter(ma);
		lv.setItemsCanFocus(false);

		name.add("Contacts");
		name.add("View Location Info");
		name.add("View Phone Numbers");
		name.add("Setting");
		name.add("Help");
		name.add("About");
	}

	public class MyAdapter extends BaseAdapter {
		LayoutInflater mInflater;

		MyAdapter() {
			mInflater = (LayoutInflater) EmergencyHelpManager1Activity.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		public int getCount() {
			// TODO Auto-generated method stub
			return name.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(final int position, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub

			View vi = convertView;
			if (convertView == null)
				vi = mInflater.inflate(R.layout.inflater_for_mainpage, null);
			tv1 = (TextView) vi.findViewById(R.id.textView1);

			tv1.setText(name.get(position));
			tv1.setTextColor(Color.BLACK);
			tv1.setClickable(true);
			ImageView imageView = (ImageView) vi.findViewById(R.id.imageView1);
			imageView.setImageResource(imageIDs[position]);
			lv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View vi,
						int rowid, long id) {
					// TODO Auto-generated method stub

					switch (rowid) {
					case 0:
						Intent intent = new Intent(getBaseContext(),
								AddContacts.class);
						startActivity(intent);
						break;
					case 1:
						Intent intent1 = new Intent(getBaseContext(),
								ViewLocation.class);
						startActivity(intent1);

						break;
					case 2:
						Intent intent11 = new Intent(getBaseContext(),
								ViewPhoneNo.class);
						startActivity(intent11);

						break;
					case 3:
						Intent intent111 = new Intent(getBaseContext(),
								Setting.class);
						startActivity(intent111);

						break;
					case 4:
						final Dialog dialog = new Dialog(
								EmergencyHelpManager1Activity.this);
						dialog.setContentView(R.layout.help);

						dialog.setTitle("Help");
						TextView h = (TextView) dialog
								.findViewById(R.id.textView1);
						h.setTextColor(Color.BLACK);
						h.setText("How to use this app\n"
								+ "Step 1: Adding Contacts\n"
								+ "Step 2: Settings\n"
								+ "In settings, you can choose servises 1.Power Button.\n2.Shake the mobile.\n"
								+ "Step 3: View Contacts\n"
								+ "Here you can see which people added in your app as helper and you can delete these contacts by long pressing on particular contact those you want to delete.\n"
								+ "Step 4: When you are in trouble you can press power button more than or equal to four times in 5 sec. to send an alert SMS to helper. And also you can shake the mobile for send SMS.\n"
								+ "to more details you can visit:http://somnathmule.blogspot.com/2013/12/help-for-emergency.html"); 
						
						h.setClickable(true);
						h.setFocusable(true);
						h.setLinkTextColor(Color.BLUE);
						h.setLinksClickable(true);
						dialog.show();

						break;
					case 5:
						final Dialog dialog1 = new Dialog(
								EmergencyHelpManager1Activity.this);
						dialog1.setContentView(R.layout.about);
						dialog1.setTitle("About");
						EditText ed = (EditText) dialog1
								.findViewById(R.id.editText1);
						ed.setText("About\n"
								+ "Emergency Help Manager\n"
								+ "team members:\nSomnath Mule :\nsnmule31@gmail.com\n"
								+ "Nagesh Dahibhate :\nnageshgd08@gmail.com\n"
								+ "");
						ed.setClickable(false);
						ed.setKeyListener(null);
						ed.setEnabled(false);
						dialog1.show();
						break;
					default:
						Toast.makeText(getApplicationContext(),
								"No any option select", Toast.LENGTH_LONG)
								.show();
						break;
					}

				}
			});
			return vi;
		}
	}

	public void onBackPressed() {
		Toast.makeText(this, "Back disabled. Use Home button.", 1).show();
	}

}