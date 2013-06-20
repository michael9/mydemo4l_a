package com.cqvip.moblelib.activity;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BookLoc;

public class DetailBookActivity extends Activity implements IBookManagerActivity {

	private Book dBook;
	private TextView baseinfo,loc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_book);
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book)bundle.getSerializable("book");
		baseinfo = (TextView) findViewById(R.id.bookinfo_txt);
		loc = (TextView) findViewById(R.id.loc_txt);
		
		ManagerService.allActivity.add(this);
		if(dBook.getRecordid()!=null){
			getLocalinfo(dBook.getRecordid());
		}
		baseinfo.setText("ÌâÃû:"+dBook.getU_title()+"\n"
				+"×÷Õß"+dBook.getAuthor()+"\n"
				+"³ö°æÉç"+dBook.getU_publish()+"\n"
				+"Ë÷ÊéºÅ"+dBook.getCallno()+"\n"
				+"·ÖÀàºÅ"+dBook.getClassno()+"\n"
				+"ISBN"+dBook.getIsbn()+"\n"
				+"¼Û¸ñ"+dBook.getU_price()+"\n"
				+"¹Ø¼ü´Ê"+dBook.getSubject()+"\n"
				+"¼ò½é"+dBook.getU_abstract()+"\n"
				);
	}

	private void getLocalinfo(String recordid) {
		
		HashMap map=new HashMap();
		map.put("id", recordid);
		ManagerService.addNewTask(new Task(Task.TASK_BOOK_INFO,map));
		
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
		BookLoc book = (BookLoc)obj[0];
		if(book!=null){
			loc.setText("ÌõÂëºÅ:"+book.getBarcode()+"\n"
					+"Ë÷ÊéºÅ"+book.getCallno()+"\n"
					+"µØÖ·"+book.getLocal()+"\n"
					+"¾íÆÚ"+book.getVolume()+"\n"
					+"ÀàÐÍ"+book.getCirtype()+"\n");
		}
		
	}

}
