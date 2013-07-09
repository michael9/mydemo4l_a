package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BookLoc;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

public class DetailBookActivity extends BaseActivity implements IBookManagerActivity {
	public static final int GETBOOKINFO = 1;
	public static final int FAVOR = 2;
	
	private Book dBook;
	private TextView booktitle_tv,textView10,textView11;
	private ListView listview;
	private BookLocAdapter adapter;
	private Context context;
	private View title_bar;
	private ImageView imgview;
	private Button btn_item_result_search_collect,btn_item_result_search_share,btn_item_result_search_buzz;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_book);
		context = this;
		listview = (ListView)findViewById(R.id.loc_list);
//		View v = LayoutInflater.from(this).inflate(R.layout.activity_detail_book, null);
//		listview.addHeaderView(v);
		imgview = (ImageView) findViewById(R.id.book_big_img);
		//设置图片
		imgview.setBackgroundResource(R.drawable.defaut_book);
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book)bundle.getSerializable("book");
		booktitle_tv=(TextView)findViewById(R.id.booktitle_tv);
		textView10=(TextView)findViewById(R.id.textView10);
		textView11=(TextView)findViewById(R.id.textView11);
		
		ManagerService.allActivity.add(this);
		if(dBook.getRecordid()!=null){
			getLocalinfo(dBook.getRecordid());
		}
		
		booktitle_tv.setText(dBook.getU_title());
		textView10.setText(
				getString(R.string.item_author)+dBook.getAuthor()+"\n"
				+getString(R.string.item_publish)+dBook.getU_publish()+"\n"
				+getString(R.string.item_subject)+dBook.getSubject()+"\n"
//				+getString(R.string.item_callno)+dBook.getCallno()+"\n"
//				+getString(R.string.item_classno)+dBook.getClassno()+"\n"
				+"ISBN:"+dBook.getIsbn()+"\n"
				+getString(R.string.item_price)+dBook.getU_price()		
				);
		textView11.setText(dBook.getU_abstract());
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
		//收藏 
		btn_item_result_search_collect=(Button)findViewById(R.id.btn_item_result_search_collect);
		btn_item_result_search_collect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (GlobleData.islogin) {
					Tool.bookfavorite(DetailBookActivity.this, dBook);
					customProgressDialog.show();
				} else {
					//只是登陆而已
					showLoginDialog(4);
				}
			}
		});
		//分享
		btn_item_result_search_share=(Button)findViewById(R.id.btn_item_result_search_share);
		btn_item_result_search_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Tool.bookshare(DetailBookActivity.this, dBook);
			}
		});
		
		//评论
		btn_item_result_search_buzz=(Button)findViewById(R.id.btn_item_result_search_buzz);
		btn_item_result_search_buzz.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (GlobleData.islogin) {
					Tool.bookbuzz(DetailBookActivity.this, dBook);
				} else {
					//只是登陆而已
					showLoginDialog(4);
				}
			}
		});
		
	}

	//显示对话框
	private void showLoginDialog(int id) {
		MainMenuActivity.cantouch = true;
		Intent intent = new Intent(context, ActivityDlg.class);
		intent.putExtra("ACTIONID", id);
		startActivityForResult(intent, id);
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
	    int type = (Integer)obj[0];
		 if(type==GETBOOKINFO){
		List<BookLoc> list = (List<BookLoc>)obj[1];
		if(list!=null&&!list.isEmpty()){
			adapter = new BookLocAdapter(context,list);
			listview.setAdapter(adapter);
		}else{
			listview.setAdapter(null);
		       }
		}else if(type == FAVOR){//判断收藏是否成功
			 Result res = (Result) obj[1];
			 if (res.getSuccess()) {
						Tool.ShowMessages(context, getResources().getString(R.string.favorsucess));
			}else{
						Tool.ShowMessages(context,  getResources().getString(R.string.already_favoriate));
						}
			}

		
	}

}
