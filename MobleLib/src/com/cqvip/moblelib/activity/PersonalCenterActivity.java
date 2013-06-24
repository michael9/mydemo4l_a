package com.cqvip.moblelib.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.MUserDao;
import com.cqvip.utils.Tool;

/**
 * <p>
 * �ļ�����: PersonalCenterActivity.java
 * �ļ�����: ��������
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
public class PersonalCenterActivity extends BaseActivity {
private LinearLayout readerinfoLayout;
private LinearLayout favorLayout;
private LinearLayout logoutLayout;
private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_personal_center);
		context = this;
		//������Ϣ
		readerinfoLayout=(LinearLayout) findViewById(R.id.readerinfoLayout);
		favorLayout=(LinearLayout) findViewById(R.id.favorLayout);
		logoutLayout=(LinearLayout) findViewById(R.id.logoutLayout);
		
		readerinfoLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PersonalCenterActivity.this, ReaderinfoActivity.class);
				startActivity(intent);
				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);	
			}
		});
		
		favorLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PersonalCenterActivity.this, MyFavorActivity.class);
				startActivity(intent);
				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);	
			}
		});
		
		logoutLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//�����Ի���ȷ���Ƿ��˳�
				AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(R.string.title_tips)
				.setMessage(R.string.confirm_quit)
				.setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// ������ݿ⣬�˺���Ϣ
						MUserDao dao = new MUserDao(context);
						try {
							dao.delInfo(GlobleData.userid);
						} catch (DaoException e) {
							Log.i("PersonalCenterActivity","===ע��ʧ��=");
							e.printStackTrace();
						}
						//���ص�¼����
						finish();
						//��ʾע���ɹ�
						Tool.ShowMessages(context, "ע���ɹ�");
						
					}
				}).setNegativeButton(getString(R.string.confirm_quit),null);
				builder.create().show();
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
