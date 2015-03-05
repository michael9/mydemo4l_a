package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.adapter.BorrowBookAdapter;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.moblelib.model.ShortBook;
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
	private List<BorrowBook>  lists;
	private RelativeLayout noborrow_rl;
	private Map<String, String> gparams;
	
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
				onError(2);
				return;
			}
		}
	};
	
	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
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
			onError(2);
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
	        	  if(customProgressDialog!=null&&customProgressDialog.isShowing())
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
	    				onError(2);
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
	        	  if(customProgressDialog!=null&&customProgressDialog.isShowing())
	        	  customProgressDialog.dismiss();
	        	  //提示用户异常
	        	  onError(2);
	          }
	      };

	
}
