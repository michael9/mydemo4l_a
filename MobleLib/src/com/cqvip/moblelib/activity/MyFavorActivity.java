package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.Favorite;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.utils.HttpUtils;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.moblelib.view.SwipHorizontalScrollView;
import com.cqvip.utils.Tool;

/**
 * <p>
 * 文件名称: MyFavorActivity.java
 * 文件描述: 我的收藏
 * 版权所有: 版权所有(C)2013-2020
 * 公          司: 重庆维普咨询有限公司
 * 内容摘要: 
 * 其他说明:
 * 完成日期： 2014年11月22日
 * 修改记录: 
 * </p>
 * 
 * @author LHP,LJ
 */

public class MyFavorActivity extends BaseFragmentImageActivity {
	public static final int GETFIRSTPAGE_SZ = 1;
	public static final int GETFIRSTPAGE_ZK = 2;
	public static final int GETNEXT = 3;

	private int curpage = 1;// 第几页
	private int perpage = Constant.DEFAULT_COUNT;// 每页显示条数
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
	Context context;
	private RelativeLayout rl_nav;
	private SwipHorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator;
	private ImageView iv_nav_left;
	private ImageView iv_nav_right;
	private ViewPager mViewPager;
	private int indicatorWidth;
	private LayoutInflater mInflater;
	private int currentIndicatorLeft = 0;
	public String[] tabTitle = new String[2]; // 标题

	protected CustomProgressDialog customProgressDialog;
	Map<Integer, List<Favorite>> arrayLists_sz, arrayLists_zk;
	ArrayList<Favorite> arrayList_zk = new ArrayList<Favorite>();
	ArrayList<Favorite> arrayList_sz = new ArrayList<Favorite>();
	private int listviewpagetag = GlobleData.BOOK_SZ_TYPE;

