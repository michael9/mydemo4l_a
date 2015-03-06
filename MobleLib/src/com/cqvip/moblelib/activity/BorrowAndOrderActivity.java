package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.adapter.BorrowBookAdapter;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.utils.HttpUtils;
import com.cqvip.utils.Tool;

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
	private ListView listview;
	private BorrowBookAdapter adapter;
	private List<BorrowBook>  lists = new ArrayList<BorrowBook>();
	private RelativeLayout noborrow_rl;
	private Map<String, String> gparams;
	private static final int RENEWSUSS = 0;
	private static final int RENEWFAIL = 0;
	
	private Handler handler = new Handler() {
		
		 @Override
	        public void handleMessage(Message message) {
			 Bundle bundle = message.getData();
			 	switch (message.what) {
				case RENEWSUSS:
					Tool.ShowMessages(BorrowAndOrderActivity.this, bundle.getString("msg"));
					adapter.notifyDataSetChanged();
					break;
				default:
					Tool.ShowMessages(BorrowAndOrderActivity.this, bundle.getString("msg"));
					break;
				}
			 	
		 }
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_borrow_and_order2);
		ImageView back = (ImageView)findViewById(R.id.return_iv);
		noborrow_rl = (RelativeLayout) findViewById(R.id.noborrow_rl);
		listview = (ListView)findViewById(R.id.borrow_list);
		getlist();
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try {
				lists = BorrowBook.formList(response);
				if(lists==null||lists.isEmpty()){
					listview.setVisibility(View.GONE);
					noborrow_rl.setVisibility(View.VISIBLE);
				}else {
					listview.setVisibility(View.VISIBLE);
					noborrow_rl.setVisibility(View.GONE);
					adapter = new BorrowBookAdapter(BorrowAndOrderActivity.this,lists,mQueue,cl_renew,el);
					listview.setAdapter(adapter);
				}			
			} catch (Exception e) {
				onError(2);
				return;
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
			mys.setRetryPolicy(HttpUtils.setTimeout());mQueue.add(mys);
			mQueue.start();
		} catch (Exception e) {
			onError(2);
		}
	}

  private void getlist() {
	  customProgressDialog.show();
	  gparams=new HashMap<String, String>();
	  gparams.put("libid", GlobleData.LIBIRY_ID);	  
	 // gparams.put("userid", GlobleData.readerid);	  
	   gparams.put("userid","109248");	  
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
	        	  if(customProgressDialog!=null&&customProgressDialog.isShowing())
	        		customProgressDialog.dismiss();
	        	  Log.i("book",response);
	    			try {
	    				ShortBook result = new ShortBook(Task.TASK_BOOK_RENEW,response);
	    				 Log.i("book",result.toString());
	    				 Log.i("book",lists.size()+","+lists.toString());
	    				 Message msg = new Message();
	    				 Bundle bundle = new Bundle();
	    					bundle.putString("msg", result .getMessage());
	    					msg.setData(bundle);
	    				if(result.getSucesss().equals("true")){
	    					if(lists==null||lists.isEmpty()){
	    						for(int i=0;i<lists.size();i++){
	    							BorrowBook book	= lists.get(i);
	    					if(book.getBarcode().equals(result.getId())){
	    						book.setRenew(1);
	    						book.setReturndate(result.getDate()+getResources().getString(R.string.alreadyrenew));
	    						msg.what = RENEW;
	    						handler.sendMessage(msg);
	    						 Log.i("book",book.toString());
	    						break;
	    					}
	    				  }
	    				}
	    					//Tool.ShowMessages(BorrowAndOrderActivity.this, result.getMessage());
	    				}
	    				msg.what = RENEWFAIL;
	    				handler.sendMessage(msg);
	    				
	    			} catch (Exception e) {
	    				e.printStackTrace();
	    				onError(2);
	    				return;
	    			}

	          }
	      };

	
}
