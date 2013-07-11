package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class DetailBookActivity extends BaseImageActivity implements IBookManagerActivity {
	public static final int GETBOOKINFO = 1;
	public static final int FAVOR = 2;
	
	private Book dBook;
	private TextView booktitle_tv,textView9,textView10,textView11;
//	private ListView listview;
	private LinearLayout loc_list_ll;
	private BookLocAdapter adapter;
	private Context context;
	private View title_bar,book_action_bar;
	private ImageView imgview;	
	private Button btn_item_result_search_collect,btn_item_result_search_share,btn_item_result_search_buzz,btn_item_result_search_download;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_book);
		context = this;
		loc_list_ll=(LinearLayout)findViewById(R.id.loc_list_ll);
//		listview = (ListView)findViewById(R.id.loc_list);
//		View v = LayoutInflater.from(this).inflate(R.layout.activity_detail_book, null);
//		listview.addHeaderView(v);
		imgview = (ImageView) findViewById(R.id.book_big_img);
		//设置图片
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book)bundle.getSerializable("book");
		boolean ismyfavor=getIntent().getBooleanExtra("ismyfavor", false);
		booktitle_tv=(TextView)findViewById(R.id.booktitle_tv);
		textView9=(TextView)findViewById(R.id.textView9);
		textView10=(TextView)findViewById(R.id.textView10);
		textView11=(TextView)findViewById(R.id.textView11);
		
		ManagerService.allActivity.add(this);
		
		if(!TextUtils.isEmpty(dBook.getCover_path())){
		mImageFetcher.loadImage(dBook.getCover_path(), imgview);
		}else{
			imgview.setBackgroundResource(R.drawable.defaut_book);
		}
		
		booktitle_tv.setText(dBook.getTitle());
		//从我的收藏传过来的isbn都是空字符串，
		String isbn="";
		if(!dBook.getIsbn().isEmpty()){
			isbn="ISBN:"+dBook.getIsbn()+"\n";
		}
		//从我的收藏传过来的是出版时间而非主题
		String timeortheme="";
		if(ismyfavor){
			timeortheme=getString(R.string.item_time)+dBook.getSubject();
		}else{
			timeortheme=getString(R.string.item_subject)+dBook.getSubject();
		}
		//从我的收藏传过来的recordid存储在Book的callno
		String recordid="";
		if(ismyfavor){
			recordid=dBook.getCallno();
		}else{
			recordid=dBook.getRecordid();
		}
		if(!TextUtils.isEmpty(recordid)){
			getLocalinfo(recordid);
		}
		
		textView10.setText(
				getString(R.string.item_author)+dBook.getAuthor()+"\n"
				+getString(R.string.item_publish)+dBook.getPublisher()+"\n"
				+timeortheme+"\n"
//				+getString(R.string.item_callno)+dBook.getCallno()+"\n"
//				+getString(R.string.item_classno)+dBook.getClassno()+"\n"
				+isbn
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
		book_action_bar=findViewById(R.id.book_action_bar);
		//收藏 
		btn_item_result_search_collect=(Button)book_action_bar.findViewById(R.id.btn_item_collect);
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
		btn_item_result_search_share=(Button)book_action_bar.findViewById(R.id.btn_item_share);
		btn_item_result_search_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Tool.bookshare(DetailBookActivity.this, dBook);
			}
		});
		
		//评论
		btn_item_result_search_buzz=(Button)book_action_bar.findViewById(R.id.btn_item_buzz);
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
		//
		btn_item_result_search_download=(Button)book_action_bar.findViewById(R.id.btn_item_download);
		btn_item_result_search_download.setVisibility(View.GONE);
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
	
	private void add2gc(List<BookLoc> list){
		if(list==null||list.isEmpty())
			return;
		for(BookLoc bl:list)
		{
//		LinearLayout mll=new LinearLayout(this);
		LinearLayout mll=(LinearLayout)getLayoutInflater().inflate(R.layout.item_location_book, null);
//		mll.inflate(this, R.layout.item_location_book, null);
		TextView barcode = (TextView) mll.findViewById(R.id.loc_barcode_txt);
		TextView callno = (TextView) mll.findViewById(R.id.loc_callno_txt);
		TextView location = (TextView) mll.findViewById(R.id.loc_location_txt);
		TextView cirtype = (TextView) mll.findViewById(R.id.loc_cirtype_txt);
		TextView status = (TextView) mll.findViewById(R.id.loc_status_txt);

			barcode.setText(context.getString(R.string.item_barcode)+bl.getBarcode());
			callno.setText(context.getString(R.string.item_callno)+bl.getCallno());
			location.setText(context.getString(R.string.item_loc)+bl.getLocal());
			cirtype.setText(context.getString(R.string.item_cirtype)+bl.getCirtype());
			status.setText(context.getString(R.string.item_status)+bl.getStatus());
			loc_list_ll.addView(mll);
		}
	}

	@Override
	public void refresh(Object... obj) {
		customProgressDialog.dismiss();
	    int type = (Integer)obj[0];
		 if(type==GETBOOKINFO){
//		List<BookLoc> list = (List<BookLoc>)obj[1];
//		if(list!=null&&!list.isEmpty()){
//			textView9.setText("馆藏信息("+list.size()+")");
//			adapter = new BookLocAdapter(context,list);
//			listview.setAdapter(adapter);
//		}else{
//			listview.setAdapter(null);
//		       }
			 add2gc((List<BookLoc>)obj[1]);
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
