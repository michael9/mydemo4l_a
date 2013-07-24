package com.cqvip.moblelib.activity;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.DownloadManager;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.activity.GroupOfReadersActivity.ViewHolder;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.Favorite;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.utils.DownloadManagerPro;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

//public class DownLoadManagerActivity extends Activity {
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_down_load_manager);
//	}
//
//
//}

public class DownLoadManagerActivity extends BaseFragmentImageActivity {
	public static final int GETFIRSTPAGE_SZ = 1;
	public static final int GETFIRSTPAGE_ZK = 2;
	public static final int GETNEXT = 3;

	private int curpage = 1;// 第几页
	private int perpage = 10;// 每页显示条数
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
	ViewPager mViewPager;
	PagerTitleStrip mPagerTitleStrip;
	Context context;
	protected CustomProgressDialog customProgressDialog;
	Map<Integer, List<Favorite>> arrayLists_sz, arrayLists_zk;
	ArrayList<Favorite> arrayList_zk = new ArrayList<Favorite>();
	ArrayList<Favorite> arrayList_sz = new ArrayList<Favorite>();
	private int listviewpagetag = GlobleData.BOOK_SZ_TYPE;
	
    private DownloadChangeObserver downloadObserver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myfavor);
		init();
		initData();
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
    protected void onResume() {
        super.onResume();
        /** observer download change **/
        getContentResolver().registerContentObserver(DownloadManagerPro.CONTENT_URI, true, downloadObserver);
        updateView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getContentResolver().unregisterContentObserver(downloadObserver);
    }
	
    private void initData() {
    	downloadObserver = new DownloadChangeObserver();
        /**
         * get download id from preferences.<br/>
         * if download id bigger than 0, means it has been downloaded, then query status and show right text;
         */
        downloadId = PreferencesUtils.getLongPreferences(context, PreferencesUtils.KEY_NAME_DOWNLOAD_ID);
        updateView();
        downloadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                File folder = new File(DOWNLOAD_FOLDER_NAME);
                if (!folder.exists() || !folder.isDirectory()) {
                    folder.mkdirs();
                }

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(APK_URL));
                request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME, DOWNLOAD_FILE_NAME);
                request.setTitle(getString(R.string.download_notification_title));
                request.setDescription("meilishuo desc");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setVisibleInDownloadsUi(false);
                // request.allowScanningByMediaScanner();
                // request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                // request.setShowRunningNotification(false);
                // request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
                request.setMimeType("application/cn.trinea.download.file");
             
                updateView();
            }
        });
        downloadCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                downloadManager.remove(downloadId);
                updateView();
            }
        });
    }
	
	private void getfavorlist(int pagecount, int perpage, int typeid, int type) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("libid", GlobleData.LIBIRY_ID);
		Log.i("MyFavorActivity_cqvipid", "" + GlobleData.cqvipid);
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
		mQueue.add(mys);
		mQueue.start();

	}

	Listener<String> backlistener_sz = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			try {
				arrayLists_sz = Favorite.formList(Task.TASK_COMMENT_BOOKLIST,
						response);
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
			customProgressDialog.dismiss();
			try {
				arrayLists_zk = Favorite.formList(Task.TASK_COMMENT_BOOKLIST,
						response);
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
				arrayLists_sz = Favorite.formList(Task.TASK_COMMENT_BOOKLIST,
						response);
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

	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			arg0.printStackTrace();
			onError(2);
		}
	};
	
    class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver(){
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            updateView();
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

			switch (position) {
			case 0:
				return "下载中".toUpperCase(l);
			case 1:
				return "已下载".toUpperCase(l);
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
			Log.i("item", "===============click=");
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
		TextView download_size;// 
		TextView download_precent;// 
		ProgressBar download_progress;// 
		Button download_cancel;//
	}

	class MyGridViewAdapter extends CursorAdapter {
		private Context myContext;
		private LayoutInflater mInflater;

		public MyGridViewAdapter(Context context, List<Favorite> list,
				Cursor cursor) {
			super(context, cursor);
			this.myContext = context;
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public View newView(Context context, Cursor cursor, ViewGroup parent) {
	        final View view =  mInflater.inflate(R.layout.item_downloadmanager, parent, false);  
	        ViewHolder holder = new ViewHolder();  
	        holder.download_size = (TextView) view.findViewById(R.id.download_size);  
	        holder.download_precent = (TextView) view.findViewById(R.id.download_precent);  
	        holder.download_progress =(ProgressBar) view.findViewById(R.id.download_progress);  
	        holder.download_cancel =  (Button) view.findViewById(R.id.download_cancel);  
	        view.setTag(holder);  
	        return view;  
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {
	        ViewHolder holder = (ViewHolder) view.getTag();  
	        holder.download_size.setText("");
		}
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

		public List<Favorite> getLists() {
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

		        ViewHolder holder = new ViewHolder();  
		        holder.download_size = (TextView) view.findViewById(R.id.download_size);  
		        holder.download_precent = (TextView) view.findViewById(R.id.download_precent);  
		        holder.download_progress =(ProgressBar) view.findViewById(R.id.download_progress);  
		        holder.download_cancel =  (Button) view.findViewById(R.id.download_cancel);  
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
			holder.commentcount.setText(commentcount + "("
					+ favorite.getCommentcount() + ")");
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

	public static boolean isDownloading(int downloadManagerStatus) {
		return downloadManagerStatus == DownloadManager.STATUS_RUNNING
				|| downloadManagerStatus == DownloadManager.STATUS_PAUSED
				|| downloadManagerStatus == DownloadManager.STATUS_PENDING;
	}
	
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
}