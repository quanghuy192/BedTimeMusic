package com.qhuy.bedtimemusic.data;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadDataFromSdCard {
	public ArrayList<HashMap<String, String>> getList(Context context) {
		// TODO Auto-generated method stub
		
		/**
		 * 
		 * Menh de where khi tham chieu den database
		 * 
		 * */
		
		String where = new String();
		where = MediaStore.Audio.Media.IS_MUSIC + "=1";
		
		/**
		 * 
		 * List music se tra ve
		 * Chua toan bo bai hat khi lay data tu
		 * the nho
		 * 
		 * */
		
		ArrayList<HashMap<String, String>> songList = new ArrayList<HashMap<String, String>>();
		Cursor managedCursor;
		try {
			
			/**
			 * 
			 * Content provider
			 * 
			 * */
			
			managedCursor = context.getContentResolver().query(
					MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
					new String[] { MediaStore.Audio.Media.TITLE,
							MediaStore.Audio.Media.DATA,
							MediaStore.Audio.Media._ID,
							MediaStore.Audio.Media.ALBUM,
							MediaStore.Audio.Media.ALBUM_ID,
							MediaStore.Audio.Media.ARTIST,
							MediaStore.Audio.Media.ARTIST_ID }, where, null,
					MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			while (managedCursor.moveToNext()) {
				HashMap<String, String> song = new HashMap<String, String>();
				
				/**
				 * 
				 * Gan gia tri vao list 
				 * ten , duong dan , ten ca si 
				 * Neu con tro null , ban ra exception
				 * 
				 * */
				
				song.put("songTitle", managedCursor.getString(0));
				song.put("songPath", managedCursor.getString(1));
				song.put("songArtist", managedCursor.getString(5));
				songList.add(song);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return songList;
	}

}
