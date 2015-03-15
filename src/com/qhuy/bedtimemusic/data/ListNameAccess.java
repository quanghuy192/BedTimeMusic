package com.qhuy.bedtimemusic.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ListNameAccess {

	private SQLiteDatabase database;
	private Database myData;

	public ListNameAccess(Context context) {
		// TODO Auto-generated constructor stub
		myData = new Database(context);
	}

	public void openToRead() {
		database = myData.getReadableDatabase();
	}

	public void openToWrite() {
		database = myData.getWritableDatabase();
	}

	public void close() {
		database.close();
	}

	public void addNewList(Note note) {
		ContentValues values = new ContentValues();
		values.put(Database.NOTE_LIST_NAME, note.getNoteList());
		values.put(Database.NOTE_DATA, note.getNoteData());
		values.put(Database.NOTE_ENABLE, String.valueOf(note.isEnable()));
		values.put(Database.KEY_ID, note.getMusicId());
		database.insert(Database.DATABASE_NOTE_LIST_NAME, null, values);
	}

}
