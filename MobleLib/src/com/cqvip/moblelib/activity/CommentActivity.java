package com.cqvip.moblelib.activity;

import java.util.HashMap;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

public class CommentActivity extends BaseActivity implements
		IBookManagerActivity, OnClickListener {
	public static final int ADDCOMMENT = 1;

	private TextView intro_tv;
	private EditText comment_et;
	private Button commit_btn;
	private Book dBook;
	private ListView comments_lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		init();
		commit_btn.setOnClickListener(this);
		
		BaseAdapter adapter=new CommentAdapter(this);
		comments_lv.setAdapter(adapter);

		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book) bundle.getSerializable("book");
		intro_tv.setText("《" + dBook.getU_title() + "》\n"
				+ getString(R.string.item_author) + dBook.getAuthor() + "\n"
				+ getString(R.string.item_publish)
				+ dBook.getU_publish()
				+ "\n"
				+ getString(R.string.item_subject)
				+ dBook.getSubject()
				+ "\n"
				// +getString(R.string.item_callno)+dBook.getCallno()+"\n"
				// +getString(R.string.item_classno)+dBook.getClassno()+"\n"
				+ "ISBN:" + dBook.getIsbn() + "\n"
				+ getString(R.string.item_price) + dBook.getU_price() + "\n"
				+ dBook.getU_abstract());
	}

	@Override
	public void init() {
		comments_lv = (ListView) findViewById(R.id.comment_lv);
		intro_tv = (TextView) findViewById(R.id.intro_tv);
		commit_btn = (Button) findViewById(R.id.commit_btn);
		comment_et = (EditText) findViewById(R.id.comment_et);
		ManagerService.allActivity.add(this);
		customProgressDialog=new CustomProgressDialog(this);
	}

	@Override
	public void onClick(View v) {
		if (dBook != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("libid", GlobleData.LIBIRY_ID);
			map.put("vipuserid", GlobleData.cqvipid);
			Log.i("添加评论", GlobleData.cqvipid);
			map.put("keyid", dBook.getCallno());
			Log.i("keyid", dBook.getCallno());
			map.put("typeid", "" + GlobleData.BOOK_SZ_TYPE);
			ManagerService.addNewTask(new Task(Task.TASK_ADD_COMMENT, map));
			customProgressDialog.show();
		}
	}

	@Override
	public void refresh(Object... obj) {
		customProgressDialog.dismiss();
		int type = (Integer) obj[0];
		// 判断收藏是否成功
		if (type == ADDCOMMENT) {
			Result res = (Result) obj[1];
			if (res.getSuccess()) {
				Tool.ShowMessages(this, "添加成功");
			} else {
				Tool.ShowMessages(this, "添加失败");
			}
			return;
		}
	}

	  static class ViewHolder{
			TextView discussant_tv;
			TextView content_tv;
			TextView time_tv;
			}
	
	public class CommentAdapter extends BaseAdapter {
		private Context context;

		public CommentAdapter(Context context) {
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder ;
			//更多
			if (position == this.getCount() - 1) {
				convertView = LayoutInflater.from(context).inflate(R.layout.moreitemsview, null);
				return convertView;
			}
			if(convertView==null||convertView.findViewById(R.id.linemore) != null){
				convertView=LayoutInflater.from(context).inflate(R.layout.item_comment, null);
				holder = new ViewHolder();
				
				holder.discussant_tv = (TextView) convertView.findViewById(R.id.discussant_tv);
				holder.content_tv = (TextView) convertView.findViewById(R.id.content_tv);
				holder.time_tv = (TextView) convertView.findViewById(R.id.time_tv);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
//			holder.discussant_tv.setText(text);
//			holder.content_tv.setText(text);
//			holder.time_tv.setText(text);
			return convertView;
		}

	}
}
