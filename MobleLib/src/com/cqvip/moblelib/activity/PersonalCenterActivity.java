package com.cqvip.moblelib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.ahslsd.R;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.MUserDao;
import com.cqvip.utils.Tool;

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
private LinearLayout favorLayout;
private LinearLayout logoutLayout;
private LinearLayout downloadLayout;
private LinearLayout borrowLayout;
private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_personal_center);
		context = this;
		//读者信息
		readerinfoLayout=(LinearLayout) findViewById(R.id.readerinfoLayout);
		favorLayout=(LinearLayout) findViewById(R.id.favoriteLayout);
		logoutLayout=(LinearLayout) findViewById(R.id.logoutLayout);
		downloadLayout=(LinearLayout) findViewById(R.id.downloadLayout);
		borrowLayout=(LinearLayout) findViewById(R.id.borrowLayout);
	
		TextView title = (TextView)findViewById(R.id.txt_header);
		title.setText(R.string.main_ebookstore);
		ImageView back = (ImageView) findViewById(R.id.img_back_header);
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
		/**
		 * 我的收藏
		 */
		favorLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PersonalCenterActivity.this, MyFavorActivity.class);
				startActivity(intent);
				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);	
			}
		});
		/**
		 * 借阅管理
		 */
		borrowLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PersonalCenterActivity.this, BorrowAndOrderActivity.class);
				startActivity(intent);
			}
		});
		
		//下載管理
		downloadLayout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(PersonalCenterActivity.this, DownLoadManagerActivity.class);
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
				//弹出对话框，确认是否退出
				//overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);	
//				AlertDialog.Builder builder = new AlertDialog.Builder(context)
//				.setTitle(R.string.title_tips)
//				.setMessage(R.string.confirm_quit)
//				.setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
//					
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						// 清楚数据库，账号信息
//						MUserDao dao = new MUserDao(context);
//						try {
//							dao.delInfo(GlobleData.userid);
//						} catch (DaoException e) {
//							Log.i("PersonalCenterActivity","===注销失败=");
//							e.printStackTrace();
//						}
//						//设置登录标识
//						MainMenuActivity.islogin = false;
//						//返回登录界面
//						finish();
//						//提示注销成功
//						Tool.ShowMessages(context, "注销成功");
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
					}
					//设置登录标识
					GlobleData.islogin = false;
					//返回登录界面
					finish();
					//提示注销成功
					Tool.ShowMessages(context, "注销登录成功");
					
				}else{
					
				}
				break;

			default:
				break;
			}
		}
}
