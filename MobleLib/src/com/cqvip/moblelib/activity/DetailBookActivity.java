package com.cqvip.moblelib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.model.Book;

public class DetailBookActivity extends Activity implements IBookManagerActivity {

	private Book dBook;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_book);
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book)bundle.getSerializable("book");
		
		if(dBook.getRecordid()!=null){
			getLocalinfo(dBook.getRecordid());
		}
	}

	private void getLocalinfo(String recordid) {
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_book, menu);
		return true;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... obj) {
		// TODO Auto-generated method stub
		
	}

}
