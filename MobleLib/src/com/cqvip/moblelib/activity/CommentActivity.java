package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cqvip.mobelib.imgutils.AsyncTask;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.CommentItemAdapter;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.Comment;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.view.DownFreshListView;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;

public class CommentActivity extends BaseActivity implements
		OnClickListener, OnItemClickListener,
		DownFreshListView.OnRefreshListener {
	public static final int ADDCOMMENT = 1;
	private static final int GETMORE = 1;
	private static final int GETHOMEPAGE = 0;

	private TextView baseinfo_tv, intro_tv;
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
	private String keyid;// 书籍唯一id
	private ImageView img;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		context = this;
		init();
		commit_btn.setOnClickListener(this);

		// 获取书籍详细对象
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book) bundle.getSerializable("book");
		// 获取书籍列席
		typeid = getIntent().getIntExtra("type", GlobleData.BOOK_SZ_TYPE);
		// 获取是否删除表识
		delFlag = getIntent().getIntExtra("flag", 0);
		// 书籍id
		keyid = getLngId(dBook.getCallno(), dBook.getRecordid());
		getHomeComment(typeid, keyid, page, Constant.DEFAULT_COUNT, GETHOMEPAGE);
		String describe = dBook.getU_abstract();
		if (TextUtils.isEmpty(describe)) {
			describe = "无";
		}

		baseinfo_tv.setText("《" + dBook.getTitle() + "》\n"
				+ getString(R.string.item_author) + dBook.getAuthor());
		intro_tv.setText(getString(R.string.item_describe) + describe);

		if (!TextUtils.isEmpty(dBook.getCover_path())) {
			//mImageFetcher.loadImage(dBook.getCover_path(), img);
	        ImageLoader mImageLoader = new ImageLoader(mQueue, new BitmapCache());
			ImageListener listener = ImageLoader.getImageListener(img, R.drawable.defaut_book, R.drawable.defaut_book);
			mImageLoader.get(dBook.getCover_path(), listener);
		} else {
			img.setImageDrawable(getResources().getDrawable(
					R.drawable.defaut_book));
		}

	}

	public void init() {
		listview = (DownFreshListView) findViewById(R.id.comment_lv);
		listview.setOnItemClickListener(this);
		listview.setOnRefreshListener(this);
		baseinfo_tv = (TextView) findViewById(R.id.baseinfo_tv);
		intro_tv = (TextView) findViewById(R.id.intro_tv);
		commit_btn = (Button) findViewById(R.id.commit_btn);
		comment_et = (EditText) findViewById(R.id.comment_et);
		TextView title = (TextView) findViewById(R.id.txt_header);
		title.setText(R.string.book_comment);
		ImageView back = (ImageView) findViewById(R.id.img_back_header);
		adapter = new CommentItemAdapter(context, null);
		// 加载数据
		// getHomeComment(1,Constant.DEFAULT_COUNT);
		img = (ImageView) findViewById(R.id.book_big_img);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	private void getHomeComment(int typeid, String keyid, int page, int count,
			int more) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("typeid", typeid + "");
		map.put("keyid", keyid);
		map.put("curpage", page + "");
		map.put("perpage", count + "");
		if (more == GETHOMEPAGE) {
			// ManagerService.addNewTask(new Task(Task.TASK_COMMENT_LIST, map));
			requestVolley(map, GlobleData.SERVER_URL
					+ "/cloud/commentlist.aspx", backlistener_list, Method.POST);
		} else {
			requestVolley(map, GlobleData.SERVER_URL
					+ "/cloud/commentlist.aspx", backlistener_listmore,
					Method.POST);
		}
	}

	private String getLngId(String callno, String recordid) {
		if (typeid == GlobleData.BOOK_SZ_TYPE) {
			if (callno.contains(",")) {
				return callno;
			}
			return callno + "," + recordid;
		} else {// 中刊返回lngid
			return callno;
		}
	}

	@Override
	public void onClick(View v) {
		String info = comment_et.getText().toString().trim();
		if (TextUtils.isEmpty(info)) {
			Tool.ShowMessages(this, "评论内容不能空");
			return;
		}
		if (dBook != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("libid", GlobleData.LIBIRY_ID);
			map.put("vipuserid", GlobleData.cqvipid);
			// Log.i("添加评论", GlobleData.cqvipid);
	//		String keyid = dBook.getCallno();
			String recordid = getTypeComment(dBook);
			 map.put("keyid", keyid);
			// Log.i("CommentActivity_keyid", dBook.getCallno());
			map.put("typeid", "" + typeid);
			// map.put("recordid", recordid);
			map.put("info", info);
			// ManagerService.addNewTask(new Task(Task.TASK_ADD_COMMENT, map));
			customProgressDialog.show();
			requestVolley(map, GlobleData.SERVER_URL + "/cloud/comment.aspx",
					backlistener_add, Method.POST);
		}
	}

	private void requestVolley(HashMap<String, String> gparams, String url,
			Listener<String> listener, int post) {
		final HashMap<String, String> gparams_t = gparams;
		StringRequest mys = new StringRequest(post, url, listener, el) {
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				return gparams_t;
			};
		};
		mQueue.add(mys);
		mQueue.start();

	}

	Listener<String> backlistener_add = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			Result res = null;
			try {
				res = new Result(response);
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				onError(2);
			}
			if (res.getSuccess()) {
				// 提示
				Tool.ShowMessages(CommentActivity.this, "添加成功");
				// 更新列表
				getHomeComment(typeid, keyid, 1, Constant.DEFAULT_COUNT,
						GETHOMEPAGE);
			} else {
				Tool.ShowMessages(CommentActivity.this, "添加失败");
			}
		}
	};

	Listener<String> backlistener_list = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			List<Comment> lists = null;
			try {
				lists = Comment.formList(response);
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (lists != null && !lists.isEmpty()) {
				adapter = new CommentItemAdapter(context, lists);
				listview.setAdapter(adapter);
			}
		}
	};

	Listener<String> backlistener_listmore = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			List<Comment> lists = null;
			try {
				lists = Comment.formList(response);
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			moreprocess.setVisibility(View.GONE);
			if (lists != null && !lists.isEmpty()) {
				adapter.addMoreData(lists);
				page++;
			} else {
				Tool.ShowMessages(context, "没有更多内容可供加载");
			}
		}
	};

	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			arg0.printStackTrace();
			onError(2);
		}
	};

	private String getTypeComment(Book dBook2) {
		if (typeid == GlobleData.BOOK_SZ_TYPE) {
			return dBook2.getRecordid();
		} else {
			return null;
		}
	}

