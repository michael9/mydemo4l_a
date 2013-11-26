package com.cqvip.moblelib.activity;

import com.cqvip.moblelib.szy.R;
import com.cqvip.moblelib.szy.R.layout;
import com.cqvip.moblelib.szy.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MessageInfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_info);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message_info, menu);
		return true;
	}

}
