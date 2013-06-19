package com.cqvip.moblelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqvip.moblelib.R;

/**
 * <p>
 * 文件名称: PersonalCenterActivity.java
 * 文件描述: 个人中心
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
public class PersonalCenterActivity extends BaseActivity {
private LinearLayout readerinfoLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_personal_center);
		//读者信息
		readerinfoLayout=(LinearLayout) findViewById(R.id.readerinfoLayout);
		readerinfoLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PersonalCenterActivity.this, ReaderinfoActivity.class);
				startActivity(intent);
				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);	
			}
		});
	
		View v = findViewById(R.id.readerserve_title);
		TextView title = (TextView)v.findViewById(R.id.txt_header);
		title.setText(R.string.main_ebookstore);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);	
			}
		});
	}


}
