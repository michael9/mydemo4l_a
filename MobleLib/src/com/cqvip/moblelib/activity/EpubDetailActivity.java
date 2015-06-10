package com.cqvip.moblelib.activity;

import java.io.File;
import java.net.URL;
import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Global;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.MEBookDao;
import com.cqvip.moblelib.entity.MEbook;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.EpubDetail;
import com.cqvip.moblelib.sm.R;
import com.cqvip.utils.Tool;
import com.google.zxing.common.GlobalHistogramBinarizer;

public class EpubDetailActivity extends BaseActivity {

	private EBook dBook;
	private ImageLoader mImageLoader;
	private NetworkImageView epub_detail_img;
	private TextView epub_detail_title, epub_detail_writer, epub_detail_detail;
	private EpubDetail mObject;
	private View title_bar;
	private Button epub_detail_read;
	private DownloadManager downloadManager;
	public static final String DOWNLOAD_FOLDER_NAME = "downloadmoblib";
	private long downloadId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_epub_detail);
		mObject = (EpubDetail) getIntent().getSerializableExtra("ed");
		dBook=new EBook(mObject.getId(), "", "", mObject.getTitle(), "", "", mObject.getWriter(), 0, 604*1024, mObject.getImgurl(), "");
		downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

		title_bar = findViewById(R.id.head_bar);
		TextView title = (TextView) title_bar.findViewById(R.id.txt_header);
		title.setText("电子图书");
		ImageView back = (ImageView) title_bar
				.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		epub_detail_read = (Button) findViewById(R.id.epub_detail_read);

		epub_detail_img = (NetworkImageView) findViewById(R.id.epub_detail_img);
		epub_detail_title = (TextView) findViewById(R.id.epub_detail_title);
		epub_detail_writer = (TextView) findViewById(R.id.epub_detail_writer);
		epub_detail_detail = (TextView) findViewById(R.id.epub_detail_detail);
		epub_detail_title.setText("书名： 《" + mObject.getTitle() + "》");
		epub_detail_writer.setText("作者： " + mObject.getWriter());
		epub_detail_detail.setText("摘要：\r\n   " + mObject.getDetail());
		epub_detail_img.setImageUrl(mObject.getImgurl(), new ImageLoader(
				mQueue, cache));
		
		epub_detail_read.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				if(Constant.isUserDisable){
					Toast.makeText(EpubDetailActivity.this, getString(R.string.tips_undo),
							Toast.LENGTH_SHORT).show();
				}else{
				if (GlobleData.islogin) {
				if (mObject.getDownloadurl() != null) {
					// 弹出对话框
//					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
//							.parse(download_url));
//					startActivity(intent);
					//1、判断是否有sd卡
					 if(!Tool.hasSDcard(EpubDetailActivity.this)){
							return;
						}
					//创建下载路径，下载名称
					 File folder = new File(DOWNLOAD_FOLDER_NAME);
					   if (!folder.exists() || !folder.isDirectory()) {
						   folder.mkdirs();
					   }
					   MEBookDao dao = new MEBookDao(EpubDetailActivity.this);
					  //1查看是否下载过
					   if(isAlreadyDownload(dao,mObject.getId())){
						   Intent _intent = new Intent(EpubDetailActivity.this,ActivityDlg.class);
						   _intent.putExtra("MSGBODY", "您已经下载该文件，是否重新下载?");
						   _intent.putExtra("BTN_CANCEL", 1);
						   startActivityForResult(_intent,0);
					   }else{
						   //加入下载列队
						   andToqueue();
						   //插入数据库保存,正在下载
						   try {
							dao.saveInfo(dBook, downloadId, MEbook.TYPE_ON_DOWNLOADING);
						} catch (DaoException e) {
							e.printStackTrace();
						}
						   start_DownLoadManagerActivity();
					 }
				}else{
					Tool.ShowMessages(EpubDetailActivity.this,getString(R.string.tips_unable_download));
					
				}
			}else{
				// 进入下载界面
				showLoginDialog(4);
				}
			}	
			}
			});
	}
	

	private String getFullname(String title_c) {
		return title_c + ".epub";
	}
	
	private void showLoginDialog(int id) {
		MainMenuActivity.cantouch = true;
		Intent intent = new Intent(EpubDetailActivity.this, ActivityDlg.class);
		intent.putExtra("ACTIONID", id);
		startActivityForResult(intent, id);
	}
	private void start_DownLoadManagerActivity(){
		   Intent intent= new Intent(EpubDetailActivity.this, DownLoadManagerActivity.class);
		   intent.putExtra("ispressdownbutton", true);
		   startActivity(intent);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//从新下载
		if(requestCode ==0&&resultCode == 0){
			 MEBookDao dao = new MEBookDao(EpubDetailActivity.this);
			 andToqueue();
			   //插入数据库保存,正在下载
			   try {
				dao.saveInfo(dBook, downloadId, MEbook.TYPE_ON_DOWNLOADING);
			} catch (DaoException e) {
				e.printStackTrace();
			}
			   start_DownLoadManagerActivity();
		}
	}
	/**
	 * 判断是否下载过该文件
	 * @param id 电子书唯一id
	 * @return
	 */
	private boolean isAlreadyDownload(MEBookDao dao,String id) {
		try {
		MEbook m = dao.queryInfo(id);
		if(m ==null)
		return false;
		} catch (DaoException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 加入下载列队，下载
	 * 
	 */
	@SuppressLint("NewApi")
	private void andToqueue() {
		DownloadManager.Request request = new DownloadManager.Request(
				Uri.parse(mObject.getDownloadurl()));
		request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME,
				getFullname(mObject.getTitle()));
		request.setTitle(mObject.getTitle());
		 request.setDescription("格式：epub,"+dBook.getPdfsize());
		if (Build.VERSION.SDK_INT >= 11)
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setVisibleInDownloadsUi(false);
		request.setMimeType("application/epub");
		try {
			downloadId = downloadManager.enqueue(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tool.ShowMessages(EpubDetailActivity.this,
					getString(R.string.tips_fail_download));
			return;
		}
		Tool.ShowMessages(EpubDetailActivity.this,
				getString(R.string.tips_begin_download));
	}
}