package com.qhuy.bedtimemusic.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.qhuy.bedtimemusic.R;

public class TabActivity extends FragmentActivity {

	private FragmentTabHost fragmentTabHost;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);



		setContentView(R.layout.main);

		fragmentTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);

		/**
		 * 
		 * Setup tab host
		 * 
		 * */

		fragmentTabHost.setup(this, getSupportFragmentManager(),
				android.R.id.tabcontent);

		/**
		 * 
		 * List tab host
		 * 
		 * */

		View listView = CreateView(fragmentTabHost.getContext(),
				R.layout.tabs_bg, R.drawable.list);
		fragmentTabHost.addTab(
				fragmentTabHost.newTabSpec("List").setIndicator(listView),
				AllListTab.class, null);

		/**
		 * 
		 * Favorite List tab host
		 * 
		 * */

		View favoriteListView = CreateView(fragmentTabHost.getContext(),
				R.layout.tabs_bg, R.drawable.heart);
		fragmentTabHost.addTab(fragmentTabHost.newTabSpec("Favorite List")
				.setIndicator(favoriteListView), FavoriteTab.class, null);
		
		/**
		 * 
		 * List favorite list 
		 * 
		 * */
		
		View listFavorite = CreateView(fragmentTabHost.getContext(),
				R.layout.tabs_bg, R.drawable.list);
		fragmentTabHost.addTab(fragmentTabHost.newTabSpec("Your List")
				.setIndicator(listFavorite), NoteTab.class, null);
		
		}
	
	/**
	 * 
	 * Phuong thuc nay tra lai view cho tab Widget
	 * Set image cho moi tab
	 * 
	 * */

	private View CreateView(Context context, int idView, int idImage) {
		View currentView = LayoutInflater.from(context).inflate(idView, null);
		ImageView currentImage = (ImageView) currentView
				.findViewById(R.id.tabsImage);
		currentImage.setImageResource(idImage);
		return currentView;
	}

}
