package com.cqvip.moblelib.activity;

import com.cqvip.moblelib.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class ActivityDlg extends Activity {
	
	RelativeLayout login_layout;
	EditText log_in_username,log_in_passwords;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dlg);
		
		login_layout=(RelativeLayout)findViewById(R.id.log_in_layout);
		log_in_username=(EditText)findViewById(R.id.log_in_username);
		log_in_passwords=(EditText)findViewById(R.id.log_in_passwords);
		
		switch (getIntent().getIntExtra("ACTIONID", 0)) {
		case 0:
			login_layout.setVisibility(View.VISIBLE);
			break;

		default:
			break;
		};
	}
	
	private void login()
	{
	
	}
}
