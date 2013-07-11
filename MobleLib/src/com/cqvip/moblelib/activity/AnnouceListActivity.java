package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cqvip.mobelib.imgutils.AsyncTask;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.view.DownFreshListView;
import com.cqvip.utils.Tool;

public class AnnouceListActivity extends BaseActivity implements IBookManagerActivity,OnItemClickListener,DownFreshListView.OnRefreshListener{

	private static final int GETMORE = 1;
	private static final int GETHOMEPAGE = 0;
	private DownFreshListView listview;
	private int type;
	private Context context;
	private int page=1;
	private View moreprocess;
	private MyNewAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_annouce_list);
		
		context = this;
		type = getIntent().getIntExtra("type", 1);
		listview = (DownFreshListView) findViewById(R.id.listview_new);
		listview.setOnItemClickListener(this);
		listview.setOnRefreshListener(this);
		adapter = new MyNewAdapter(context,null);
		customProgressDialog.show();
		switch (type) {
		case  Constant.SPEECH_NEWS:
			setheadbar("新闻动态");
			break;
			
		case  Constant.SPPECH_FREE:
			setheadbar("公益讲座");
			break;

		default:
			break;
		}
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

	private void getHomePage(int page, int defaultCount,int mwhat) {
		if(!ManagerService.allActivity.contains(this)){
			ManagerService.allActivity.add(this);
			}
		HashMap map=new HashMap();
		map.put("page",page+"");
		map.put("count", Constant.DEFAULT_COUNT+"");
		switch(type){
		case Constant.SPEECH_NEWS://新闻动态
			if(mwhat == GETHOMEPAGE){
			ManagerService.addNewTask(new Task(Task.TASK_ANNOUNCE_NEWS,map));
			}else{
				ManagerService.addNewTask(new Task(Task.TASK_ANNOUNCE_NEWS_MORE,map));
			}
			break;
		case Constant.SPPECH_FREE://公益讲座
			if(mwhat == GETHOMEPAGE){
			ManagerService.addNewTask(new Task(Task.TASK_ANNOUNCE_WELFARE,map));
			}else{
			ManagerService.addNewTask(new Task(Task.TASK_ANNOUNCE_WELFARE_MORE,map));
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
					
					return mlists.size()+1;
				}
				return 1;
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
			if((this.getCount()-1)>0&&position < (this.getCount()-1)){
				return position;
			}else{
				return -2;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//更多
			if (position == this.getCount() - 1) {
				convertView = LayoutInflater.from(context).inflate(R.layout.moreitemsview, null);
				return convertView;
			}
			
			if(convertView==null||convertView.findViewById(R.id.linemore) != null){
				convertView = LayoutInflater.from(context).inflate(R.layout.item_news, null);
				
				TextView tx = (TextView)convertView.findViewById(R.id.tv_item_topic);
				
				tx.setText(mlists.get(position).getMessage());
			}
			return convertView;
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
			ShortBook book = adapter.getList().get(position-1);
			if(book!=null){
				Log.i("ResultOnSearchActivity",book.toString());
				Intent _intent = new Intent(context,DetailAdvancedBookActivity.class);
				_intent.putExtra("id", book.getId());
				_intent.putExtra("type", type);
				startActivity(_intent);
			}
		}
		
		
	}

	@Override
	public void init() {
		
	}

	@Override
	public void refresh(Object... obj) {
		customProgressDialog.dismiss();
		Integer state = (Integer)obj[0];
		List<ShortBook> lists=(List<ShortBook>)obj[1];
		switch(state){
		case Task.TASK_ANNOUNCE_NEWS:
		case Task.TASK_ANNOUNCE_WELFARE:
			
			if(lists!=null&&!lists.isEmpty()){
			adapter = new MyNewAdapter(context,lists);
			listview.setAdapter(adapter);
			}
			//TODO
			break;
		case Task.TASK_ANNOUNCE_NEWS_MORE:
		case Task.TASK_ANNOUNCE_WELFARE_MORE:
			moreprocess.setVisibility(View.GONE);
			if(lists!=null&&!lists.isEmpty()){
				adapter.addMoreData(lists);
			  }else{
					Tool.ShowMessages(context, "没有更多内容可供加载");
				}
			break;
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
