package com.qhuy.bedtimemusic.data;

public class Music {
	private String songTitle;
	private String songArtist;
	private String songPath;

	public Music() {
		// TODO Auto-generated constructor stub
	}

	public Music(String songTitle, String songArtist, String songPath, long id) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.songArtist = songArtist;
		this.songPath = songPath;
		this.songTitle = songTitle;
	}

	public String getSongTitle() {
		return songTitle;
	}

	public void setSongTitle(String songTitle) {
		this.songTitle = songTitle;
	}

	public String getSongArtist() {
		return songArtist;
	}

	public void setSongArtist(String songArtist) {
		this.songArtist = songArtist;
	}

	public String getSongPath() {
		return songPath;
	}

	public void setSongPath(String songPath) {
		this.songPath = songPath;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	private long id;

}
