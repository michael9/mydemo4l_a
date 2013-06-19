package com.cqvip.moblelib.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqvip.moblelib.R;

/**
 * <p>
 * �ļ�����: AnnounceActivity.java
 * �ļ�����: ���ڹ���
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
