package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookLocAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BookLoc;
import com.cqvip.moblelib.view.CustomProgressDialog;

public class DetailBookActivity extends BaseActivity implements IBookManagerActivity {

	private Book dBook;
	private TextView loc,textView10;
	private ListView listview;
	private BookLocAdapter adapter;
	private Context context;
	private View title_bar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_book);
		context = this;
		listview = (ListView)findViewById(R.id.loc_list);
//		View v = LayoutInflater.from(this).inflate(R.layout.activity_detail_book, null);
//		listview.addHeaderView(v);
		
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book)bundle.getSerializable("book");
//		baseinfo = (TextView) findViewById(R.id.bookinfo_txt);
		//loc = (TextView) findViewById(R.id.loc_txt);
		textView10=(TextView)findViewById(R.id.textView10);
		customProgressDialog=CustomProgressDialog.createDialog(this);
		
		ManagerService.allActivity.add(this);
		if(dBook.getRecordid()!=null){
			getLocalinfo(dBook.getRecordid());
		}
		
		
//		baseinfo.setText("¡¶"+dBook.getU_title()+"¡·\n"
//				+getString(R.string.item_author)+dBook.getAuthor()+"\n"
//				+getString(R.string.item_publish)+dBook.getU_publish()+"\n"
//				+getString(R.string.item_callno)+dBook.getCallno()+"\n"
//				+getString(R.string.item_classno)+dBook.getClassno()+"\n"
//				+"ISBN:"+dBook.getIsbn()+"\n"
//				+getString(R.string.item_price)+dBook.getU_price()+"\n"
//				+getString(R.string.item_subject)+dBook.getSubject()+"\n"
////				+"Â¼Ã²Â½Ã©"+dBook.getU_abstract()+"\n"
//				);
		textView10.setText("¡¶"+dBook.getU_title()+"¡·\n"
				+getString(R.string.item_author)+dBook.getAuthor()+"\n"
				+getString(R.string.item_publish)+dBook.getU_publish()+"\n"
				+getString(R.string.item_subject)+dBook.getSubject()+"\n"
//				+getString(R.string.item_callno)+dBook.getCallno()+"\n"
//				+getString(R.string.item_classno)+dBook.getClassno()+"\n"
				+"ISBN:"+dBook.getIsbn()+"\n"
				+getString(R.string.item_price)+dBook.getU_price()+"\n"				
				+dBook.getU_abstract());
		
		//listview.setAdapter(adapter);
		title_bar=findViewById(R.id.head_bar);
		TextView title = (TextView)title_bar.findViewById(R.id.txt_header);
		title.setText(R.string.book_detail);
		ImageView back = (ImageView)title_bar.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void getLocalinfo(String recordid) {
		customProgressDialog.show();
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
		customProgressDialog.dismiss();
		List<BookLoc> list = (List<BookLoc>)obj[0];
		if(list!=null&&!list.isEmpty()){
			adapter = new BookLocAdapter(context,list);
			listview.setAdapter(adapter);
		}else{
			listview.setAdapter(null);
		}
		
	}

}
