package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.AdvancedBookAdapter;
import com.cqvip.moblelib.adapter.BookAdapter;
import com.cqvip.moblelib.adapter.BorrowBookAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * <p>
 * 文件名称: BorrowAndOrderActivity.java
 * 文件描述: 借阅管理
 * 版权所有: 版权所有(C)2013-2020
 * 公          司: 重庆维普咨询有限公司
 * 内容摘要: 
 * 其他说明:
 * 完成日期： 201年5月10日
 * 修改记录: 
 * </p>
 * 
 * @author LHP,LJ
 */
public class BorrowAndOrderActivity extends BaseActivity {

	public static final int BORROWLIST = 1;
	public static final int RENEW = 2;
	private ViewPager mPager;//页卡内容
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView t1, t2;// 页卡头标
	private int currIndex = 0;// 当前页卡编号
	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	private ListView listview;
	private BorrowBookAdapter adapter;
	private List<BorrowBook>  lists;
	private RelativeLayout noborrow_rl;
	private Map<String, String> gparams;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow_and_order2);
		View v = findViewById(R.id.borrow_title);
		TextView title = (TextView)v.findViewById(R.id.txt_header);
		title.setText(R.string.main_borrow);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		noborrow_rl = (RelativeLayout) findViewById(R.id.noborrow_rl);
//		if(adapter!=null){
//			
//			
//		}
		listview = (ListView)findViewById(R.id.borrow_list);
		getlist();
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		history.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//			}
//		});
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int postion,
					long id) {
				
				
			}
		}) ;
	}

	private Listener<String> borrowlist_ls = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			try {
				List<BorrowBook>lists = BorrowBook.formList(response);
				if(lists==null||lists.isEmpty()){
					listview.setVisibility(View.GONE);
					noborrow_rl.setVisibility(View.VISIBLE);
				}else {
					listview.setVisibility(View.VISIBLE);
					noborrow_rl.setVisibility(View.GONE);
					adapter = new BorrowBookAdapter(BorrowAndOrderActivity.this,lists,mQueue,cl_renew,el_new);
					listview.setAdapter(adapter);
				}			
			} catch (Exception e) {
				// TODO: handle exception
				return;
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

  private void getlist() {
	  customProgressDialog.show();
	  gparams=new HashMap<String, String>();
	  gparams.put("userid", GlobleData.readerid);	  
		requestVolley(GlobleData.SERVER_URL
				+ "/library/user/borrowlist.aspx", borrowlist_ls,
				Method.POST);
}
	  /**
	   * 续借成功回调
	   * @return
	   */
  	  private Response.Listener<String> cl_renew = new Response.Listener<String>() {
	          @Override
	          public void onResponse(String response) {
	        		customProgressDialog.dismiss();
	    			try {
	    				ShortBook result = new ShortBook(Task.TASK_BOOK_RENEW,response);
	    				if(result!=null){
	    				if(result.getSucesss().equals("true")){
	    				for(int i=0;i<lists.size();i++){
	    					if(result.getId().equals(lists.get(i).getBarcode())){
	    						lists.get(i).setRenew(1);
	    						lists.get(i).setReturndate(result.getDate()+getResources().getString(R.string.alreadyrenew));
	    						adapter.notifyDataSetChanged();
	    						break;
	    					}
	    				  }
	    				}
	    				Tool.ShowMessages(BorrowAndOrderActivity.this, result.getMessage());
	    			}
	    			} catch (Exception e) {
	    				return;
	    			}

	          }
	      };
  	  /**
  	   * 续借异常
  	   * @return
  	   */
	  private Response.ErrorListener el_new = new Response.ErrorListener() {
	          @Override
	          public void onErrorResponse(VolleyError error) {
	        	  customProgressDialog.dismiss();
	        	  //提示用户异常
	          }
	      };


//	@Override
//	public void refresh(Object... obj) {
//		customProgressDialog.dismiss();
//		Integer type = (Integer)obj[0];
//		switch(type){
//		case BORROWLIST:
//			lists = (List<BorrowBook>)obj[1];
//			if(lists==null||lists.isEmpty()){
//				listview.setVisibility(View.GONE);
//				noborrow_rl.setVisibility(View.VISIBLE);
//			   //Tool.ShowMessages(context, "查询无记录");
//			}else {
//				listview.setVisibility(View.VISIBLE);
//				noborrow_rl.setVisibility(View.GONE);
//				adapter = new BorrowBookAdapter(BorrowAndOrderActivity.this,lists);
//				listview.setAdapter(adapter);
//			}
//			break;
//			
//		case RENEW:
//			//成功
//			ShortBook result = (ShortBook)obj[1];
//			if(result!=null){
////				if(result.getSucesss().equals("true")){
//				for(int i=0;i<lists.size();i++){
//					if(result.getId().equals(lists.get(i).getBarcode())){
//						lists.get(i).setRenew(1);
//						lists.get(i).setReturndate(result.getDate()+getResources().getString(R.string.alreadyrenew));
//						adapter.notifyDataSetChanged();
//						break;
//					}
//				  }
////				}
//				Tool.ShowMessages(BorrowAndOrderActivity.this, result.getMessage());
//			}
//			break;
//			
//		}
//	}
	
}
