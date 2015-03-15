package com.qhuy.bedtimemusic.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

	// create table
	public static final String DATABASE_LIST = "listTable";
	public static final String DATABASE_NOTE_LIST_NAME = "notelist";

	private static final String DATABASE_NAME = "musicDatabase.db";

	// key
	public static final String KEY_ID = "key";
	public static final String NOTELIST_KEY_ID = "yourlistkey";

	// column
	public static final String PATH_NAME = "pathName";
	public static final String SONG_NAME = "songNametext";
	public static final String ARTIST_NAME = "artistName";
	public static final String NOTE_LIST_NAME = "noteList";
	public static final String NOTE_DATA = "note";
	public static final String NOTE_ENABLE = "Enable";

	// version
	private static final int DATABASE_VERSION = 1;

	// create list table
	private static final String CREATE_TABLE_LIST = " create table "
			+ DATABASE_LIST + " ( " + KEY_ID
			+ " integer primary key autoincrement, " + PATH_NAME
			+ " text not null, " + ARTIST_NAME + " text not null, " + SONG_NAME
			+ " text not null); ";

	private static final String CREATE_LIST_NAME = " create table "
			+ DATABASE_NOTE_LIST_NAME + " ( " + NOTELIST_KEY_ID
			+ " integer primary key autoincrement, " + NOTE_LIST_NAME
			+ " text not null " + NOTE_DATA + " text not null " + NOTE_ENABLE
			+ " text not null " + KEY_ID + " integer not null); ";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_LIST);
		db.execSQL(CREATE_LIST_NAME);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(" DROP TABLE IF EXIST " + DATABASE_LIST);
		db.execSQL(" DROP TABLE IF EXIST " + CREATE_LIST_NAME);

		// recreate
		onCreate(db);
	}

}
