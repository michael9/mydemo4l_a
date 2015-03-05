package com.cqvip.moblelib.activity;

import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.sychild.R.layout;
import com.cqvip.moblelib.sychild.R.menu;
import com.cqvip.moblelib.constant.Constant;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * <p>
 * 文件名称: SuggestedReadingActivity.java
 * 文件描述: 推荐阅读
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
public class SuggestedReadingActivity extends BaseActivity {

	
	
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggested_reading);
		context = this;
		ImageView back = (ImageView)findViewById(R.id.return_iv);
		
		LinearLayout l1 = (LinearLayout) findViewById(R.id.suggestLayout);
		LinearLayout l2 = (LinearLayout) findViewById(R.id.newbookLayout);
		
		l1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AdvancedBookActivity.class);
				intent.putExtra("type",Constant.HOTBOOK);
				startActivity(intent);
				// overridePendingTransition(R.anim.slide_right_in,
				// R.anim.slide_left_out);
			}
		});
		l2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AdvancedBookActivity.class);
				intent.putExtra("type",Constant.NEWBOOK);
				startActivity(intent);
				// overridePendingTransition(R.anim.slide_right_in,
				// R.anim.slide_left_out);

			}
		});
		
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
				
			}
		});
	}


}
