package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.adapter.BookAdapter;
import com.cqvip.moblelib.ahcm.BuildConfig;
import com.cqvip.moblelib.ahcm.R;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.utils.HttpUtils;
import com.cqvip.moblelib.view.DropDownListView;
import com.cqvip.moblelib.view.PopupMenu;
import com.cqvip.moblelib.view.PopupMenu.onMyItemOnClickListener;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;

public class ResultOnSearchActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener, onMyItemOnClickListener {

	private static final String TAG = "ResultOnSearchActivity";
	private final int GETFIRSTPAGE = 1;
	private final int GETNEXTPAGE = 2;

	public static final int DEFAULT_COUNT = Constant.DEFAULT_COUNT;
	private EditText edit;
	private TextView searchCount;
	private LinearLayout ll_total_esearch;
	private Context context;
	private DropDownListView listview;
	private String key;
	private int page = 1;
	private BookAdapter adapter;
	private View moreprocess;
	private RelativeLayout noResult_rl;
	private View title_bar;
	private Map<String, String> gparams;

	private TextView tx_search_condition;// 显示搜索条件
	private ImageView icon_search;// 搜索按钮
	private ImageView icon_clear;// 清除按钮
	private PopupMenu popup; // 弹出框
	private String[] searchType;// 条件
	private TextView btn_search;// 搜索按钮
	private String search_condition;
	private View search_layout_container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_book_search);
		context = this;
		ll_total_esearch = (LinearLayout) findViewById(R.id.ll_total_esearch);
		searchCount = (TextView) findViewById(R.id.txt_total_esearch);

		initSearch();

		listview = (DropDownListView) findViewById(R.id.search_res_lv);
		listview.setOnItemClickListener((OnItemClickListener) this);
		listview.setOnBottomListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 判断是否是ISBN
				if (Tool.isbnMatch(key)) {
					getHomePage(key, page + 1, DEFAULT_COUNT, GETNEXTPAGE,
							GlobleData.QUERY_ISBN);
				} else {
					getHomePage(key, page + 1, DEFAULT_COUNT, GETNEXTPAGE,
							search_condition);
				}
				page = page + 1;
			}
		});
		noResult_rl = (RelativeLayout) findViewById(R.id.noresult_rl);
		edit.setText(getIntent().getStringExtra("ISBN"));

		if (getIntent().getBooleanExtra("isfromDetailAdvancedBookActivity",
				false)) {
			key = getIntent().getStringExtra("bookname");
			customProgressDialog.show();
			getHomePage(key, GETFIRSTPAGE, DEFAULT_COUNT, GETFIRSTPAGE,
					GlobleData.QUERY_ALL);
			this.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		}

		btn_search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				hideKeybord();
