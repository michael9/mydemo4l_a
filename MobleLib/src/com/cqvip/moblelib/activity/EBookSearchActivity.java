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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.cqvip.moblelib.adapter.EbookAdapter;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.SearchHistoryDao;
import com.cqvip.moblelib.entity.SearchHistory_ZK;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.view.DropDownListView;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;

public class EBookSearchActivity extends BaseActivity implements
		OnItemClickListener {

	public static final int GETFIRSTPAGE = 1;
	public static final int GETNEXTPAGE = 2;
	public static final int FAVOR = 3;
	public static final int DEFAULT_COUNT = Constant.DEFAULT_COUNT;
	private EditText edit;
	private TextView searchCount;
	private ImageView imgsearch;
	private Context context;
	private DropDownListView listview;
	private int page = 1;
	private EbookAdapter adapter;
	private RelativeLayout noResult_rl;
	private View title_bar;
	private BitmapCache cache;
	private Map<String, String> gparams;

	public static HashMap<String, Boolean> favors = new HashMap<String, Boolean>();// 保持收藏状态，更新界面

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
		noResult_rl = (RelativeLayout) findViewById(R.id.noresult_rl);
		// 获取更多
		listview.setOnBottomListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getHomePage(edit.getText().toString().trim(), page + 1,
						DEFAULT_COUNT, 1);
				page = page + 1;
			}
		});
		imgsearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeybord();
				if (TextUtils.isEmpty(edit.getText().toString().trim())) {
					Tool.ShowMessages(context, "请输入关键字");
					return;
				}
				if (!Tool.checkNetWork(context)) {
					return;
				}
				customProgressDialog.show();
				page = 1;
				getHomePage(edit.getText().toString().trim(), page,
						DEFAULT_COUNT, 0);
				//加入数据库
				addDatabase(edit.getText().toString().trim());
			}
		});
		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (TextUtils.isEmpty(edit.getText().toString())) {
					return true;
				}
				// 隐藏键盘
				hideKeybord();
				// 检查网络
				if (!Tool.checkNetWork(context)) {
					return false;
				}
				// 网络访问,获取首页
				customProgressDialog.show();
				page = 1;
				getHomePage(edit.getText().toString().trim(), 1, DEFAULT_COUNT,
						0);
				//加入数据库
				addDatabase(edit.getText().toString().trim());
				return true;
			}

		});

		// title_bar = findViewById(R.id.head_bar);
		// TextView title = (TextView) title_bar.findViewById(R.id.txt_header);
		// title.setText(R.string.main_ebook);
		ImageView back = (ImageView) findViewById(R.id.return_iv);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// listview.setOnScrollListener(new OnScrollListener() {
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem,
		// int visibleItemCount, int totalItemCount) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
	}
	
	private void addDatabase(String key2) {
		SearchHistory_ZK searchHistory_ZK=new SearchHistory_ZK();
		searchHistory_ZK.setName(key2);
		SearchHistoryDao<SearchHistory_ZK> searchHistoryDao=new SearchHistoryDao<SearchHistory_ZK>(this, searchHistory_ZK);
		SearchHistory_ZK SearchHistory_ZK01=null;
		List<SearchHistory_ZK> list_SearchHistory_ZK=null;
		try {
			SearchHistory_ZK01=searchHistoryDao.queryInfo(key2);
			list_SearchHistory_ZK=searchHistoryDao.queryInfobydate();
		} catch (DaoException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(SearchHistory_ZK01!=null){
			Log.i("ResultOnSearchAct", "addDatabase01");
			try {
				SearchHistory_ZK01.setDate(new Date());
				searchHistoryDao.updateState(SearchHistory_ZK01);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(list_SearchHistory_ZK!=null&&list_SearchHistory_ZK.size()>=Constant.DEFAULT_COUNT_SEARCHHISTORY){
			Log.i("ResultOnSearchAct", "addDatabase02");
			
			try {
				searchHistoryDao.delete(list_SearchHistory_ZK.get(Constant.DEFAULT_COUNT_SEARCHHISTORY-1));
				searchHistoryDao.add(searchHistory_ZK);
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			try {
				searchHistoryDao.add(searchHistory_ZK);
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
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();
			try {
				// 获取返回记录数
				int count = EBook.ebookCount(response);
				if (count > 0) {
					searchCount.setVisibility(View.VISIBLE);
					String temp = "搜索到与 \"<font face=\"arial\" color=\"red\">"
							+ edit.getText().toString().trim()
							+ "</font>\"  相关内容 " + count + " 个";
					searchCount.setText(Html.fromHtml(temp));
				} else {
					searchCount.setVisibility(View.GONE);
				}
				// JSONObject mj=new JSONObject(response);
				List<EBook> lists = EBook.formList(response);
				if (lists != null && !lists.isEmpty()) {
					listview.setVisibility(View.VISIBLE);
					noResult_rl.setVisibility(View.GONE);
					cache = new BitmapCache(Tool.getCachSize());
					adapter = new EbookAdapter(context, lists, new ImageLoader(
							mQueue, cache));
					if (lists.size() < DEFAULT_COUNT) {
						listview.setHasMore(false);
						listview.setAdapter(adapter);
						listview.onBottomComplete();
					} else {
						listview.setHasMore(true);
						listview.setAdapter(adapter);
					}
				} else {
					listview.setVisibility(View.GONE);
					noResult_rl.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				onError(2);
				e.printStackTrace();
			}

		}
	};

	Listener<String> backlistenermore = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			try {
				// JSONObject mj=new JSONObject(response);
				List<EBook> lists = EBook.formList(response);
				if (lists != null && !lists.isEmpty()
						&& lists.size() == DEFAULT_COUNT) {
					adapter.addMoreData(lists);
					listview.onBottomComplete();
				} else if (lists != null && lists.size() > 0) {
					adapter.addMoreData(lists);
					listview.setHasMore(false);
					listview.onBottomComplete();
				} else {
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
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
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
	private void getHomePage(String key, int page, int count, int type) {
		gparams = new HashMap<String, String>();
		gparams.put("title", key);
		gparams.put("curpage", "" + page);// 当前页数
		gparams.put("perpage", "" + count);// 条数

		if (type == 0) {
			if (listview.getFooterViewsCount() == 0) {
				listview.addfootview();
			}
			requestVolley(GlobleData.SERVER_URL + "/zk/search.aspx",
					backlistener, Method.POST);
		} else {
			requestVolley(GlobleData.SERVER_URL + "/zk/search.aspx",
					backlistenermore, Method.POST);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {
		EBook book = adapter.getLists().get(positon);
		if (book != null) {
			Intent _intent = new Intent(context, EbookDetailActivity.class);
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
