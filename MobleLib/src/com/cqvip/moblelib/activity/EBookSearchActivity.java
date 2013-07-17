package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.mobelib.imgutils.ImageCache.ImageCacheParams;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookAdapter;
import com.cqvip.moblelib.adapter.EbookAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

public class EBookSearchActivity extends BaseActivity implements OnItemClickListener {
	
	public static final int GETFIRSTPAGE = 1;
	public static final int GETNEXTPAGE = 2;
	public static final int FAVOR = 3;
	public static final int DEFAULT_COUNT = 10;
	private EditText edit;
	private ImageView imgsearch;
	private Context context;
	private ListView listview;
	private String key;
	private int page=1;
	private EbookAdapter adapter;
	private RelativeLayout noResult_rl;
	private View title_bar;
	private ImageFetcher mImageFetcher;
	private Map<String, String> gparams;
	public static HashMap<String,Boolean> favors = new HashMap<String, Boolean>();//保持收藏状态，更新界面
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_on_search);
		context = this;
		imgsearch = (ImageView)findViewById(R.id.search_seach_btn);
		edit = (EditText)findViewById(R.id.search_et);
		listview = (ListView)findViewById(R.id.search_res_lv);
		listview.setOnItemClickListener((OnItemClickListener)this);
		customProgressDialog=CustomProgressDialog.createDialog(this);
		noResult_rl = (RelativeLayout) findViewById(R.id.noresult_rl);
		//内存占用整个app1/8
		ImageCacheParams cacheParams = new ImageCacheParams(context, GlobleData.IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.125f); // Set memory cache to 12.5% of app memory
		 mImageFetcher = new ImageFetcher(context, getResources().getDimensionPixelSize(R.dimen.bookicon_width),
				   getResources().getDimensionPixelSize(R.dimen.bookicon_height));
	     mImageFetcher.setLoadingImage(R.drawable.defaut_book);
	     mImageFetcher.addImageCache(cacheParams);
		imgsearch.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					hideKeybord();
					if(TextUtils.isEmpty(edit.getText().toString().trim())){
						Tool.ShowMessages(context, "请输入关键字");
						return;
					}
				  //搜索新鲜事
					if(!Tool.checkNetWork(context)){
						return ;
					}
					getHomePage(edit.getText().toString().trim(),page,DEFAULT_COUNT,0);
//					Tool.ShowMessages(context, "开始搜索");
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
					hideKeybord();
					//检查网络
					if(!Tool.checkNetWork(context)){
						return false;
					}
					//网络访问,获取首页
					page = 1;
					getHomePage(edit.getText().toString().trim(),1,DEFAULT_COUNT,0);
					//Tool.ShowMessages(context, "开始搜索");
					return true;
				}

			});
		  
		  title_bar=findViewById(R.id.head_bar);
			TextView title = (TextView)title_bar.findViewById(R.id.txt_header);
			title.setText(R.string.main_ebook);
			ImageView back = (ImageView)title_bar.findViewById(R.id.img_back_header);
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
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		if(imm.isActive()){
			imm.hideSoftInputFromWindow(edit.getWindowToken(), 0);
		}
	}
	
	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			try {
				// JSONObject mj=new JSONObject(response);
				List<EBook> lists = EBook.formList(response);
				if (lists != null && !lists.isEmpty()) {
					listview.setVisibility(View.VISIBLE);
					noResult_rl.setVisibility(View.GONE);
					adapter = new EbookAdapter(context, lists, mImageFetcher);
					listview.setAdapter(adapter);
				} else {
					listview.setVisibility(View.GONE);
					noResult_rl.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	Listener<String> backlistenermore = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
//			moreprocess.setVisibility(View.GONE);
			try {
				// JSONObject mj=new JSONObject(response);
				List<EBook> lists = EBook.formList(response);
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
	/**
	 * 请求网络，获取数据
	 * @param key
	 * @param page
	 * @param count
	 */
	private void getHomePage(String key,int page ,int count,int type) {
		customProgressDialog.show();
//		HashMap map=new HashMap();
//		map.put("key", key);
//		map.put("page", page);
//		map.put("count", count);
//		Task tsHome;
		gparams = new HashMap<String, String>();
		gparams.put("title", key);
		gparams.put("curpage", ""+page);//当前页数
		gparams.put("perpage",""+count );//条数
		
		if(type == 0){
			requestVolley(GlobleData.SERVER_URL
					+ "/zk/search.aspx", backlistener,
					Method.POST);
		}else{
			requestVolley(GlobleData.SERVER_URL
					+ "/zk/search.aspx", backlistenermore,
					Method.POST);
		}
	}

	 @Override
	    public void onResume() {
	        super.onResume();
	        mImageFetcher.setExitTasksEarly(false);
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        mImageFetcher.setPauseWork(false);
	        mImageFetcher.setExitTasksEarly(true);
	        mImageFetcher.flushCache();
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        mImageFetcher.closeCache();
	    }
//	@Override
//	public void refresh(Object... obj) {
//		customProgressDialog.dismiss();
//		hideKeybord();
//		//显示
//		int type = (Integer)obj[0];
//		
//		//判断收藏是否成功
//		 if(type == FAVOR){
//			 Result res = (Result) obj[1];
//			 if (res.getSuccess()) {
//						Tool.ShowMessages(context, "收藏成功");
//			}else{
//						Tool.ShowMessages(context, "收藏失败");
//						}
//						return;
//			}
//		
//		List<EBook> lists = (List<EBook>)obj[1];
//		if(type == GETFIRSTPAGE ){
//		if(lists!=null&&!lists.isEmpty()){
//			listview.setVisibility(View.VISIBLE);
//			noResult_rl.setVisibility(View.GONE);
//			adapter = new EbookAdapter(context,lists,mImageFetcher);
//			listview.setAdapter(adapter);
//			
//		}else{
//			listview.setVisibility(View.GONE);
//			noResult_rl.setVisibility(View.VISIBLE);
//		}
//		}else if(type == GETNEXTPAGE){
//			if(lists!=null&&!lists.isEmpty()){
//			adapter.addMoreData(lists);
//			}else{
//				Tool.ShowMessages(context, "没有更多内容可供加载");
//			}
//		}
//	}
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
			EBook book = adapter.getLists().get(positon);
			if(book!=null){
				Log.i("ResultOnSearchActivity",book.toString());
				Intent _intent = new Intent(context,EbookDetailActivity.class);
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
