package com.cqvip.moblelib.activity;


import com.cqvip.moblelib.sm.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

public class EpubShowActivity extends Activity
{
//	public BookView bookView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epub_show);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		String path=getIntent().getStringExtra("path");
		String path2=path.substring(0, path.lastIndexOf("/"));
//		bookView = (BookView) findViewById(R.id.bookView1);
//		bookView.setPath(path2);
//		bookView.initBook();
//		bookView.openShelf();
	}
}