package com.qhuy.bedtimemusic.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.qhuy.bedtimemusic.R;
import com.qhuy.bedtimemusic.data.ListNameAccess;
import com.qhuy.bedtimemusic.data.Note;

public class MyDialog extends Dialog implements OnClickListener {
	private Context context;
	private EditText editText;
	private Button positiveBtn, negativeBtn;
	private ImageButton mp3;
	private ListNameAccess listNameAc;

	public MyDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog);
		editText = (EditText) findViewById(R.id.addname);
		positiveBtn = (Button) findViewById(R.id.positive);
		negativeBtn = (Button) findViewById(R.id.negative);
		mp3 = (ImageButton) findViewById(R.id.mp3Icon);
		positiveBtn.setOnClickListener(this);
		negativeBtn.setOnClickListener(this);
		mp3.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.positive: {
			addNewListname(editText.getText().toString());
			Log.d("EDIT>>>>>", editText.getText().toString());
			dismiss();
		}

			break;
		case R.id.negative: {
			dismiss();
		}

			break;
		default:
			break;
		}
	}

	private void addNewListname(String string) {
		// TODO Auto-generated method stub
		Note note = new Note();
		listNameAc = new ListNameAccess(context);
		listNameAc.openToWrite();
		listNameAc.addNewList(note);
		listNameAc.close();
	}

}
