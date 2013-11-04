package com.cqvip.moblelib.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.activity.BaseActivity.MyGestrueListener;
import com.cqvip.moblelib.db.SearchHistoryDao;
import com.cqvip.moblelib.entity.SearchHistory_ZK;
import com.cqvip.moblelib.nanshan.R;
import com.cqvip.moblelib.view.KeywordsView;

/**
 * <p>
 * 文件名称: EBookActiviy.java 文件描述: 电子书 版权所有: 版权所有(C)2013-2020 公 司: 重庆维普咨询有限公司
 * 内容摘要: 其他说明: 完成日期： 201年5月10日 修改记录:
 * </p>
 * 
 * @author LHP,LJ
 */
public class EBookActiviy extends BaseActivity implements
View.OnClickListener{

	// private static final String[] EBOOKTYPE = new String[] {
	// "馆藏书目", "维普期刊", "万方期刊","超星图书","内置图书"
	// };
	private Context context;
	private String[] EBOOKTYPE;
	private ViewGroup searchbar;
	private int[] drawableids = { R.drawable.sign_vip, R.drawable.sign_chaoxing,
			R.drawable.sign_fangzheng };
	private  int currentID=-1;
	private EditText editText;
	private ScrollView myScrollView;

	private String[] totalKeys = null;
	private String[] key_words = new String[15];
	private KeywordsView showKeywords = null;
	private LinearLayout searchLayout = null;
	private GestureDetector mggd;
	/**
	 * 判断是在外页面还是内页面
	 */
	private boolean isOutter;
	private ImageView delete_iv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ebook_activiy);
		context=this;
		editText=(EditText) findViewById(R.id.search_et);
