package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookAdapter;
import com.cqvip.moblelib.adapter.EbookAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

public class EBookSearchActivity extends BaseActivity implements IBookManagerActivity,OnItemClickListener {
	
	public static final int GETFIRSTPAGE = 1;
	public static final int GETNEXTPAGE = 2;
	public static final int DEFAULT_COUNT = 10;
	private EditText edit;
	private ImageView imgsearch;
	private Context context;
	private ListView listview;
	private String key;
	private int page=1;
	private EbookAdapter adapter;
	private RelativeLayout noResult_rl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_on_search);
		context = this;
		imgsearch = (ImageView)findViewById(R.id.search_seach_btn);
		edit = (EditText)findViewById(R.id.search_et);
		listview = (ListView)findViewById(R.id.search_res_lv);
		listview.setOnItemClickListener((OnItemClickListener)this);
		ManagerService.allActivity.add(this);
		customProgressDialog=CustomProgressDialog.createDialog(this);
		noResult_rl = (RelativeLayout) findViewById(R.id.noresult_rl);
		
		imgsearch.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					hideKeybord();
					if(TextUtils.isEmpty(edit.getText().toString())){
						Tool.ShowMessages(context, "请输入关键字");
						return;
					}
				  //搜索新鲜事
					if(!Tool.checkNetWork(context)){
						return ;
					}
					getHomePage(edit.getText().toString().trim(),page,DEFAULT_COUNT,0);
//					Tool.ShowMessages(context, "开始搜索");
				}
			});
		  edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
			  @Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					if(TextUtils.isEmpty(edit.getText().toString())){
						return true;
					}
					key = edit.getText().toString();
					//隐藏键盘
					hideKeybord();
					//检查网络
					if(!Tool.checkNetWork(context)){
						return false;
					}
					//网络访问,获取首页
					page = 1;
					getHomePage(edit.getText().toString().trim(),1,DEFAULT_COUNT,0);
					Tool.ShowMessages(context, "开始搜索");
					return true;
				}

			});
	}
	
	/**
	 * 隐藏键盘
	 */
	private void hideKeybord() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()){
			imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
		}
	}
	
	/**
	 * 请求网络，获取数据
	 * @param key
	 * @param page
	 * @param count
	 */
	private void getHomePage(String key,int page ,int count,int type) {
		customProgressDialog.show();
		HashMap map=new HashMap();
		map.put("key", key);
		map.put("page", page);
		map.put("count", count);
		Task tsHome;
		if(type == 0){
		 tsHome=new Task(Task.TASK_QUERY_EBOOK,map);
		}else{
		 tsHome=new Task(Task.TASK_QUERY_EBOOK_MORE,map);
		}
		ManagerService.addNewTask(tsHome);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void refresh(Object... obj) {
		customProgressDialog.dismiss();
		//显示
		int type = (Integer)obj[0];
		List<EBook> lists = (List<EBook>)obj[1];
		if(type == GETFIRSTPAGE ){
		if(lists!=null&&!lists.isEmpty()){
			listview.setVisibility(View.VISIBLE);
			noResult_rl.setVisibility(View.GONE);
			adapter = new EbookAdapter(context,lists);
			listview.setAdapter(adapter);
			
		}else{
			listview.setVisibility(View.GONE);
			noResult_rl.setVisibility(View.VISIBLE);
		}
		}else if(type == GETNEXTPAGE){
			if(lists!=null&&!lists.isEmpty()){
			adapter.addMoreData(lists);
			}else{
				Tool.ShowMessages(context, "没有更多内容可供加载");
			}
		}
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {
		if (id == -2) //更多
		{
			//进度条
			View moreprocess = arg1.findViewById(R.id.footer_progress);
			moreprocess.setVisibility(View.VISIBLE);
			//请求网络更多
			getHomePage(key,page+1,DEFAULT_COUNT,1);
			page = page+1;
		}else{
			EBook book = adapter.getLists().get(positon);
			if(book!=null){
				Log.i("ResultOnSearchActivity",book.toString());
				Intent _intent = new Intent(context,EbookDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("ebook", book);
				_intent.putExtra("detaiinfo", bundle);
				startActivity(_intent);
			}
			
//			Book book =  lists.get(position-1);
//			if(book!=null){
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("book", book);
//			_intent.putExtra("detaiinfo", bundle);
//			startActivityForResult(_intent, 1);
			}
		}
		

}
