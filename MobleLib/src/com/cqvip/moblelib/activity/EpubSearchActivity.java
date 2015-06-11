package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.adapter.EbookAdapter;
import com.cqvip.moblelib.adapter.EpubAdapter;
import com.cqvip.moblelib.adapter.ZLFBookAdapter;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.EpubDetail;
import com.cqvip.moblelib.model.ZLFBook;
import com.cqvip.moblelib.sm.R;
import com.cqvip.moblelib.utils.HttpUtils;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EpubSearchActivity extends BaseActivity {
	
	private EditText edit;
	private TextView searchCount;
	private LinearLayout ll_total_esearch;
	private ImageView imgsearch;
	private Context context;
//	private BitmapCache cache;
	private PullToRefreshGridView epubgrid;
	private Map<String, String> gparams;
	private View title_bar,searchbar;
	private EpubAdapter mAdapter;  
	private int gpage=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epubsearch);		
		context=this;		
		searchbar=(View)findViewById(R.id.searchbar);
		edit=(EditText)searchbar.findViewById(R.id.et_search);
		imgsearch=(ImageView)searchbar.findViewById(R.id.im_seach_icon);
		imgsearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gpage=1;
				getsearch(edit.getText().toString(),gpage,6);
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
				gpage=1;
				getsearch(edit.getText().toString(),gpage,6);
				return true;
			}

		});
		
		
		title_bar = findViewById(R.id.head_bar);
		TextView title = (TextView) title_bar.findViewById(R.id.txt_header);
		title.setText("电子图书");
		ImageView back = (ImageView) title_bar
				.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		gpage=1;
		getsearch("",gpage,6);
		
		epubgrid=(PullToRefreshGridView)findViewById(R.id.epubgrid);
		
		epubgrid.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				 Intent intent=new Intent(EpubSearchActivity.this,EpubDetailActivity.class);
				 Bundle mBundle = new Bundle();
				 mBundle.putSerializable("ed",mAdapter.getLists().get(arg2));
				 intent.putExtras(mBundle);
				 startActivity(intent);
			}
			
		});
		
		epubgrid.setOnRefreshListener(new OnRefreshListener2<GridView>()
				{

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<GridView> refreshView)
			{
				gpage=1;
				getsearch("",gpage,6);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<GridView> refreshView)
			{
				
				gpage++;
				getsearchmore("",gpage,6);
			}
		});
	
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		hideKeybord();
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
	
	private void  getsearch(String key,int page,int pagesize)
	{
		customProgressDialog.show();
		gparams = new HashMap<String, String>();
		gparams.put("keyword", key);
		gparams.put("page", "" +page);// 当前页数
		gparams.put("pageSize", "" + pagesize);// 条数
		requestVolley(GlobleData.EPUB_HOME_URL+"index.asmx/Query",backlistener,Method.POST);
	}
	private void  getsearchmore(String key,int page,int pagesize)
	{
		gparams = new HashMap<String, String>();
		gparams.put("keyword", key);
		gparams.put("page", "" +page);// 当前页数
		gparams.put("pageSize", "" + pagesize);// 条数
		requestVolley(GlobleData.EPUB_HOME_URL+"index.asmx/Query",backmorelistener,Method.POST);
	}
	
	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try{
			String result = new String(response.getBytes("iso-8859-1"),"utf-8");
			
			List<EpubDetail> lists = EpubDetail.formList(result) ;
			if (lists != null && !lists.isEmpty()) {
//				cache = new BitmapCache(Tool.getCachSize());
				mAdapter = new EpubAdapter(context, lists,  new ImageLoader(mQueue, cache));
				epubgrid.setAdapter(mAdapter);
				
			}
			}
			catch (Exception e) {
				// TODO: handle exception
				Log.d("dec", e.getMessage());
			}
			epubgrid.onRefreshComplete();
		}
	};
	
	Listener<String> backmorelistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try{
			String result = new String(response.getBytes("iso-8859-1"),"utf-8");
			
			List<EpubDetail> lists = EpubDetail.formList(result) ;
			if (lists != null && !lists.isEmpty()) {
				cache = new BitmapCache(Tool.getCachSize());
				mAdapter.addMoreData(lists);
			}
			}
			catch (Exception e) {
				// TODO: handle exception
				Log.d("dec", e.getMessage());
			}
			epubgrid.onRefreshComplete();
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
			mys.setRetryPolicy(HttpUtils.setTimeout());mQueue.add(mys);
			mQueue.start();
		} catch (Exception e) {
			e.printStackTrace();
			onError(2);
		}
	}
	

	
	
}