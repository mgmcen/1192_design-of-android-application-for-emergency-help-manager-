package com.ehm.ws;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseAdapter {
	static final String DATABASE_NAME = "PhoneNo.db";
	static final int DATABASE_VERSION = 1;

	public static final int NAME_COLUMN = 1;
	// private static String TABLE = "PhoneNo";
	// TODO: Create public field for each column in your table.
	// SQL Statement to create a new database.

	static final String DATABASE_CREATE = "create table " + "PhoneNo" + "( "
			+ "ID" + " integer primary key autoincrement,"
			+ "name varchar not null," + "number varchar not null);";

	// Variable to hold the database instance
	public SQLiteDatabase db;
	// Context of the application using the database.
	private final Context context;
	// Database open/upgrade helper
	private DataBaseHelper dbHelper;
	public static String RID = "ID";

	public DatabaseAdapter(Context _context) {
		this.context = _context;
		dbHelper = new DataBaseHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public DatabaseAdapter open() throws SQLException {
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		db.close();
	}

	public SQLiteDatabase getDatabaseInstance() {
		return db;
	}

	public void insertEntry(String name, String number) {
		ContentValues newValues = new ContentValues();
		// Assign values for each row.

		newValues.put("Name", name);
		newValues.put("Number", number.trim());

		// Insert the row into your table
		db.insert("phoneNo", null, newValues);

	}

	public Cursor getAllContacts() {
		return db.query("phoneNo", new String[] { "ID", "Name", "Number" },
				null, null, null, null, null);
	}

	public String getSinlgeEntry(String name, String number) {
		Cursor cursor = db.query("PhoneNo", null, " id=?", new String[] { name,
				number }, null, null, null);
		if (cursor.getCount() < 1) {
			cursor.close();
			return "Not Exit";
		}
		cursor.moveToFirst();
		String no1 = cursor.getString(cursor.getColumnIndex("number"));
		cursor.close();
		db.close();
		return no1;

	}

	// ---deletes a particular contact---
	public boolean deleteContact(String name) {
		
		db.execSQL("DELETE FROM PhoneNo WHERE name = '" + name + "' ;");
		return true;
	}

	public Cursor selectContact() {
		return db.query("PhoneNo", new String[] { "ID", "Name", "Number" },
				null, null, null, null, null);

	}
	public Cursor selectContact1(String str11,String str22) {
		return db.rawQuery("SELECT COUNT(ID) FROM PhoneNo WHERE name = '"
				+ str11 + "' AND number = '" + str22 + "' ", null);

	}
	public Cursor limitContact() {
		return db.rawQuery("SELECT COUNT(ID) FROM PhoneNo", null);

	}

}
