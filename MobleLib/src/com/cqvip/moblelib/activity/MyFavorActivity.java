package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
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

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.Favorite;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

public class MyFavorActivity extends BaseFragmentImageActivity {
	public static final int GETFIRSTPAGE_SZ = 1;
	public static final int GETFIRSTPAGE_ZK = 2;
	public static final int GETNEXT = 3;

	private int curpage = 1;// 第几页
	private int perpage = 10;// 每页显示条数
	private Favorite del_favorite;
	private View moreprocess;
	private int curpage_sz = 1, curpage_zk = 1;// 第几页

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
	Map<Integer, List<Favorite>> arrayLists_sz, arrayLists_zk;
	ArrayList<Favorite> arrayList_zk = new ArrayList<Favorite>();
	ArrayList<Favorite> arrayList_sz = new ArrayList<Favorite>();
	private int listviewpagetag=GlobleData.BOOK_SZ_TYPE;

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
		getfavorlist(curpage, perpage, GlobleData.BOOK_SZ_TYPE, GETFIRSTPAGE_SZ);
		getfavorlist(curpage, perpage, GlobleData.BOOK_ZK_TYPE, GETFIRSTPAGE_ZK);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 101:
			if (resultCode == 0 && del_favorite != null) {

				HashMap<String, String> map = new HashMap<String, String>();
				map.put("libid", GlobleData.LIBIRY_ID);
				map.put("vipuserid", GlobleData.cqvipid);
				Log.i("删除收藏", GlobleData.cqvipid);
				map.put("keyid", del_favorite.getFavoritekeyid());
				// Log.i("keyid", favorite.getFavoritekeyid());
				map.put("typeid", "" + del_favorite.getTypeid());
				requestVolley(map, GlobleData.SERVER_URL
						+ "/cloud/favoritecancel.aspx", backlistenercancel,
						Method.POST);
			}
			break;