//				key = edit.getText().toString().trim();
//				if (!Tool.checkNetWork(context)) {
//					return;
//				}
//				if (TextUtils.isEmpty(key)) {
//					Tool.ShowMessages(context,
//							getResources()
//									.getString(R.string.tips_nosearch_key));
//					return;
//				}
//				// 判断是否是isbn号查询
//				customProgressDialog.show();
//				page = 1;
//				if (Tool.isbnMatch(key)) {
//					getHomePage(key, GETFIRSTPAGE, DEFAULT_COUNT, GETFIRSTPAGE,
//							GlobleData.QUERY_ISBN);
//				} else {
//					getHomePage(key, GETFIRSTPAGE, DEFAULT_COUNT, GETFIRSTPAGE,
//							search_condition);
//				}
				Toast.makeText(ResultOnSearchActivity.this,
						getString(R.string.tips_undo), Toast.LENGTH_SHORT)
						.show();
			}

		});

		edit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				String str = edit.getText().toString();
				if (!TextUtils.isEmpty(str)) {
					icon_clear.setVisibility(View.VISIBLE);
				} else {
					icon_clear.setVisibility(View.GONE);
				}

			}
		});

		edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// if (TextUtils.isEmpty(edit.getText().toString())) {
				// return true;
				// }
				// key = edit.getText().toString().trim();
				// // 隐藏键盘
				// hideKeybord();
				// // 检查0网络
				// if (!Tool.checkNetWork(context)) {
				// return false;
				// }
				// // 网络访问,获取首页
				// customProgressDialog.show();
				// page = 1;
				// if (Tool.isbnMatch(key)) {
				// getHomePage(key, GETFIRSTPAGE, DEFAULT_COUNT, GETFIRSTPAGE,
				// GlobleData.QUERY_ISBN);
				// } else {
				// getHomePage(key, GETFIRSTPAGE, DEFAULT_COUNT, GETFIRSTPAGE,
				// search_condition);
				// }
				// return true;
				// }
				Toast.makeText(ResultOnSearchActivity.this,
						getString(R.string.tips_undo), Toast.LENGTH_SHORT)
						.show();
				return false;

			}
		});

		title_bar = findViewById(R.id.head_bar);
		TextView title = (TextView) title_bar.findViewById(R.id.txt_header);
		title.setText(R.string.main_search);
		ImageView back = (ImageView) title_bar
				.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	/**
	 * 初始化搜索框
	 */
	private void initSearch() {
		searchType = getResources().getStringArray(R.array.booksearchtype);
		edit = (EditText) findViewById(R.id.et_search_keyword);
		icon_search = (ImageView) this.findViewById(R.id.img_search_icon);
		icon_clear = (ImageView) this.findViewById(R.id.icon_btn_clear);
		search_layout_container = this.findViewById(R.id.ly_left_contain);
		tx_search_condition = (TextView) this
				.findViewById(R.id.tv_search_condition);
		btn_search = (TextView) findViewById(R.id.btn_search);
		popup = new PopupMenu(this, searchType);
		popup.setonMyItemOnClickListener(this);
		search_layout_container.setOnClickListener(this);
		tx_search_condition.setOnClickListener(this);
		icon_clear.setOnClickListener(this);
		search_condition = GlobleData.QUERY_ALL;
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
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();
			hideKeybord();
			try {
				// 获取返回记录数
				int count = Book.bookCount(response);
				if (count > 0) {
					ll_total_esearch.setVisibility(View.VISIBLE);
					searchCount.setText("共计搜索到" + count + "条记录");
				} else {
					ll_total_esearch.setVisibility(View.GONE);
				}
				if (BuildConfig.DEBUG) {
					Log.i(TAG, "count" + count);
					Log.i(TAG, "response" + response);
				}
				// JSONObject mj=new JSONObject(response);
				List<Book> lists = Book.formList(response);
				if (lists != null && !lists.isEmpty()) {
					listview.setVisibility(View.VISIBLE);
					noResult_rl.setVisibility(View.GONE);
					cache = new BitmapCache(Tool.getCachSize());
					adapter = new BookAdapter(context, lists, new ImageLoader(
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
				e.printStackTrace();
				onError(2);
			}

		}
	};

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (popup.isShowing()) {
			popup.closeWindow();
		}
		return super.dispatchTouchEvent(ev);
	}

	Listener<String> backlistenermore = new Listener<String>() {
		@Override
		public void onResponse(String response) {

			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();
			// moreprocess.setVisibility(View.GONE);
			try {
				// JSONObject mj=new JSONObject(response);
				List<Book> lists = Book.formList(response);
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

	private void requestVolley(String addr, Listener<String> bl, int method) {
		try {
			StringRequest mys = new StringRequest(method, addr, bl, el) {

				protected Map<String, String> getParams()
						throws com.android.volley.AuthFailureError {
					return gparams;
				};
			};
			if (BuildConfig.DEBUG) {
				Log.i("requestVolley", "======addr=============" + addr);
				Log.i("requestVolley",
						"======gparams=============" + gparams.toString());
			}
			mys.setRetryPolicy(HttpUtils.setTimeout());
			mys.setRetryPolicy(HttpUtils.setTimeout());
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
		gparams.put("libid", GlobleData.LIBIRY_ID);
		gparams.put("tables", GlobleData.QUERY_TABLE);
		gparams.put("curpage", "" + page);
		gparams.put("perpage", "" + count);
		gparams.put("field", field);

		if (type == GETFIRSTPAGE) {
			if (listview.getFooterViewsCount() == 0) {
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {

		Book book = adapter.getLists().get(positon);
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ly_left_contain:
			if (popup != null) {
				if (popup.isShowing()) {
					popup.closeWindow();
				} else {
					popup.showWindow(v);
				}
			}
			break;
		case R.id.icon_btn_clear:
			edit.setText("");
		default:
			break;
		}

	}

	@Override
	public void onMyItemClick(ListView view, final PopupWindow popupWindow) {
		view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long id) {
				switch (position) {
				case 0:
					search_condition = GlobleData.QUERY_ALL;
					tx_search_condition.setVisibility(View.GONE);
					icon_search.setVisibility(View.VISIBLE);
					search_condition = getSearchType(position);
					break;
				case 1:
				case 2:
				case 3:
					tx_search_condition.setVisibility(View.VISIBLE);
					icon_search.setVisibility(View.GONE);
					tx_search_condition.setText(searchType[position]);
					search_condition = getSearchType(position);
					break;
				}

				if (popupWindow != null && popupWindow.isShowing()) {
					popupWindow.dismiss();
				}
			}

			private String getSearchType(int position) {
				switch (position) {
				case 0:
					return GlobleData.QUERY_ALL;
				case 1:
					return GlobleData.QUERY_TITLE;
				case 2:
					return GlobleData.QUERY_AUTHOR;
				case 3:
					return GlobleData.QUERY_ISBN;
				default:
					return GlobleData.QUERY_ALL;
				}
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (popup != null) {
				if (popup.isShowing()) {
					popup.closeWindow();
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

}
