package com.qhuy.bedtimemusic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.qhuy.bedtimemusic.R;
import com.qhuy.bedtimemusic.activity.MainActivity;
import com.qhuy.bedtimemusic.data.MusicAccessDatabase;
import com.qhuy.bedtimemusic.utilities.Utilities;

public class PlayerService extends Service implements OnClickListener,
		OnCompletionListener, OnSeekBarChangeListener {

	private ArrayList<HashMap<String, String>> songsListingSD = new ArrayList<HashMap<String, String>>();
	private int seekForwardTime = 5000;
	private int seekBackwardTime = 5000;
	private boolean mRepeat = false;
	private boolean mShuffle = false;

	public static SeekBar progress;
	public static Handler progressBarHandler = new Handler();
	public static ImageView play, next, privious, reload, shuffle, btnForward,
			btnBackward;
	public static TextView textSong, mTextTime;
	public static int currentSongIndex = -1;
	public static ArrayList<HashMap<String, String>> songsListFavorite = new ArrayList<HashMap<String, String>>();
	public static MediaPlayer mediaPlayer;
	public static TextView songCurrentDurationLabel;
	public static TextView songTotalDurationLabel;
	public static int key = 1;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mediaPlayer = new MediaPlayer();

		/**
		 * 
		 * Dang ki phuong thuc khi 1 bai nhac da chay xong De chuyen sang bai
		 * khac
		 * 
		 * */

		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.reset();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}

	// TODO Auto-generated constructor stub

	/**
	 * 
	 * Phuong thuc chay dau tien khi goi toi service
	 * 
	 * */

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		/**
		 * 
		 * Khoi tao giao diennguoi dung
		 * 
		 * */

		initUI();
		int songIndex;

		/**
		 * 
		 * Neu gia tri intent nhan ve khac null Lay gia tri vij tri bai hat duoc
		 * nguoi dung click de chay nhac
		 * 
		 * */

		if (intent == null) {

			/**
			 * 
			 * Gan mac dinh neu intent = null
			 * 
			 * */

			songIndex = 0;
		} else {
			songIndex = intent.getIntExtra("songIndex", 0);
		}

		/**
		 * 
		 * Key la de kiem tra service duoc goi tu list hay favorite Neu key bang
		 * 1 thi dc goi tu list Neu key = 0 thi dc goi tu favorite
		 * 
		 * */

		key = intent.getIntExtra("Key", 1);
		setCurrentListSong(key);

		/**
		 * 
		 * Chay bai nhac hien tai neu song index khac currentSongindex , sau khi
		 * gan
		 * 
		 * */

		if (songIndex != currentSongIndex) {
			currentSongIndex = songIndex;
			playSong(currentSongIndex);

		} else if (currentSongIndex != -1) {
			textSong.setText(songsListingSD.get(songIndex).get("songTitle"));
			playSong(currentSongIndex);
			if (mediaPlayer.isPlaying()) {
				play.setImageResource(R.drawable.ic_media_pause);
			} else {
				play.setImageResource(R.drawable.ic_media_play);
			}

		}
		initNotification(songIndex);
		/**
		 * 
		 * Se khong chay lai , khi service bi dung bat thuong
		 * 
		 * */

		return START_NOT_STICKY;
	}

	private void setCurrentListSong(int keyId) {
		// TODO Auto-generated method stub
		if (keyId == 1) {
			songsListingSD = MainActivity.songsList;
		} else {
			songsListingSD = songsListFavorite;
		}
	}

	private void playSong(int songIndex) {
		// TODO Auto-generated method stub
		try {

			mediaPlayer.reset();
			mediaPlayer.setDataSource(songsListingSD.get(songIndex).get(
					"songPath"));
			mediaPlayer.prepare();
			mediaPlayer.start();
			play.setImageResource(R.drawable.ic_media_pause);
			textSong.setText(songsListingSD.get(songIndex).get("songTitle"));
			progress.setProgress(0);
			progress.setMax(100);
			updateProgressBar();

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void updateProgressBar() {
		// TODO Auto-generated method stub

		progressBarHandler.postDelayed(mUpdateTimeTask, 100);

	}

	private void initUI() {
		// TODO Auto-generated method stub
		try {
			MusicAccessDatabase readList = new MusicAccessDatabase(this);
			readList.openToRead();

			songsListFavorite = readList.getList();
			readList.close();

			textSong = MainActivity.text;
			mTextTime = MainActivity.mSetTime;

			songCurrentDurationLabel = MainActivity.songCurrentDurationLabel;

			songTotalDurationLabel = MainActivity.songTotalDurationLabel;
			progress = MainActivity.progress;
			reload = MainActivity.reload;
			shuffle = MainActivity.shuffle;
			play = MainActivity.play;
			next = MainActivity.next;
			privious = MainActivity.privious;
			btnForward = MainActivity.btnForward;
			btnBackward = MainActivity.btnBackward;

			mTextTime.setOnClickListener(this);
			reload.setOnClickListener(this);
			shuffle.setOnClickListener(this);
			play.setOnClickListener(this);
			next.setOnClickListener(this);
			privious.setOnClickListener(this);
			btnForward.setOnClickListener(this);
			btnBackward.setOnClickListener(this);

			progress.setOnSeekBarChangeListener(this);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			Log.e("TAG", e.toString());
		}

	}

	/**
	 * 
	 * Chay bai tiep theo
	 * 
	 * */

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		if (mRepeat) {
			playSong(currentSongIndex);
		} else if (mShuffle) {
			Random rand = new Random();
			currentSongIndex = rand.nextInt((songsListingSD.size() - 1) + 1);
		} else if (currentSongIndex == songsListingSD.size() - 1)
			currentSongIndex = 0;
		else
			currentSongIndex++;
		playSong(currentSongIndex);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {

		/**
		 * 
		 * Repeat
		 * 
		 * */

		case R.id.btnRepeat: {
			if (!mRepeat) {
				Toast.makeText(getBaseContext(), " Repeat on ",
						Toast.LENGTH_SHORT).show();
				mRepeat = true;
				mShuffle = false;
				reload.setImageResource(R.drawable.btn_repeat_focused);
				shuffle.setImageResource(R.drawable.btn_shuffle);
			} else {
				mRepeat = false;
				// mShuffle = true;
				reload.setImageResource(R.drawable.btn_repeat);
			}

		}
			break;

		/**
		 * 
		 * Shuffle
		 * 
		 * */

		case R.id.btnShuffle: {
			if (!mShuffle) {
				Toast.makeText(getBaseContext(), " Shuffle on ",
						Toast.LENGTH_SHORT).show();
				mShuffle = true;
				mRepeat = false;
				shuffle.setImageResource(R.drawable.btn_shuffle_focused);
				reload.setImageResource(R.drawable.btn_repeat);
			} else {
				mShuffle = false;
				// mRepeat = true;
				shuffle.setImageResource(R.drawable.btn_shuffle);
			}

		}
			break;

		/**
		 * 
		 * Play
		 * 
		 * */

		case R.id.btn_play_imageview: {
			if (mediaPlayer.isPlaying()) {
				if (mediaPlayer != null) {
					mediaPlayer.pause();
					play.setImageResource(R.drawable.ic_media_play);
				}
			} else {
				if (mediaPlayer != null) {
					mediaPlayer.start();
					play.setImageResource(R.drawable.ic_media_pause);
				}
			}

		}

			break;

		/**
		 * 
		 * Next
		 * 
		 * */

		case R.id.btn_next_imageview: {
			onCompletion(mediaPlayer);
		}

			break;

		/**
		 * 
		 * Privious
		 * 
		 * */

		case R.id.btn_previous_imageview: {
			if (mRepeat) {
				playSong(currentSongIndex);
			} else if (mShuffle) {
				Random rand = new Random();
				currentSongIndex = rand
						.nextInt((songsListingSD.size() - 1) + 1);
			} else if (currentSongIndex == songsListingSD.size() - 1)
				currentSongIndex = 0;
			else
				currentSongIndex--;
			playSong(currentSongIndex);

		}

			break;

		/**
		 * 
		 * Forward
		 * 
		 * */

		case R.id.btn_forward_imageview: {

			int currentPosition = mediaPlayer.getCurrentPosition();

			if (currentPosition + seekForwardTime <= mediaPlayer.getDuration()) {

				mediaPlayer.seekTo(currentPosition + seekForwardTime);
			} else {

				mediaPlayer.seekTo(mediaPlayer.getDuration());
			}
		}

			break;

		/**
		 * 
		 * Backward
		 * 
		 * */

		case R.id.btn_backward_imagview: {

			int currentPosition2 = mediaPlayer.getCurrentPosition();

			if (currentPosition2 - seekBackwardTime >= 0) {

				mediaPlayer.seekTo(currentPosition2 - seekBackwardTime);

			} else {

				mediaPlayer.seekTo(0);
			}
		}

			break;

		default:
			break;
		}
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 
	 * Phuong thuc run chay ngam de update seekbar
	 * 
	 * */

	private static Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = 0;
			try {

				// Lay gia tri thoi gian cua ca bai hat
				totalDuration = mediaPlayer.getDuration();

			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			long currentDuration = 0;
			try {

				// Lay gia tri thoi gian hien tai
				currentDuration = mediaPlayer.getCurrentPosition();

			} catch (IllegalStateException e) {
				e.printStackTrace();
			}

			// Displaying Total Duration time
			songTotalDurationLabel.setText(""
					+ Utilities.milliSecondsToTimer(totalDuration));

			Log.i("Total>>>>>", Utilities.milliSecondsToTimer(currentDuration));

			// Displaying time completed playing
			songCurrentDurationLabel.setText(""
					+ Utilities.milliSecondsToTimer(currentDuration));

			Log.i("Current>>>>>",
					Utilities.milliSecondsToTimer(currentDuration));

			// Updating progress bar
			int mProgress = (int) (Utilities.getProgressPercentage(
					currentDuration, totalDuration));

			progress.setProgress(mProgress);

			Log.d("percent", String.valueOf(mProgress));
			Log.d("total", String.valueOf(songCurrentDurationLabel));
			Log.d("curent", String.valueOf(songTotalDurationLabel));
			// Running this thread after 100 milliseconds
			progressBarHandler.postDelayed(this, 100);

		}
	};

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		progressBarHandler.removeCallbacks(mUpdateTimeTask);
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		progressBarHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mediaPlayer.getDuration();
		int currentPosition = Utilities.progressToTimer(seekBar.getProgress(),
				totalDuration);

		// forward or backward to certain seconds
		mediaPlayer.seekTo(currentPosition);

		// update timer progress again
		updateProgressBar();
	}

	public void onDestroy() {
		super.onDestroy();
		cancelService();
	}

	public static void cancelService() {
		currentSongIndex = -1;
		// Remove progress bar update Hanlder callBacks
		progressBarHandler.removeCallbacks(mUpdateTimeTask);
		Log.d("Player Service", "Player Service Stopped");

		if (mediaPlayer != null) {
			if (mediaPlayer.isPlaying()) {
				mediaPlayer.stop();
			}
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	// --------------------Push Notification
	// Set up the notification ID
	public static final int NOTIFICATION_ID = 1;
	private NotificationManager mNotificationManager;

	// Create Notification
	private void initNotification(int songIndex) {
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.ic_media_play;
		CharSequence tickerText = "Music";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		notification.flags = Notification.FLAG_ONGOING_EVENT;
		Context context = getApplicationContext();
		CharSequence songName = songsListingSD.get(songIndex).get("songTitle");

		Intent notificationIntent = new Intent(this, MainActivity.class);
		/*
		 * PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
		 * notificationIntent, 0);
		 */
		notification.setLatestEventInfo(context, songName, null, null);
		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}

}
