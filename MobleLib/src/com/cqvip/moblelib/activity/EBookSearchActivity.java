package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.EbookAdapter;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
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
		//获取更多
		listview.setOnBottomListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getHomePage(edit.getText().toString().trim(), page + 1, DEFAULT_COUNT, 1);
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
				return true;
			}

		});

		title_bar = findViewById(R.id.head_bar);
		TextView title = (TextView) title_bar.findViewById(R.id.txt_header);
		title.setText(R.string.main_ebook);
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
				int count = EBook.ebookCount(response);
				if(count>0){
					searchCount.setVisibility(View.VISIBLE);
					searchCount.setText("共计搜索到"+count+"条记录");
				}else{
					searchCount.setVisibility(View.GONE);
				}
				// JSONObject mj=new JSONObject(response);
				List<EBook> lists = EBook.formList(response);
				if (lists != null && !lists.isEmpty()) {
					listview.setVisibility(View.VISIBLE);
					noResult_rl.setVisibility(View.GONE);
					cache = new BitmapCache(Tool.getCachSize());
					adapter = new EbookAdapter(context, lists,  new ImageLoader(mQueue, cache));
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
			}

		}
	};

	Listener<String> backlistenermore = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			try {
				// JSONObject mj=new JSONObject(response);
				List<EBook> lists = EBook.formList(response);
				if (lists != null && !lists.isEmpty()) {
					adapter.addMoreData(lists);
					listview.onBottomComplete();
				} else {
					listview.setHasMore(false);
					listview.onBottomComplete();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();

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
