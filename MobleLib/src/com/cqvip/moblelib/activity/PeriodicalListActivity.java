package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.PeriodicalAdapter;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Periodical;
import com.cqvip.moblelib.view.DropDownListView;
import com.cqvip.utils.Tool;

/**
 * 模拟期刊列表显示
 * @author luojiang
 *
 */
public class PeriodicalListActivity extends BaseImageActivity implements
		OnItemClickListener {

	public static final int GETFIRSTPAGE = 1;
	public static final int GETNEXTPAGE = 2;
	public static final int FAVOR = 3;
	public static final int DEFAULT_COUNT = Constant.DEFAULT_COUNT;
	private TextView searchCount;
	private Context context;
	private DropDownListView listview;
	// private String editekey;
	private int page = 1;
	private PeriodicalAdapter adapter;
	private RelativeLayout noResult_rl;
	private View title_bar;
	// private ImageFetcher mImageFetcher;
	private Map<String, String> gparams;
	private String classid;
	
	public static HashMap<String, Boolean> favors = new HashMap<String, Boolean>();// 保持收藏状态，更新界面

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_periodical_list);
		context = this;
		searchCount = (TextView) findViewById(R.id.txt_total_esearch);
		listview = (DropDownListView) findViewById(R.id.search_res_lv);
		listview.setOnItemClickListener((OnItemClickListener) this);
		noResult_rl = (RelativeLayout) findViewById(R.id.noresult_rl);
		//获取更多
		listview.setOnBottomListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getHomePage(classid, page + 1,
						DEFAULT_COUNT, 1);
				page = page + 1;
			}
		});
		title_bar = findViewById(R.id.head_bar);
		TextView title = (TextView) title_bar.findViewById(R.id.txt_header);
		title.setText(R.string.main_periodical);
		ImageView back = (ImageView) title_bar
				.findViewById(R.id.img_back_header);
		
		classid = getIntent().getStringExtra("classid");
		
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		getHomePage(classid, page, DEFAULT_COUNT, 0);
	}


	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try {
				// 获取返回记录数
				int count = Periodical.getCount(response);
				if (count > 0) {
					searchCount.setVisibility(View.VISIBLE);
					searchCount.setText("共计搜索到" + count + "条记录");
				} else {
					searchCount.setVisibility(View.GONE);
				}
				// JSONObject mj=new JSONObject(response);
				Periodical temp =Periodical.formObject(response,Task.TASK_PERIODICAL_SUBTYPE);
				List<Periodical> lists = temp.qklist;
				if (lists != null && !lists.isEmpty()) {
					listview.setVisibility(View.VISIBLE);
					noResult_rl.setVisibility(View.GONE);
					adapter = new PeriodicalAdapter(context, lists,mImageFetcher);
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
				Periodical temp =Periodical.formObject(response,Task.TASK_PERIODICAL_SUBTYPE);
				List<Periodical> lists = temp.qklist;
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
		gparams.put("classid", key);
		gparams.put("perpage", count+"");
		gparams.put("curpage", page+"");
		
		if (type == 0) {
			requestVolley(GlobleData.SERVER_URL + "/qk/search.aspx",
					backlistener, Method.POST);
		} else {
			requestVolley(GlobleData.SERVER_URL + "/qk/search.aspx",
					backlistenermore, Method.POST); 
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {
			Periodical periodical = adapter.getLists().get(positon);
			// Book book = lists.get(position-1);
			 if(periodical!=null){
			Intent _intent = new Intent(context,PeriodicalContentActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("periodical", periodical);
			_intent.putExtra("detaiinfo", bundle);
		    context.startActivity(_intent);
			 }
		}
}