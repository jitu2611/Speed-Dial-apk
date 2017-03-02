package com.example.speeddial;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class Database extends ContentProvider {

	public static String Providername = "com.example.speeddial.jitesh";
	public static String url = "content://" + Providername + "/contact";
	public static Uri Contenturi = Uri.parse(url);

	interface contact {
		String _ID = "_id";
		String NAME = "name";
		String PHONE = "phone";
		String IMAGE = "image";
	}

	HashMap<String, String> pro;

	static final int all = 1;
	static final int spe = 2;

	static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(Providername, "contact", all);
		uriMatcher.addURI(Providername, "contact/#", spe);
	}

	private SQLiteDatabase db;
	static final String DATABASE_NAME = "jitesh";
	static final String STUDENTS_TABLE_NAME = "contact";
	static final int DATABASE_VERSION =2;

	static final String CreateTable = "CREATE TABLE "
			+ STUDENTS_TABLE_NAME
			+ "( _id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL , phone TEXT NOT NULL , image TEXT NOT NULL ) ;";

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CreateTable);
		}

		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
			// TODO Auto-generated method stub
			int version = arg1;
			if (version == 1) {
				version = 2;

			}
			if (version != DATABASE_VERSION) {
				arg0.execSQL("DROP TABLE IF EXISTS " + STUDENTS_TABLE_NAME);
				onCreate(arg0);
			}
		}

		
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case all:
			count = db.delete(STUDENTS_TABLE_NAME, selection, selectionArgs);
			break;
		case spe:
			String id = uri.getPathSegments().get(1);
			count = db.delete(STUDENTS_TABLE_NAME, contact._ID
					+ " = "
					+ id
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
							+ ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		/** * Add a new student record */
		long rowID = db.insert(STUDENTS_TABLE_NAME, "", values);
		/** * If record is added successfully */
		if (rowID > 0) {
			Uri _uri = ContentUris.withAppendedId(Contenturi, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		throw new SQLException("Failed to add a record into " + uri);
	}

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		/**
		 * * Create a write able database which will trigger its * creation if
		 * it doesn't already exist.
		 */
		db = dbHelper.getWritableDatabase();
		return (db == null) ? false : true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(STUDENTS_TABLE_NAME);
		switch (uriMatcher.match(uri)) {
		case all:
			qb.setProjectionMap(pro);
			break;

		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}

		Cursor c = qb.query(db, projection, selection, selectionArgs, null,
				null, null);
		/** * register to watch a content URI for changes */
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case all:
			count = db.update(STUDENTS_TABLE_NAME, values, selection,
					selectionArgs);
			break;
		case spe:
			count = db.update(
					STUDENTS_TABLE_NAME,
					values,
					Database.contact._ID
							+ " = "
							+ uri.getPathSegments().get(1)
							+ (!TextUtils.isEmpty(selection) ? " AND ("
									+ selection + ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
