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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.mobelib.imgutils.AsyncTask;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.activity.AnnouceListActivity.MyNewAdapter;
import com.cqvip.moblelib.adapter.AdvancedBookAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.moblelib.view.DownFreshListView;
import com.cqvip.utils.Tool;

public class AdvancedBookActivity extends BaseActivity implements OnItemClickListener,DownFreshListView.OnRefreshListener {

	private  final int GETMORE = 1;
	private  final int GETHOMEPAGE = 0;
	
	private  AdvancedBookAdapter adapter;
	private DownFreshListView listview;
	private int type;
	private Context context;
	private int page=1;
	private View moreprocess;
	private int sendtype;
	private Map<String, String> gparams;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_book);
		context = this;
		type = getIntent().getIntExtra("type", 1);
		
		switch (type) {
		case Constant.HOTBOOK:
			sendtype=Task.TASK_SUGGEST_HOTBOOK;
			setheadbar("热门图书");
			break;
			
		case Constant.NEWBOOK:
			sendtype= Task.TASK_SUGGEST_NEWBOOK;
			setheadbar("新书推荐");
			break;

		default:
			break;
		}
		
		
		listview = (DownFreshListView) findViewById(R.id.listview_abook);
		listview.setOnItemClickListener(this);
		listview.setOnRefreshListener(this);
		adapter = new AdvancedBookAdapter(context,null);
		
		//获取列表
		getHomePage(page, Constant.DEFAULT_COUNT,GETHOMEPAGE);
		
		
	}
	
	private void setheadbar(String title)
	{
		View headbar,btn_back;
		TextView bar_title;
		customProgressDialog=CustomProgressDialog.createDialog(this);
		headbar=findViewById(R.id.head_bar);
		bar_title=(TextView)headbar.findViewById(R.id.txt_header);
		bar_title.setText(title);
		btn_back=headbar.findViewById(R.id.img_back_header);
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
				List<ShortBook> lists= ShortBook.formList(sendtype, response);
				if(lists!=null&&!lists.isEmpty()){
					adapter = new  AdvancedBookAdapter(context,lists,mQueue);
					listview.setAdapter(adapter);
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
			customProgressDialog.dismiss();
			try {
				moreprocess.setVisibility(View.GONE);
				List<ShortBook> lists= ShortBook.formList(sendtype, response);
				if(lists!=null&&!lists.isEmpty()){
					adapter.addMoreData(lists);
				  }else{
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		if (id == -2) //更多
		{
			//进度条
		    moreprocess = arg1.findViewById(R.id.footer_progress);
			moreprocess.setVisibility(View.VISIBLE);
			//请求网络更多
			getHomePage(page+1,Constant.DEFAULT_COUNT,GETMORE);
			page = page+1;
		}else{
			//可滑动的listview需要 position-1
			ShortBook book = adapter.getLists().get(position-1);
			if(book!=null){
				Log.i("ResultOnSearchActivity",book.toString());
				Intent _intent = new Intent(context,DetailAdvancedBookActivity.class);
				_intent.putExtra("id", book.getId());
				
				_intent.putExtra("type", type);
				startActivity(_intent);
			}
			}
		}

	
	private void getHomePage(int page, int defaultCount,int mwhat) {
		customProgressDialog.show();
		gparams=new HashMap<String, String>();
		gparams.put("libid", GlobleData.LIBIRY_ID);
		gparams.put("curpage", ""+page);
		gparams.put("perpage",""+ Constant.DEFAULT_COUNT);

		
		switch(type){
		case Constant.HOTBOOK:
			gparams.put("announcetypeid", ""+3);		
			break;
		case Constant.NEWBOOK:
			gparams.put("announcetypeid", ""+4);		
			break;
		}
		if(mwhat == GETHOMEPAGE){
			requestVolley(GlobleData.SERVER_URL
					+ "/library/announce/list.aspx", backlistener,
					Method.POST);
		}else{
			requestVolley(GlobleData.SERVER_URL
					+ "/library/announce/list.aspx", backlistenermore,
					Method.POST);
		}
		
	}


	@Override
	public void onRefresh() {
		getHomePage(1, Constant.DEFAULT_COUNT,GETHOMEPAGE);
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}
			//刷新完成
			@Override
			protected void onPostExecute(Void result) {
				
				listview.onRefreshComplete();

			}

		}.execute(null, null);
		
	}
		
	
  
}
