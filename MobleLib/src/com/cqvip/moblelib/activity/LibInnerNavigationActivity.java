package com.cqvip.moblelib.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqvip.moblelib.szy.R;
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
	private TextView tv_floor1, tv_floor2, tv_floor3,tv_floor4, tv_floor5, tv_floor6;
	private TextView title;
	private ImageView back;
	private int[] mapArrays = { R.drawable.l01, R.drawable.l02,
			R.drawable.l03,R.drawable.l04,R.drawable.l05,R.drawable.l06 };

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
		tv_floor1.setBackgroundResource(R.drawable.GreenYellow);
	}

	private void initialView() {
		title = (TextView) findViewById(R.id.txt_header);
		title.setText(R.string.guide_navigation);
		back = (ImageView) findViewById(R.id.img_back_header);
		tv_floor1 = (TextView) findViewById(R.id.tv_floor_1);
		tv_floor2 = (TextView) findViewById(R.id.tv_floor_2);
		tv_floor3 = (TextView) findViewById(R.id.tv_floor_3);
		tv_floor4 = (TextView) findViewById(R.id.tv_floor_4);
		tv_floor5 = (TextView) findViewById(R.id.tv_floor_5);
		tv_floor6 = (TextView) findViewById(R.id.tv_floor_6);
	}

	private void setOnclickListener() {
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		tv_floor1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_floor1.setBackgroundResource(R.drawable.GreenYellow);
				pinchableImageView
						.setLayoutParams(new FrameLayout.LayoutParams(
								SCREEN_WIDTH, pinchableImageView
										.getViewHeight()));
				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), mapArrays[0]));
				tv_floor2.setBackgroundResource(R.drawable.silvergray);
				tv_floor3.setBackgroundResource(R.drawable.silvergray);
				tv_floor4.setBackgroundResource(R.drawable.silvergray);
				tv_floor5.setBackgroundResource(R.drawable.silvergray);
				tv_floor6.setBackgroundResource(R.drawable.silvergray);
			}
		});
		tv_floor2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_floor2.setBackgroundResource(R.drawable.GreenYellow);
				pinchableImageView
						.setLayoutParams(new FrameLayout.LayoutParams(
								SCREEN_WIDTH, pinchableImageView
										.getViewHeight()));
				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), mapArrays[1]));
				tv_floor1.setBackgroundResource(R.drawable.silvergray);
				tv_floor3.setBackgroundResource(R.drawable.silvergray);
				tv_floor4.setBackgroundResource(R.drawable.silvergray);
				tv_floor5.setBackgroundResource(R.drawable.silvergray);
				tv_floor6.setBackgroundResource(R.drawable.silvergray);
			}
		});
		tv_floor3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_floor3.setBackgroundResource(R.drawable.GreenYellow);
				pinchableImageView
						.setLayoutParams(new FrameLayout.LayoutParams(
								SCREEN_WIDTH, pinchableImageView
										.getViewHeight()));
				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), mapArrays[2]));
				tv_floor2.setBackgroundResource(R.drawable.silvergray);
				tv_floor1.setBackgroundResource(R.drawable.silvergray);
				tv_floor4.setBackgroundResource(R.drawable.silvergray);
				tv_floor5.setBackgroundResource(R.drawable.silvergray);
				tv_floor6.setBackgroundResource(R.drawable.silvergray);
			}
		});
		tv_floor4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_floor4.setBackgroundResource(R.drawable.GreenYellow);
				pinchableImageView
						.setLayoutParams(new FrameLayout.LayoutParams(
								SCREEN_WIDTH, pinchableImageView
										.getViewHeight()));
				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), mapArrays[3]));
				tv_floor1.setBackgroundResource(R.drawable.silvergray);
				tv_floor2.setBackgroundResource(R.drawable.silvergray);
				tv_floor3.setBackgroundResource(R.drawable.silvergray);
				tv_floor5.setBackgroundResource(R.drawable.silvergray);
				tv_floor6.setBackgroundResource(R.drawable.silvergray);
			}
		});
		
		tv_floor5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_floor5.setBackgroundResource(R.drawable.GreenYellow);
				pinchableImageView
						.setLayoutParams(new FrameLayout.LayoutParams(
								SCREEN_WIDTH, pinchableImageView
										.getViewHeight()));
				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), mapArrays[4]));
				tv_floor1.setBackgroundResource(R.drawable.silvergray);
				tv_floor2.setBackgroundResource(R.drawable.silvergray);
				tv_floor3.setBackgroundResource(R.drawable.silvergray);
				tv_floor4.setBackgroundResource(R.drawable.silvergray);
				tv_floor6.setBackgroundResource(R.drawable.silvergray);
			}
		});
		
		tv_floor6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_floor6.setBackgroundResource(R.drawable.GreenYellow);
				pinchableImageView
						.setLayoutParams(new FrameLayout.LayoutParams(
								SCREEN_WIDTH, pinchableImageView
										.getViewHeight()));
				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), mapArrays[5]));
				tv_floor1.setBackgroundResource(R.drawable.silvergray);
				tv_floor2.setBackgroundResource(R.drawable.silvergray);
				tv_floor3.setBackgroundResource(R.drawable.silvergray);
				tv_floor4.setBackgroundResource(R.drawable.silvergray);
				tv_floor5.setBackgroundResource(R.drawable.silvergray);
			}
		});
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(pinchableImageView!=null)
		pinchableImageView.recycle();
	}
}
