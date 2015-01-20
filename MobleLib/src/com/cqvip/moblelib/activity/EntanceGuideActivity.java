package com.cqvip.moblelib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqvip.moblelib.sm.R;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.view.mylinearlayout;

/**
 * <p>
 * 文件名称: EntanceGuideActivity.java 
 * 文件描述: 入馆指南 版权所有: 版权所有(C)2013-2020 
 * 公 司:重庆维普咨询有限公司
 *  内容摘要: 其他说明: 
 *  完成日期： 2013年5月10日 
 *  修改记录:
 * </p>
 * 
 * @author LHP,LJ
 */
public class EntanceGuideActivity extends BaseActivity {
	private LinearLayout ll_gpsLayout;
	private mylinearlayout myll;
	private LinearLayout l1, l2, l3, l4, l5, l6, l7, l8;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.part_guide);
		context = this;
		initialView();
		//setOnclickListener();
		View v = findViewById(R.id.guide_title);
		TextView title = (TextView) v.findViewById(R.id.txt_header);
		title.setText(R.string.main_guide);
		ImageView back = (ImageView) v.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		LinearLayout l1 = (LinearLayout) findViewById(R.id.enterguideLayout);
//		LinearLayout l2 = (LinearLayout) findViewById(R.id.cardLayout);
		LinearLayout l3 = (LinearLayout) findViewById(R.id.timeLayout);
//		LinearLayout l4 = (LinearLayout) findViewById(R.id.readerknowLayout);
//		LinearLayout l5 = (LinearLayout) findViewById(R.id.serverLayout);
//		LinearLayout l6 = (LinearLayout) findViewById(R.id.transportLayout);
		// LinearLayout l7 = (LinearLayout)findViewById(R.id.gpsLayout);
		LinearLayout l8 = (LinearLayout) findViewById(R.id.probLayout);

		l1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, DetailTextActivity.class);
				intent.putExtra("enter", 1);
				startActivity(intent);
				// overridePendingTransition(R.anim.slide_right_in,
				// R.anim.slide_left_out);
			}
		});
//		l2.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, DetailTextActivity.class);
//				intent.putExtra("enter", 2);
//				startActivity(intent);
//				// overridePendingTransition(R.anim.slide_right_in,
//				// R.anim.slide_left_out);
//
//			}
//		});
		l3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(context, DetailTextActivity.class);
				intent.putExtra("enter", 3);
				startActivity(intent);
				// overridePendingTransition(R.anim.slide_right_in,
				// R.anim.slide_left_out);
			}
		});
//		l4.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//
//				Intent intent = new Intent(context, DetailTextActivity.class);
//				intent.putExtra("enter", 4);
//				startActivity(intent);
//				// overridePendingTransition(R.anim.slide_right_in,
//				// R.anim.slide_left_out);
//			}
//		});
//		l5.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(context, DetailTextActivity.class);
//				intent.putExtra("enter", 5);
//				startActivity(intent);
//				// overridePendingTransition(R.anim.slide_right_in,
//				// R.anim.slide_left_out);
//
//			}
//		});
//		l6.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
////				Intent intent = new Intent(context, DetailTextActivity.class);
////				intent.putExtra("enter", 6);
////				startActivity(intent);
//				// overridePendingTransition(R.anim.slide_right_in,
//				// R.anim.slide_left_out);
//				Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://j.map.baidu.com/6V_Ii"));
//				startActivity(intent);			
//			}
//		});
		// l7.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// Intent intent = new Intent(context,DetailTextActivity.class);
		// intent.putExtra("enter", 7);
		// startActivity(intent);
		// overridePendingTransition(R.anim.slide_right_in,
		// R.anim.slide_left_out);
		// }
		// });
		l8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, DetailTextActivity.class);
				intent.putExtra("enter", 8);
				//startActivity(intent);
				//Tool.ShowMessages(EntanceGuideActivity.this, "暂无问题");
				// overridePendingTransition(R.anim.slide_right_in,
				// R.anim.slide_left_out);
//				Intent mintent = new Intent(context, AnnouceListActivity.class);
//				mintent.putExtra("type",Constant.QUESTION);
				startActivity(intent);

			}
		});
	}

	private void initialView() {
		//ll_gpsLayout = (LinearLayout) findViewById(R.id.gpsLayout);
		myll =  (mylinearlayout) findViewById(R.id.myll);
		myll.setActivity(this);
	}

	private void setOnclickListener() {
		ll_gpsLayout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EntanceGuideActivity.this,
						LibInnerNavigationActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		Log.e("entanceguide", "finish");
	}
}
