package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.AdvancedBookAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.utils.Tool;

public class AnnouceListActivity extends BaseActivity implements IBookManagerActivity,OnItemClickListener{

	private ListView listview;
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
		
		listview = (ListView) findViewById(R.id.listview_new);
		adapter = new MyNewAdapter(context,null);
		ManagerService.allActivity.add(this);
		getHomePage(page, Constant.DEFAULT_COUNT);
		
	}

	private void getHomePage(int page, int defaultCount) {
		HashMap map=new HashMap();
		map.put("page",page);
		map.put("count", Constant.DEFAULT_COUNT);
		switch(type){
		case Constant.SPEECH_NEWS://新闻动态
			ManagerService.addNewTask(new Task(Task.TASK_ANNOUNCE_NEWS,map));
			break;
		case Constant.SPPECH_FREE://公益讲座
			ManagerService.addNewTask(new Task(Task.TASK_ANNOUNCE_WELFARE,map));
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
		private List<ShortBook> lists;
		
		public MyNewAdapter(Context con){
			context = con;
		}
		public MyNewAdapter(Context context,
				List<ShortBook> lists) {
			this.context = context;
			this.lists = lists;
		}
	
		public void addMoreData(List<ShortBook> moreStatus)
		{
			this.lists.addAll(moreStatus);//把新数据增加到原有集合
			this.notifyDataSetChanged();
		}
		@Override
		public int getCount() {

			 if(lists!=null){
					
					return lists.size()+1;
				}
				return 0;
			
		}
		public List<ShortBook> getList(){
			return lists;
		}
		
		@Override
		public Object getItem(int position) {
			return lists.get(position);
		}

		@Override
		public long getItemId(int position) {
			if(position < (this.getCount()-1)){
				return position;
			}else{
				return -2;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			//更多
			TextView tx = null;
			if (position == this.getCount() - 1) {
				convertView = LayoutInflater.from(context).inflate(R.layout.moreitemsview, null);
				return convertView;
			}
			
			if(convertView==null||convertView.findViewById(R.id.linemore) != null){
				convertView = LayoutInflater.from(context).inflate(R.layout.item_news, null);
				tx = (TextView)convertView.findViewById(R.id.tv_item_topic);
				
			}
			tx.setText(lists.get(position).getDate());
			return convertView;
		}
		
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
		if (id == -2) //更多
		{
			//进度条
			View moreprocess = arg1.findViewById(R.id.footer_progress);
			moreprocess.setVisibility(View.VISIBLE);
			//请求网络更多
			getHomePage(page+1,Constant.DEFAULT_COUNT);
			page = page+1;
		}else{
			ShortBook book = adapter.getList().get(position);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void refresh(Object... obj) {
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
		case Task.TASK_SUGGEST_HOTBOOK_MORE:
		case Task.TASK_SUGGEST_NEWBOOK_MORE:
			moreprocess.setVisibility(View.GONE);
			if(lists!=null&&!lists.isEmpty()){
				adapter.addMoreData(lists);
			  }else{
					Tool.ShowMessages(context, "没有更多内容可供加载");
				}
			break;
		}
		
	}
}
