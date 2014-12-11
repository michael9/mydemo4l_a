package com.cqvip.moblelib.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cqvip.moblelib.ahslsd.R;
import com.cqvip.moblelib.view.PinchableImageView;

/**
 * <p>
 * 文件名称: LibInnerNavigationActivity.java
 * 文件描述: 馆内导航
 * 版权所有: 版权所有(C)2013-2020
 * 公          司: 重庆维普咨询有限公司
 * 内容摘要: 
 * 其他说明:
 * 完成日期： 201年5月15日
 * 修改记录: 
 * </p>
 * 
 * @author LHP,LJ
 */

public class LibInnerNavigationActivity extends BaseActivity {
	public static int SCREEN_WIDTH, SCREEN_HEIGHT;
	private PinchableImageView pinchableImageView;
	//private TextView tv_floor1, tv_floor2, tv_floor3,tv_floor4, tv_floor5, tv_floor6;
	private TextView title;
	private ImageView back;
	private int[] mapArrays = { R.drawable.l01, R.drawable.l02,
			R.drawable.l03,R.drawable.l04,R.drawable.l05,R.drawable.l06 };
	private  int[] tabTitle={ R.string.floor_1,  R.string.floor_2, R.string.floor_3, R.string.floor_4, R.string.floor_5, R.string.floor_6, };; // 标题
	
	private int indicatorWidth;
	private int currentIndicatorLeft = 0;
	private RadioGroup rg_nav_content;
	private ImageView iv_nav_indicator;
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_lib_inner_navigation);
		pinchableImageView = (PinchableImageView) findViewById(R.id.mPinchableImageView);
		Display display = getWindowManager().getDefaultDisplay();
		SCREEN_WIDTH = display.getWidth();
		SCREEN_HEIGHT = display.getHeight();
		initialView();
		setOnclickListener();

		Bitmap bip = BitmapFactory.decodeResource(getResources(),
				mapArrays[0]);
		pinchableImageView.setWH(SCREEN_WIDTH, SCREEN_HEIGHT);
		pinchableImageView.setImageBitmap(bip);
		//tv_floor1.setBackgroundResource(R.drawable.GreenYellow);
	}

	private void initialView() {
		title = (TextView) findViewById(R.id.txt_header);
		title.setText(R.string.guide_navigation);
		back = (ImageView) findViewById(R.id.img_back_header);
//		tv_floor1 = (TextView) findViewById(R.id.tv_floor_1);
//		tv_floor2 = (TextView) findViewById(R.id.tv_floor_2);
//		tv_floor3 = (TextView) findViewById(R.id.tv_floor_3);
//		tv_floor4 = (TextView) findViewById(R.id.tv_floor_4);
//		tv_floor5 = (TextView) findViewById(R.id.tv_floor_5);
//		tv_floor6 = (TextView) findViewById(R.id.tv_floor_6);
		
		rg_nav_content = (RadioGroup) findViewById(R.id.rg_nav_content);
		iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		indicatorWidth = dm.widthPixels / tabTitle.length;
		// TODO step0 初始化滑动下标的宽 根据屏幕宽度和可见数量 来设置RadioButton的宽度)
		LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		iv_nav_indicator.setLayoutParams(cursor_Params);
		mInflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
		
		initNavigationHSV();
	}

	private void setOnclickListener() {
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
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
					pinchableImageView
					.setLayoutParams(new FrameLayout.LayoutParams(
							SCREEN_WIDTH, pinchableImageView
									.getViewHeight()));
			     pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), mapArrays[checkedId]));
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

