package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.model.BookLoc;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.ShortBook;

public class EbookDetailActivity extends Activity implements IBookManagerActivity {

	private EBook dBook;
	private TextView author,from,type,page,title,content;
	private String download_url = null;
	private TextView share,favor,comment,download;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ebook_detail);
		
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (EBook)bundle.getSerializable("ebook");
		author = (TextView) findViewById(R.id.ebook_author_txt);
		from = (TextView) findViewById(R.id.ebook_from_txt);
		type =(TextView)findViewById(R.id.ebook_type_txt);
		page =(TextView)findViewById(R.id.ebook_page_txt);
		title =(TextView)findViewById(R.id.ebook_title_txt);
		content =(TextView)findViewById(R.id.ebook_content_abst);
		
		share =(TextView)findViewById(R.id.ebook_share_txt);
		favor =(TextView)findViewById(R.id.ebook_favorite_txt);
		comment =(TextView)findViewById(R.id.ebook_comment_txt);
		download =(TextView)findViewById(R.id.ebook_down_txt);
		
		ManagerService.allActivity.add(this);
		if(dBook.getLngid()!=null){
			getLocalinfo(dBook.getLngid());
		}
		   String author1 = getResources().getString(R.string.item_author);
		   String from1 = getResources().getString(R.string.ebook_orang);
		   String page1 =  getResources().getString(R.string.ebook_page);
		   String describe1 = getResources().getString(R.string.ebook_abstrac);
		   String type1 = getResources().getString(R.string.ebook_type);
		
		
		title.setText(dBook.getTitle_c());
		author.setText(author1+dBook.getWriter());
		from.setText(from1+dBook.getName_c()+","+dBook.getYears()+","+"第"+dBook.getNum()+"期");
		type.setText(type1+"PDF,"+dBook.getPdfsize()/1024+"KB");
		page.setText(page1+dBook.getPagecount());
		content.setText(describe1+dBook.getRemark_c());
		
		download.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(download_url!=null){
					//弹出对话框
					
					//下载
				}
				
			}
		});
		
		
	}

	private void getLocalinfo(String recordid) {
		
		HashMap map=new HashMap();
		map.put("lngid", recordid);
		ManagerService.addNewTask(new Task(Task.TASK_EBOOK_DOWN,map));
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... obj) {
		List<ShortBook> book = (List<ShortBook>)obj[0];
		if(book!=null){
			download_url = book.get(0).getDate();
			Log.i("url","download_url"+download_url);
		}
		
	}

}