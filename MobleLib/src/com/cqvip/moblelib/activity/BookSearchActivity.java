package com.cqvip.moblelib.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.view.StableGridView;
import com.cqvip.utils.Tool;

/**
 * <p>
 * 文件名称: BookSearchActivity.java
 * 文件描述: 馆藏查询
 * 版权所有: 版权所有(C)2013-2020
 * 公          司: 重庆维普咨询有限公司
 * 内容摘要: 包含条件搜索，分类查询
 * 其他说明:
 * 完成日期： 201年5月10日
 * 修改记录: 
 * </p>
 * 
 * @author LHP,LJ
 */
public class BookSearchActivity extends BaseActivity {
	
	private Context context;
	private ViewPager mPager;//页卡内容
	private List<View> listViews; // Tab页面列表
	private ImageView cursor;// 动画图片
	private TextView t1, t2;// 页卡头标
	private int currIndex = 0;// 当前页卡编号
	private int offset = 0;// 动画图片偏移量
	private int bmpW;// 动画图片宽度
	private static final String[] MEDIC= {
		"基础医学","基础医学","医学技巧","医学技巧","基础医学","基础医学","医学技巧","医学技巧"
	};
	private static final String[] TECHNOLOGY= {
		"自然科学","自然科学","医学技巧","医学技巧","基础医学","基础医学","医学技巧","医学技巧"
	};
	 private static final String[] TYPE = new String[] {
	        "中文", "西文", "征订"
	    };
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_book_search1);
		context = this;
		View v = findViewById(R.id.seach_title);
		TextView title = (TextView)v.findViewById(R.id.txt_header);
		title.setText(R.string.main_search);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
//		InitImageView();
//		InitTextView();
//		InitViewPager();
//		
		 final EditText et = (EditText)findViewById(R.id.search_et);
		    et.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
					Intent intent = new Intent(context,ResultOnSearchActivity.class) ;
					startActivity(intent);
				}
			});
		
		
	}

	private void InitImageView() {
		cursor = (ImageView) findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.eg_cursor)
				.getWidth();// 获取图片宽度
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
		
	}

	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.search_vPager);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		View v1 = mInflater.inflate(R.layout.search_condition, null);
		View v2 = mInflater.inflate(R.layout.search_category, null);
		listViews.add(v1);
		listViews.add(v2);
		mPager.setAdapter(new MyPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());
		
//		Button btnsearch = (Button)v1.findViewById(R.id.search_seach_btn);
//		btnsearch.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//			Toast.makeText(context, "目止", Toast.LENGTH_SHORT).show();
//			}
//		});
		
//		    Spinner s1 = (Spinner) v1.findViewById(R.id.spinner1);
//	        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
//	                this, R.array.searchtype, android.R.layout.simple_spinner_item);
//	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//	        s1.setAdapter(adapter);
//	        s1.setOnItemSelectedListener(
//	                new OnItemSelectedListener() {
//	                    public void onItemSelected(
//	                            AdapterView<?> parent, View view, int position, long id) {
//	                      //  showToast("Spinner1: position=" + position + " id=" + id);
//	                    }
//
//	                    public void onNothingSelected(AdapterView<?> parent) {
//	                    //    showToast("Spinner1: unselected");
//	                    }
//	                });
		ListView list = (ListView)v1.findViewById(R.id.choice_check_list); 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,  android.R.layout.simple_list_item_multiple_choice,TYPE);
		list.setAdapter(adapter);
		list.setItemsCanFocus(false);
		list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		//搜索
	    final EditText et = (EditText)v1.findViewById(R.id.search_et);
	    et.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
				Intent intent = new Intent(context,ResultOnSearchActivity.class) ;
				startActivity(intent);
			}
		});

		
		
		
		
		
		StableGridView g1 = (StableGridView)v2.findViewById(R.id.gridview_medic);
		StableGridView g2 = (StableGridView)v2.findViewById(R.id.gridview_technology);
		StableGridView g3 = (StableGridView)v2.findViewById(R.id.gridview3);
		StableGridView g4 = (StableGridView)v2.findViewById(R.id.gridview4);
		StableGridView g5 = (StableGridView)v2.findViewById(R.id.gridview5);
		
		TextView txmedic = (TextView)v2.findViewById(R.id.search_medic_title);
		txmedic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(context,"哈哈哈哈哈", Toast.LENGTH_SHORT).show();
				
			}
		});
		g1.setAdapter(new MyGridAdapter(context,MEDIC));
		g2.setAdapter(new MyGridAdapter(context,TECHNOLOGY));
		g3.setAdapter(new MyGridAdapter(context,MEDIC));
		g4.setAdapter(new MyGridAdapter(context,TECHNOLOGY));
		g5.setAdapter(new MyGridAdapter(context,MEDIC));
		
		g2.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Log.i("onItemclick","g1click");
			Toast.makeText(getApplicationContext(),"GridView", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		g1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				//	Intent intent = new Intent() ;
				Log.i("onItemclick","g2click");
			//Toast.makeText(getApplicationContext(), MEDIC[position], Toast.LENGTH_SHORT).show();
				switch (position) {
    			//入馆指南
    			case 0:
    				startActivity(new Intent(context, ResultForSearchActivity.class));
    				break;
    				//馆藏查询
    			case 1:
    				startActivity(new Intent(context, ResultForSearchActivity.class));
    				break;
    			//读者服务
    			case 2:
    				startActivity(new Intent(context, ResultForSearchActivity.class));
    				break;
    			//阅读指引
    			case 3:
    				startActivity(new Intent(context, ResultForSearchActivity.class));
    				break;
    			//我的书圈
    			case 4:
    				startActivity(new Intent(context, ResultForSearchActivity.class));	
    				break;
    			//电子书架
    			case 5:
    			
    				startActivity(new Intent(context, ResultForSearchActivity.class));
    				break;
    				//馆内公告
    			case 6:
    				startActivity(new Intent(context, ResultForSearchActivity.class));
    				
    				break;
    				//图书借阅
    			case 7:
    			
    				startActivity(new Intent(context, ResultForSearchActivity.class));
    				break;
    				//图书预约
    			case 8:
    				startActivity(new Intent(context, ResultForSearchActivity.class));
    				
    				break;
    			//其它
    			default:
    				break;
    			}
			}
	
		});
	}
	/**
	 * 初始化textview
	 */
	private void InitTextView() {
		t1 = (TextView)findViewById(R.id.search_choice_title);
		t2 = (TextView)findViewById(R.id.search_category_title);
		
		t1.setOnClickListener(new MyOnClickListener(0));
		t2.setOnClickListener(new MyOnClickListener(1));
		
	}
	
	/**
	 * 头标点击监听
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
	 * ViewPager适配器
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
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		//int two = one * 2;// 页卡1 -> 页卡3 偏移量

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
			animation.setFillAfter(true);// True:图片停在动画结束位置
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
//				tx.setOnClickListener(new View.OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						
////						Toast.makeText(mcontext, lists[position], Toast.LENGTH_SHORT).show();
//					}
//				});
				
			}
			return convertView;
		}
		
	}



	
}
