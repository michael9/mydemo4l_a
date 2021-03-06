package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActivityManager;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

public class ResultOnSearchActivity extends BaseImageActivity implements IBookManagerActivity,OnItemClickListener {
	
	public static final int GETFIRSTPAGE = 1;
	public static final int GETNEXTPAGE = 2;

	public static final int DEFAULT_COUNT = 10;
	private EditText edit;
	private ImageButton imgsearch;
	private Context context;
	private ListView listview;
	private String key;
	private int page = 1;
	private BookAdapter adapter;
	private View moreprocess;
	private RelativeLayout noResult_rl;
	private View title_bar;
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
		customProgressDialog=CustomProgressDialog.createDialog(this);
		noResult_rl = (RelativeLayout) findViewById(R.id.noresult_rl);
		edit.setText(getIntent().getStringExtra("ISBN"));
		
		imgsearch.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					hideKeybord();
					key = edit.getText().toString().trim();
					if(!Tool.checkNetWork(context)){
						return ;
					}
					if(TextUtils.isEmpty(key)){
						Tool.ShowMessages(context, "请输入关键字");
						return;
					}
					//判断是否是isbn号查询
					page = 1;
					if(Tool.isbnMatch(key)){
					getHomePage(key,GETFIRSTPAGE,DEFAULT_COUNT,GETFIRSTPAGE,GlobleData.QUERY_ISBN);
					}else{
					getHomePage(key,GETFIRSTPAGE,DEFAULT_COUNT,GETFIRSTPAGE,GlobleData.QUERY_ALL);
					}
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
					key = edit.getText().toString().trim();
					//隐藏键盘
					hideKeybord();
					//检查0网络
					if(!Tool.checkNetWork(context)){
						return false;
					}
					//网络访问,获取首页
					page = 1;
					if(Tool.isbnMatch(key)){
						getHomePage(key,GETFIRSTPAGE,DEFAULT_COUNT,GETFIRSTPAGE,GlobleData.QUERY_ISBN);
						}else{
						getHomePage(key,GETFIRSTPAGE,DEFAULT_COUNT,GETFIRSTPAGE,GlobleData.QUERY_ALL);
						}
					return true;
				}

			});
	
		  title_bar=findViewById(R.id.head_bar);
			TextView title = (TextView)title_bar.findViewById(R.id.txt_header);
			title.setText(R.string.main_search);
			ImageView back = (ImageView)title_bar.findViewById(R.id.img_back_header);
			back.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});
//			 ActivityManager activityManager = (ActivityManager) this.getSystemService("activity");
//		        Log.i("MemoryClass","" + activityManager.getMemoryClass());
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
	private void getHomePage(String key,int page ,int count,int type,String field) {
		customProgressDialog.show();
		HashMap map=new HashMap();
		map.put("key", key);
		map.put("page", page);
		map.put("count", count);
		map.put("library",GlobleData.SZLG_LIB_ID);
		map.put("field",field);
		Task tsHome;
		if(type == GETFIRSTPAGE){
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
		customProgressDialog.dismiss();
		hideKeybord();
		int type = (Integer)obj[0];
		 
		List<Book> lists = (List<Book>)obj[1];
		if(type == GETFIRSTPAGE ){
			if(lists!=null&&!lists.isEmpty()){
				listview.setVisibility(View.VISIBLE);
				noResult_rl.setVisibility(View.GONE);
			adapter = new BookAdapter(context,lists,mImageFetcher);
			listview.setAdapter(adapter);
			
		}
		else
		{
			//Tool.ShowMessages(context, "不好意思，没有找到你想找的资源！");
			//listview.setAdapter(new SimpleAdapter(context, getData(), android.R.layout.simple_list_item_1, new String[]{"no"}, new int[] { android.R.id.text1 }));
			listview.setVisibility(View.GONE);
			noResult_rl.setVisibility(View.VISIBLE);
			
		}
		}else if(type == GETNEXTPAGE){
			moreprocess.setVisibility(View.GONE);
			if(lists!=null&&!lists.isEmpty()){
				adapter.addMoreData(lists);
				}else{
					Tool.ShowMessages(context, "没有更多内容可供加载");
				}
		}
	}
	private List<? extends Map<String, ?>> getData() {
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
			Map<String,String> map = new HashMap<String,String>();
			map.put("no","查询无记录");
			list.add(map);
        return list;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {
		Log.i("item","===============click=");
		if (id == -2) //更多
		{
			//进度条
			moreprocess = arg1.findViewById(R.id.footer_progress);
			moreprocess.setVisibility(View.VISIBLE);
			//请求网络更多
			if(Tool.isbnMatch(key)){
				getHomePage(key,page+1,DEFAULT_COUNT,GETNEXTPAGE,GlobleData.QUERY_ISBN);
				}else{
				getHomePage(key,page+1,DEFAULT_COUNT,GETNEXTPAGE,GlobleData.QUERY_ALL);
				}
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
