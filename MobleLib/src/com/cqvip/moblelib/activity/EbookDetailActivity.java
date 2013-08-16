package com.cqvip.moblelib.activity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.MEBookDao;
import com.cqvip.moblelib.entity.MEbook;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;

public class EbookDetailActivity extends BaseActivity {

	private EBook dBook;
	private TextView author, from, type, page, title, content, time;
	private String download_url = null;
	private Button btn_ebook_detail_great, btn_ebook_detail_buzz,
			btn_ebook_detail_share, btn_ebook_detail_collect,
			btn_ebook_detail_download;
	private View title_bar, book_action_bar;
	private ImageView img_book;
	// private ImageFetcher mImageFetcher;
	private Map<String, String> gparams;
	//下载
	private Context context;
	 public static final String     DOWNLOAD_FOLDER_NAME = "downloadmoblib";
	 private long                   downloadId           = 0;
	 private DownloadManager        downloadManager;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ebook_detail);
		context = this;
		//设置标题，显示下载按钮
		View v = findViewById(R.id.head_bar);
		ImageView download = (ImageView)v.findViewById(R.id.btn_right_header);
		download.setVisibility(View.VISIBLE);
		
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (EBook) bundle.getSerializable("book");
		author = (TextView) findViewById(R.id.ebook_author_txt);
		from = (TextView) findViewById(R.id.ebook_from_txt);
		type = (TextView) findViewById(R.id.ebook_type_txt);
		page = (TextView) findViewById(R.id.ebook_page_txt);
		title = (TextView) findViewById(R.id.ebook_title_txt);
		content = (TextView) findViewById(R.id.ebook_content_abst);
		time = (TextView) findViewById(R.id.ebook_time_txt);
		img_book = (ImageView) findViewById(R.id.ebook_icon_img);

		book_action_bar = findViewById(R.id.book_action_bar);
		btn_ebook_detail_great = (Button) book_action_bar
				.findViewById(R.id.btn_item_great);
		btn_ebook_detail_buzz = (Button) book_action_bar
				.findViewById(R.id.btn_item_buzz);
		btn_ebook_detail_share = (Button) book_action_bar
				.findViewById(R.id.btn_item_share);
		btn_ebook_detail_collect = (Button) book_action_bar
				.findViewById(R.id.btn_item_collect);
		btn_ebook_detail_download = (Button) book_action_bar
				.findViewById(R.id.btn_item_download);

		btn_ebook_detail_download.setVisibility(View.INVISIBLE);
		 downloadManager = (DownloadManager)getSystemService(DOWNLOAD_SERVICE);
		if (dBook.getLngid() != null) {
			getLocalinfo(dBook.getLngid());
		}
		String author1 = getResources().getString(R.string.item_author);
		String from1 = getResources().getString(R.string.ebook_orang);
		String time1 = getResources().getString(R.string.ebook_time);
		String page1 = getResources().getString(R.string.ebook_page);
		String describe1 = getResources().getString(R.string.ebook_abstrac);
		String type1 = getResources().getString(R.string.ebook_type);

		ImageLoader mImageLoader = new ImageLoader(mQueue, new BitmapCache());
		ImageListener listener = ImageLoader.getImageListener(img_book,
				R.drawable.defaut_book, R.drawable.defaut_book);
		mImageLoader.get(dBook.getImgurl(), listener);

		title.setText(dBook.getTitle_c());
		author.setText(author1 + dBook.getWriter());
		from.setText(from1 + dBook.getName_c());
		time.setText(time1 + dBook.getYears() + "年," + "第" + dBook.getNum()
				+ "期");
		page.setText(page1 + dBook.getPagecount());
		if (dBook.getPdfsize() != 0) {
			type.setText(type1 + "PDF," + dBook.getPdfsize() / 1024 + "KB");
		} else {
			type.setVisibility(View.GONE);
		}
		content.setText(describe1 + dBook.getRemark_c());
		// //判断是否已经收藏
		// btn_ebook_detail_collect.setText(isFavorite(dBook.isIsfavorite()));

		btn_ebook_detail_buzz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Tool.bookEbuzz(EbookDetailActivity.this, dBook);
				if (GlobleData.islogin) {
					Intent _intent = new Intent(EbookDetailActivity.this,
							CommentActivity.class);
					Book book = new Book(null, dBook.getOrgan(), dBook
							.getTitle_c(), dBook.getWriter(), dBook.getLngid(),
							null, null, dBook.getRemark_c(), "");
					Bundle bundle = new Bundle();
					bundle.putSerializable("book", book);
					_intent.putExtra("detaiinfo", bundle);
					_intent.putExtra("type", GlobleData.BOOK_ZK_TYPE);
					startActivity(_intent);
				} else {
					// 只是登陆而已
					showLoginDialog(4);
				}
			}
		});
		btn_ebook_detail_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Tool.bookEshare(EbookDetailActivity.this, dBook);
			}
		});
		btn_ebook_detail_collect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (GlobleData.islogin) {
					// Tool.bookEfavorite(EbookDetailActivity.this, dBook);

					if (dBook != null) {
						customProgressDialog.show();
						gparams = new HashMap<String, String>();
						gparams.put("libid", GlobleData.LIBIRY_ID);
						gparams.put("vipuserid", GlobleData.cqvipid);
						gparams.put("keyid", dBook.getLngid());
						gparams.put("typeid", "" + GlobleData.BOOK_ZK_TYPE);
						requestVolley(GlobleData.SERVER_URL
								+ "/cloud/favorite.aspx",
								add_forvorite_ls, Method.POST);
					}
				} else {
					// 只是登陆而已
					showLoginDialog(4);
				}
			}
		});
		// 下载
		btn_ebook_detail_download.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				if (GlobleData.islogin) {
				if (download_url != null) {
					// 弹出对话框
//					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
//							.parse(download_url));
//					startActivity(intent);
					//1、判断是否有sd卡
					 if(!Tool.hasSDcard(context)){
							return;
						}
					//创建下载路径，下载名称
					 File folder = new File(DOWNLOAD_FOLDER_NAME);
					   if (!folder.exists() || !folder.isDirectory()) {
						   folder.mkdirs();
					   }
					   MEBookDao dao = new MEBookDao(context);
					  //1查看是否下载过
					   if(isAlreadyDownload(dao,dBook.getLngid())){
						   Intent _intent = new Intent(context,ActivityDlg.class);
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
					 }
				}else{
					Tool.ShowMessages(context,getString(R.string.tips_unable_download));
				}
			}else{
				// 进入下载界面
				showLoginDialog(4);
				}
			}
				
		});

		title_bar = findViewById(R.id.head_bar);
		TextView title = (TextView) title_bar.findViewById(R.id.txt_header);
		title.setText(R.string.book_detail);
		ImageView back = (ImageView) title_bar
				.findViewById(R.id.img_back_header);
        download.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (GlobleData.islogin) {
				startActivity(new Intent(context,DownLoadManagerActivity.class));
				}else {
					// 只是登陆而已
					showLoginDialog(4);
				}
			}
		});
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//从新下载
		if(requestCode ==0&&resultCode == 0){
			 MEBookDao dao = new MEBookDao(context);
			 andToqueue();
			   //插入数据库保存,正在下载
			   try {
				dao.saveInfo(dBook, downloadId, MEbook.TYPE_ON_DOWNLOADING);
			} catch (DaoException e) {
				e.printStackTrace();
			}
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
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(download_url));
		request.setDestinationInExternalPublicDir(DOWNLOAD_FOLDER_NAME, getFullname(dBook.getTitle_c()));
		request.setTitle(dBook.getTitle_c());
		request.setDescription("格式：pdf,"+dBook.getPdfsize());
		if(Build.VERSION.SDK_INT>=11)
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		request.setVisibleInDownloadsUi(false);
		request.setMimeType("application/pdf");
		try {
			downloadId = downloadManager.enqueue(request);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Tool.ShowMessages(context, getString(R.string.tips_fail_download));
			return;
		}
		Tool.ShowMessages(context, getString(R.string.tips_begin_download));
	}
	/**
	 * 下载文件名称
	 * @param title_c
	 * @return
	 */
	private String getFullname(String title_c) {
		return title_c+".pdf";
	}
	// 显示对话框
	private void showLoginDialog(int id) {
		MainMenuActivity.cantouch = true;
		Intent intent = new Intent(EbookDetailActivity.this, ActivityDlg.class);
		intent.putExtra("ACTIONID", id);
		startActivityForResult(intent, id);
	}

	// //判断是否收藏
	// private String isFavorite(boolean isfavorite) {
	// if(isfavorite||EBookSearchActivity.favors.containsKey(dBook.getLngid())){
	// return getResources().getString(R.string.already_favoriate);
	// }else{
	// return getResources().getString(R.string.add_favorite);
	// }
	// }

	private void getLocalinfo(String recordid) {

		gparams = new HashMap<String, String>();
		gparams.put("lngid", recordid);
		requestVolley(GlobleData.SERVER_URL + "/zk/articledown.aspx",
				detail_ls, Method.POST);
	}

	Listener<String> detail_ls = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			btn_ebook_detail_download.setVisibility(View.VISIBLE);
			try {
				List<ShortBook> book = ShortBook.formList(Task.TASK_EBOOK_DOWN,
						response);
				if (book != null) {
					download_url = book.get(0).getDate();
				}else{
					Tool.ShowMessages(context,getString(R.string.tips_unable_download));
				}
				
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	Listener<String> add_forvorite_ls = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			try {
				Result result = new Result(response);
				Tool.ShowMessages(EbookDetailActivity.this, result.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();

		}
	};

	private void requestVolley(String addr, Listener<String> bl, int method) {
		try {
			StringRequest mys = new StringRequest(method, addr, bl, el) {

				protected Map<String, String> getParams()
						throws com.android.volley.AuthFailureError {
					return gparams;
				};
			};
			mQueue.add(mys);
			mQueue.start();
		} catch (Exception e) {
			e.printStackTrace();
			onError(2);
		}
	}

}
