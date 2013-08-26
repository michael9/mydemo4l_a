package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.mobelib.imgutils.AsyncTask;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.AdvancedBookAdapter;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.view.DownFreshListView;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;

public class AdvancedBookActivity extends BaseActivity implements
		OnItemClickListener {

	private final int GETMORE = 1;
	private final int GETHOMEPAGE = 0;

	private AdvancedBookAdapter adapter;
	private GridView gridview_abook;
	private int type;
	private Context context;
	private int page = 1;
	private View moreprocess;
	private int sendtype;
	private Map<String, String> gparams;
	private BitmapCache cache;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_book);
		context = this;
		type = getIntent().getIntExtra("type", 1);

		switch (type) {
		case Constant.HOTBOOK:
			sendtype = Task.TASK_SUGGEST_HOTBOOK;
			setheadbar("热门图书");
			break;

		case Constant.NEWBOOK:
			sendtype = Task.TASK_SUGGEST_NEWBOOK;
			setheadbar("新书推荐");
			break;

		default:
			break;
		}

		gridview_abook = (GridView) findViewById(R.id.gridview_abook);
		gridview_abook.setOnItemClickListener(this);
		// listview.setOnRefreshListener(this);
		adapter = new AdvancedBookAdapter(context, null);

		// 获取列表
		customProgressDialog.show();
		getHomePage(page, Constant.DEFAULT_COUNT, GETHOMEPAGE);

	}

	private void setheadbar(String title) {
		View headbar, btn_back;
		TextView bar_title;
		headbar = findViewById(R.id.head_bar);
		bar_title = (TextView) headbar.findViewById(R.id.txt_header);
		bar_title.setText(title);
		btn_back = headbar.findViewById(R.id.img_back_header);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			try {
				List<ShortBook> lists = ShortBook.formList(sendtype, response);
				if (lists != null && !lists.isEmpty()) {
					cache = new BitmapCache(Tool.getCachSize());
					adapter = new AdvancedBookAdapter(context, lists, new ImageLoader(mQueue, cache));
					gridview_abook.setAdapter(adapter);
				}
			} catch (Exception e) {
				// TODO: handle exception
				return;
			}
		}
	};

	private Listener<String> backlistenermore = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			try {
				moreprocess.setVisibility(View.GONE);
				List<ShortBook> lists = ShortBook.formList(sendtype, response);
				if (lists != null && !lists.isEmpty()) {
					adapter.addMoreData(lists);
				} else {
					Tool.ShowMessages(context, "没有更多内容可供加载");
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
			// TODO: handle exception
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		// 可滑动的listview需要 position-1
		ShortBook book = adapter.getLists().get(position);
		if (book != null) {
			Intent _intent = new Intent(context,
					DetailAdvancedBookActivity.class);
			_intent.putExtra("id", book.getId());
			_intent.putExtra("type", type);
			startActivity(_intent);
		}
	}

	private void getHomePage(int page, int defaultCount, int mwhat) {
		gparams = new HashMap<String, String>();
		gparams.put("libid", GlobleData.LIBIRY_ID);
		gparams.put("curpage", "" + page);
		gparams.put("perpage", "" + Constant.DEFAULT_COUNT);

		switch (type) {
		case Constant.HOTBOOK:
			gparams.put("announcetypeid", "" + 3);
			break;
		case Constant.NEWBOOK:
			gparams.put("announcetypeid", "" + 4);
			break;
		}
		if (mwhat == GETHOMEPAGE) {
			requestVolley(
					GlobleData.SERVER_URL + "/library/announce/list.aspx",
					backlistener, Method.POST);
		} else {
			requestVolley(
					GlobleData.SERVER_URL + "/library/announce/list.aspx",
					backlistenermore, Method.POST);
		}

	}

	// @Override
	// public void onRefresh() {
	// getHomePage(1, Constant.DEFAULT_COUNT,GETHOMEPAGE);
	// new AsyncTask<Void, Void, Void>() {
	// protected Void doInBackground(Void... params) {
	//
	// try {
	// Thread.sleep(2000);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// return null;
	// }
	// //刷新完成
	// @Override
	// protected void onPostExecute(Void result) {
	//
	// listview.onRefreshComplete();
	//
	// }
	//
	// }.execute(null, null);
	//
	// }
	@Override
	protected void onDestroy() {
		super.onDestroy();
		  if (cache != null) {
			  cache = null;
	        }
	}
}
