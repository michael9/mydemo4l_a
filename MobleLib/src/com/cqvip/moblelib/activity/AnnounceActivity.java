package com.cqvip.moblelib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cqvip.moblelib.ahslsd.R;
import com.cqvip.moblelib.constant.Constant;

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

public class AnnounceActivity extends BaseActivity implements OnClickListener {

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
		//���Ŷ�̬
		LinearLayout l1_news = (LinearLayout) findViewById(R.id.an_out_Layout);
		//֪ͨ����
		LinearLayout l2_anno = (LinearLayout) findViewById(R.id.an_anno_Layout);
		//ר�⽲��
		LinearLayout l3_subject = (LinearLayout) findViewById(R.id.an_subject_Layout);
		//ר�ҽ���
		LinearLayout l4_professor = (LinearLayout) findViewById(R.id.an_speechLayout);
		
		l1_news.setOnClickListener(this);
		l2_anno.setOnClickListener(this);
		l3_subject.setOnClickListener(this);
		l4_professor.setOnClickListener(this);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	@Override
	public void onClick(View v) {
		Intent intent = new Intent(context, AnnouceListActivity.class);
		switch (v.getId()) {
		case R.id.an_out_Layout:
		intent.putExtra("type",Constant.SPEECH_NEWS);
		startActivity(intent);
			break;
		case R.id.an_anno_Layout:
			intent.putExtra("type",Constant.SPEECH_ANNOUNCE);
			startActivity(intent);
			break;
		case R.id.an_subject_Layout:
			intent.putExtra("type",Constant.SPEECH_SUBJECT);
			startActivity(intent);
			break;
		case R.id.an_speechLayout:
			intent.putExtra("type",Constant.SPPECH_FREE);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
