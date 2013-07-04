package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Favorite;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

public class MyCommentByBooktypeActivity extends FragmentActivity implements
		IBookManagerActivity {
	public static final int FAVOR = 1;
	public static final int CANCELFAVOR = 2;

	private String curpage = "1";// 第几页
	private String perpage = "10";// 每页显示条数

	MyGridViewAdapter adapter_zk, adapter_sz;
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	PagerTitleStrip mPagerTitleStrip;
	Context context;
	protected CustomProgressDialog customProgressDialog;
	Map<Integer, List<Favorite>> arrayLists;
	List<Favorite> arrayList_zk = new ArrayList<Favorite>();
	List<Favorite> arrayList_sz = new ArrayList<Favorite>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfavor);
		init();
		context = this;
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		// 获取数据
		ManagerService.allActivity.add(this);
		getfavorlist();
	}

	private void getfavorlist() {
		HashMap map = new HashMap();
		map.put("libid", GlobleData.LIBIRY_ID);
		Log.i("MyFavorActivity_cqvipid", "" + GlobleData.cqvipid);
		map.put("vipuserid", GlobleData.cqvipid);
		map.put("curpage", curpage);
		map.put("perpage", perpage);
		ManagerService.addNewTask(new Task(Task.TASK_GET_FAVOR, map));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Log.i("getItem", "getItem");
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		/**
		 * 重写此方法为了使notifyDataSetChanged有效 Called when the host view is
		 * attempting to determine if an item's position has changed. Returns
		 * POSITION_UNCHANGED if the position of the given item has not changed
		 * or POSITION_NONE if the item is no longer present in the adapter. The
		 * default implementation assumes that items will never change position
		 * and always returns POSITION_UNCHANGED.
		 */
		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return POSITION_NONE;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Log.i("getPageTitle", "getPageTitle");
			Locale l = Locale.getDefault();
			if (adapter_zk != null && adapter_sz != null) {
				adapter_zk.notifyDataSetChanged();
				adapter_sz.notifyDataSetChanged();
			}
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */

	public class DummySectionFragment extends Fragment implements
			OnItemClickListener, OnItemLongClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
			Log.i("MyFavorActivity", "DummySectionFragment");
		}

		List<Favorite> arrayList_temp;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.i("MyFavorActivity", "onCreateView");
			View rootView = inflater.inflate(R.layout.myfavor_fragment,
					container, false);
			ListView listView = (ListView) rootView
					.findViewById(R.id.favorlist);
			int i = getArguments().getInt(ARG_SECTION_NUMBER);
			if (i == 0) {
				arrayList_temp = arrayList_zk;
				adapter_zk = new MyGridViewAdapter(getActivity(), arrayList_zk);
				listView.setAdapter(adapter_zk);
			} else if (i == 1) {
				arrayList_temp = arrayList_sz;
				adapter_sz = new MyGridViewAdapter(getActivity(), arrayList_sz);
				listView.setAdapter(adapter_sz);
			}

			listView.setOnItemClickListener((OnItemClickListener) this);
			listView.setOnItemLongClickListener(this);
			return rootView;
		}

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int positon,
				long id) {
			// Log.i("item","===============click=");
			// if (id == -2) //更多
			// {
			// //进度条
			// View moreprocess = arg1.findViewById(R.id.footer_progress);
			// moreprocess.setVisibility(View.VISIBLE);
			// //请求网络更多
			// if(Tool.isbnMatch(key)){
			// getHomePage(key,page+1,DEFAULT_COUNT,GETNEXTPAGE,GlobleData.QUERY_ISBN);
			// }else{
			// getHomePage(key,page+1,DEFAULT_COUNT,GETNEXTPAGE,GlobleData.QUERY_ALL);
			// }
			// page = page+1;
			// }else{
			// Book book = adapter.getLists().get(positon);
			// if(book!=null){
			// Log.i("ResultOnSearchActivity",book.toString());
			// Intent _intent = new Intent(context,DetailBookActivity.class);
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("book", book);
			// _intent.putExtra("detaiinfo", bundle);
			// startActivity(_intent);
			// }
			//
			// // Book book = lists.get(position-1);
			// // if(book!=null){
			// // Bundle bundle = new Bundle();
			// // bundle.putSerializable("book", book);
			// // _intent.putExtra("detaiinfo", bundle);
			// // startActivityForResult(_intent, 1);
			// }
		}

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				int position, long id) {
			Favorite favorite = arrayList_temp.get(position);
			if (favorite != null) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("libid", GlobleData.LIBIRY_ID);
				map.put("vipuserid", GlobleData.cqvipid);
				Log.i("删除收藏", GlobleData.cqvipid);
				map.put("keyid", favorite.getFavoritekeyid());
				Log.i("keyid", favorite.getFavoritekeyid());
				map.put("typeid", "" + favorite.getTypeid());
				ManagerService
						.addNewTask(new Task(Task.TASK_CANCEL_FAVOR, map));
				customProgressDialog.show();
			}
			return false;
		}
	}

	static class ViewHolder {

		TextView title;// 书名
		TextView author;// 作者
		TextView publisher;// 出版社
		TextView publishyear;// 出版时间
		TextView commentcount;//评论数
		ImageView img;// 时间图片 不用修改
	}

	class MyGridViewAdapter extends BaseAdapter {
		private Context myContext;
		private List<Favorite> arrayList;

		public MyGridViewAdapter(Context context, List<Favorite> list) {
			this.myContext = context;
			this.arrayList = list;
			Log.i("MyFavorActivity", "MyGridViewAdapter");
		}

		@Override
		public int getCount() {
			Log.i("getCount", "getCount");
			if (arrayList != null) {
				return arrayList.size() + 1;
			}
			return 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			if ((this.getCount() - 1) > 0 && position < (this.getCount() - 1)) {
				return position;
			} else {
				return -2;
			}
		}

		public void refresh() {
			notifyDataSetChanged();
		}

		// @Override
		// public View getView(int position, View convertView, ViewGroup parent)
		// {
		// if (convertView == null) {
		// convertView = LayoutInflater.from(myContext).inflate(
		// R.layout.myfavor_fragmen_item, null);
		// }
		// ImageView iv = (ImageView) convertView
		// .findViewById(R.id.imageView1);
		// TextView tc = (TextView) convertView.findViewById(R.id.textView1);
		// return convertView;
		// }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;

			// 更多
			if (position == this.getCount() - 1) {
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.moreitemsview, null);
				return convertView;
			}
			if (convertView == null
					|| convertView.findViewById(R.id.linemore) != null) {
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.item_result_search, null);
				holder = new ViewHolder();

				holder.title = (TextView) convertView
						.findViewById(R.id.re_name_txt);
				holder.author = (TextView) convertView
						.findViewById(R.id.re_author_txt);
				holder.publisher = (TextView) convertView
						.findViewById(R.id.re_addr_txt);
				holder.publishyear = (TextView) convertView
						.findViewById(R.id.re_time_txt);
				holder.img = (ImageView) convertView
						.findViewById(R.id.re_book_img);
				holder.commentcount = (TextView) convertView
						.findViewById(R.id.re_hot_txt);
				holder.commentcount.setTextColor(getResources().getColor(R.color.green_light));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String author = context.getResources().getString(
					R.string.item_author);
			String publish = context.getResources().getString(
					R.string.item_publish);
			String time = context.getResources().getString(R.string.item_time);
			String commentcount = context.getResources().getString(
					R.string.item_comment_count);
			Favorite favorite = arrayList.get(position);
			holder.title.setText(favorite.getTitle());
			holder.author.setText(author + favorite.getWriter());
			holder.publisher.setText(publish + favorite.getOrgan());
			holder.publishyear.setText(time + favorite.getYears());
			holder.commentcount.setText(commentcount +"(" +favorite.getLngid()+")");

			return convertView;
		}
	}

	@Override
	public void init() {
		mPagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
		mPagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		TextView title = (TextView) findViewById(R.id.txt_header);
		title.setText(R.string.mycomments);
		ImageView back = (ImageView) findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				// overridePendingTransition(R.anim.slide_left_in,
				// R.anim.slide_right_out);
			}
		});

		customProgressDialog = CustomProgressDialog.createDialog(this);
		customProgressDialog.show();
	}

	@Override
	public void refresh(Object... obj) {
		customProgressDialog.dismiss();
		Log.i("MyFavorActivity_refresh", "refresh");
		int temp = (Integer) obj[0];
		if (temp == FAVOR) {
			arrayLists = (Map<Integer, List<Favorite>>) obj[1];
			if (arrayLists != null && !arrayLists.isEmpty()) {
				arrayList_zk.clear();
				arrayList_sz.clear();
				arrayList_zk = arrayLists.get(GlobleData.BOOK_ZK_TYPE);
				arrayList_sz = arrayLists.get(GlobleData.BOOK_SZ_TYPE);
				mSectionsPagerAdapter.notifyDataSetChanged();
			}
		} else if (temp == CANCELFAVOR) {
			Result res = (Result) obj[1];
			if (res.getSuccess()) {
				Tool.ShowMessages(context, getResources().getString(R.string.cancelcommentdone));
				getfavorlist();
			} else {
				Tool.ShowMessages(context, getResources().getString(R.string.cancelcommentfail));
			}
		}
	}

	public void onError(int a) {
		if (customProgressDialog != null && customProgressDialog.isShowing()) {
			customProgressDialog.dismiss();
		}
		if (a == 2) {// 加载失败
			Tool.ShowMessages(this, getResources().getString(R.string.loadfail));
		} else if (a == 6) {
			Tool.ShowMessages(this,
					getResources().getString(R.string.cancelcommentfail));
		}
	}
}
