package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

import org.xml.sax.DTDHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.AdvancedBookAdapter;
import com.cqvip.moblelib.adapter.BookAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.utils.Tool;

public class AdvancedBookActivity extends BaseActivity implements IBookManagerActivity,OnItemClickListener {

	private  AdvancedBookAdapter adapter;
	private ListView listview;
	private int type;
	private Context context;
	private int page=1;
	private View moreprocess;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_advanced_book);
		context = this;
		type = getIntent().getIntExtra("type", 1);
		
		listview = (ListView) findViewById(R.id.listview_abook);
		adapter = new AdvancedBookAdapter(context,null);
		ManagerService.allActivity.add(this);
		//获取列表
		getHomePage(page, Constant.DEFAULT_COUNT);
		
		
		
	}


	@Override
	public void init() {
		
	}

	@Override
	public void refresh(Object... obj) {
		Integer state = (Integer)obj[0];
		List<ShortBook> lists=(List<ShortBook>)obj[1];
		switch(state){
		case Task.TASK_SUGGEST_HOTBOOK:
		case Task.TASK_SUGGEST_NEWBOOK:
			
			if(lists!=null&&!lists.isEmpty()){
			adapter = new AdvancedBookAdapter(context,lists);
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
			ShortBook book = adapter.getLists().get(position);
			if(book!=null){
				Log.i("ResultOnSearchActivity",book.toString());
				Intent _intent = new Intent(context,DetailAdvancedBookActivity.class);
				_intent.putExtra("id", book.getId());
				
				_intent.putExtra("type", type);
				startActivity(_intent);
			}
			}
		}

	
	private void getHomePage(int page, int defaultCount) {
		HashMap map=new HashMap();
		map.put("page",page);
		map.put("count", Constant.DEFAULT_COUNT);
		switch(type){
		case Constant.HOTBOOK:
			ManagerService.addNewTask(new Task(Task.TASK_SUGGEST_HOTBOOK,map));
			break;
		case Constant.NEWBOOK:
			ManagerService.addNewTask(new Task(Task.TASK_SUGGEST_NEWBOOK,map));
			break;
		}
		
	}
		
	
  
}
