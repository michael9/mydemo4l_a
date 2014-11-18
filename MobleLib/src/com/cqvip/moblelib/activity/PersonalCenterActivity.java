package com.cqvip.moblelib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.MUserDao;
import com.cqvip.moblelib.sychild.R;
import com.cqvip.utils.Tool;

/**
 * <p>
 * �ļ����: PersonalCenterActivity.java
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
//private LinearLayout favorLayout;
private LinearLayout logoutLayout;
private LinearLayout downloadLayout,myfavorLayout;
private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_personal_center);
		context = this;
		//������Ϣ
		readerinfoLayout=(LinearLayout) findViewById(R.id.readerinfoLayout);
		//favorLayout=(LinearLayout) findViewById(R.id.favorLayout);
		logoutLayout=(LinearLayout) findViewById(R.id.logoutLayout);
		downloadLayout=(LinearLayout) findViewById(R.id.downloadLayout);
		myfavorLayout=(LinearLayout) findViewById(R.id.myfavorLayout);
	
		ImageView back = (ImageView)findViewById(R.id.return_iv);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				// overridePendingTransition(R.anim.slide_left_in,
				// R.anim.slide_right_out);
			}
		});
		
		readerinfoLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PersonalCenterActivity.this, ReaderinfoActivity.class);
				startActivity(intent);
				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);	
			}
		});
		
//		favorLayout.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent=new Intent(PersonalCenterActivity.this, MyFavorActivity.class);
//				startActivity(intent);
//				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);	
//			}
//		});
		//���d����
		downloadLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PersonalCenterActivity.this, DownLoadManagerActivity.class);
				startActivity(intent);
			}
		});
		
		//�ҵ��ղ�
		myfavorLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PersonalCenterActivity.this, MyFavorActivity.class);
				startActivity(intent);
			}
		});
		
		logoutLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent =new Intent(PersonalCenterActivity.this,ActivityDlg.class);
				intent.putExtra("ACTIONID", 0);
				intent.putExtra("MSGBODY", getResources().getString(R.string.confirm_quit));
				intent.putExtra("BTN_CANCEL", 1);
				startActivityForResult(intent, 1);
				//�����Ի���ȷ���Ƿ��˳�
				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);	
//				AlertDialog.Builder builder = new AlertDialog.Builder(context)
//				.setTitle(R.string.title_tips)
//				.setMessage(R.string.confirm_quit)
//				.setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// �����ݿ⣬�˺���Ϣ
//						MUserDao dao = new MUserDao(context);
//						try {
//							dao.delInfo(GlobleData.userid);
//						} catch (DaoException e) {
//							Log.i("PersonalCenterActivity","===ע��ʧ��=");
//							e.printStackTrace();
//						}
//						//���õ�¼��ʶ
//						MainMenuActivity.islogin = false;
//						//���ص�¼����
//						finish();
//						//��ʾע��ɹ�
//						Tool.ShowMessages(context, "ע��ɹ�");
//						
//					}
//				}).setNegativeButton(getString(R.string.confirm_cancel),null);
//				builder.create().show();
//			}
//		});
//	
//		View v = findViewById(R.id.readerserve_title);
//		TextView title = (TextView)v.findViewById(R.id.txt_header);
//		title.setText(R.string.main_ebookstore);
//		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
//		back.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				finish();
//				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);	
			}
		});
	}


	@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			switch (requestCode) {
			case 1:
				if(resultCode==0)
				{
					MUserDao dao = new MUserDao(context);
					try {
						dao.delInfo(GlobleData.userid);
					} catch (DaoException e) {
						e.printStackTrace();
						onError(2);
					}
					//���õ�¼��ʶ
					GlobleData.islogin = false;
					//���ص�¼����
					finish();
					//��ʾע��ɹ�
					Tool.ShowMessages(context, "ע���¼�ɹ�");
					
				}else{
					
				}
				break;

			default:
				break;
			}
		}
}
