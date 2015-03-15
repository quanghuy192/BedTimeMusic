package com.qhuy.bedtimemusic.data;

public class Note {

	private int id;
	private String noteList;
	private String noteData;
	private boolean enable;
	private int musicId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNoteList() {
		return noteList;
	}

	public void setNoteList(String noteList) {
		this.noteList = noteList;
	}

	public String getNoteData() {
		return noteData;
	}

	public void setNoteData(String noteData) {
		this.noteData = noteData;
	}

	public boolean isEnable() {
		return enable;
	}

	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	public int getMusicId() {
		return musicId;
	}

	public void setMusicId(int musicId) {
		this.musicId = musicId;
	}

}