		default:
			break;
		}

	}

	public void senddel() {
		Intent intent = new Intent();
		intent.setClass(MyFavorActivity.this, ActivityDlg.class);
		intent.putExtra("ACTIONID", 0);
		intent.putExtra("MSGBODY", "确定删除该收藏");
		intent.putExtra("BTN_CANCEL", 1);
		startActivityForResult(intent, 101);
	}

	private void getfavorlist(int pagecount, int perpage, int typeid, int type) {
		HashMap<String, String> gparams = new HashMap<String, String>();
		gparams.put("libid", GlobleData.LIBIRY_ID);
		Log.i("MyFavorActivity_cqvipid", "" + GlobleData.cqvipid);
		gparams.put("vipuserid", GlobleData.cqvipid);
		gparams.put("curpage", "" + pagecount);
		gparams.put("perpage", "" + perpage);
		gparams.put("typeid", "" + typeid);
		// ManagerService.addNewTask(new Task(Task.TASK_GET_FAVOR, map));
		switch (type) {
		case GETFIRSTPAGE_SZ:
			requestVolley(gparams, GlobleData.SERVER_URL
					+ "/cloud/favoritelistuser.aspx", backlistener_sz,
					Method.POST);
			break;
		case GETFIRSTPAGE_ZK:
			requestVolley(gparams, GlobleData.SERVER_URL
					+ "/cloud/favoritelistuser.aspx", backlistener_zk,
					Method.POST);
			break;
		case GETNEXT:
			requestVolley(gparams, GlobleData.SERVER_URL
					+ "/cloud/favoritelistuser.aspx", backlistenermore,
					Method.POST);
			break;
		default:
			break;
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

	Listener<String> backlistener_sz = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			try {
				arrayLists_sz = Favorite
						.formList(Task.TASK_GET_FAVOR, response);
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				onError(2);
			}
			if (isdeleted_sz) {
				arrayList_sz.clear();
				isdeleted_sz = false;
			}
			if (arrayLists_sz != null && !arrayLists_sz.isEmpty()) {
				if (arrayLists_sz.get(GlobleData.BOOK_SZ_TYPE).isEmpty()) {
					return;
				}
				arrayList_sz.addAll(arrayLists_sz.get(GlobleData.BOOK_SZ_TYPE));
				adapter_sz.notifyDataSetChanged();
			}
		}
	};

	Listener<String> backlistener_zk = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			try {
				arrayLists_zk = Favorite
						.formList(Task.TASK_GET_FAVOR, response);
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				onError(2);
			}
			if (isdeleted_zk) {
				arrayList_zk.clear();
				isdeleted_zk = false;
			}
			if (arrayLists_zk != null && !arrayLists_zk.isEmpty()) {
				if (arrayLists_zk.get(GlobleData.BOOK_ZK_TYPE).isEmpty()) {
					return;
				}
				arrayList_zk.addAll(arrayLists_zk.get(GlobleData.BOOK_ZK_TYPE));
				adapter_zk.notifyDataSetChanged();
			}
		}
	};

	Listener<String> backlistenermore = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			try {
				arrayLists_sz = Favorite
						.formList(Task.TASK_GET_FAVOR, response);
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				onError(2);
			}
			if (arrayLists_sz != null && !arrayLists_sz.isEmpty()) {
				if ((arrayLists_sz.get(GlobleData.BOOK_ZK_TYPE) == null || arrayLists_sz
						.get(GlobleData.BOOK_ZK_TYPE).isEmpty())
						&& (arrayLists_sz.get(GlobleData.BOOK_SZ_TYPE) == null || arrayLists_sz
								.get(GlobleData.BOOK_SZ_TYPE).isEmpty())) {
					Tool.ShowMessages(context, "没有更多内容可供加载");
					moreprocess.setVisibility(View.GONE);
					return;
				}
			}
			if (arrayLists_sz != null && !arrayLists_sz.isEmpty()) {
				if (arrayLists_sz.get(GlobleData.BOOK_ZK_TYPE).isEmpty()
						&& arrayLists_sz.get(GlobleData.BOOK_SZ_TYPE).isEmpty()) {
					return;
				}
				if(listviewpagetag==GlobleData.BOOK_SZ_TYPE){
					curpage_sz++;
				arrayList_sz.addAll(arrayLists_sz.get(GlobleData.BOOK_SZ_TYPE));
				// mSectionsPagerAdapter.notifyDataSetChanged();
				adapter_sz.notifyDataSetChanged();
				}else{
					curpage_zk++;
					arrayList_zk.addAll(arrayLists_sz.get(GlobleData.BOOK_ZK_TYPE));
					adapter_zk.notifyDataSetChanged();
				}

			}
		}
	};

	Listener<String> backlistenercancel = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			Result res = null;
			try {
				res = new Result(response);
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				onError(6);
			}
			if (res != null) {
				if (res.getSuccess()) {
					Tool.ShowMessages(context, "取消收藏成功");
					int temp_pagecount = 0;
					if (listview_id == GlobleData.BOOK_SZ_TYPE) {
						isdeleted_sz = true;
						arrayList_sz.remove(listview_item_position);
						adapter_sz.notifyDataSetChanged();
						temp_pagecount = perpage * curpage_sz;
						getfavorlist(curpage, temp_pagecount, listview_id,
								GETFIRSTPAGE_SZ);
					} else if (listview_id == GlobleData.BOOK_ZK_TYPE) {
						isdeleted_zk = true;
						arrayList_zk.remove(listview_item_position);
						adapter_zk.notifyDataSetChanged();
						temp_pagecount = perpage * curpage_zk;
						getfavorlist(curpage, temp_pagecount, listview_id,
								GETFIRSTPAGE_ZK);
					}

				} else {
					Tool.ShowMessages(context, "取消收藏失败");
				}
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
			Log.i("MyFavorActivity", "SectionsPagerAdapter_getItemPosition:");
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
				Log.i("MyFavorActivity", "SectionsPagerAdapter_getPageTitle_0");
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				Log.i("MyFavorActivity", "SectionsPagerAdapter_getPageTitle_1");
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
			OnItemClickListener {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
			// Log.i("MyFavorActivity", "DummySectionFragment");
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
				arrayList_temp = arrayList_sz;
				adapter_sz = new MyGridViewAdapter(getActivity(), arrayList_sz,
						mImageFetcher);
				listView.setAdapter(adapter_sz);
				listView.setTag(GlobleData.BOOK_SZ_TYPE);
			} else if (i == 1) {
				listView.setTag(GlobleData.BOOK_ZK_TYPE);
				arrayList_temp = arrayList_zk;
				adapter_zk = new MyGridViewAdapter(getActivity(), arrayList_zk,
						mImageFetcher);
				listView.setAdapter(adapter_zk);
			}

			listView.setOnItemClickListener(this);
			//listView.setOnItemLongClickListener(this);
			return rootView;
		}

		Favorite favorite;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int positon,
				long id) {
			Log.i("item", "===============click=");
			if (id == -2) {
				if (parent.getAdapter().getCount() == 1) {
					return;
				}
				// 进度条
				moreprocess = view.findViewById(R.id.footer_progress);
				moreprocess.setVisibility(View.VISIBLE);
				//Log.i("parent.getTag()", "" + parent.getTag());
				listviewpagetag=(Integer) parent.getTag();
				// 请求网络更多
				if (listviewpagetag == GlobleData.BOOK_SZ_TYPE) {
					getfavorlist(curpage_sz+1, perpage, GlobleData.BOOK_SZ_TYPE,
							GETNEXT);
				} else if (listviewpagetag == GlobleData.BOOK_ZK_TYPE) {
					getfavorlist(curpage_zk+1, perpage, GlobleData.BOOK_ZK_TYPE,
							GETNEXT);
				}
			} else {
				if ((Integer) parent.getTag() == GlobleData.BOOK_SZ_TYPE) {
					favorite = arrayList_sz.get(positon);
					String lngid = favorite.getLngid();// J228.5/1:4,863174

					Book book = new Book("", favorite.getOrgan(),
							favorite.getTitle(), favorite.getWriter(), lngid,
							favorite.getYears(), favorite.getPrice(),
							favorite.getRemark(), favorite.getImgurl());
					if (book != null) {
						Log.i("ResultOnSearchActivity", book.toString());
						Intent _intent = new Intent(context,
								DetailBookActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("book", book);
						_intent.putExtra("detaiinfo", bundle);
						_intent.putExtra("ismyfavor", true);
						startActivity(_intent);
					}
				} else if ((Integer) parent.getTag() == GlobleData.BOOK_ZK_TYPE) {
					favorite = arrayList_zk.get(positon);
					EBook book = new EBook(favorite.getLngid(),
							favorite.getYears(), favorite.getNum(),
							favorite.getTitle(), favorite.getOrgan(),
							favorite.getRemark(), favorite.getWriter(),
							favorite.getPagecount(), 0, favorite.getImgurl());
					if (book != null) {
						Log.i("ResultOnSearchActivity", book.toString());
						Intent _intent = new Intent(context,
								EbookDetailActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("book", book);
						_intent.putExtra("detaiinfo", bundle);
						startActivity(_intent);
					}
				}

			}
		}

//		@Override
//		public boolean onItemLongClick(AdapterView<?> parent, View view,
//				int position, long id) {
//			if (parent.getAdapter().getItemId(position) != -2) {
//				int listviewid = (Integer) parent.getTag();
//				listview_id = listviewid;
//				listview_item_position = position;
//				del_favorite = arrayList_temp.get(position);
//
//				senddel();
//			}
//			return false;
//		}
	}

	private int listview_id = GlobleData.BOOK_SZ_TYPE;
	private int listview_item_position = 0;

	static class ViewHolder {

		TextView title;// 书名
		TextView author;// 作者
		TextView publisher;// 出版社
		TextView publishyear;// 出版时间
		ImageView img;// 时间图片 不用修改
		// TextView u_page;//页数
		// TextView u_abstract;//简介
		TextView isbn;
		Button favor_cancel;
		//Button btn_comment, btn_item_result_search_share, favorite;

	}

	class MyGridViewAdapter extends BaseAdapter {
		private Context myContext;
		private List<Favorite> arrayList;
		private ImageFetcher fetch;

		public MyGridViewAdapter(Context context, List<Favorite> list) {
			this.myContext = context;
			this.arrayList = list;
			Log.i("MyFavorActivity", "MyGridViewAdapter");
		}

		public MyGridViewAdapter(Context context, List<Favorite> list,
				ImageFetcher fetch) {
			this.myContext = context;
			this.arrayList = list;
			this.fetch = fetch;
			Log.i("MyFavorActivity", "MyGridViewAdapter");
		}

		public List<Favorite> getList() {
			return this.arrayList;
		}

		@Override
		public int getCount() {
			Log.i("getCount", "getCount");
			if (arrayList != null && !arrayList.isEmpty()) {
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (this.getCount() == 1) {
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.moreitemsview, null);
				convertView.setClickable(false);
				TextView tv = (TextView) convertView
						.findViewById(R.id.footer_txt);
				tv.setText("亲，您所在分类没有收藏哦");
				tv.setTextColor(context.getResources().getColor(
						R.drawable.silvergray));
				tv.setTextSize(context.getResources().getDimension(
						R.dimen.search_detail_txtsize_tv));
				tv.setClickable(false);
				return convertView;
			}
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
				TextView txt_abst = (TextView) convertView
						.findViewById(R.id.txt_abst);
				txt_abst.setVisibility(View.GONE);
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
				// holder.u_abstract = (TextView)
				// convertView.findViewById(R.id.re_hot_txt);
				holder.isbn = (TextView) convertView
						.findViewById(R.id.re_hot_txt);
				holder.favor_cancel=(Button) convertView.findViewById(R.id.favor_cancel);
				holder.favor_cancel.setVisibility(View.VISIBLE);
//				holder.btn_item_result_search_share = (Button) convertView
//						.findViewById(R.id.btn_item_result_search_share);
//				holder.favorite = (Button) convertView
//						.findViewById(R.id.btn_item_result_search_collect);
//				holder.btn_comment = (Button) convertView
//						.findViewById(R.id.btn_comment);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String author = context.getResources().getString(
					R.string.item_author);
			String publish = context.getResources().getString(
					R.string.item_publish);
			String time = context.getResources().getString(R.string.item_time);
//			String describe = context.getResources().getString(
//					R.string.item_describe);
			Favorite favorite = arrayList.get(position);
			holder.title.setText(favorite.getTitle());
			holder.author.setText(author + favorite.getWriter());
			holder.publisher.setText(publish + favorite.getOrgan());
			holder.publishyear.setText(time + favorite.getYears());
			holder.isbn.setText("ISBN:" + favorite.getLngid());
			final ViewGroup temp_parent=parent;
			final int temp_position=position;
			holder.favor_cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
						int listviewid = (Integer) temp_parent.getTag();
						listview_id = listviewid;
						listview_item_position = temp_position;
						del_favorite = arrayList.get(temp_position);
						senddel();
				}
			});
			// 图片
			if (!TextUtils.isEmpty(favorite.getImgurl())) {
				fetch.loadImage(favorite.getImgurl(), holder.img);
			} else {
				holder.img.setImageDrawable(getResources().getDrawable(
						R.drawable.defaut_book));
			}

			return convertView;
		}
	}

	public void init() {
		mPagerTitleStrip = (PagerTitleStrip) findViewById(R.id.pager_title_strip);
		mPagerTitleStrip.setTextSpacing(-50);
		mPagerTitleStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
		TextView title = (TextView) findViewById(R.id.txt_header);
		title.setText(R.string.serv_favorite);
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

	private boolean isdeleted_sz = false;
	private boolean isdeleted_zk = false;

	public void onError(int a) {
		if (customProgressDialog != null && customProgressDialog.isShowing()) {
			customProgressDialog.dismiss();
		}
		if (a == 2) {// 加载失败
			Tool.ShowMessages(this, getResources().getString(R.string.loadfail));
		} else if (a == 6) {
			Tool.ShowMessages(this,
					getResources().getString(R.string.cancelfavorfail));
		}
	}
}
