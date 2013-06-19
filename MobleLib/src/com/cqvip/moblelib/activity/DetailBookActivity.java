package com.cqvip.moblelib.activity;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.R.layout;
import com.cqvip.moblelib.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class DetailBookActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_book);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_book, menu);
		return true;
	}

}
