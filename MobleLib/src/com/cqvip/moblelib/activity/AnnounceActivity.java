package com.cqvip.moblelib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.constant.Constant;

/**
 * <p>
 * �ļ����: AnnounceActivity.java
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

	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_announce);
		context = this;
		ImageView back = (ImageView)findViewById(R.id.return_iv);
		//���Ŷ�̬
		LinearLayout l1 = (LinearLayout) findViewById(R.id.an_out_Layout);
		//���潲��
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
