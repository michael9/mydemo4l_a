package com.cqvip.moblelib.activity;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.adapter.BookAdapter;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.SearchHistoryDao;
import com.cqvip.moblelib.entity.SearchHistory_SZ;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.view.DropDownListView;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;

public class ResultOnSearchActivity extends BaseActivity implements
		 OnItemClickListener {

	public  final int GETFIRSTPAGE = 1;
	public  final int GETNEXTPAGE = 2;

	public static final int DEFAULT_COUNT = Constant.DEFAULT_COUNT;
	private EditText edit;
	private TextView searchCount;
	private ImageView imgsearch;
	private Context context;
	private DropDownListView listview;
	private String key;
	private int page = 1;
	private BookAdapter adapter;
	private View moreprocess;
	private RelativeLayout noResult_rl;
	private View title_bar;
	private Map<String, String> gparams;
	private BitmapCache cache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_on_search);
		context = this;
		imgsearch = (ImageView) findViewById(R.id.search_seach_btn);
		edit = (EditText) findViewById(R.id.search_et);
		searchCount = (TextView) findViewById(R.id.txt_total_esearch);
		listview = (DropDownListView) findViewById(R.id.search_res_lv);
		listview.setOnItemClickListener((OnItemClickListener) this);
		listview.setOnBottomListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Tool.isbnMatch(key)) {
					getHomePage(key, page + 1, DEFAULT_COUNT, GETNEXTPAGE,
							GlobleData.QUERY_ISBN);
				} else {
					getHomePage(key, page + 1, DEFAULT_COUNT, GETNEXTPAGE,
							GlobleData.QUERY_TITLE);
				}
				page = page + 1;
			}
		});
		noResult_rl = (RelativeLayout) findViewById(R.id.noresult_rl);
		edit.setText(getIntent().getStringExtra("ISBN"));
		
		if(getIntent().getBooleanExtra("isfromDetailAdvancedBookActivity", false)){//从推荐阅读或者从搜索记录start的
			key=getIntent().getStringExtra("bookname");
			imgsearch.setFocusable(true);
			customProgressDialog.show();
			getHomePage(key, GETFIRSTPAGE, DEFAULT_COUNT, GETFIRSTPAGE,
					GlobleData.QUERY_TITLE);
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		}

		imgsearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeybord();
				key = edit.getText().toString().trim();
				if (!Tool.checkNetWork(context)) {
					return;
				}
				if (TextUtils.isEmpty(key)) {
					Tool.ShowMessages(context, "请输入关键字");
					return;
				}
				// 判断是否是isbn号查询
				customProgressDialog.show();
				page = 1;
				if (Tool.isbnMatch(key)) {
					getHomePage(key, GETFIRSTPAGE, DEFAULT_COUNT, GETFIRSTPAGE,
							GlobleData.QUERY_ISBN);
				} else {
					getHomePage(key, GETFIRSTPAGE, DEFAULT_COUNT, GETFIRSTPAGE,
							GlobleData.QUERY_TITLE);
					//加入数据库
					addDatabase(edit.getText().toString().trim());
				}
			}

		});

		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (TextUtils.isEmpty(edit.getText().toString())) {
					return true;
				}
				key = edit.getText().toString().trim();
				// 隐藏键盘
				hideKeybord();
				// 检查0网络
				if (!Tool.checkNetWork(context)) {
					return false;
				}
				// 网络访问,获取首页
				customProgressDialog.show();
				page = 1;
				if (Tool.isbnMatch(key)) {
					getHomePage(key, GETFIRSTPAGE, DEFAULT_COUNT, GETFIRSTPAGE,
							GlobleData.QUERY_ISBN);
				} else {
					getHomePage(key, GETFIRSTPAGE, DEFAULT_COUNT, GETFIRSTPAGE,
							GlobleData.QUERY_TITLE);
					//加入数据库
					addDatabase(edit.getText().toString().trim());
				}
				return true;
			}

		});

