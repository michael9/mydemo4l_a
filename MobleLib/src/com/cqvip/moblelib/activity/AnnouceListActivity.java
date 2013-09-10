package com.cqvip.moblelib.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.mobelib.imgutils.AsyncTask;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.view.DownFreshListView;
import com.cqvip.moblelib.view.DropDownListView;
import com.cqvip.moblelib.view.DropDownListView.OnDropDownListener;
import com.cqvip.utils.Tool;

public class AnnouceListActivity extends BaseActivity implements OnItemClickListener{

	private  final int GETMORE = 1;
	private  final int GETHOMEPAGE = 0;
	private DropDownListView listview;
	private int type;
	private Context context;
	private int page=1;
	private View moreprocess;
	private MyNewAdapter adapter;
	private Map<String, String> gparams;
	private int sendtype;
	//private boolean isFirstLoad = false; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_annouce_list);
		
		context = this;
		type = getIntent().getIntExtra("type", 1);
		listview = (DropDownListView) findViewById(R.id.listview_new);
		listview.setOnItemClickListener(this);
		  // set drop down listener
//        listview.setOnDropDownListener(new OnDropDownListener() {
//
//            @Override
//            public void onDropDown() {
//            	page = 1;//重置page
//            	isFirstLoad = false;
//        		getHomePage(page, Constant.DEFAULT_COUNT,GETHOMEPAGE);
//            }
//        });

        // set on bottom listener
        listview.setOnBottomListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
            	getHomePage(page+1,Constant.DEFAULT_COUNT,GETMORE);
    			page = page+1;
            }
        });
		
		
		adapter = new MyNewAdapter(context,null);
		
		switch (type) {
		case  Constant.SPEECH_NEWS:
			sendtype=Task.TASK_ANNOUNCE_NEWS;
			setheadbar(getResources().getString(R.string.announce_out));
			break;
			
		case  Constant.SPPECH_FREE:
			sendtype=Task.TASK_ANNOUNCE_WELFARE;
			setheadbar(getResources().getString(R.string.free_speech));
			break;
		case  Constant.QUESTION:
			sendtype=Task.TASK_E_CAUTION;
			setheadbar(getResources().getString(R.string.guide_problem));
			break;
			
		default:
			break;
		}
		customProgressDialog.show();
		//isFirstLoad = true;
		getHomePage(page, Constant.DEFAULT_COUNT,GETHOMEPAGE);
	}
	
	private void setheadbar(String title)
	{
		View headbar,btn_back;
		TextView bar_title;
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
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
//			 if(!isFirstLoad){
//			 SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm:ss");
//             listview.onDropDownComplete(getString(R.string.update_at) + dateFormat.format(new Date()));
//			 }
			try {
				List<ShortBook> lists= ShortBook.formList(sendtype, response);
				if(lists!=null&&!lists.isEmpty()){
					adapter = new MyNewAdapter(context,lists);
					if(lists.size()<Constant.DEFAULT_COUNT){
						listview.setHasMore(false);
						listview.setAdapter(adapter);
						listview.onBottomComplete();
					}else{
						listview.setHasMore(true);
						listview.setAdapter(adapter);
					}
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
			
			try {
				List<ShortBook> lists= ShortBook.formList(sendtype, response);
				if(lists!=null&&!lists.isEmpty()){
					adapter.addMoreData(lists);
					listview.onBottomComplete();
				  }else{
						//Tool.ShowMessages(context, getResources().getString(R.string.tips_nomore_data));
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
			// TODO: handle exception
		}
	}
	
	private void getHomePage(int page, int defaultCount,int mwhat) {
		gparams=new HashMap<String, String>();
		gparams.put("libid", GlobleData.LIBIRY_ID);
		gparams.put("curpage", ""+page);
		gparams.put("perpage",""+ Constant.DEFAULT_TEXT_COUNT);
		
		switch(type){
		case Constant.SPEECH_NEWS://新闻动态
			gparams.put("announcetypeid", ""+2);		
			if(mwhat == GETHOMEPAGE){
				requestVolley(GlobleData.SERVER_URL
						+ "/library/announce/list.aspx", backlistener,
						Method.POST);
			}else{
				requestVolley(GlobleData.SERVER_URL
						+ "/library/announce/list.aspx", backlistenermore,
						Method.POST);
			}
			break;
		case Constant.SPPECH_FREE://公益讲座
			gparams.put("announcetypeid", ""+1);		
			if(mwhat == GETHOMEPAGE){
				requestVolley(GlobleData.SERVER_URL
						+ "/library/announce/list.aspx", backlistener,
						Method.POST);
			}else{
				requestVolley(GlobleData.SERVER_URL
						+ "/library/announce/list.aspx", backlistenermore,
						Method.POST);
			}
			break;
		case Constant.QUESTION://常见问题
			gparams.put("announcetypeid", ""+5);			
			if(mwhat == GETHOMEPAGE){
				requestVolley(GlobleData.SERVER_URL
						+ "/library/announce/html.aspx", backlistener,
						Method.POST);
			}else{
				requestVolley(GlobleData.SERVER_URL
						+ "/library/announce/html.aspx", backlistenermore,
						Method.POST);
			}
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.annouce_list, menu);
		return true;
	}
	class MyNewAdapter extends BaseAdapter{
		private Context context;
		private List<ShortBook> mlists;
		public MyNewAdapter(Context con){
			context = con;
		}
		public MyNewAdapter(Context context,
				List<ShortBook> lists) {
			this.context = context;
			this.mlists = lists;
		}
	
		public void addMoreData(List<ShortBook> moreStatus)
		{
			this.mlists.addAll(moreStatus);//把新数据增加到原有集合
			this.notifyDataSetChanged();
		}
		@Override
		public int getCount() {
			 if(mlists!=null){
					
					return mlists.size();
				}
				return 0;
			}
		public List<ShortBook> getList(){
			return mlists;
		}
		
		@Override
		public Object getItem(int position) {
			return mlists.get(position);
		}

		@Override
		public long getItemId(int position) {
			if(this.getCount()>0&&position < this.getCount()){
				return position;
			}else{
				return 0;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView==null){
				convertView = LayoutInflater.from(context).inflate(R.layout.item_news, null);
			}
			TextView tx = (TextView)convertView.findViewById(R.id.tv_item_topic);
			tx.setText(mlists.get(position).getMessage());
			return convertView;
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			ShortBook book = adapter.getList().get(position-1);
			if(book!=null){
				Intent _intent = new Intent(context,DetailAdvancedBookActivity.class);
				_intent.putExtra("id", book.getId());
				_intent.putExtra("type", type);
				startActivity(_intent);
			}
	}


}