//	@Override
//	public void refresh(Object... obj) {
//		customProgressDialog.dismiss();
//		int type = (Integer) obj[0];
//		switch (type) {
//		case Task.TASK_ADD_COMMENT:
//			Result res = (Result) obj[1];
//			if (res.getSuccess()) {
//				// 提示
//				Tool.ShowMessages(this, "添加成功");
//				// 更新列表
//				getHomeComment(typeid, keyid, 1, Constant.DEFAULT_COUNT,
//						GETHOMEPAGE);
//			} else {
//				Tool.ShowMessages(this, "添加失败");
//			}
//			break;
//		case Task.TASK_COMMENT_LIST:
//			List<Comment> lists = (List<Comment>) obj[1];
//			if (lists != null && !lists.isEmpty()) {
//				adapter = new CommentItemAdapter(context, lists);
//				listview.setAdapter(adapter);
//			}
//			// TODO
//			break;
//		case Task.TASK_COMMENT_LIST_MORE:
//			List<Comment> lists1 = (List<Comment>) obj[1];
//			moreprocess.setVisibility(View.GONE);
//			if (lists1 != null && !lists1.isEmpty()) {
//				adapter.addMoreData(lists1);
//			} else {
//				Tool.ShowMessages(context, "没有更多内容可供加载");
//			}
//			break;
//		}
//
//	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long id) {
		if (id == -2) // 更多
		{
			// 进度条
			moreprocess = arg1.findViewById(R.id.footer_progress);
			moreprocess.setVisibility(View.VISIBLE);
			// 请求网络更多
			getHomeComment(typeid, keyid, page + 1, Constant.DEFAULT_COUNT,
					GETMORE);
		}

	}

	@Override
	public void onRefresh() {
		getHomeComment(typeid, keyid, 1, Constant.DEFAULT_COUNT, GETHOMEPAGE);
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {

				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return null;
			}

			// 刷新完成
			@Override
			protected void onPostExecute(Void result) {

				listview.onRefreshComplete();

			}

		}.execute(null, null);

	}
}
