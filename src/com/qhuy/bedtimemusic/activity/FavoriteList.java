package com.qhuy.bedtimemusic.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qhuy.bedtimemusic.R;
import com.qhuy.bedtimemusic.data.Music;
import com.qhuy.bedtimemusic.data.MusicAccessDatabase;
import com.qhuy.bedtimemusic.service.PlayerService;

public class FavoriteList extends Fragment {

	private ListView favoriteList;
	private ArrayAdapter<Music> adapter;
	private ArrayList<HashMap<String, String>> listSong;
	private ViewHolder viewHolder;
	public static Intent playerService;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.favorite_layout, null);

		/**
		 * 
		 * Tham chieu den danh sach bai hat trong CurrentActivity , neu tham
		 * chieu null thi throw Exception
		 * 
		 * */

		MusicAccessDatabase readDatabase = new MusicAccessDatabase(
				getActivity());
		readDatabase.openToRead();
		try {

			listSong = readDatabase.getList();

		} catch (Exception e) {
			e.printStackTrace();
		}

		favoriteList = (ListView) view.findViewById(R.id.listview);
		adapter = new MyAdapter(getActivity(), R.layout.favorite_layout,
				getItems());
		favoriteList.setAdapter(adapter);
		favoriteList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				playerService = new Intent(getActivity(), PlayerService.class);
				playerService.putExtra("songIndex", position);
				playerService.putExtra("Key", 0);
				if (PlayerService.mediaPlayer == null) {
					getActivity().startService(playerService);
				} else {
					getActivity().stopService(playerService);
					getActivity().startService(playerService);
				}
				getActivity().finish();
			}
		});

		/**
		 * 
		 * Dang ki ContextMenu
		 * 
		 * */

		registerForContextMenu(favoriteList);
		readDatabase.close();
		return view;
	}

	private class MyAdapter extends ArrayAdapter<Music> {

		ArrayList<Music> arrayList;

		public MyAdapter(Context context, int resource, ArrayList<Music> objects) {
			super(context, resource, objects);
			// TODO Auto-generated constructor stub
			arrayList = objects;
		}

		/**
		 * GetView de tra lai custemView cho moi row
		 * */

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view = convertView;

			/**
			 * Gan view = convertwView Neu view == null , thi tao moi
			 * */

			if (view == null) {
				LayoutInflater layoutinflate = (LayoutInflater) getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				view = layoutinflate.inflate(R.layout.favorite_item, parent,
						false);
				viewHolder = new ViewHolder(view);
				// use viewHorder
				view.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) view.getTag();
			}
			Music music = arrayList.get(position);

			/**
			 * Set Text cho moi row
			 * */

			viewHolder.songTitle.setText(music.getSongTitle());
			viewHolder.songArtist.setText(music.getSongArtist());
			return view;
		}

	}

	/**
	 * Tra lai List Music cho ListView
	 * */

	private ArrayList<Music> getItems() {
		// TODO Auto-generated method stub
		ArrayList<Music> musicList = new ArrayList<Music>();
		for (int i = 0; i < listSong.size(); i++) {
			Music music = new Music();
			music.setSongTitle(listSong.get(i).get("songTitle"));
			music.setSongPath(listSong.get(i).get("songPath"));
			music.setSongArtist(listSong.get(i).get("songArtist"));
			music.setId(Long.valueOf(listSong.get(i).get("songId")));
			musicList.add(music);
		}
		return musicList;
	}

	class ViewHolder {
		TextView songTitle, songArtist;

		ViewHolder(View base) {
			this.songTitle = (TextView) base.findViewById(R.id.songTitle);
			this.songArtist = (TextView) base.findViewById(R.id.songArtist);
		}

	}

	/**
	 * ContextView
	 * */

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		MenuInflater menuInflate = getActivity().getMenuInflater();
		menuInflate.inflate(R.menu.favorite_context, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		@SuppressWarnings({})
		ArrayAdapter<Music> adapter = (ArrayAdapter<Music>) favoriteList
				.getAdapter();

		/**
		 * 
		 * Lay item
		 * 
		 * */

		MusicAccessDatabase writeDatabase = new MusicAccessDatabase(
				getActivity());
		writeDatabase.openToWrite();
		switch (item.getItemId()) {
		case R.id.remove: {
			Music removeMusic = adapter.getItem(menuInfo.position);
			writeDatabase.remove(removeMusic);
			adapter.remove(removeMusic);

			/**
			 * 
			 * Xoa music vao favorite list tu moi vi tri duoc adapter lay ra
			 * 
			 * */

			if (PlayerService.mediaPlayer != null
					&& PlayerService.mediaPlayer.isPlaying())
				;
			PlayerService.mediaPlayer.pause();
			Toast.makeText(getActivity(),
					removeMusic.getSongTitle() + " is removed ",
					Toast.LENGTH_SHORT).show();
			Log.i("TAG >>>>>>>>>>>> ", " REMOVE THIS SONG DONE >>>>>> ");
		}
			break;
		default:
			break;
		}

		/**
		 * 
		 * Dang ki su thay doi cho adapter
		 * 
		 * */

		adapter.notifyDataSetChanged();
		writeDatabase.close();
		return super.onContextItemSelected(item);
	}

}