	private int sz_count, zk_count; // 条数

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_periodical_classfy);
		init();
		setListener();
		context = this;
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(mSectionsPagerAdapter);
		// 获取数据
		getfavorlist(curpage, perpage, GlobleData.BOOK_SZ_TYPE, GETFIRSTPAGE_SZ);
		getfavorlist(curpage, perpage, GlobleData.BOOK_ZK_TYPE, GETFIRSTPAGE_ZK);
	}

	private void setListener() {
		mViewPager
				.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						// RadioButton点击 performClick()
						if (rg_nav_content != null
								&& rg_nav_content.getChildCount() > position) {
							((RadioButton) rg_nav_content.getChildAt(position))
									.performClick();
						}
					}

					@Override
					public void onPageScrolled(int arg0, float arg1, int arg2) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onPageScrollStateChanged(int arg0) {
						// TODO Auto-generated method stub
					}
				});

		rg_nav_content
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (rg_nav_content.getChildAt(checkedId) != null) {
							// 滑动动画
							TranslateAnimation animation = new TranslateAnimation(
									currentIndicatorLeft,
									((RadioButton) rg_nav_content
											.getChildAt(checkedId)).getLeft(),
									0f, 0f);
							animation.setInterpolator(new LinearInterpolator());
							animation.setDuration(100);
							animation.setFillAfter(true);
							// 滑块执行位移动画
							iv_nav_indicator.startAnimation(animation);
							mViewPager.setCurrentItem(checkedId); // ViewPager
																	// 跟随一起 切换
							// 记录当前 下标的距最左侧的 距离
							currentIndicatorLeft = ((RadioButton) rg_nav_content
									.getChildAt(checkedId)).getLeft();
							// Log.i("PeriodicalClassfyActivity", ""+((checkedId
							// > 1 ? ((RadioButton)
							// rg_nav_content.getChildAt(checkedId)).getLeft() :
							// 0) - ((RadioButton)
							// rg_nav_content.getChildAt(2)).getLeft()));
							// mHsv.smoothScrollTo(
							// (checkedId > 1 ? ((RadioButton)
							// rg_nav_content.getChildAt(checkedId)).getLeft() :
							// 0) - ((RadioButton)
							// rg_nav_content.getChildAt(2)).getLeft(), 0);
						}
					}
				});
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
		mys.setRetryPolicy(HttpUtils.setTimeout());mQueue.add(mys);
		mQueue.start();

	}

	Listener<String> backlistener_sz = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();
			try {
				Favorite favorite = Favorite.formList(Task.TASK_GET_FAVOR,
						response);
				if (favorite != null) {
					arrayLists_sz = favorite.map;
					sz_count = favorite.recordcount;
					mSectionsPagerAdapter.notifyDataSetChanged();
				}
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				onError(2);
			}
			if (isdeleted_sz) {
				arrayList_sz.clear();
				isdeleted_sz = false;
			}
			if (arrayLists_sz == null ||arrayLists_sz.isEmpty()) {
				return;
			}else if (arrayLists_sz.get(GlobleData.BOOK_SZ_TYPE)==null||arrayLists_sz.get(GlobleData.BOOK_SZ_TYPE).isEmpty()) {
					return;
				}
				arrayList_sz.addAll(arrayLists_sz.get(GlobleData.BOOK_SZ_TYPE));
				adapter_sz.notifyDataSetChanged();
		}
	};

	Listener<String> backlistener_zk = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();
			try {
				Favorite favorite = Favorite.formList(Task.TASK_GET_FAVOR,
						response);
				if (favorite != null) {
					arrayLists_zk = favorite.map;
					zk_count = favorite.recordcount;
					mSectionsPagerAdapter.notifyDataSetChanged();
				}
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				onError(2);
			}
			if (isdeleted_zk) {
				arrayList_zk.clear();
				isdeleted_zk = false;
			}
			if (arrayLists_zk == null || arrayLists_zk.isEmpty()) {
				return;
			}else if (arrayLists_zk.get(GlobleData.BOOK_ZK_TYPE)==null||arrayLists_zk.get(GlobleData.BOOK_ZK_TYPE).isEmpty()) {
					return;
				}
				arrayList_zk.addAll(arrayLists_zk.get(GlobleData.BOOK_ZK_TYPE));
				adapter_zk.notifyDataSetChanged();
		}
	};

	Listener<String> backlistenermore = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			try {
				Favorite favorite = Favorite.formList(Task.TASK_GET_FAVOR,
						response);
				arrayLists_sz = favorite.map;
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
				if (listviewpagetag == GlobleData.BOOK_SZ_TYPE) {
					curpage_sz++;
					arrayList_sz.addAll(arrayLists_sz
							.get(GlobleData.BOOK_SZ_TYPE));
					// mSectionsPagerAdapter.notifyDataSetChanged();
					adapter_sz.notifyDataSetChanged();
				} else {
					curpage_zk++;
					arrayList_zk.addAll(arrayLists_sz
							.get(GlobleData.BOOK_ZK_TYPE));
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
			tabTitle[0] = getString(R.string.title_section1) + "(" + sz_count
					+ ")";
			tabTitle[1] = getString(R.string.title_section2) + "(" + zk_count
					+ ")";
			initNavigationHSV();
			return POSITION_UNCHANGED;
		}

		@Override
		public int getCount() {
			return tabTitle.length;
		}

		// @Override
		// public CharSequence getPageTitle(int position) {
		// //Log.i("getPageTitle", "getPageTitle");
		// Locale l = Locale.getDefault();
		// switch (position) {
		// case 0:
		// return
		// (getString(R.string.title_section1)+"("+sz_count+")").toUpperCase(l);
		// case 1:
		// return
		// (getString(R.string.title_section2)+"("+zk_count+")").toUpperCase(l);
		// }
		// return null;
		// }
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
		}

		List<Favorite> arrayList_temp;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
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
			// listView.setOnItemLongClickListener(this);
			return rootView;
		}

		Favorite favorite;

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int positon,
				long id) {
			if (id == -2) {
				if (parent.getAdapter().getCount() == 1) {
					return;
				}
				// 进度条
				moreprocess = view.findViewById(R.id.footer_progress);
				moreprocess.setVisibility(View.VISIBLE);
				// Log.i("parent.getTag()", "" + parent.getTag());
				listviewpagetag = (Integer) parent.getTag();
				// 请求网络更多
				if (listviewpagetag == GlobleData.BOOK_SZ_TYPE) {
					getfavorlist(curpage_sz + 1, perpage,
							GlobleData.BOOK_SZ_TYPE, GETNEXT);
				} else if (listviewpagetag == GlobleData.BOOK_ZK_TYPE) {
					getfavorlist(curpage_zk + 1, perpage,
							GlobleData.BOOK_ZK_TYPE, GETNEXT);
				}
			} else {
				if ((Integer) parent.getTag() == GlobleData.BOOK_SZ_TYPE) {
					favorite = arrayList_sz.get(positon);
					String lngid = favorite.getLngid();// 
					//深职院的书籍直接使用recordid modify by lj 20131212
					Book book = new Book(favorite.getLngid(), favorite.getOrgan(),
							favorite.getTitle(), favorite.getWriter(), lngid,
							favorite.getYears(), favorite.getPrice(),
							favorite.getRemark(), favorite.getImgurl());
					if (book != null) {
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
							favorite.getPagecount(), 0, favorite.getImgurl(),favorite.getWeburl());
					if (book != null) {
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

		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// if (parent.getAdapter().getItemId(position) != -2) {
		// int listviewid = (Integer) parent.getTag();
		// listview_id = listviewid;
		// listview_item_position = position;
		// del_favorite = arrayList_temp.get(position);
		//
		// senddel();
		// }
		// return false;
		// }
	}

	private int listview_id = GlobleData.BOOK_SZ_TYPE;
	private int listview_item_position = 0;

	static class ViewHolder {

		TextView title;// 书名
		TextView author;// 作者
		TextView publisher;// 出版社
		ImageView img;// 时间图片 不用修改
		TextView isbn;
		TextView favor_cancel;
		// Button btn_comment, btn_item_result_search_share, favorite;

	}

	class MyGridViewAdapter extends BaseAdapter {
		private Context myContext;
		private List<Favorite> arrayList;
		private ImageFetcher fetch;

		public MyGridViewAdapter(Context context, List<Favorite> list) {
			this.myContext = context;
			this.arrayList = list;
		}

		public MyGridViewAdapter(Context context, List<Favorite> list,
				ImageFetcher fetch) {
			this.myContext = context;
			this.arrayList = list;
			this.fetch = fetch;
		}

		public List<Favorite> getList() {
			return this.arrayList;
		}

		@Override
		public int getCount() {
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
			// Log.i("getView20130808", "getView");
			final ViewHolder holder;
			if (this.getCount() == 1) {
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.moreitemsview, null);
				convertView.setClickable(false);
				TextView tv = (TextView) convertView
						.findViewById(R.id.footer_txt);
				tv.setText("亲，您所在分类没有收藏哦");
				tv.setTextAppearance(myContext, R.style.TextStyle_nullcontent);
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
				// Log.i("convertView20130808", "convertView");
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.item_book_favor, null);
				holder = new ViewHolder();
				holder.title = (TextView) convertView
						.findViewById(R.id.re_name_txt);
				holder.author = (TextView) convertView
						.findViewById(R.id.re_author_txt);
				// holder.publisher = (TextView) convertView
				// .findViewById(R.id.re_addr_txt);
				holder.img = (ImageView) convertView
						.findViewById(R.id.re_book_img);
				// holder.u_abstract = (TextView)
				// convertView.findViewById(R.id.re_hot_txt);
				holder.isbn = (TextView) convertView
						.findViewById(R.id.txt_abst);
				holder.favor_cancel = (TextView) convertView
						.findViewById(R.id.favor_cancel);
				holder.favor_cancel.setTextColor(getResources().getColor(
						R.color.green_light));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String author = context.getResources().getString(
					R.string.item_author);
			String publish = context.getResources().getString(
					R.string.item_publish);
			String time = context.getResources().getString(R.string.item_time);
			// String describe = context.getResources().getString(
			// R.string.item_describe);
			Favorite favorite = arrayList.get(position);
			holder.title.setText(favorite.getTitle());
			holder.author.setText(author + favorite.getWriter());
			// holder.publisher.setText(publish +
			// favorite.getOrgan()+","+favorite.getYears());
			holder.isbn.setText("收藏时间:" + favorite.getFavoritetime());
			final ViewGroup temp_parent = parent;
			final int temp_position = position;
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
			if (favorite.getTypeid().equals(GlobleData.BOOK_ZK_TYPE + "")) {
				holder.img.setVisibility(View.GONE);
			} else {
				// 图片
				if (!TextUtils.isEmpty(favorite.getImgurl())) {
					fetch.loadImage(favorite.getImgurl(), holder.img);
				} else {
					holder.img.setImageDrawable(getResources().getDrawable(
							R.drawable.defaut_book));
				}
			}
			return convertView;
		}
	}

	public void init() {
		tabTitle[0] = getString(R.string.title_section1) + "(" + sz_count + ")";
		tabTitle[1] = getString(R.string.title_section2) + "(" + zk_count + ")";

		rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
		mHsv = (SwipHorizontalScrollView) findViewById(R.id.mHsv);
		// 内容
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		iv_nav_left = (ImageView) findViewById(R.id.iv_nav_left);
		iv_nav_right = (ImageView) findViewById(R.id.iv_nav_right);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		iv_nav_right.setVisibility(View.GONE);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		indicatorWidth = dm.widthPixels / tabTitle.length;
		// TODO step0 初始化滑动下标的宽 根据屏幕宽度和可见数量 来设置RadioButton的宽度)
		LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		iv_nav_indicator.setLayoutParams(cursor_Params);
		mHsv.setSomeParam(rl_nav, iv_nav_left, iv_nav_right, this);
		// 获取布局填充器
		mInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		// 另一种方式获取
		// LayoutInflater mInflater = LayoutInflater.from(this);
		initNavigationHSV();
		
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

	private void initNavigationHSV() {
		rg_nav_content.removeAllViews();
		for (int i = 0; i < tabTitle.length; i++) {
			RadioButton rb = (RadioButton) mInflater.inflate(
					R.layout.nav_radiogroup_item, null);
			if (i == 0) {
				rb.setChecked(true);
			}
			rb.setId(i);
			rb.setText(tabTitle[i]);
			rb.setLayoutParams(new LayoutParams(indicatorWidth,
					LayoutParams.MATCH_PARENT));
			rg_nav_content.addView(rb);
		}
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

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (customProgressDialog != null) {
			customProgressDialog.dismiss();
			customProgressDialog = null;
		}
	}
}
