package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.List;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookAdapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * <p>
 * �ļ�����: BorrowAndOrderActivity.java
 * �ļ�����: ���Ĺ���
 * ��Ȩ����: ��Ȩ����(C)2013-2020
 * ��          ˾: ����ά����ѯ���޹�˾
 * ����ժҪ: 
 * ����˵��:
 * ������ڣ� 201��5��10��
 * �޸ļ�¼: 
 * </p>
 * 
 * @author LHP,LJ
 */
public class BorrowAndOrderActivity2 extends BaseActivity {

	
	private Context context;
	private ViewPager mPager;//ҳ������
	private List<View> listViews; // Tabҳ���б�
	private ImageView cursor;// ����ͼƬ
	private TextView t1, t2;// ҳ��ͷ��
	private int currIndex = 0;// ��ǰҳ�����
	private int offset = 0;// ����ͼƬƫ����
	private int bmpW;// ����ͼƬ���

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_borrow_and_order);
		context = this;
		View v = findViewById(R.id.borrow_title);
		TextView title = (TextView)v.findViewById(R.id.txt_header);
		title.setText(R.string.main_borrow);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		ImageView history = (ImageView)v.findViewById(R.id.btn_right_header);
		history.setVisibility(View.VISIBLE);
		history.setImageResource(R.drawable.lscx);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		history.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		InitImageView();
		InitTextView();
		InitViewPager();
	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.eg_cursor)
				.getWidth();// ��ȡͼƬ���
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
		offset = (screenW / 3 - bmpW) / 2;// ����ƫ����
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// ���ö�����ʼλ��
		
	}

	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.borrow_vPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		View v1 = mInflater.inflate(R.layout.borrow_borrow, null);
		View v2 = mInflater.inflate(R.layout.borrow_order, null);
		listViews.add(v1);
		listViews.add(v2);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
       	ListView lv1 = (ListView)v1.findViewById(R.id.borrow_list);	
    	ListView lv2 = (ListView)v2.findViewById(R.id.order_list);
		
    	lv1.setAdapter(new BookAdapter(context));
    	lv2.setAdapter(new BookAdapter(context));
	}

	private void InitTextView() {
		t1 = (TextView)findViewById(R.id.borrow_title_txt);
		t2 = (TextView)findViewById(R.id.order_title_txt);
		
		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		
	}
	/**
	 * ͷ��������
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem(index);
		}
	};
	/**
	 * ViewPager������
	 */
	public class MyPagerAdapter extends PagerAdapter {
		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	

	/**
	 * ҳ���л�����
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// ҳ��1 -> ҳ��2 ƫ����
		//int two = one * 2;// ҳ��1 -> ҳ��3 ƫ����

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {
			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} 
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} 
				break;
			}
			currIndex = arg0;
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(300);
			cursor.startAnimation(animation);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}


	class MyGridAdapter extends BaseAdapter{
		
		private Context mcontext;
		private String[] lists;
		public MyGridAdapter(){
			
		}
		public MyGridAdapter(Context context, String[] medic) {
			mcontext = context;
			lists = medic;
		}

		@Override
		public int getCount() {
			
			return lists.length;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = LayoutInflater.from(mcontext).inflate(R.layout.item_searchtxt, null);
				TextView tx = (TextView)convertView.findViewById(R.id.search_item_txt);
				
				tx.setText(lists[position]);
				
			}
			return convertView;
		}
		
	}
}
