package com.cqvip.moblelib.activity;

import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.sychild.R.layout;
import com.cqvip.moblelib.sychild.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p>
 * �ļ����: RefServiceActivity.java
 * �ļ�����: �ο���ѯ
 * ��Ȩ����: ��Ȩ����(C)2013-2020
 * ��          ˾: ����ά����ѯ���޹�˾
 * ����ժҪ: 
 * ����˵��:
 * ������ڣ� 201��5��10��
 * �޸ļ�¼: 
 * </p>
 * 
 * @author LHP,LJ
 */
public class RefServiceActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_ref_service);
		View v = findViewById(R.id.ref_title);
		TextView title = (TextView)v.findViewById(R.id.txt_header);
		title.setText(R.string.main_bookcomment);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		ImageView ckzx = (ImageView)v.findViewById(R.id.btn_right_header);
		ckzx.setVisibility(View.VISIBLE);
		ckzx.setImageResource(R.drawable.kszx);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});
		ckzx.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
	}


}
