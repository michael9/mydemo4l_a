package com.cqvip.moblelib.activity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
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
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.artifex.mupdfdemo.MuPDFActivity;
import com.cqvip.dao.DaoException;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.bate1.R;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.MEBookDao;
import com.cqvip.moblelib.entity.MEbook;
import com.cqvip.moblelib.view.SwipHorizontalScrollView;
import com.cqvip.utils.DownloadManagerPro;
import com.cqvip.utils.FileUtils;
import com.cqvip.utils.Tool;

public class DownLoadManagerActivity extends BaseFragmentImageActivity {

	public static final int LOADING_BOOK = 98;
	public static final int LOADED_BOOK = 99;
	// public static final int GETFIRSTPAGE_ZK = 2;
	// public static final int GETNEXT = 3;

	// private int curpage = 1;// 第几页
	// private int perpage = 10;// 每页显示条数
	// private View moreprocess;
	// private int curpage_sz = 1, curpage_zk = 1;
	// 缓存
	private HashMap<Long, Boolean> loded;
	MyGridViewAdapter adapter_sz;
	MyGridViewAdapter_Loaded adapter_zk;
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
	public static String[] tabTitle = { "已下载", "下载中" }; // 标题
	private LayoutInflater mInflater;
	private int currentIndicatorLeft = 0;
	// protected CustomProgressDialog customProgressDialog;
	// Map<Integer, List<Favorite>> arrayLists_sz, arrayLists_zk;
	// ArrayList<Favorite> arrayList_zk = new ArrayList<Favorite>();
	// ArrayList<Favorite> arrayList_sz = new ArrayList<Favorite>();

	private DownloadChangeObserver downloadObserver;
	private MyHandler handler;
	private DownloadManagerPro downloadManagerPro;
	private DownloadManager downloadManager;

	private ArrayList<MEbook> mebooks_list = new ArrayList<MEbook>();// 获取downloadid,下载中
	private ArrayList<MEbook> mebooks_listloaded = new ArrayList<MEbook>();// 已下载
	private ArrayList<int[]> _lists = new ArrayList<int[]>();// 获取下载状态

	// private ArrayList<int[]> _listsloaded= new ArrayList<int[]>();//已下载id
	final static String TAG = "DownLoadManagerActivity";
	private boolean ispressdownbutton;

