package com.qhuy.bedtimemusic.activity;

import android.app.Activity;
import android.os.Bundle;

import com.qhuy.bedtimemusic.R;

public class ListActivity extends Activity {
	
	/**
	 * 
	 * Activity chi hien thi fragment tu xml
	 * fragment chua cac tag host
	 * 
	 * */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_list);
		
	}

}
