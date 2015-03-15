package com.qhuy.bedtimemusic.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicListDatabase extends SQLiteOpenHelper {

	// create table
	public static final String DATABASE_LIST = "listTable";
	private static final String DATABASE_NAME = "musicDatabase.sqlite";

	// key
	public static final String KEY_ID = "key";

	// column
	public static final String PATH_NAME = "pathName";
	public static final String SONG_NAME = "songNametext";
	public static final String ARTIST_NAME = "artistName";

	// version
	private static final int DATABASE_VERSION = 1;

	// create list table
	private static final String CREATE_TABLE_LIST = " create table "
			+ DATABASE_LIST + " ( " + KEY_ID
			+ " integer primary key autoincrement, " + PATH_NAME
			+ " text not null, " + ARTIST_NAME + " text not null, " + SONG_NAME
			+ " text not null); ";

	public MusicListDatabase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE_LIST);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL(" DROP TABLE IF EXIST " + DATABASE_LIST);

		// recreate
		onCreate(db);
	}

}
