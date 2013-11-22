package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
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
import com.cqvip.moblelib.model.Favorite;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.szy.R;
import com.cqvip.moblelib.utils.HttpUtils;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.moblelib.view.SwipHorizontalScrollView;
import com.cqvip.utils.Tool;

/**
 * <p>
 * 文件名称: GroupOfReadersActivity.java 文件描述: 书友圈 版权所有: 版权所有(C)2013-2020 公 司:
 * 重庆维普咨询有限公司 内容摘要: 其他说明: 完成日期： 201年5月10日 修改记录:
 * </p>
 * 
 * @author LHP,LJ
 */
public class GroupOfReadersActivity extends BaseFragmentImageActivity {
	public static final int GETFIRSTPAGE_SZ = 1;
	public static final int GETFIRSTPAGE_ZK = 2;
	public static final int GETNEXT = 3;

	private int curpage = 1;// 第几页
	private int perpage = Constant.DEFAULT_COUNT;// 每页显示条数
	private View moreprocess;
	private int curpage_sz = 1, curpage_zk = 1;

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
	public  String[] tabTitle=new String[2]; // 标题
	
	protected CustomProgressDialog customProgressDialog;
	Map<Integer, List<Favorite>> arrayLists_sz, arrayLists_zk;
	ArrayList<Favorite> arrayList_zk = new ArrayList<Favorite>();
	ArrayList<Favorite> arrayList_sz = new ArrayList<Favorite>();
	private int listviewpagetag = GlobleData.BOOK_SZ_TYPE;
	
	private int sz_count,zk_count; //条数

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
	
