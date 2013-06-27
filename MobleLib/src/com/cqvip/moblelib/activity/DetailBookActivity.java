package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookLocAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BookLoc;

public class DetailBookActivity extends BaseActivity implements IBookManagerActivity {

	private Book dBook;
	private TextView baseinfo,loc,textView10;
	private ListView listview;
	private BookLocAdapter adapter;
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location_book);
		context = this;
		listview = (ListView)findViewById(R.id.loc_list);
		View v = LayoutInflater.from(this).inflate(R.layout.activity_detail_book, null);
		listview.addHeaderView(v);
		
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book)bundle.getSerializable("book");
		baseinfo = (TextView) findViewById(R.id.bookinfo_txt);
		//loc = (TextView) findViewById(R.id.loc_txt);
		textView10=(TextView)findViewById(R.id.textView10);
		
		ManagerService.allActivity.add(this);
		if(dBook.getRecordid()!=null){
			getLocalinfo(dBook.getRecordid());
		}
		
		
		baseinfo.setText("¡¶"+dBook.getU_title()+"¡·\n"
				+getString(R.string.item_author)+dBook.getAuthor()+"\n"
				+getString(R.string.item_publish)+dBook.getU_publish()+"\n"
				+getString(R.string.item_callno)+dBook.getCallno()+"\n"
				+getString(R.string.item_classno)+dBook.getClassno()+"\n"
				+"ISBN:"+dBook.getIsbn()+"\n"
				+getString(R.string.item_price)+dBook.getU_price()+"\n"
				+getString(R.string.item_subject)+dBook.getSubject()+"\n"
//				+"Â¼Ã²Â½Ã©"+dBook.getU_abstract()+"\n"
				);
		textView10.setText(dBook.getU_abstract());
		
		//listview.setAdapter(adapter);
		
		
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
		List<BookLoc> list = (List<BookLoc>)obj[0];
		if(list!=null&&!list.isEmpty()){
//			BookLoc book = list.get(0);
//			loc.setText(getString(R.string.item_barcode)+book.getBarcode()+"\n"
//					+getString(R.string.item_callno)+book.getCallno()+"\n"
//					+getString(R.string.item_loc)+book.getLocal()+"\n"
//					+getString(R.string.item_volume)+book.getVolume()+"\n"
//					+getString(R.string.item_cirtype)+book.getCirtype()+"\n"
//					+getString(R.string.item_status)+book.getStatus()
//					);
			adapter = new BookLocAdapter(context,list);
			listview.setAdapter(adapter);
		}
		
	}

}
