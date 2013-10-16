package com.cqvip.moblelib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqvip.moblelib.bate1.R;
import com.cqvip.moblelib.constant.Constant;

/**
 * <p>
 * 文件名称: AnnounceActivity.java
 * 文件描述: 馆内公告
 * 版权所有: 版权所有(C)2013-2020
 * 公          司: 重庆维普咨询有限公司
 * 内容摘要: 
 * 其他说明:
 * 完成日期： 201年5月10日
 * 修改记录: 
 * </p>
 * 
 * @author LHP,LJ
 */

public class AnnounceActivity extends BaseActivity {

	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_announce);
		context = this;
		View v = findViewById(R.id.readerserve_title);
		TextView title = (TextView)v.findViewById(R.id.txt_header);
		title.setText(R.string.main_notice);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		//新闻动态
		LinearLayout l1 = (LinearLayout) findViewById(R.id.an_out_Layout);
		//公益讲座
		LinearLayout l2 = (LinearLayout) findViewById(R.id.an_speechLayout);
		
		l1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AnnouceListActivity.class);
				intent.putExtra("type",Constant.SPEECH_NEWS);
				startActivity(intent);
				// overridePendingTransition(R.anim.slide_right_in,
				// R.anim.slide_left_out);
			}
		});
		l2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AnnouceListActivity.class);
				intent.putExtra("type",Constant.SPPECH_FREE);
				startActivity(intent);
				// overridePendingTransition(R.anim.slide_right_in,
				// R.anim.slide_left_out);

			}
		});
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			//	overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});
	}


}