	private void getfavorlist(int pagecount, int perpage, int typeid, int type) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("libid", GlobleData.LIBIRY_ID);
		map.put("vipuserid", GlobleData.cqvipid);
		map.put("curpage", "" + pagecount);
		map.put("perpage", "" + perpage);
		map.put("typeid", "" + typeid);
		// ManagerService.addNewTask(new Task(Task.TASK_COMMENT_BOOKLIST, map));
		switch (type) {
		case GETFIRSTPAGE_SZ:
			requestVolley(map, GlobleData.SERVER_URL
					+ "/cloud/commentlistuser.aspx", backlistener_sz,
					Method.POST);
			break;
		case GETFIRSTPAGE_ZK:
			requestVolley(map, GlobleData.SERVER_URL
					+ "/cloud/commentlistuser.aspx", backlistener_zk,
					Method.POST);
			break;
		case GETNEXT:
			requestVolley(map, GlobleData.SERVER_URL
					+ "/cloud/commentlistuser.aspx", backlistenermore,
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
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try {
				Favorite favorite=Favorite.formList(Task.TASK_COMMENT_BOOKLIST, response);
				if(favorite!=null){
				arrayLists_sz = favorite.map;
				sz_count=favorite.recordcount;
				mSectionsPagerAdapter.notifyDataSetChanged();
				}
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				onError(2);
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
			//customProgressDialog.dismiss();
			try {
				Favorite favorite=Favorite.formList(Task.TASK_COMMENT_BOOKLIST, response);
				if(favorite!=null){
				arrayLists_zk = favorite.map;
				zk_count=favorite.recordcount;
				mSectionsPagerAdapter.notifyDataSetChanged();
				}
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				onError(2);
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
				Favorite favorite=Favorite.formList(Task.TASK_COMMENT_BOOKLIST, response);
				arrayLists_sz = favorite.map;
				sz_count=favorite.recordcount;
			} catch (BookException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				onError(2);
			}
            if(arrayLists_sz==null||arrayLists_sz.isEmpty()){
				Tool.ShowMessages(context, "没有更多内容可供加载");
				moreprocess.setVisibility(View.GONE);
				return;
            }
			if (listviewpagetag == GlobleData.BOOK_SZ_TYPE) {
				if (arrayLists_sz.get(GlobleData.BOOK_SZ_TYPE) != null
						&& !arrayLists_sz.get(GlobleData.BOOK_SZ_TYPE)
								.isEmpty()) {
					curpage_sz++;
					arrayList_sz.addAll(arrayLists_sz
							.get(GlobleData.BOOK_SZ_TYPE));
					// mSectionsPagerAdapter.notifyDataSetChanged();
					adapter_sz.notifyDataSetChanged();
				} else {
					Tool.ShowMessages(context, "没有更多内容可供加载");
					moreprocess.setVisibility(View.GONE);
				}
			} else {
				if (arrayLists_sz.get(GlobleData.BOOK_ZK_TYPE) != null
						&& !arrayLists_sz.get(GlobleData.BOOK_ZK_TYPE)
								.isEmpty()) {
					curpage_zk++;
					arrayList_zk.addAll(arrayLists_sz
							.get(GlobleData.BOOK_ZK_TYPE));
					adapter_zk.notifyDataSetChanged();
				} else {
					Tool.ShowMessages(context, "没有更多内容可供加载");
					moreprocess.setVisibility(View.GONE);
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

//		@Override
//		public CharSequence getPageTitle(int position) {
//			//Log.i("getPageTitle", "getPageTitle_"+position);
//			Locale l = Locale.getDefault();
//			switch (position) {
//			case 0:
//				return (getString(R.string.title_section1)+"("+sz_count+")").toUpperCase(l);
//			case 1:
//				return (getString(R.string.title_section2)+"("+zk_count+")").toUpperCase(l);
//			}
//			return null;
//		}
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
			return rootView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int positon,
				long id) {
			if (id == -2) { // 更多
				if (parent.getAdapter().getCount() == 1) {
					return;
				}
				// 进度条
				moreprocess = view.findViewById(R.id.footer_progress);
				moreprocess.setVisibility(View.VISIBLE);
				// Log.i("parent.getTag()", "" + parent.getTag());
				// 请求网络更多
				listviewpagetag = (Integer) parent.getTag();
				if (listviewpagetag == GlobleData.BOOK_SZ_TYPE) {
					getfavorlist(curpage_sz + 1, perpage,
							GlobleData.BOOK_SZ_TYPE, GETNEXT);
				} else if (listviewpagetag == GlobleData.BOOK_ZK_TYPE) {
					getfavorlist(curpage_zk + 1, perpage,
							GlobleData.BOOK_ZK_TYPE, GETNEXT);
				}
			} else {
				Favorite favorite = null;
				int typeflag = 5;
				if ((Integer) parent.getTag() == GlobleData.BOOK_SZ_TYPE) {
					favorite = adapter_sz.getLists().get(positon);
					typeflag = 5;
				} else if ((Integer) parent.getTag() == GlobleData.BOOK_ZK_TYPE) {
					favorite = adapter_zk.getLists().get(positon);
					typeflag = 4;
				}
				Book book = new Book(favorite.getLngid(), favorite.getOrgan(),
						favorite.getTitle(), favorite.getWriter(),
						favorite.getLngid(), favorite.getYears(),
						favorite.getPrice(), favorite.getRemark(),
						favorite.getImgurl());
				Tool.getCommentList(context, book, typeflag);

				// Book book = adapter.getLists().get(positon);
				// if(book!=null){
				// Log.i("ResultOnSearchActivity",book.toString());
				// Intent _intent = new
				// Intent(context,DetailBookActivity.class);
				// Bundle bundle = new Bundle();
				// bundle.putSerializable("book", book);
				// _intent.putExtra("detaiinfo", bundle);
				// startActivity(_intent);
				// }

				// Book book = lists.get(position-1);
				// if(book!=null){
				// Bundle bundle = new Bundle();
				// bundle.putSerializable("book", book);
				// _intent.putExtra("detaiinfo", bundle);
				// startActivityForResult(_intent, 1);
			}
		}

		// @Override
		// public boolean onItemLongClick(AdapterView<?> parent, View view,
		// int position, long id) {
		// Favorite favorite = arrayList_temp.get(position);
		// if (favorite != null) {
		// HashMap<String, String> map = new HashMap<String, String>();
		// map.put("libid", GlobleData.LIBIRY_ID);
		// map.put("vipuserid", GlobleData.cqvipid);
		// Log.i("删除收藏", GlobleData.cqvipid);
		// map.put("keyid", favorite.getFavoritekeyid());
		// Log.i("keyid", favorite.getFavoritekeyid());
		// map.put("typeid", "" + favorite.getTypeid());
		// ManagerService
		// .addNewTask(new Task(Task.TASK_CANCEL_FAVOR, map));
		// customProgressDialog.show();
		// }
		// return false;
		// }
	}

	static class ViewHolder {
		TextView title;// 书名
		TextView author;// 作者
		TextView publisher;// 出版社
		TextView commentcount;// 评论数
		// TextView abst_tv;// 简介
		ImageView img;// 时间图片 不用修改
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

		public List<Favorite> getLists() {
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (this.getCount() == 1) {
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.moreitemsview, null);
				convertView.setClickable(false);
				TextView tv = (TextView) convertView
						.findViewById(R.id.footer_txt);
				tv.setText("亲，您所在分类没有评论哦");
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
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.item_book_comment, null);
				holder = new ViewHolder();

				holder.title = (TextView) convertView
						.findViewById(R.id.re_name_txt);
				holder.author = (TextView) convertView
						.findViewById(R.id.re_author_txt);
				holder.publisher = (TextView) convertView
						.findViewById(R.id.re_addr_txt);
				holder.img = (ImageView) convertView
						.findViewById(R.id.re_book_img);
				holder.commentcount = (TextView) convertView
						.findViewById(R.id.txt_abst);
				holder.commentcount.setTextColor(getResources().getColor(
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
			String commentcount = context.getResources().getString(
					R.string.item_comment_count);

			Favorite favorite = arrayList.get(position);
			holder.title.setText(favorite.getTitle());
			holder.author.setText(author + favorite.getWriter());
			if(favorite.getTypeid().equals(GlobleData.BOOK_ZK_TYPE+"")){
				holder.publisher.setVisibility(View.GONE);
			}else{
			holder.publisher.setText(publish + favorite.getOrgan()+","+favorite.getYears());
			}
			holder.commentcount.setText(commentcount + "("
					+ favorite.getCommentcount() + ")");
			
			if(favorite.getTypeid().equals(GlobleData.BOOK_ZK_TYPE+"")){
				holder.img.setVisibility(View.GONE);
			}else{
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
		tabTitle[0] =getString(R.string.title_section1)+"("+sz_count+")";
		tabTitle[1] =getString(R.string.title_section2)+"("+zk_count+")";
		
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
	// @Override
	// public void refresh(Object... obj) {
	// customProgressDialog.dismiss();
	// Log.i("MyFavorActivity_refresh", "refresh");
	// int temp = (Integer) obj[0];
	// if (temp == COMMENTLIST) {
	// arrayLists = (Map<Integer, List<Favorite>>) obj[1];
	// if (arrayLists != null && !arrayLists.isEmpty()) {
	// // if
	// //
	// ((curpage_sz>1||curpage_zk>1)&&(arrayLists.get(GlobleData.BOOK_ZK_TYPE)==null||arrayLists.get(GlobleData.BOOK_ZK_TYPE).isEmpty())&&
	// //
	// (arrayLists.get(GlobleData.BOOK_SZ_TYPE)==null||arrayLists.get(GlobleData.BOOK_SZ_TYPE).isEmpty()))
	// // {
	// // Tool.ShowMessages(context, "没有更多内容可供加载");
	// // moreprocess.setVisibility(View.GONE);
	// // return;
	// // }
	// ArrayList<Favorite> temp_sz_list = (ArrayList<Favorite>) arrayLists
	// .get(GlobleData.BOOK_SZ_TYPE);
	// ArrayList<Favorite> temp_zk_list = (ArrayList<Favorite>) arrayLists
	// .get(GlobleData.BOOK_ZK_TYPE);
	// if (temp_sz_list != null) {
	// arrayList_sz
	// .addAll(arrayLists.get(GlobleData.BOOK_SZ_TYPE));
	// // mSectionsPagerAdapter.notifyDataSetChanged();
	// adapter_sz.notifyDataSetChanged();
	// } else if (temp_zk_list != null) {
	// arrayList_zk
	// .addAll(arrayLists.get(GlobleData.BOOK_ZK_TYPE));
	// adapter_zk.notifyDataSetChanged();
	// }
	// } else if (curpage_sz > 1 || curpage_zk > 1) {
	// Tool.ShowMessages(context, "没有更多内容可供加载");
	// if (moreprocess != null)
	// moreprocess.setVisibility(View.GONE);
	// }
	// }
	// }

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
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(customProgressDialog!=null){
			customProgressDialog.dismiss();
			customProgressDialog=null;
		}
	}
}
