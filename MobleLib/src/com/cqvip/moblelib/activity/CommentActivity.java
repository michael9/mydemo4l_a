package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.mobelib.imgutils.AsyncTask;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.CommentItemAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.Comment;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.view.DownFreshListView;
import com.cqvip.utils.Tool;

public class CommentActivity extends BaseActivity implements
		IBookManagerActivity, OnClickListener,OnItemClickListener,DownFreshListView.OnRefreshListener {
	public static final int ADDCOMMENT = 1;
	private static final int GETMORE = 1;
	private static final int GETHOMEPAGE = 0;
	
	private TextView baseinfo_tv,intro_tv;
	private EditText comment_et;
	private Button commit_btn;
	private Book dBook;
	private DownFreshListView listview;
	private int typeid;
	private CommentItemAdapter adapter;
	private int delFlag;
	private int page = 1;
	private Context context;
	private View moreprocess;
	private String keyid;//书籍唯一id
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		context = this;
		init();
		commit_btn.setOnClickListener(this);
		
		//获取书籍详细对象
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book) bundle.getSerializable("book");
		//获取书籍列席
		typeid = getIntent().getIntExtra("type", GlobleData.BOOK_SZ_TYPE);
		//获取是否删除表识
		delFlag = getIntent().getIntExtra("flag", 0);
		//书籍id
	    keyid = getLngId(dBook.getCallno(),dBook.getRecordid());
		getHomeComment(typeid,keyid,page,Constant.DEFAULT_COUNT,GETHOMEPAGE);
		String describe=dBook.getU_abstract();
		if(TextUtils.isEmpty(describe)){
			describe="无";
		}
		
		baseinfo_tv.setText("《" + dBook.getTitle() + "》\n"
				+ getString(R.string.item_author) + dBook.getAuthor());
		intro_tv.setText(getString(R.string.item_describe)+describe);
	}

	@Override
	public void init() {
		listview = (DownFreshListView) findViewById(R.id.comment_lv);
		listview.setOnItemClickListener(this);
		listview.setOnRefreshListener(this);
		baseinfo_tv = (TextView) findViewById(R.id.baseinfo_tv);
		intro_tv= (TextView) findViewById(R.id.intro_tv);
		commit_btn = (Button) findViewById(R.id.commit_btn);
		comment_et = (EditText) findViewById(R.id.comment_et);
		TextView title = (TextView)findViewById(R.id.txt_header);
		title.setText(R.string.book_comment);
		ImageView back = (ImageView)findViewById(R.id.img_back_header);
		adapter = new CommentItemAdapter(context,null);
		//加载数据
		//getHomeComment(1,Constant.DEFAULT_COUNT);
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}

	private void getHomeComment(int typeid,String keyid,int page, int count,int more) {
		if(!ManagerService.allActivity.contains(this)){
			ManagerService.allActivity.add(this);
			}
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("typeid", typeid+"");
		map.put("keyid",keyid);
		map.put("page", page+"");
		map.put("count", count+"");
		if(more == GETHOMEPAGE){
		ManagerService.addNewTask(new Task(Task.TASK_COMMENT_LIST, map));
		}else{
		ManagerService.addNewTask(new Task(Task.TASK_COMMENT_LIST_MORE, map));
		}
	}

	private String getLngId(String callno, String recordid) {
		if(typeid == GlobleData.BOOK_SZ_TYPE){
			return callno+","+recordid;
		}else {//中刊返回lngid
				return callno;
		}
	}

	@Override
	public void onClick(View v) {
		String info=comment_et.getText().toString().trim();
		if(TextUtils.isEmpty(info)){
			Tool.ShowMessages(this, "评论内容不能空");
			return;
		}
		if (dBook != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("libid", GlobleData.LIBIRY_ID);
			map.put("vipuserid", GlobleData.cqvipid);
			Log.i("添加评论", GlobleData.cqvipid);
			map.put("keyid", dBook.getCallno());
			Log.i("CommentActivity_keyid", dBook.getCallno());
			map.put("typeid", "" + typeid);
			map.put("recordid", getTypeComment(dBook));
			map.put("content", info);
			ManagerService.addNewTask(new Task(Task.TASK_ADD_COMMENT, map));
			customProgressDialog.show();
		}
	}

	private String getTypeComment(Book dBook2) {
		if(typeid == GlobleData.BOOK_SZ_TYPE){
			return dBook2.getRecordid();
		}else {
			return null;
		}
	}

	@Override
	public void refresh(Object... obj) {
		customProgressDialog.dismiss();
		int type = (Integer) obj[0];
		switch(type){
		case Task.TASK_ADD_COMMENT:
			Result res = (Result) obj[1];
			if (res.getSuccess()) {
				//提示
				Tool.ShowMessages(this, "添加成功");
				//更新列表
				getHomeComment(typeid,keyid,1,Constant.DEFAULT_COUNT,GETHOMEPAGE);
			} else {
				Tool.ShowMessages(this, "添加失败");
			}
			break;
		case Task.TASK_COMMENT_LIST:
			 List<Comment> lists =(List<Comment>)obj[1];
			if(lists!=null&&!lists.isEmpty()){
				adapter = new CommentItemAdapter(context,lists);
				listview.setAdapter(adapter);
				}
				//TODO
				break;
		case Task.TASK_COMMENT_LIST_MORE:
			List<Comment> lists1 =(List<Comment>)obj[1];
				moreprocess.setVisibility(View.GONE);
				if(lists1!=null&&!lists1.isEmpty()){
					adapter.addMoreData(lists1);
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
		    moreprocess = arg1.findViewById(R.id.footer_progress);
			moreprocess.setVisibility(View.VISIBLE);
			//请求网络更多
			getHomeComment(typeid,keyid,page+1,Constant.DEFAULT_COUNT,GETMORE);
			page = page+1;
		
		}
		
	}

	@Override
	public void onRefresh() {
		getHomeComment(typeid,keyid,1,Constant.DEFAULT_COUNT,GETHOMEPAGE);
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