//		tv_floor1.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_floor1.setBackgroundResource(R.drawable.GreenYellow);
//				pinchableImageView
//						.setLayoutParams(new FrameLayout.LayoutParams(
//								SCREEN_WIDTH, pinchableImageView
//										.getViewHeight()));
//				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
//						getResources(), mapArrays[0]));
//				tv_floor2.setBackgroundResource(R.drawable.silvergray);
//				tv_floor3.setBackgroundResource(R.drawable.silvergray);
//				tv_floor4.setBackgroundResource(R.drawable.silvergray);
//				tv_floor5.setBackgroundResource(R.drawable.silvergray);
//				tv_floor6.setBackgroundResource(R.drawable.silvergray);
//			}
//		});
//		tv_floor2.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_floor2.setBackgroundResource(R.drawable.GreenYellow);
//				pinchableImageView
//						.setLayoutParams(new FrameLayout.LayoutParams(
//								SCREEN_WIDTH, pinchableImageView
//										.getViewHeight()));
//				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
//						getResources(), mapArrays[1]));
//				tv_floor1.setBackgroundResource(R.drawable.silvergray);
//				tv_floor3.setBackgroundResource(R.drawable.silvergray);
//				tv_floor4.setBackgroundResource(R.drawable.silvergray);
//				tv_floor5.setBackgroundResource(R.drawable.silvergray);
//				tv_floor6.setBackgroundResource(R.drawable.silvergray);
//			}
//		});
//		tv_floor3.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_floor3.setBackgroundResource(R.drawable.GreenYellow);
//				pinchableImageView
//						.setLayoutParams(new FrameLayout.LayoutParams(
//								SCREEN_WIDTH, pinchableImageView
//										.getViewHeight()));
//				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
//						getResources(), mapArrays[2]));
//				tv_floor2.setBackgroundResource(R.drawable.silvergray);
//				tv_floor1.setBackgroundResource(R.drawable.silvergray);
//				tv_floor4.setBackgroundResource(R.drawable.silvergray);
//				tv_floor5.setBackgroundResource(R.drawable.silvergray);
//				tv_floor6.setBackgroundResource(R.drawable.silvergray);
//			}
//		});
//		tv_floor4.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_floor4.setBackgroundResource(R.drawable.GreenYellow);
//				pinchableImageView
//						.setLayoutParams(new FrameLayout.LayoutParams(
//								SCREEN_WIDTH, pinchableImageView
//										.getViewHeight()));
//				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
//						getResources(), mapArrays[3]));
//				tv_floor1.setBackgroundResource(R.drawable.silvergray);
//				tv_floor2.setBackgroundResource(R.drawable.silvergray);
//				tv_floor3.setBackgroundResource(R.drawable.silvergray);
//				tv_floor5.setBackgroundResource(R.drawable.silvergray);
//				tv_floor6.setBackgroundResource(R.drawable.silvergray);
//			}
//		});
//		
//		tv_floor5.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_floor5.setBackgroundResource(R.drawable.GreenYellow);
//				pinchableImageView
//						.setLayoutParams(new FrameLayout.LayoutParams(
//								SCREEN_WIDTH, pinchableImageView
//										.getViewHeight()));
//				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
//						getResources(), mapArrays[4]));
//				tv_floor1.setBackgroundResource(R.drawable.silvergray);
//				tv_floor2.setBackgroundResource(R.drawable.silvergray);
//				tv_floor3.setBackgroundResource(R.drawable.silvergray);
//				tv_floor4.setBackgroundResource(R.drawable.silvergray);
//				tv_floor6.setBackgroundResource(R.drawable.silvergray);
//			}
//		});
//		
//		tv_floor6.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				tv_floor6.setBackgroundResource(R.drawable.GreenYellow);
//				pinchableImageView
//						.setLayoutParams(new FrameLayout.LayoutParams(
//								SCREEN_WIDTH, pinchableImageView
//										.getViewHeight()));
//				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
//						getResources(), mapArrays[5]));
//				tv_floor1.setBackgroundResource(R.drawable.silvergray);
//				tv_floor2.setBackgroundResource(R.drawable.silvergray);
//				tv_floor3.setBackgroundResource(R.drawable.silvergray);
//				tv_floor4.setBackgroundResource(R.drawable.silvergray);
//				tv_floor5.setBackgroundResource(R.drawable.silvergray);
//			}
//		});
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

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(pinchableImageView!=null)
		pinchableImageView.recycle();
	}
}
