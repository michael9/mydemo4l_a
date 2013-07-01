package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

public class MyFavorActivity extends FragmentActivity implements
		IBookManagerActivity {

	private String curpage = "1";// 第几页
	private String perpage = "20";// 每页显示条数
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
	ArrayList<ArrayList<Object>> arrayLists = new ArrayList<ArrayList<Object>>();
	ArrayList<Book> alArrayList;

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
//		ManagerService.allActivity.add(this);
//		HashMap map = new HashMap();
//		map.put("libid", GlobleData.LIBIRY_ID);
//		map.put("vipuserid", GlobleData.cqvipid);
//		map.put("curpage", curpage);
//		map.put("perpage", perpage);
//		ManagerService.addNewTask(new Task(Task.TASK_GET_FAVOR, map));
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
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
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
	ListView listView;
	MyGridViewAdapter adapter;

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

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			Log.i("MyFavorActivity", "onCreateView");
			View rootView = inflater.inflate(R.layout.myfavor_fragment,
					container, false);
			// TextView dummyTextView = (TextView) rootView
			// .findViewById(R.id.section_label);
			// dummyTextView.setText(Integer.toString(getArguments().getInt(
			// ARG_SECTION_NUMBER)));
			listView = (ListView) rootView.findViewById(R.id.favorlist);
			listView.setOnItemClickListener((OnItemClickListener) this);
			adapter = new MyGridViewAdapter(context, getArguments().getInt(
					ARG_SECTION_NUMBER));
			// listView.setAdapter(adapter);
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
	}

	static class ViewHolder {

		TextView title;// 书名
		TextView author;// 作者
		TextView publisher;// 出版社
		TextView publishyear;// 出版时间
		ImageView img;// 时间图片 不用修改
		// TextView u_page;//页数
		// TextView u_abstract;//简介
		TextView isbn;

		Button btn_comment, btn_item_result_search_share, favorite;

	}

	class MyGridViewAdapter extends BaseAdapter {
		private Context myContext;

		public MyGridViewAdapter(Context context, int a) {
			this.myContext = context;
			Log.i("MyFavorActivity", "MyGridViewAdapter");
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 14;
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
				convertView = LayoutInflater.from(context).inflate(
						R.layout.moreitemsview, null);
				return convertView;
			}
			if (convertView == null
					|| convertView.findViewById(R.id.linemore) != null) {
				convertView = LayoutInflater.from(context).inflate(
						R.layout.myfavor_fragmen_item, null);
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
				// holder.u_abstract = (TextView)
				// convertView.findViewById(R.id.re_hot_txt);
				holder.isbn = (TextView) convertView
						.findViewById(R.id.re_hot_txt);
				holder.btn_item_result_search_share = (Button) convertView
						.findViewById(R.id.btn_item_result_search_share);
				holder.favorite = (Button) convertView
						.findViewById(R.id.btn_item_result_search_collect);
				holder.btn_comment = (Button) convertView
						.findViewById(R.id.btn_comment);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			String author = context.getResources().getString(
					R.string.item_author);
			String publish = context.getResources().getString(
					R.string.item_publish);
			String time = context.getResources().getString(R.string.item_time);
			String describe = context.getResources().getString(
					R.string.item_describe);
			Book book = alArrayList.get(position);
			holder.title.setText(book.getTitle());
			holder.author.setText(author + book.getAuthor());
			holder.publisher.setText(publish + book.getPublisher());
			holder.publishyear.setText(time + book.getPublishyear());
			holder.isbn.setText("ISBN:" + book.getIsbn());

			// 分享
			holder.btn_item_result_search_share.setTag(position);
			holder.btn_item_result_search_share
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							int pos = (Integer) v.getTag();
							Intent intent = new Intent(Intent.ACTION_SEND);
							intent.setType("image/*");
							intent.putExtra(Intent.EXTRA_SUBJECT, "图书分享");
							intent.putExtra(
									Intent.EXTRA_TEXT,
									("好书分享:"
											+ (alArrayList.get(pos)).getTitle()
											+ "\r\n作者:"
											+ (alArrayList.get(pos))
													.getAuthor()
											+ "\r\n出版社:"
											+ (alArrayList.get(pos))
													.getPublisher()
											+ "\r\n出版日期:"
											+ (alArrayList.get(pos))
													.getPublishyear()
											+ "\r\nISBN:" + (alArrayList
											.get(pos)).getIsbn()));
							intent.putExtra(
									Intent.EXTRA_STREAM,
									Uri.decode("http://www.szlglib.com.cn/images/logo.jpg")); // 分享图片"http://www.szlglib.com.cn/images/logo.jpg"
							context.startActivity(Intent.createChooser(intent,
									"分享到"));

						}
					});
			// 评论
			holder.btn_comment.setTag(position);
			holder.btn_comment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int pos = (Integer) v.getTag();
					Book book = alArrayList.get(pos);
					if (book != null) {
						Intent intent = new Intent(context,
								CommentActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("book", book);
						intent.putExtra("detaiinfo", bundle);
						context.startActivity(intent);
					}
				}
			});

			return convertView;
		}
	}

	@Override
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

	@Override
	public void refresh(Object... obj) {
		customProgressDialog.dismiss();
		alArrayList = (ArrayList<Book>) obj[0];
		if (alArrayList != null && !alArrayList.isEmpty()) {
			listView.setAdapter(adapter);
		}
	}

	public void onError(int a) {
		if (customProgressDialog != null && customProgressDialog.isShowing()) {
			customProgressDialog.dismiss();
		}
		if (a == 2) {// 加载失败
			Tool.ShowMessages(this, getResources().getString(R.string.loadfail));
		}
	}
}