//		title_bar = findViewById(R.id.head_bar);
//		TextView title = (TextView) title_bar.findViewById(R.id.txt_header);
//		title.setText(R.string.main_search);
		ImageView back = (ImageView)findViewById(R.id.return_iv);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void addDatabase(String key2) {
		SearchHistory_SZ searchHistory_SZ=new SearchHistory_SZ();
		searchHistory_SZ.setName(key2);
		SearchHistoryDao<SearchHistory_SZ> searchHistoryDao=new SearchHistoryDao<SearchHistory_SZ>(this, searchHistory_SZ);
		SearchHistory_SZ searchHistory_SZ01=null;
		List<SearchHistory_SZ> list_SearchHistory_SZ=null;
		try {
			searchHistory_SZ01=searchHistoryDao.queryInfo(key2);
			list_SearchHistory_SZ=searchHistoryDao.queryInfobydate();
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(searchHistory_SZ01!=null){
			Log.i("ResultOnSearchAct", "addDatabase01");
			try {
				searchHistory_SZ01.setDate(new Date());
				searchHistoryDao.updateState(searchHistory_SZ01);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(list_SearchHistory_SZ!=null&&list_SearchHistory_SZ.size()>=Constant.DEFAULT_COUNT_SEARCHHISTORY){
			Log.i("ResultOnSearchAct", "addDatabase02");
			
			try {
				searchHistoryDao.delete(list_SearchHistory_SZ.get(Constant.DEFAULT_COUNT_SEARCHHISTORY-1));
				searchHistoryDao.add(searchHistory_SZ);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				searchHistoryDao.add(searchHistory_SZ);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 隐藏键盘
	 */
	private void hideKeybord() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm.isActive()) {
			imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
		}
	}

	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try {
				//获取返回记录数
				int count = Book.bookCount(response);
				if(count>0){
					searchCount.setVisibility(View.VISIBLE);
					String temp="搜索到与 \"<font face=\"arial\" color=\"red\">"+key+"</font>\"  相关内容 "+count+" 个";
					searchCount.setText(Html.fromHtml(temp));
				}else{
					searchCount.setVisibility(View.GONE);
				}
				// JSONObject mj=new JSONObject(response);
				List<Book> lists = Book.formList(response);
				if (lists != null && !lists.isEmpty()) {
					listview.setVisibility(View.VISIBLE);
					noResult_rl.setVisibility(View.GONE);
					cache = new BitmapCache(Tool.getCachSize());
					adapter = new BookAdapter(context, lists,  new ImageLoader(mQueue, cache));
					if(lists.size()<DEFAULT_COUNT){
						listview.setHasMore(false);
						listview.setAdapter(adapter);
						listview.onBottomComplete();
					}else{
						listview.setHasMore(true);
						listview.setAdapter(adapter);
					}
				} else {
					listview.setVisibility(View.GONE);
					noResult_rl.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				e.printStackTrace();
				onError(2);
			}

		}
	};

	Listener<String> backlistenermore = new Listener<String>() {
		@Override
		public void onResponse(String response) {
		
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
		//moreprocess.setVisibility(View.GONE);
			try {
				// JSONObject mj=new JSONObject(response);
				List<Book> lists = Book.formList(response);
				if (lists != null && !lists.isEmpty()&&lists.size()==DEFAULT_COUNT) {
					adapter.addMoreData(lists);
					listview.onBottomComplete();
				} else if(lists != null &&lists.size()>0){
					adapter.addMoreData(lists);
					listview.setHasMore(false);
					listview.onBottomComplete();	
				}else
				{
					listview.setHasMore(false);
					listview.onBottomComplete();
				}

			} catch (Exception e) {
				onError(2);
			}

		}
	};

	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
            onError(2);
		}
	};

	private void requestVolley(String addr, Listener<String> bl, int method) {
		try {
			StringRequest mys = new StringRequest(method, addr, bl, el) {

				protected Map<String, String> getParams()
						throws com.android.volley.AuthFailureError {
					return gparams;
				};
			};
			mQueue.add(mys);
			mQueue.start();
		} catch (Exception e) {
			e.printStackTrace();
			onError(2);
		}
	}

	/**
	 * 请求网络，获取数据
	 * 
	 * @param key
	 * @param page
	 * @param count
	 */
	private void getHomePage(String key, int page, int count, int type,
			String field) {
		gparams = new HashMap<String, String>();
		gparams.put("keyword", key);
		gparams.put("curpage", "" + page);
		gparams.put("perpage", "" + count);
		gparams.put("libid", GlobleData.LIBIRY_ID);
		gparams.put("field", field);

		if (type == GETFIRSTPAGE) {
			if(listview.getFooterViewsCount()==0){
				listview.addfootview();
			}
			
			requestVolley(GlobleData.SERVER_URL
					+ "/library/bookquery/search.aspx", backlistener,
					Method.POST);
		} else {
			requestVolley(GlobleData.SERVER_URL
					+ "/library/bookquery/search.aspx", backlistenermore,
					Method.POST);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.result_on_search, menu);
		return true;
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
	 	    Log.i("ResultOnSea", ""+position);
			Book book = adapter.getLists().get(position);//此地的position要包括listview的header
			if (book != null) {
				Intent _intent = new Intent(context, DetailBookActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("book", book);
				_intent.putExtra("detaiinfo", bundle);
				startActivity(_intent);
			}
		}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		  if (cache != null) {
			  cache = null;
	        }
	}

}
