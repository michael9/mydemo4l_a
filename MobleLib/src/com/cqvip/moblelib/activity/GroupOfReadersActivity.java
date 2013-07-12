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
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.R;

import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.Favorite;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

/**
 * <p>
 * �ļ�����: GroupOfReadersActivity.java �ļ�����: ����Ȧ ��Ȩ����: ��Ȩ����(C)2013-2020 �� ˾:
 * ����ά����ѯ���޹�˾ ����ժҪ: ����˵��: ������ڣ� 201��5��10�� �޸ļ�¼:
 * </p>
 * 
 * @author LHP,LJ
 */
public class GroupOfReadersActivity extends BaseFragmentImageActivity implements
		IBookManagerActivity {
	public static final int COMMENTLIST = 1;

	private int curpage = 1;// �ڼ�ҳ
	private int perpage = 10;// ÿҳ��ʾ����
	private View moreprocess;
	private int type=GlobleData.BOOK_SZ_TYPE;
	private int curpage_sz = 1, curpage_zk=1;
	
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
	ArrayList<Favorite> arrayList_zk = new ArrayList<Favorite>();
	ArrayList<Favorite> arrayList_sz = new ArrayList<Favorite>();

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
		// ��ȡ����
		ManagerService.allActivity.add(this);
		getfavorlist(curpage,perpage,GlobleData.BOOK_SZ_TYPE);
		getfavorlist(curpage,perpage,GlobleData.BOOK_ZK_TYPE);
	}

	private void getfavorlist(int pagecount,int perpage,int typeid) {
		HashMap map = new HashMap();
		map.put("libid", GlobleData.LIBIRY_ID);
		Log.i("MyFavorActivity_cqvipid", "" + GlobleData.cqvipid);
		map.put("vipuserid", GlobleData.cqvipid);
		map.put("curpage", ""+pagecount);
		map.put("perpage", ""+perpage);
		map.put("typeid", ""+typeid);
		ManagerService.addNewTask(new Task(Task.TASK_COMMENT_BOOKLIST, map));
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
		 * ��д�˷���Ϊ��ʹnotifyDataSetChanged��Ч Called when the host view is
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
				adapter_sz = new MyGridViewAdapter(getActivity(), arrayList_sz,mImageFetcher);
				listView.setAdapter(adapter_sz);
				listView.setTag(GlobleData.BOOK_SZ_TYPE);
			} else if (i == 1) {
				listView.setTag(GlobleData.BOOK_ZK_TYPE);
				arrayList_temp = arrayList_zk;
				adapter_zk = new MyGridViewAdapter(getActivity(), arrayList_zk,mImageFetcher);
				listView.setAdapter(adapter_zk);
			}

			listView.setOnItemClickListener(this);
			return rootView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int positon,
				long id) {
			Log.i("item", "===============click=");
			if (id == -2){ // ����
				if(parent.getAdapter().getCount()==1){
					return;
				}
				//������
				moreprocess = view.findViewById(R.id.footer_progress);
				moreprocess.setVisibility(View.VISIBLE);
				Log.i("parent.getTag()",""+parent.getTag());
				//�����������		
				if((Integer)parent.getTag()==GlobleData.BOOK_SZ_TYPE){
				curpage_sz++;
				getfavorlist(curpage_sz,perpage,GlobleData.BOOK_SZ_TYPE);
				}else if((Integer)parent.getTag()==GlobleData.BOOK_ZK_TYPE){
				curpage_zk++;
				getfavorlist(curpage_zk,perpage,GlobleData.BOOK_ZK_TYPE);
				}
			} else {
				Favorite favorite = null;
				int typeflag = 5;
				if((Integer)parent.getTag()==GlobleData.BOOK_SZ_TYPE){
					favorite = adapter_sz.getLists().get(positon);
					typeflag = 5;
					}else if((Integer)parent.getTag()==GlobleData.BOOK_ZK_TYPE){
					favorite = adapter_zk.getLists().get(positon);
					typeflag = 4;
					}
				Book book = new Book(favorite.getLngid(), favorite.getOrgan(),
						favorite.getTitle(), favorite.getWriter(),
						favorite.getLngid(), favorite.getYears(),
						favorite.getPrice(), favorite.getRemark(),"");
				Tool.getCommentList(context, book,typeflag);

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

//		@Override
//		public boolean onItemLongClick(AdapterView<?> parent, View view,
//				int position, long id) {
//			Favorite favorite = arrayList_temp.get(position);
//			if (favorite != null) {
//				HashMap<String, String> map = new HashMap<String, String>();
//				map.put("libid", GlobleData.LIBIRY_ID);
//				map.put("vipuserid", GlobleData.cqvipid);
//				Log.i("ɾ���ղ�", GlobleData.cqvipid);
//				map.put("keyid", favorite.getFavoritekeyid());
//				Log.i("keyid", favorite.getFavoritekeyid());
//				map.put("typeid", "" + favorite.getTypeid());
//				ManagerService
//						.addNewTask(new Task(Task.TASK_CANCEL_FAVOR, map));
//				customProgressDialog.show();
//			}
//			return false;
//		}
	}
	
	private int listview_id=GlobleData.BOOK_SZ_TYPE;
	private int listview_item_position=0;
	static class ViewHolder {
		TextView title;// ����
		TextView author;// ����
		TextView publisher;// ������
		TextView publishyear;// ����ʱ��
		TextView commentcount;// ������
		// TextView abst_tv;// ���
		ImageView img;// ʱ��ͼƬ �����޸�
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
		public MyGridViewAdapter(Context context, List<Favorite> list,ImageFetcher fetch) {
			this.myContext = context;
			this.arrayList = list;
			this.fetch = fetch;
			Log.i("MyFavorActivity", "MyGridViewAdapter");
		}

		public List<Favorite> getLists(){
			return this.arrayList;
		}
		
		@Override
		public int getCount() {
			Log.i("getCount", "getCount");
			if (arrayList!=null&&!arrayList.isEmpty()) {
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
			if(this.getCount() == 1){
				convertView = LayoutInflater.from(myContext).inflate(
						R.layout.moreitemsview, null);
				convertView.setClickable(false);
				TextView tv=(TextView)convertView.findViewById(R.id.footer_txt);
				tv.setText("�ף������ڷ���û������Ŷ");
				tv.setTextColor(context.getResources().getColor(R.drawable.silvergray));
				tv.setTextSize(context.getResources().getDimension(R.dimen.search_detail_txtsize_tv));
				tv.setClickable(false);
				return convertView;
			}
			// ����
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
				convertView.findViewById(R.id.txt_abst)
						.setVisibility(View.GONE);
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
			holder.publisher.setText(publish + favorite.getOrgan());
			holder.publishyear.setText(time + favorite.getYears());
			holder.commentcount.setText(commentcount + "("
					+ favorite.getCommentcount() + ")");
			//ͼƬ
			if(!TextUtils.isEmpty(favorite.getImgurl())){
			fetch.loadImage(favorite.getImgurl(),holder.img);
			}else{
			holder.img.setBackgroundResource(R.drawable.defaut_book);
			}
			
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
		if (temp == COMMENTLIST) {
			arrayLists = (Map<Integer, List<Favorite>>) obj[1];
			if (arrayLists != null && !arrayLists.isEmpty()) {
//				if ((curpage_sz>1||curpage_zk>1)&&(arrayLists.get(GlobleData.BOOK_ZK_TYPE)==null||arrayLists.get(GlobleData.BOOK_ZK_TYPE).isEmpty())&&
//						(arrayLists.get(GlobleData.BOOK_SZ_TYPE)==null||arrayLists.get(GlobleData.BOOK_SZ_TYPE).isEmpty())) {
//					Tool.ShowMessages(context, "û�и������ݿɹ�����");
//					moreprocess.setVisibility(View.GONE);
//					return;
//				}
				ArrayList<Favorite> temp_sz_list=(ArrayList<Favorite>) arrayLists.get(GlobleData.BOOK_SZ_TYPE);
				ArrayList<Favorite> temp_zk_list=(ArrayList<Favorite>) arrayLists.get(GlobleData.BOOK_ZK_TYPE);
				if(temp_sz_list!=null){
					arrayList_sz.addAll(arrayLists.get(GlobleData.BOOK_SZ_TYPE));
					//mSectionsPagerAdapter.notifyDataSetChanged();
					adapter_sz.notifyDataSetChanged();
					}else if(temp_zk_list!=null){
				arrayList_zk.addAll(arrayLists.get(GlobleData.BOOK_ZK_TYPE));
				adapter_zk.notifyDataSetChanged();
				}
			}else{
				Tool.ShowMessages(context, "û�и������ݿɹ�����");
				moreprocess.setVisibility(View.GONE);
			}
		} 
	}

	public void onError(int a) {
		if (customProgressDialog != null && customProgressDialog.isShowing()) {
			customProgressDialog.dismiss();
		}
		if (a == 2) {// ����ʧ��
			Tool.ShowMessages(this, getResources().getString(R.string.loadfail));
		} else if (a == 6) {
			Tool.ShowMessages(this,
					getResources().getString(R.string.cancelcommentfail));
		}
	}
}
