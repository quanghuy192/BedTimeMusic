package com.qhuy.bedtimemusic.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.qhuy.bedtimemusic.R;
import com.qhuy.bedtimemusic.data.LoadDataFromSdCard;
import com.qhuy.bedtimemusic.service.PlayerService;
import com.qhuy.bedtimemusic.utilities.CountDownClock;

public class CurrentActivity extends Activity implements
		android.view.View.OnClickListener {

	public static ImageView play, next, privious, clock, reload, shuffle, list,
			btnForward, btnBackward;
	public static ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	public static TextView text, mSetTime, songCurrentDurationLabel,
			songTotalDurationLabel;
	public static SeekBar progress;

	private final static long ONE_SECOND = 1000;
	private Spinner mSpinner;
	private String[] ArrayTime;
	private Intent intent;
	private CountDownTimer countDownTime;
	private long timeOff = 1;
	private PendingIntent pendingIntent;
	private BroadcastReceiver boardCastReceive;
	private AlarmManager alarmManager;
	private CountDownClock currentSeconds;
	private TelephonyManager telephonyManager;
	private MyPhoneState myPhone = new MyPhoneState();
	private boolean turnClock = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.layout_main);
		startUI();

		telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		telephonyManager.listen(myPhone, PhoneStateListener.LISTEN_CALL_STATE);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, ArrayTime);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(adapter);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			/*
			 * Dat thoi gian hen gio mac dinh de chay nguoc
			 */

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (ArrayTime[position].toString().equals("15 minutes")) {
					mSetTime.setText("00:15:00");
					timeOff = ONE_SECOND * 15 * 60;
				} else if (ArrayTime[position].toString().equals("30 minutes")) {
					mSetTime.setText("00:30:00");
					timeOff = ONE_SECOND * 30 * 60;
				} else if (ArrayTime[position].toString().equals("45 minutes")) {
					mSetTime.setText("00:45:00");
					timeOff = ONE_SECOND * 45 * 60;
				} else if (ArrayTime[position].toString().equals("60 minutes")) {
					mSetTime.setText("01:00:00");
					timeOff = ONE_SECOND * 60 * 60;
				} else
					mSetTime.setText("00:00:00");

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				mSetTime.setText("00:00:00");
			}
		});
	}

	/*
	 * Khoi tao giao dien
	 */

	private void startUI() {
		// TODO Auto-generated method stub

		ArrayTime = getResources().getStringArray(R.array.set_time);
		text = (TextView) findViewById(R.id.textSong);
		mSpinner = (Spinner) findViewById(R.id.spinner);
		mSetTime = (TextView) findViewById(R.id.set_time);

		songCurrentDurationLabel = (TextView) findViewById(R.id.current_time_txt);
		songTotalDurationLabel = (TextView) findViewById(R.id.total_time_txt);
		progress = (SeekBar) findViewById(R.id.progress);
		clock = (ImageView) findViewById(R.id.clock);
		reload = (ImageView) findViewById(R.id.btnRepeat);
		shuffle = (ImageView) findViewById(R.id.btnShuffle);
		play = (ImageView) findViewById(R.id.btn_play_imageview);
		next = (ImageView) findViewById(R.id.btn_next_imageview);
		btnForward = (ImageView) findViewById(R.id.btn_forward_imageview);
		btnBackward = (ImageView) findViewById(R.id.btn_backward_imagview);
		privious = (ImageView) findViewById(R.id.btn_previous_imageview);
		list = (ImageView) findViewById(R.id.list);

		clock.setOnClickListener(this);
		reload.setOnClickListener(this);
		shuffle.setOnClickListener(this);
		play.setOnClickListener(this);
		next.setOnClickListener(this);
		privious.setOnClickListener(this);
		btnForward.setOnClickListener(this);
		btnBackward.setOnClickListener(this);
		list.setOnClickListener(this);

		/*
		 * Lay danh sach cac bai hat Dua ra cac exception
		 */

		try {

			LoadDataFromSdCard loadAll = new LoadDataFromSdCard();
			songsList = loadAll.getList(this);

		} catch (RuntimeException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		if (alarmManager != null) {
			alarmManager.cancel(pendingIntent);
			unregisterReceiver(boardCastReceive);
			turnClock = false;
			countDownTime.cancel();
		}
		cancelNotification();

		if (CurrentListSong.playerService != null) {
			stopService(CurrentListSong.playerService);
		} else {
			stopService(FavoriteList.playerService);
		}
	}

	public void cancelNotification() {

		// Tao 1 string chua thong bao service xung quanh
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		// Tao 1 doi tuong notification lay tham so la doi tuong service
		Log.i("CANCEL>>>>>>>>>>>>>>", "CANCEL");
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(notificationServiceStr);
		// tat thong bao notification
		mNotificationManager.cancel(PlayerService.NOTIFICATION_ID);
	}

	@Override
	public void onClick(View currentView) {
		// TODO Auto-generated method stub
		switch (currentView.getId()) {
		case R.id.list: {

			// Neu danh sach khac null , thi list button duwowc click
			// Neu list null thi dua ra Toast messenger

			if (songsList.size() > 0) {
				intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			} else {
				Toast.makeText(this, "Playlist is empty, please check! !!!",
						Toast.LENGTH_SHORT).show();
			}

		}
			break;
		case R.id.clock: {

			// Neu nguoi dung chua hen gio thi se dua ra
			// Toast messenger canh bao
			// Neu nguoi dung da hen hio de tat nhac thi
			// bat dau dem nguoc thoi gian

			if (timeOff != 1 && !turnClock) {
				turnClock = true;
				Log.i("TAG______________________", "Clock is clicked");
				Toast.makeText(
						this,
						" Arter " + timeOff / (60 * 1000)
								+ " minutes , the music is going to be off !",
						Toast.LENGTH_LONG).show();
				currentSeconds = new CountDownClock();

				countDownTime = new CountDownTimer(timeOff, 1000) {

					// Phuong thuc nay se dua ra thoi gian con lai
					// se hien thi tren man hinh
					// Tham so dau vao de tinh toan thoi gian la
					// 1 millionseconds

					public void onTick(long millisUntilFinished) {
						mSetTime.setText("00:"
								+ currentSeconds
										.getMinutes_clock(millisUntilFinished)
								+ ":"
								+ currentSeconds
										.getSeconds_clock(millisUntilFinished));
					}

					// Phuong thuc nay se duoc goi ra
					// sau khi qua trinh dem nguoc ket thuc
					// Dua ra man hinh 1 Toast messenger

					public void onFinish() {
						mSetTime.setText("00:00:00");
						turnClock = false;
					}
				};
				countDownTime.start();
				boardCastReceive = new BroadcastReceiver() {

					/*
					 * Dang ki 1 Boaadcast Received de nhan Intent khi hen gio
					 * tat nhac Dua ra 1 Log khi qua tirnh ket thuc Service nhac
					 * chay ngam se duoc dung Neu Alarm Manager khac null thi se
					 * huy dang ki Alarm va BoardCast Received
					 */

					@Override
					public void onReceive(Context context, Intent intent) {
						// TODO Auto-generated method stub
						Log.i("TAG______________________",
								"Alarm has received !!!");
						if (PlayerService.mediaPlayer != null
								&& PlayerService.mediaPlayer.isPlaying()) {
							PlayerService.mediaPlayer.pause();
							play.setImageResource(R.drawable.ic_media_play);
						}
						if (alarmManager != null) {
							alarmManager.cancel(pendingIntent);
							unregisterReceiver(boardCastReceive);
						}
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(
								context);
						alertDialog
								.setTitle(" Notification ")
								.setMessage(" Have a good time !")
								.setNeutralButton("OK",
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												// TODO Auto-generated method
												// stub
												dialog.cancel();
											}
										});

						alertDialog.show();
					}

				};

				registerReceiver(boardCastReceive, new IntentFilter(
						"com.setTimeDown"));

				pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(
						"com.setTimeDown"), 0);

				alarmManager = (AlarmManager) (this
						.getSystemService(Context.ALARM_SERVICE));
				alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
						SystemClock.elapsedRealtime() + timeOff, pendingIntent);
			} else if (!turnClock) {
				Toast.makeText(this, " Set time first !", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(this, " Alarm is working !!! !",
						Toast.LENGTH_SHORT).show();
			}

		}
			break;
		default:
			break;
		}
	}

	/*
	 * OnPause huy dang ki trang thai neu co cuoc goi den
	 */

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (telephonyManager != null)
			telephonyManager.listen(myPhone, PhoneStateListener.LISTEN_NONE);
	}

	/*
	 * OnResume Dang ki nhan trang thai neu co cuoc goi den
	 */

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (telephonyManager != null)
			telephonyManager.listen(myPhone,
					PhoneStateListener.LISTEN_CALL_STATE);
	}

	class MyPhoneState extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {

			/*
			 * Tat nhac neu co cuoc goi den
			 */

			case TelephonyManager.CALL_STATE_RINGING:
				if (PlayerService.mediaPlayer != null
						&& PlayerService.mediaPlayer.isPlaying()) {
					PlayerService.mediaPlayer.pause();
				}
				break;

			/*
			 * Bat nhac sau khi ket thuc cuoc goi
			 */

			case TelephonyManager.CALL_STATE_IDLE:

				if (state != TelephonyManager.CALL_STATE_RINGING
						&& state != TelephonyManager.CALL_STATE_OFFHOOK) {
					Log.e("ERORR____>>>>>>>>", " KHONG RUNG , KHONG MAY BAN ");
					if (PlayerService.mediaPlayer != null && !PlayerService.mediaPlayer.isPlaying()) {
							Log.e("ERORR____>>>>>>>>",
									" KHONG KIEM TRA DUOC MEDIA DANG CHAY ");
							PlayerService.mediaPlayer.start();
						}
					}
				
				/*
				 * Tat nhac khi quay so
				 */
				
				break ;

			case TelephonyManager.CALL_STATE_OFFHOOK:

			default:
				break;
			}
		}
	}

}