//		myScrollView=(ScrollView) findViewById(R.id.myScrollView);
//		myScrollView.setOnTouchListener(this);
		ImageView back = (ImageView)findViewById(R.id.return_iv);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();

			}
		});
		hideinputmethod();
		
		//搜索历史
		searchLayout = (LinearLayout) this.findViewById(R.id.searchContent);
		delete_iv = (ImageView) findViewById(R.id.delete_iv);
		showKeywords = (KeywordsView) this.findViewById(R.id.word);
		showKeywords.setDuration(2000l);
		showKeywords.setOnClickListener(this);
		this.mggd = new GestureDetector(this,
				new MyGestrueListener());
		showKeywords.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				boolean a= mggd.onTouchEvent(event);
				Log.i("EBookAct", "onTouch_return:"+a);
				return a; // 注册点击事件
				
			}
		});
		isOutter = true;
		handler.sendEmptyMessage(Msg_Start_Load);
		
		// SearchView sc = (SearchView)findViewById(R.id.search_view);
		EBOOKTYPE = getResources().getStringArray(R.array.ebooktype);

		ListView lv = (ListView) findViewById(R.id.ebook_search_list);
		final MyAdapter  adapter = new MyAdapter(this, EBOOKTYPE, drawableids);
		lv.setAdapter(adapter);
		setListViewHeightBasedOnChildren(lv);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==0){
//					adapter.setCurrentID(position);
//					adapter.notifyDataSetChanged();
					startActivity(new Intent(EBookActiviy.this,
					PeriodicalClassfyActivity.class));
				}
			}

		});

		searchbar = (ViewGroup) findViewById(R.id.searchbar);
		searchbar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EBookActiviy.this,
						EBookSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});

		final EditText et = (EditText) findViewById(R.id.search_et);
		et.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
				Intent intent = new Intent(EBookActiviy.this,
						EBookSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
	private void setListViewHeightBasedOnChildren(ListView listView) {
		//获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter(); 
		if (listAdapter == null) {
		// pre-condition
		return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
		View listItem = listAdapter.getView(i, null, listView);
		listItem.measure(0, 0); //计算子项View 的宽高
		totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		//listView.getDividerHeight()获取子项间分隔符占用的高度
		//params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
		}

	public class MyAdapter extends BaseAdapter {
		private String[] eBookTypes;
		private Context context;
		private int[] drawableids;
		int  currentID=-1;

		public MyAdapter(Context context, String[] eBookTypes, int[] drawableids) {
			this.eBookTypes = eBookTypes;
			this.context = context;
			this.drawableids = drawableids;
		}

		public void setCurrentID(int currentID) {
			this.currentID = currentID;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return eBookTypes.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater;
			if (convertView == null) {
				inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.item_ebooktype, null);
				CheckBox checkBox=(CheckBox) convertView.findViewById(R.id.checkbox);
				if(position!=0){
					checkBox.setEnabled(false);
				}
			}
			ImageView iv = (ImageView) convertView
					.findViewById(R.id.booktype_img);
			TextView tv=(TextView) convertView
					.findViewById(android.R.id.text1);
//			CheckedTextView checkedTextView = (CheckedTextView) convertView
//					.findViewById(android.R.id.text1);
			CheckBox checkBox=(CheckBox) convertView.findViewById(R.id.checkbox);
			if(position==0){
				checkBox.setChecked(true);
			}
			iv.setImageResource(drawableids[position]);
			tv.setText(eBookTypes[position]);
//			  if(position==this.currentID)
//				  checkBox.setChecked(true);
//			  else
//				  checkBox.setChecked(false);
			return convertView;
		}
	}
	
	private void hideinputmethod() {
		if (android.os.Build.VERSION.SDK_INT <= 10) { 
			editText.setInputType(InputType.TYPE_NULL);
			} else {
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
			Class<EditText> cls = EditText.class;
			Method setSoftInputShownOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
			setSoftInputShownOnFocus.setAccessible(true);
			setSoftInputShownOnFocus.invoke(editText, false);
			} catch (Exception e) {}
			}
	}	
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		if(showKeywords!=null){
			showKeywords.removeAllViews();
			showKeywords.show_restart();
			if(tv_onclick!=null){
				tv_onclick.setVisibility(View.VISIBLE);
			}
			}
	}

	List<SearchHistory_ZK> list_SearchHistory_ZK;

	private void getSearchHistoryfromDatabase() {
		SearchHistory_ZK searchHistory_ZK = new SearchHistory_ZK();
		SearchHistoryDao<SearchHistory_ZK> searchHistoryDao = new SearchHistoryDao<SearchHistory_ZK>(
				this, searchHistory_ZK);
		try {
			list_SearchHistory_ZK = searchHistoryDao.queryInfobydate();
			if (list_SearchHistory_ZK != null) {
				int i = 0;
				for (SearchHistory_ZK element : list_SearchHistory_ZK) {
					Log.i("BookSearchAct" + i++, element.getName());
				}
			}
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	private String[] getRandomArray() {
		List<String> list_str = new ArrayList<String>();
		if(list_SearchHistory_ZK!=null)
		for (SearchHistory_ZK sh : list_SearchHistory_ZK) {
			list_str.add(sh.getName());
		}
		int size = list_str.size();
		return list_str.toArray(new String[size]);
	}

	private static final int Msg_Start_Load = 0x0102;
	private static final int Msg_Load_End = 0x0203;

	private LoadKeywordsTask task = null;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Msg_Start_Load:

				task = new LoadKeywordsTask();
				new Thread(task).start();

				break;
			case Msg_Load_End:
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				break;
			}

		}
	};

	private class LoadKeywordsTask implements Runnable {
		@Override
		public void run() {
			try {
				getSearchHistoryfromDatabase();
				key_words = getRandomArray();
				if (key_words.length > 0)
					handler.sendEmptyMessage(Msg_Load_End);
			} catch (Exception e) {
			}
		}
	}

	private void feedKeywordsFlow(KeywordsView keyworldFlow, String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			String tmp = arr[i];
			keyworldFlow.feedKeyword(tmp);
		}
	}
	
	private int minVelocitx = 1000;
	private int MinDistance = 50;
	class MyGestrueListener extends SimpleOnGestureListener {

		
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if(e1!=null){
				if (e2.getX() - e1.getX() > MinDistance&&(Math.abs(velocityX)>minVelocitx||Math.abs(velocityY)>velocityY)) { // 右滑
					key_words = getRandomArray();
					showKeywords.rubKeywords();
					feedKeywordsFlow(showKeywords, key_words);
					showKeywords.go2Shwo(KeywordsView.ANIMATION_OUT);
					return false;
				}

			if (e2.getX() - e1.getX() < -MinDistance&&(Math.abs(velocityX)>minVelocitx||Math.abs(velocityY)>velocityY)) {// 左滑
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				return false;
			}
			if (e2.getY() - e1.getY() < -MinDistance&&(Math.abs(velocityX)>minVelocitx||Math.abs(velocityY)>velocityY)) {// 上滑
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				return false;
			}
			if (e2.getY() - e1.getY() > MinDistance&&(Math.abs(velocityX)>minVelocitx||Math.abs(velocityY)>velocityY)) {// 下滑
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_OUT);
				return false;
			}
			}
			return false;
		}
	}
	TextView tv_onclick;
	@Override
	public void onClick(View v) {
		// System.out.println("V"+v);
		// // TODO Auto-generated method stub
		// if(isOutter){
		// isOutter = false;
		//
		// String kw = ((TextView) v).getText().toString();
		// // Log.i(TAG, "keywords = "+kw);
		// if (!kw.trim().equals("")) {
		// searchLayout.removeAllViews();
		//
		// }
		// Toast.makeText(this, "选中的内容是：" + ((TextView) v).getText().toString(),
		// 1)
		// .show();
		// }
		switch (v.getId()) {
		case R.id.delete_iv:
//			key_words = getRandomArray();
//			showKeywords.rubKeywords();
//			feedKeywordsFlow(showKeywords, key_words);
//			showKeywords.go2Shwo(KeywordsView.ANIMATION_OUT);
			senddel(0);
			break;

		default:
			tv_onclick=(TextView) v;
			Intent intent = new Intent(context, ResultOnSearchActivity.class);
			intent.putExtra("isfromDetailAdvancedBookActivity", true);
			intent.putExtra("bookname", ((TextView) v).getText().toString());
			showKeywords.disapperByOnclick((TextView)v, intent);
  		    //startActivity(intent);
			// finish();
			break;
		}

	}

	/**
	 * 删除对话框
	 */
	public void senddel(int type) {
		Intent intent = new Intent();
		intent.setClass(this, ActivityDlg.class);
		intent.putExtra("ACTIONID", 0);
		intent.putExtra("MSGBODY", "确定删除搜索历史？");
		intent.putExtra("BTN_CANCEL", 1);
		startActivityForResult(intent, type);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
		showKeywords.rubAllViews();
		list_SearchHistory_ZK=null;
		SearchHistoryDao<SearchHistory_ZK> searchHistoryDao=new SearchHistoryDao<SearchHistory_ZK>(this, new SearchHistory_ZK());
		try {
			searchHistoryDao.deleteAll();
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}

//	@Override
//	public boolean onTouch(View v, MotionEvent event) {
//		ScrollView scrollView=(ScrollView)v;
//		if(event.getAction()==MotionEvent.ACTION_MOVE){
//			
//
//			//可以监听到ScrollView的滚动事件
//
//			
//
//			}
//
//			return false;
//	}
}
