package com.qhuy.bedtimemusic.utilities;

public class CountDownClock {

	String minutes_Clock = "";

	String seconds_Clock = "";

	public CountDownClock() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * 
	 * Tra lai thoi gian hien tai tinh bang phut
	 * Neu gia tri > 10 thi tra lai thoi gian thuc
	 * Neu gia tri nho hon 10 thi them 0 vao dau
	 * Ep kieu sang Int
	 * 
	 * */

	public String getMinutes_clock(long millisUntilFinished) {
		int minutes = (int) (millisUntilFinished % (1000 * 60 * 60))
				/ (1000 * 60);
		if (minutes < 10) {
			minutes_Clock = "0" + minutes;
		} else {
			minutes_Clock = "" + minutes;
		}
		return minutes_Clock;
	}

	public void setMinutes_clock(String minutes_clock) {
		this.minutes_Clock = minutes_clock;
	}
	
	/**
	 * 
	 * Tra lai thoi gian hien tai tinh bang giay
	 * Neu gia tri > 10 thi tra lai thoi gian thuc
	 * Neu gia tri nho hon 10 thi them 0 vao dau
	 * Ep kieu sang Int
	 * 
	 * */

	public String getSeconds_clock(long millisUntilFinished) {
		int seconds = (int) ((millisUntilFinished % (1000 * 60 * 60))
				% (1000 * 60) / 1000);
		if (seconds < 10) {
			seconds_Clock = "0" + seconds;
		} else {
			seconds_Clock = "" + seconds;
		}
		return seconds_Clock;
	}

	public void setSeconds_clock(String seconds_clock) {
		this.seconds_Clock = seconds_clock;
	}

	long curentSeconds;

	public CountDownClock(long seconds) {
		// TODO Auto-generated constructor stub
		curentSeconds = seconds;
	}

}
