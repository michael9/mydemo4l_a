package com.cqvip.moblelib.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.android.volley.RequestQueue;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.fragment.PeriodicalTypeFragment;
import com.cqvip.moblelib.fragment.SpecialPeriodicalFragment;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.moblelib.view.SwipHorizontalScrollView;

/**
 * 期刊大全分类界面，可滑动listview,提供分类添加功能
 * @author luojiang
 *
 */
public class PeriodicalClassfyActivity extends BaseFragmentImageActivity {

	private RelativeLayout rl_nav;
	private SwipHorizontalScrollView mHsv;
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator;
	private ImageView iv_nav_left;
	private ImageView iv_nav_right;
	private ViewPager mViewPager;
	private int indicatorWidth;
	public static String[] tabTitle = { "特别推荐","医药卫生","工程技术","人文社科","自然科学","农林牧鱼"};	//标题
	private LayoutInflater mInflater;
	private TabFragmentPagerAdapter mAdapter;
	private int currentIndicatorLeft = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_periodical_classfy);
		
		fvByid();
		initview();
		setListener();
		
		
	}

	private void setListener() {
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				//RadioButton点击  performClick()
				if(rg_nav_content!=null && rg_nav_content.getChildCount()>position){
					((RadioButton)rg_nav_content.getChildAt(position)).performClick();
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
		
		rg_nav_content.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(rg_nav_content.getChildAt(checkedId)!=null){
					//滑动动画
					TranslateAnimation animation = new TranslateAnimation(
							currentIndicatorLeft ,
							((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft(), 0f, 0f);
					animation.setInterpolator(new LinearInterpolator());
					animation.setDuration(100);
					animation.setFillAfter(true);
					
					//滑块执行位移动画
					iv_nav_indicator.startAnimation(animation);
					
					mViewPager.setCurrentItem(checkedId);	//ViewPager 跟随一起 切换
					
					//记录当前 下标的距最左侧的 距离
					currentIndicatorLeft = ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft();
					Log.i("PeriodicalClassfyActivity", ""+((checkedId > 1 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(2)).getLeft()));
					mHsv.smoothScrollTo(
							(checkedId > 1 ? ((RadioButton) rg_nav_content.getChildAt(checkedId)).getLeft() : 0) - ((RadioButton) rg_nav_content.getChildAt(2)).getLeft(), 0);
				}
			}
		});
		
	}

	private void initview() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		indicatorWidth = dm.widthPixels / 4;
		//TODO step0 初始化滑动下标的宽    根据屏幕宽度和可见数量 来设置RadioButton的宽度)
		LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		iv_nav_indicator.setLayoutParams(cursor_Params);
		
		mHsv.setSomeParam(rl_nav, iv_nav_left, iv_nav_right, this);
		
		//获取布局填充器
		mInflater = (LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);

		//另一种方式获取
//		LayoutInflater mInflater = LayoutInflater.from(this);  
		
		initNavigationHSV();
		
		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
		mViewPager.setOffscreenPageLimit(5);
	}

	private void initNavigationHSV() {
			rg_nav_content.removeAllViews();
		for(int i=0;i<tabTitle.length;i++){
			RadioButton rb = (RadioButton) mInflater.inflate(R.layout.nav_radiogroup_item, null);
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

	private void fvByid() {
	rl_nav = (RelativeLayout) findViewById(R.id.rl_nav);
		
		mHsv = (SwipHorizontalScrollView) findViewById(R.id.mHsv);
		//内容
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		
		iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		iv_nav_left = (ImageView) findViewById(R.id.iv_nav_left);
		iv_nav_right = (ImageView) findViewById(R.id.iv_nav_right);
		
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);
		
		findViewById(R.id.readerserve_title).setVisibility(View.GONE);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.periodical_classfy, menu);
		return true;
	}
	
	  /**
     * Called by the ViewPager child fragments to load images via the one ImageFetcher
     */
    public ImageFetcher getImageFetcher() {
        return mImageFetcher;
    }
    
    public RequestQueue getRequestQueue(){
    	return mQueue;
    }

    public CustomProgressDialog getCustomDialog(){
    	return customProgressDialog;
    }
	public static class TabFragmentPagerAdapter extends FragmentPagerAdapter{

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment ft = null;
			switch (arg0) {
			case 0:
				ft =SpecialPeriodicalFragment.instance(arg0);
				break;
			default:
				ft = PeriodicalTypeFragment.instance(arg0);
				break;
			}
			return ft;
		}

		@Override
		public int getCount() {
			
			return tabTitle.length;
		}
		
	}
}