	private String del_bookname;
	private int download_status;
	private MEbook del_book;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_periodical_classfy);
		init();
		setListener();
		initData();
		context = this;
		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());
		// Set up the ViewPager with the sections adapter.
		mViewPager.setAdapter(mSectionsPagerAdapter);
		// 获取数据
		// getfavorlist(curpage, perpage, GlobleData.BOOK_SZ_TYPE,
		// GETFIRSTPAGE_SZ);
		// getfavorlist(curpage, perpage, GlobleData.BOOK_ZK_TYPE,
		// GETFIRSTPAGE_ZK);
		ispressdownbutton = getIntent().getBooleanExtra("ispressdownbutton",
				false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		/** observer download change **/
		getContentResolver().registerContentObserver(
				DownloadManagerPro.CONTENT_URI, true, downloadObserver);
		getDownloadStatus();
	}

	@Override
	protected void onPause() {
		super.onPause();
		getContentResolver().unregisterContentObserver(downloadObserver);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		if (hasFocus && ispressdownbutton) {
			mViewPager.setCurrentItem(1);
		}
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
							Log.i(TAG,
									"currentIndicatorLeft:"
											+ currentIndicatorLeft
											+ "--checkedId:"
											+ checkedId
											+ "--"
											+ ((RadioButton) rg_nav_content
													.getChildAt(checkedId))
													.getLeft());
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

	MEBookDao meBookDao;

	private void initData() {
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

		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
		downloadObserver = new DownloadChangeObserver();
		handler = new MyHandler();
		downloadManagerPro = new DownloadManagerPro(downloadManager);
		loded = new HashMap<Long, Boolean>();
		meBookDao = new MEBookDao(this);
		try {
			mebooks_list = (ArrayList<MEbook>) meBookDao.queryall(0);
			mebooks_listloaded = (ArrayList<MEbook>) meBookDao.queryall(1);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 下载中

		getDownloadStatus();
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

	public void getDownloadStatus() {
		ArrayList<int[]> temp_lists = new ArrayList<int[]>();
		if (mebooks_list != null && !mebooks_list.isEmpty()) {
			int a = mebooks_list.size();
			for (int i = 0; i < a; i++) {
				int[] bytesAndStatus = downloadManagerPro
						.getBytesAndStatus(mebooks_list.get(i).getDownloadid());
				temp_lists.add(bytesAndStatus);
			}
		}
		_lists.clear();
		_lists.addAll(temp_lists);
		if (adapter_sz != null) {
			// adapter_sz.notifyDataSetChanged();
			handler.sendMessage(handler.obtainMessage(0));
		}
	}

	private class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				adapter_sz.notifyDataSetChanged();
				break;
			}
		}
	}

	class DownloadChangeObserver extends ContentObserver {
		public DownloadChangeObserver() {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			getDownloadStatus();
		}
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
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		// /**
		// * 重写此方法为了使notifyDataSetChanged有效 Called when the host view is
		// * attempting to determine if an item's position has changed. Returns
		// * POSITION_UNCHANGED if the position of the given item has not
		// changed
		// * or POSITION_NONE if the item is no longer present in the adapter.
		// The
		// * default implementation assumes that items will never change
		// position
		// * and always returns POSITION_UNCHANGED.
		// */
		// @Override
		// public int getItemPosition(Object object) {
		// // TODO Auto-generated method stub
		// return POSITION_NONE;
		// }

		@Override
		public int getCount() {
			return tabTitle.length;
		}

		// @Override
		// public CharSequence getPageTitle(int position) {
		// Locale l = Locale.getDefault();
		//
		// switch (position) {
		// case 0:
		// return "已下载".toUpperCase(l);
		// case 1:
		// return "下载中".toUpperCase(l);
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

		// ArrayList<int[]> temp_aArrayList;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.myfavor_fragment,
					container, false);
			ListView listView = (ListView) rootView
					.findViewById(R.id.favorlist);
			int i = getArguments().getInt(ARG_SECTION_NUMBER);
			if (i == 0) {
				listView.setTag(GlobleData.BOOK_ZK_TYPE);
				adapter_zk = new MyGridViewAdapter_Loaded(getActivity(),
						mebooks_listloaded, mImageFetcher);
				listView.setAdapter(adapter_zk);
			} else if (i == 1) {
				adapter_sz = new MyGridViewAdapter(getActivity(), _lists,
						mImageFetcher);
				listView.setAdapter(adapter_sz);
				listView.setTag(GlobleData.BOOK_SZ_TYPE);
			}

			listView.setOnItemClickListener(this);
			return rootView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int positon,
				long id) {
			if (id == -2) { // 更多
				// if (parent.getAdapter().getCount() == 1) {
				// return;
				// }
				// // 进度条
				// moreprocess = view.findViewById(R.id.footer_progress);
				// moreprocess.setVisibility(View.VISIBLE);
				// // Log.i("parent.getTag()", "" + parent.getTag());
				// // 请求网络更多
				// listviewpagetag = (Integer) parent.getTag();
				// if (listviewpagetag == GlobleData.BOOK_SZ_TYPE) {
				// getfavorlist(curpage_sz + 1, perpage,
				// GlobleData.BOOK_SZ_TYPE, GETNEXT);
				// } else if (listviewpagetag == GlobleData.BOOK_ZK_TYPE) {
				// getfavorlist(curpage_zk + 1, perpage,
				// GlobleData.BOOK_ZK_TYPE, GETNEXT);
				// }
			} else {
				MEbook mEbook = null;
				if ((Integer) parent.getTag() == GlobleData.BOOK_SZ_TYPE) {
					mEbook = mebooks_list.get(positon);
				} else if ((Integer) parent.getTag() == GlobleData.BOOK_ZK_TYPE) {
					mEbook = mebooks_listloaded.get(positon);
				}
				String filename = getfillName(mEbook.getDownloadid());
				if (mEbook != null && mEbook.getIsdownload() == 1
						&& !filename.equals("")) {
					String filepath = Environment.getExternalStorageDirectory()
							+ File.separator
							+ EbookDetailActivity.DOWNLOAD_FOLDER_NAME
							+ File.separator + filename;
					Log.i("filepath", filepath);
					File file = new File(filepath);
					if (file.exists()) {
						// Intent intent = new
						// Intent(DownLoadManagerActivity.this,OpenFileActivity.class);
						// intent.setDataAndType(Uri.fromFile(file),
						// "application/pdf");
						// intent.setAction("android.intent.action.VIEW");
						// startActivity(intent);
						Uri uri = Uri.parse(filepath);
						Intent intent = new Intent(context, MuPDFActivity.class);
						intent.setAction(Intent.ACTION_VIEW);
						intent.setData(uri);
						startActivity(intent);
					} else {
						Tool.ShowMessagel(context, "文件" + filename + "不存在");
					}
				}
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
		ImageView download_image;
		TextView download_title;
		TextView download_size;//
		TextView download_precent;//
		ProgressBar download_progress;//
		ImageView download_cancel;//
		TextView downloadtip;
	}

	class MyGridViewAdapter_Loaded extends BaseAdapter {
		private Context myContext;
		private ArrayList<MEbook> arrayList;
		private ImageFetcher fetch;

		public MyGridViewAdapter_Loaded(Context context, ArrayList<MEbook> list) {
			this.myContext = context;
			this.arrayList = list;
		}

		public MyGridViewAdapter_Loaded(Context context,
				ArrayList<MEbook> list, ImageFetcher fetch) {
			this.myContext = context;
			this.arrayList = list;
			this.fetch = fetch;
		}

		@Override
		public int getCount() {
			if (arrayList != null && !arrayList.isEmpty()) {
				return arrayList.size();
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
			if (arrayList != null && !arrayList.isEmpty()) {
				return position;
			} else {
				return -2;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (arrayList == null || arrayList.isEmpty()) {
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.moreitemsview, null);
				convertView.setClickable(false);
				TextView tv = (TextView) convertView
						.findViewById(R.id.footer_txt);
				tv.setText("亲，没有已下载的电子书");
				tv.setTextAppearance(myContext, R.style.TextStyle_nullcontent);
				tv.setClickable(false);
				return convertView;
			}
			// 更多
			// if (position == this.getCount() - 1) {
			// convertView = LayoutInflater.from(myContext).inflate(
			// R.layout.moreitemsview, null);
			// return convertView;
			// }
			if (convertView == null
					|| convertView.findViewById(R.id.linemore) != null) {
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.item_downloadmanager, null);
				holder = new ViewHolder();
				holder.download_title = (TextView) convertView
						.findViewById(R.id.download_title);
				holder.download_title.setMaxLines(2);
				holder.download_size = (TextView) convertView
						.findViewById(R.id.download_size);
				holder.download_image = (ImageView) convertView
						.findViewById(R.id.book_img);
				convertView.findViewById(R.id.download_precent).setVisibility(
						View.GONE);
				convertView.findViewById(R.id.download_progress).setVisibility(
						View.GONE);
				holder.download_cancel = (ImageView) convertView
						.findViewById(R.id.download_cancel);
				convertView.findViewById(R.id.download_tip).setVisibility(
						View.GONE);
				;
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final MEbook book = arrayList.get(position);
			final String bookname = getfillName(book.getDownloadid());
			if (book != null && !bookname.equals("")) {
				holder.download_title.setText(bookname);
				holder.download_size.setText(getResources().getString(
						R.string.item_size)
						+ getAppSize(book.getPdfsize()));
				// 图片
				if (!TextUtils.isEmpty(book.getImgurl())) {
					fetch.loadImage(book.getImgurl(), holder.download_image);
				} else {
					holder.download_image.setImageDrawable(getResources()
							.getDrawable(R.drawable.defaut_book));
				}
				holder.download_cancel
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								saveObject(bookname, 0, book);
								senddel(LOADED_BOOK);

							}
						});
			}
			// 图片
			// if (!TextUtils.isEmpty(favorite.getImgurl())) {
			// fetch.loadImage(favorite.getImgurl(), holder.img);
			// } else {
			// holder.img.setImageDrawable(getResources().getDrawable(
			// R.drawable.defaut_book));
			// }

			return convertView;
		}
	}

	private String getfillName(Long downloadid) {
		String title = downloadManagerPro.getFileName(downloadid);
		if (title != null) {
			return title.substring(title.lastIndexOf("/") + 1, title.length());
		} else {
			return "";
		}
	}

	class MyGridViewAdapter extends BaseAdapter {
		private Context myContext;
		private ArrayList<int[]> arrayList;
		private ImageFetcher fetch;

		public MyGridViewAdapter(Context context, ArrayList<int[]> list) {
			this.myContext = context;
			this.arrayList = list;
		}

		public MyGridViewAdapter(Context context, ArrayList<int[]> list,
				ImageFetcher fetch) {
			this.myContext = context;
			this.arrayList = list;
			this.fetch = fetch;
		}

		@Override
		public int getCount() {
			if (arrayList != null && !arrayList.isEmpty()) {
				return arrayList.size();
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
			if (arrayList != null && !arrayList.isEmpty()) {
				return position;
			} else {
				return -2;
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ViewHolder holder;
			if (arrayList == null || arrayList.isEmpty()) {
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.moreitemsview, null);
				convertView.setClickable(false);
				TextView tv = (TextView) convertView
						.findViewById(R.id.footer_txt);
				tv.setTextSize(R.dimen.listview_nullcontent_textsize);
				tv.setText("亲，没有下载中的电子书");
				tv.setTextAppearance(myContext, R.style.TextStyle_nullcontent);
				tv.setClickable(false);
				return convertView;
			}
			// 更多
			// if (position == this.getCount() - 1) {
			// convertView = LayoutInflater.from(myContext).inflate(
			// R.layout.moreitemsview, null);
			// return convertView;
			// }
			if (convertView == null
					|| convertView.findViewById(R.id.linemore) != null) {
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.item_downloadmanager, null);
				holder = new ViewHolder();
				holder.download_image = (ImageView) convertView
						.findViewById(R.id.book_img);
				holder.download_title = (TextView) convertView
						.findViewById(R.id.download_title);
				holder.download_size = (TextView) convertView
						.findViewById(R.id.download_size);
				holder.download_precent = (TextView) convertView
						.findViewById(R.id.download_precent);
				holder.download_progress = (ProgressBar) convertView
						.findViewById(R.id.download_progress);
				holder.download_cancel = (ImageView) convertView
						.findViewById(R.id.download_cancel);
				holder.downloadtip = (TextView) convertView
						.findViewById(R.id.download_tip);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final MEbook book = mebooks_list.get(position);
			final int downloadstatus;
			if (!arrayList.isEmpty()) {
				int[] int_array = arrayList.get(position);
				// 图片
				if (!TextUtils.isEmpty(book.getImgurl())) {
					fetch.loadImage(book.getImgurl(), holder.download_image);
				} else {
					holder.download_image.setImageDrawable(getResources()
							.getDrawable(R.drawable.defaut_book));
				}
				// Log.i("int_array", ""+int_array[1]);
				holder.download_title.setText(book.getTitle_c());
				holder.download_progress.setMax(int_array[1]);
				holder.download_progress.setProgress(int_array[0]);
				holder.download_size.setText(getAppSize(int_array[0]) + "/"
						+ getAppSize(int_array[1]));
				holder.download_precent.setText(getNotiPercent(int_array[0],
						int_array[1]));
				downloadstatus = int_array[2];
				if (downloadstatus == DownloadManager.STATUS_SUCCESSFUL
						&& !loded.containsKey(book.getDownloadid())) {
					holder.downloadtip.setText("下载完成，请点击打开");
					// 放入缓存
					loded.put(book.getDownloadid(), true);
					// 更新数据库
					book.setPdfsize((long) int_array[1]);
					updateDateBase(book);
				}

				final String bookname = getfillName(book.getDownloadid());
				if (!bookname.equals("")) {
					holder.download_cancel
							.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									saveObject(bookname, downloadstatus, book);
									senddel(LOADING_BOOK);
								}

							});
				}
			}
			// 图片
			// if (!TextUtils.isEmpty(favorite.getImgurl())) {
			// fetch.loadImage(favorite.getImgurl(), holder.img);
			// } else {
			// holder.img.setImageDrawable(getResources().getDrawable(
			// R.drawable.defaut_book));
			// }

			return convertView;
		}

		private void updateDateBase(final MEbook book) {
			try {
				book.setIsdownload(MEbook.TYPE_DOWNLOADED);
				meBookDao.updateState(book);
			} catch (DaoException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 删除对话框
	 */
	public void senddel(int type) {
		Intent intent = new Intent();
		intent.setClass(this, ActivityDlg.class);
		intent.putExtra("ACTIONID", 0);
		intent.putExtra("MSGBODY", "确定删除该记录？");
		intent.putExtra("BTN_CANCEL", 1);
		startActivityForResult(intent, type);
	}

	/**
	 * 删除更新，暂存
	 * 
	 * @param bookname
	 * @param downloadstatus
	 * @param book
	 */
	private void saveObject(String bookname, int downloadstatus, MEbook book) {
		del_bookname = bookname;
		del_book = book;
		download_status = downloadstatus;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 0) {
			switch (requestCode) {
			case LOADING_BOOK:
				if (mebooks_list != null && !mebooks_list.isEmpty()) {
					try {
						downloadManager.remove(del_book.getDownloadid());
						meBookDao.deldownload(del_book.getDownloadid());
						mebooks_list.remove(del_book);
						if (download_status == DownloadManager.STATUS_SUCCESSFUL) {
							FileUtils.DeleteFolder(Environment
									.getExternalStorageDirectory()
									+ File.separator
									+ EbookDetailActivity.DOWNLOAD_FOLDER_NAME
									+ File.separator + del_bookname);
						}
					} catch (DaoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					getDownloadStatus();
				}

				break;
			case LOADED_BOOK:
				if (!mebooks_listloaded.isEmpty()) {
					try {
						meBookDao.deldownload(del_book.getDownloadid());
						mebooks_listloaded.remove(del_book);
						adapter_zk.notifyDataSetChanged();
						FileUtils.DeleteFolder(Environment
								.getExternalStorageDirectory()
								+ File.separator
								+ EbookDetailActivity.DOWNLOAD_FOLDER_NAME
								+ File.separator + del_bookname);
					} catch (DaoException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			default:
				break;
			}
		}
	}

	static final DecimalFormat DOUBLE_DECIMAL_FORMAT = new DecimalFormat("0.##");
	public static final int MB_2_BYTE = 1024 * 1024;
	public static final int KB_2_BYTE = 1024;

	/**
	 * @param size
	 * @return
	 */
	public static CharSequence getAppSize(long size) {
		if (size <= 0) {
			return "0M";
		}

		if (size >= MB_2_BYTE) {
			return new StringBuilder(16).append(
					DOUBLE_DECIMAL_FORMAT.format((double) size / MB_2_BYTE))
					.append("M");
		} else if (size >= KB_2_BYTE) {
			return new StringBuilder(16).append(
					DOUBLE_DECIMAL_FORMAT.format((double) size / KB_2_BYTE))
					.append("K");
		} else {
			return size + "B";
		}
	}

	public static String getNotiPercent(long progress, long max) {
		int rate = 0;
		if (progress <= 0 || max <= 0) {
			rate = 0;
		} else if (progress > max) {
			rate = 100;
		} else {
			rate = (int) ((double) progress / max * 100);
		}
		return new StringBuilder(16).append(rate).append("%").toString();
	}

	public void init() {
		rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
		mHsv = (SwipHorizontalScrollView) findViewById(R.id.mHsv);
		// 内容
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		iv_nav_left = (ImageView) findViewById(R.id.iv_nav_left);
		iv_nav_right = (ImageView) findViewById(R.id.iv_nav_right);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		iv_nav_right.setVisibility(View.GONE);

		TextView title = (TextView) findViewById(R.id.txt_header);
		title.setText(R.string.serv_download);
		ImageView back = (ImageView) findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
