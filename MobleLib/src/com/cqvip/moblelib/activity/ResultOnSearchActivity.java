package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

public class ResultOnSearchActivity extends Activity implements IBookManagerActivity,OnItemClickListener {
	
	public static final int GETFIRSTPAGE = 1;
	public static final int GETNEXTPAGE = 2;
	public static final int DEFAULT_COUNT = 10;
	private EditText edit;
	private ImageButton imgsearch;
	private Context context;
	private ListView listview;
	private String key;
	private int page=1;
	private BookAdapter adapter;
	private CustomProgressDialog progressDialog;  
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_on_search);
		context = this;
		imgsearch = (ImageButton)findViewById(R.id.search_seach_btn);
		edit = (EditText)findViewById(R.id.search_et);
		listview = (ListView)findViewById(R.id.search_res_lv);
		listview.setOnItemClickListener((OnItemClickListener)this);
		ManagerService.allActivity.add(this);
		progressDialog=CustomProgressDialog.createDialog(this);
		
		edit.setText(getIntent().getStringExtra("ISBN"));
		
		imgsearch.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					if(imm.isActive()){
						imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
					}
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
//					progressDialog.show();
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
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
					//检查网络
					if(!Tool.checkNetWork(context)){
						return false;
					}
					//网络访问,获取首页
					page = 1;
					getHomePage(edit.getText().toString().trim(),1,DEFAULT_COUNT,0);
//					Tool.ShowMessages(context, "开始搜索");
//					progressDialog.show();
					return true;
				}

			});
	}
	/**
	 * 请求网络，获取数据
	 * @param key
	 * @param page
	 * @param count
	 */
	private void getHomePage(String key,int page ,int count,int type) {
		progressDialog.show();
		HashMap map=new HashMap();
		map.put("key", key);
		map.put("page", page);
		map.put("count", count);
		Task tsHome;
		if(type == 0){
		 tsHome=new Task(Task.TASK_QUERY_BOOK,map);
		}else{
		 tsHome=new Task(Task.TASK_QUERY_MORE,map);
		}
		ManagerService.addNewTask(tsHome);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result_on_search, menu);
		return true;
	}
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void refresh(Object... obj) {
		//显示
		progressDialog.dismiss();
		int type = (Integer)obj[0];
		List<Book> lists = (List<Book>)obj[1];
		if(type == GETFIRSTPAGE ){
		if(lists!=null){
			adapter = new BookAdapter(context,lists);
			listview.setAdapter(adapter);
			
		}
		else
		{
			Tool.ShowMessages(context, "不好意思，没有找到你想找的资源！");
		}
		}else if(type == GETNEXTPAGE){
			adapter.addMoreData(lists);
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
			Book book = adapter.getLists().get(positon);
			if(book!=null){
				Log.i("ResultOnSearchActivity",book.toString());
				Intent _intent = new Intent(context,DetailBookActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("book", book);
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
