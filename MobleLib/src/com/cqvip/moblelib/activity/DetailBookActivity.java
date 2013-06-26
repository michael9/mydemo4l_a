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

public class DetailBookActivity extends BaseActivity implements IBookManagerActivity {

	private Book dBook;
	private TextView baseinfo,loc,textView10;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_book);
		
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book)bundle.getSerializable("book");
		baseinfo = (TextView) findViewById(R.id.bookinfo_txt);
		loc = (TextView) findViewById(R.id.loc_txt);
		textView10=(TextView)findViewById(R.id.textView10);
		
		ManagerService.allActivity.add(this);
		if(dBook.getRecordid()!=null){
			getLocalinfo(dBook.getRecordid());
		}
		
		
		baseinfo.setText("°∂"+dBook.getU_title()+"°∑\n"
				+"◊˜’ﬂ:"+dBook.getAuthor()+"\n"
				+"≥ˆ∞Ê…Á:"+dBook.getU_publish()+"\n"
				+""+dBook.getCallno()+"\n"
				+""+dBook.getClassno()+"\n"
				+"ISBN:"+dBook.getIsbn()+"\n"
				+"º€∏Ò:"+dBook.getU_price()+"\n"
				+""+dBook.getSubject()+"\n"
//				+"¬º√≤¬Ω√©"+dBook.getU_abstract()+"\n"
				);
		textView10.setText(dBook.getU_abstract());
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
			loc.setText("√å√µ√Ç√´¬∫√Ö:"+book.getBarcode()+"\n"
					+"√ã√∑√ä√©¬∫√Ö"+book.getCallno()+"\n"
					+"¬µ√ò√ñ¬∑"+book.getLocal()+"\n"
					+"¬æ√≠√Ü√ö"+book.getVolume()+"\n"
					+"√Ä√†√ê√ç"+book.getCirtype()+"\n"
					+getString(R.string.item_status)+book.getStatus()
					);
		}
		
	}

	@Override
	public void onError() {
//		if(progressDialog!=null&&iserror&&progressDialog.isShowing()){
//			progressDialog.dismiss();
//		}
	}
}
