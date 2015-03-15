package com.qhuy.bedtimemusic.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class MusicAccessDatabase {

	private MusicListDatabase dataList;
	private SQLiteDatabase dataBase;
	private String[] listColumn = { MusicListDatabase.KEY_ID, MusicListDatabase.PATH_NAME,
			MusicListDatabase.SONG_NAME, MusicListDatabase.ARTIST_NAME };

	public static ArrayList<HashMap<String, String>> songListDB;

	// Constructor
	public MusicAccessDatabase(Context context) {
		// TODO Auto-generated constructor stub
		dataList = new MusicListDatabase(context);
	}

	public void openToRead() {
		try {
			dataBase = dataList.getReadableDatabase();
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void openToWrite() {
		try {
			dataBase = dataList.getWritableDatabase();		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	public void close() {
		dataBase.close();
	}

	// Get list favorite
	public ArrayList<HashMap<String, String>> getList() {
		songListDB = new ArrayList<HashMap<String, String>>();
		Cursor listCursor = dataBase.query(MusicListDatabase.DATABASE_LIST, listColumn,
				null, null, null, null, null);
		listCursor.moveToFirst();

		/**
		 * 
		 * Neu con tro khac null
		 * 
		 * */

		while (!listCursor.isAfterLast()) {
			HashMap<String, String> list = new HashMap<String, String>();
			list.put("songId", String.valueOf(listCursor.getLong(0)));
			Log.i("TAG", String.valueOf(listCursor.getLong(0)));
			list.put("songTitle", listCursor.getString(2));
			Log.i("TAG", listCursor.getString(2));
			list.put("songPath", listCursor.getString(1));
			Log.i("TAG", listCursor.getString(1));
			list.put("songArtist", listCursor.getString(3));
			Log.i("TAG", listCursor.getString(3));
			songListDB.add(list);
			listCursor.moveToNext();
		}
		listCursor.close();

		/**
		 * 
		 * Tra lai list music tu database
		 * 
		 * */

		return songListDB;

	}

	public void add(Music currentMusic) {

		/**
		 * 
		 * Them gia tri vao bang contenvalue
		 * 
		 * */

		ContentValues contentValue = new ContentValues();

		/**
		 * 
		 * Log
		 * 
		 * */

		contentValue.put(MusicListDatabase.PATH_NAME, currentMusic.getSongPath());
		Log.i("TAG", currentMusic.getSongPath());
		contentValue.put(MusicListDatabase.SONG_NAME, currentMusic.getSongTitle());
		Log.i("TAG", currentMusic.getSongTitle());
		contentValue.put(MusicListDatabase.ARTIST_NAME, currentMusic.getSongArtist());
		Log.i("TAG", currentMusic.getSongArtist());

		/**
		 * 
		 * Insert vao database
		 * 
		 * */

		long id = dataBase.insert(MusicListDatabase.DATABASE_LIST, null, contentValue);
		Cursor cursor = dataBase.query(MusicListDatabase.DATABASE_LIST, listColumn,
				MusicListDatabase.KEY_ID + "=" + id, null, null, null, null);
		cursor.moveToFirst();
		Log.i("TAG", String.valueOf(cursor.getLong(0)));
		Log.i("TAG", cursor.getString(1));
		Log.i("TAG", cursor.getString(2));
		Log.i("TAG", cursor.getString(3));
		dataBase.close();

	}

	/**
	 * 
	 * Remove song
	 * 
	 * */

	public void remove(Music currentMusic) {
		long id = currentMusic.getId();
		dataBase.delete(MusicListDatabase.DATABASE_LIST, MusicListDatabase.KEY_ID + "=" + id,
				null);
	}

	/**
	 * 
	 * Remove all
	 * 
	 * */

	public void removeAll() {
		dataBase.delete(MusicListDatabase.DATABASE_LIST, null, null);
	}

}
