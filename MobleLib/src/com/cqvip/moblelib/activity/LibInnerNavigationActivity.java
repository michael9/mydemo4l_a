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

import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.view.PinchableImageView;

/**
 * <p>
 * �ļ����: LibInnerNavigationActivity.java
 * �ļ�����: ���ڵ���
 * ��Ȩ����: ��Ȩ����(C)2013-2020
 * ��          ˾: ����ά����ѯ���޹�˾
 * ����ժҪ: 
 * ����˵��:
 * ������ڣ� 201��5��15��
 * �޸ļ�¼: 
 * </p>
 * 
 * @author LHP,LJ
 */

public class LibInnerNavigationActivity extends BaseActivity {
	public static int SCREEN_WIDTH, SCREEN_HEIGHT;
	private PinchableImageView pinchableImageView;
	private TextView tv_floor1, tv_floor2, tv_floor3;
	private TextView title;
	private ImageView back;
	private int[] mapArrays = { R.drawable.l02, R.drawable.l04,
			R.drawable.l05 };

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
		tv_floor1.setBackgroundResource(R.color.blue_dark);
	}

	private void initialView() {
		title = (TextView) findViewById(R.id.txt_header);
		title.setText(R.string.guide_navigation);
		back = (ImageView)findViewById(R.id.return_iv);
		tv_floor1 = (TextView) findViewById(R.id.tv_floor_1);
		tv_floor2 = (TextView) findViewById(R.id.tv_floor_2);
		tv_floor3 = (TextView) findViewById(R.id.tv_floor_3);
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
				tv_floor1.setBackgroundResource(R.color.blue_dark);
				pinchableImageView
						.setLayoutParams(new FrameLayout.LayoutParams(
								SCREEN_WIDTH, pinchableImageView
										.getViewHeight()));
				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), mapArrays[0]));
				tv_floor2.setBackgroundResource(R.drawable.lightgray);
				tv_floor3.setBackgroundResource(R.drawable.lightgray);
			}
		});
		tv_floor2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_floor2.setBackgroundResource(R.color.blue_dark);
				pinchableImageView
						.setLayoutParams(new FrameLayout.LayoutParams(
								SCREEN_WIDTH, pinchableImageView
										.getViewHeight()));
				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), mapArrays[1]));
				tv_floor1.setBackgroundResource(R.drawable.lightgray);
				tv_floor3.setBackgroundResource(R.drawable.lightgray);
			}
		});
		tv_floor3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				tv_floor3.setBackgroundResource(R.color.blue_dark);
				pinchableImageView
						.setLayoutParams(new FrameLayout.LayoutParams(
								SCREEN_WIDTH, pinchableImageView
										.getViewHeight()));
				pinchableImageView.setImageBitmap(BitmapFactory.decodeResource(
						getResources(), mapArrays[2]));
				tv_floor2.setBackgroundResource(R.drawable.lightgray);
				tv_floor1.setBackgroundResource(R.drawable.lightgray);
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
