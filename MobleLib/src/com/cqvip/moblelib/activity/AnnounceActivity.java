package com.cqvip.moblelib.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqvip.moblelib.R;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//hejun
		//maqz232dfddfdsdfff2222
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_announce);
		View v = findViewById(R.id.readerserve_title);
		TextView title = (TextView)v.findViewById(R.id.txt_header);
		title.setText(R.string.main_notice);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			//	overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});
	}


}
