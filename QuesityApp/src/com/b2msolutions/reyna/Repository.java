package com.b2msolutions.reyna;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Repository extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "reyna.db";

	public static final int DATABASE_VERSION = 1;
	
	private static final String TAG = "Repository";

	public Repository(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		Logger.v(TAG, "onCreate");
		db.execSQL("CREATE TABLE Message (id INTEGER PRIMARY KEY AUTOINCREMENT, url TEXT, body TEXT);");
		db.execSQL("CREATE TABLE Header (id INTEGER PRIMARY KEY AUTOINCREMENT, messageid INTEGER, key TEXT, value TEXT, FOREIGN KEY(messageid) REFERENCES message(id));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.v(TAG, "onUpgrade");
	}

	public void insert(Message message) {
        Logger.v(TAG, "insert");
		if (message == null)
			return;

		SQLiteDatabase db = null;
		try {
			db = this.getWritableDatabase();
			db.beginTransaction();
			ContentValues values = new ContentValues();
			values.put("url", message.getUrl());
			values.put("body", message.getBody());

			long messageid = db.insert("Message", null, values);			
			this.addHeaders(db, messageid, message.getHeaders());
			db.setTransactionSuccessful();

            Logger.i("reyna", "Repository: inserted message " + messageid);
		} finally {
			if (db != null) {
				db.endTransaction();
			}
		}
	}

	public Message getNext() throws URISyntaxException {
        Logger.v(TAG, "getNext");
		Cursor messageCursor = null;
		Cursor headersCursor = null;

		try {
			SQLiteDatabase db = this.getReadableDatabase();
			messageCursor = db.query("Message", new String[] { "id", "url",
					"body" }, null, null, null, null, "id", "1");
			if (!messageCursor.moveToFirst())
				return null;

			long messageid = messageCursor.getLong(0);
			String url = messageCursor.getString(1);
			String body = messageCursor.getString(2);

			headersCursor = db.query("Header", new String[] { "id", "key",
					"value" }, "messageid = " + messageid, null, null, null,
					null);

			ArrayList<Header> headers = new ArrayList<Header>();
			while (headersCursor.moveToNext()) {
				headers.add(new Header(headersCursor.getLong(0), headersCursor
						.getString(1), headersCursor.getString(2)));
			}

			Header[] headersForMessage = new Header[headers.size()];
			headers.toArray(headersForMessage);

			return new Message(messageid, url, body,
					(Header[]) headersForMessage);
		} finally {
			if (messageCursor != null)
				messageCursor.close();
			if (headersCursor != null)
				headersCursor.close();
		}
	}

	public void delete(Message message) {
        Logger.v(TAG, "delete");
		if (message == null)
			return;
		if (message.getId() == null)
			return;

		SQLiteDatabase db = this.getReadableDatabase();
		if (!this.doesMessageExist(db, message))
			return;

		this.deleteExistingMessage(db, message);
	}

	private void deleteExistingMessage(SQLiteDatabase db, Message message) {
        Logger.v(TAG, "deleteExistingMessage");
		db.beginTransaction();
		try {
			String[] args = new String[] { message.getId().toString() };
			db.delete("Header", "messageid = ?", args);
			db.delete("Message", "id = ?", args);
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	private boolean doesMessageExist(SQLiteDatabase db, Message message) {
        Logger.v(TAG, "doesMessageExist");
		
		Cursor cursor = null;
		try {
			cursor = db.query("Message", new String[] { "id" }, "id = ?",
					new String[] { message.getId().toString() }, null, null,
					null);
			return cursor.moveToFirst();
		} finally {
			if (cursor != null)
				cursor.close();
		}
	}

	private void addHeaders(SQLiteDatabase db, long messageid, Header[] headers) {
        Logger.v(TAG, "addHeaders");
		
		for (Header header : headers) {
			ContentValues headerValues = new ContentValues();
			headerValues.put("messageid", messageid);
			headerValues.put("key", header.getKey());
			headerValues.put("value", header.getValue());

			db.insert("Header", null, headerValues);
		}
	}
}
