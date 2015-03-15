package com.qhuy.bedtimemusic.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.qhuy.bedtimemusic.R;
import com.qhuy.bedtimemusic.data.ListNameAccess;

public class NoteTab extends Fragment implements OnClickListener {

	private ListView yourList;
	private Button add;
	private ListNameAccess listNameAc;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		View view = inflater.inflate(R.layout.notelist, null);
		add = (Button) view.findViewById(R.id.addBtn);
		add.setOnClickListener(this);
		return view;

		/**
		 * 
		 * Tham chieu den danh sach bai hat trong CurrentActivity , neu tham
		 * chieu null thi throw Exception
		 * 
		 * */

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub

		new MyDialog(getActivity()).show();

	}
}
