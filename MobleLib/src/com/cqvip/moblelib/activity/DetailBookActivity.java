package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.adapter.BookLocAdapter;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BookLoc;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.utils.HttpUtils;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;

/**
 * <p>
 * 文件名称: DetailBookActivity.java
 * 文件描述: 馆藏图书详细
 * 版权所有: 版权所有(C)2013-2020
 * 公          司: 重庆维普咨询有限公司
 * 内容摘要: 
 * 其他说明:
 * 完成日期： 201年11月20日
 * 修改记录: 修改收藏的方式，图书id，直接取recordid 2013.ll.l6 by lj
 * </p>
 * 
 * @author LHP,LJ
 */
public class DetailBookActivity extends BaseActivity {
	public final int GETBOOKINFO = 1;
	public final int FAVOR = 2;
	private Book dBook;
	private TextView booktitle_tv, textView9, textView10, textView11;
	private LinearLayout loc_list_ll;
	private BookLocAdapter adapter;
	private Context context;
	private View title_bar, book_action_bar;
	private ImageView imgview;
	boolean ismyfavor;
	private Map<String, String> gparams;
	private TextView btn_item_result_search_collect,
			btn_item_result_search_share, btn_item_result_search_buzz,
			btn_item_result_search_download;

	private int fromFlage;// 表示从哪个activity跳转过来，评论过来不显示评论按钮

	//private static final String FILE_NAME = "/pic.jpg";
	//public static String TEST_IMAGE=null;
	Bitmap bitmap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_book);
		context = this;
		loc_list_ll = (LinearLayout) findViewById(R.id.loc_list_ll);
		imgview = (ImageView) findViewById(R.id.book_big_img);
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book) bundle.getSerializable("book");
		fromFlage = getIntent().getIntExtra("from", 0);
		ismyfavor = getIntent().getBooleanExtra("ismyfavor", false);
		booktitle_tv = (TextView) findViewById(R.id.booktitle_tv);
		textView9 = (TextView) findViewById(R.id.textView9);
		textView10 = (TextView) findViewById(R.id.textView10);
		textView11 = (TextView) findViewById(R.id.textView11);
		//if(!TextUtils.isEmpty(dBook.getCover_path())){
		if(cache==null){
			cache = new BitmapCache(Tool.getCachSize());
		}
		ImageLoader mImageLoader = new ImageLoader(mQueue, cache);
		ImageListener listener = ImageLoader.getImageListener(imgview,
				R.drawable.defaut_book, R.drawable.defaut_book);
      	ImageContainer imageContainer=mImageLoader.get(dBook.getCover_path(), listener);
      	bitmap=imageContainer.getBitmap();
//		}else{
//			bitmap = null;
//		}
		ShareSDK.initSDK(this);
		
		imgview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(dBook.getCover_path_big())) {
					return;
				}
				String bigimg = dBook.getCover_path_big();
				Intent intent = new Intent(context, BigImgActivity.class);
				intent.putExtra("bigurl", bigimg);
				startActivity(intent);
			}
		});
		booktitle_tv.setText(dBook.getTitle());
		// 从我的收藏传过来的isbn都是空字符串，
		String isbn = "";
		if (!TextUtils.isEmpty(dBook.getIsbn())) {
			isbn = "ISBN:" + dBook.getIsbn() + "\n";
		}
		// 从我的收藏传过来的是出版时间而非主题
		String timeortheme = "";
		if (ismyfavor) {
			timeortheme = getString(R.string.item_time) + dBook.getSubject();
		} else {
			timeortheme = getString(R.string.item_subject) + dBook.getSubject();
		}
		// 从我的收藏传过来的recordid存储在Book的callno
		String recordid = "";
		if (ismyfavor) {
			recordid = dBook.getCallno();
		} else {
			recordid = dBook.getRecordid();
		}
		if (recordid.contains(",")) {
			recordid = recordid.split(",")[1];
		}
		if (!TextUtils.isEmpty(recordid)) {
			getLocalinfo(recordid);
		}
		if (ismyfavor) {

			textView10.setText(getString(R.string.item_author)
					+ dBook.getAuthor() + "\n"
					+ getString(R.string.item_publish) + dBook.getPublisher()
					+ "\n" + timeortheme);
		} else {
			textView10.setText(getString(R.string.item_author)
					+ dBook.getAuthor() + "\n"
					+ getString(R.string.item_publish) + dBook.getPublisher()+","+dBook.getPublishyear()
					+ "\n" + timeortheme
					+ "\n"
					// +getString(R.string.item_callno)+dBook.getCallno()+"\n"
					+ getString(R.string.item_classno) + dBook.getClassno()
					+ "\n" + isbn + getString(R.string.item_price)
					+ dBook.getU_price());
		}
		textView11.setText("        "+dBook.getU_abstract());
		// listview.setAdapter(adapter);
		title_bar = findViewById(R.id.head_bar);
		TextView title = (TextView) title_bar.findViewById(R.id.txt_header);
		title.setText(R.string.book_detail);
		ImageView back = (ImageView)findViewById(R.id.return_iv);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		book_action_bar = findViewById(R.id.book_action_bar);
		// 收藏
		btn_item_result_search_collect = (TextView) book_action_bar
				.findViewById(R.id.btn_item_collect);
		btn_item_result_search_collect
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						if (GlobleData.islogin) {
							gparams = new HashMap<String, String>();
							gparams.put("libid", GlobleData.LIBIRY_ID);
							gparams.put("vipuserid", GlobleData.cqvipid);
							gparams.put("typeid", "" + GlobleData.BOOK_SZ_TYPE);
//							gparams.put(
//									"keyid",
//									Tool.formSZbookID(dBook.getCallno(),
//											dBook.getRecordid()));
							gparams.put("keyid",dBook.getRecordid());//深职院修改为recordid
							customProgressDialog.show();
							requestVolley(GlobleData.SERVER_URL
									+ "/cloud/favorite.aspx", bookfavorite_ls,
									Method.POST);
						} else {
							// 只是登陆而已
							showLoginDialog(4);
						}
					}
				});
		// 分享
		btn_item_result_search_share = (TextView) book_action_bar
				.findViewById(R.id.btn_item_share);
		btn_item_result_search_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Tool.bookshare_bysharesdk(DetailBookActivity.this, dBook,bitmap);
			}
		});

		// 评论
		btn_item_result_search_buzz = (TextView) book_action_bar
				.findViewById(R.id.btn_item_buzz);
		if (fromFlage == 1) {
			btn_item_result_search_buzz.setVisibility(View.GONE);
		}
		btn_item_result_search_buzz.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (GlobleData.islogin) {
					Tool.bookbuzz(DetailBookActivity.this, dBook);
				} else {
					// 只是登陆而已
					showLoginDialog(4);
				}
			}
		});
		//
		btn_item_result_search_download = (TextView) book_action_bar
				.findViewById(R.id.btn_item_download);
		btn_item_result_search_download.setVisibility(View.GONE);
	}

	@Override
	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}
	
	// 显示对话框
	private void showLoginDialog(int id) {
		MainMenuActivity.cantouch = true;
		Intent intent = new Intent(context, ActivityDlg.class);
		intent.putExtra("ACTIONID", id);
		startActivityForResult(intent, id);
	}

	private Listener<String> bookfavorite_ls = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();
			try {
				Result r = new Result(response);
				Tool.ShowMessagel(DetailBookActivity.this, r.getMessage());
			} catch (Exception e) {
				onError(2);
				return;
			}
		}
	};

	private Listener<String> back_ls = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			if (customProgressDialog != null
					&& customProgressDialog.isShowing())
				customProgressDialog.dismiss();
			try {
				final List<BookLoc> list = BookLoc.formList(response);
				new Thread(new Runnable() {

					@Override
					public void run() {
						add2gc(list);
					}
				}).start();

			} catch (Exception e) {
				onError(2);
				return;
			}
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
			mys.setRetryPolicy(HttpUtils.setTimeout());mQueue.add(mys);
			mQueue.start();
		} catch (Exception e) {
			onError(2);
		}
	}

	private void getLocalinfo(String recordid) {
		customProgressDialog.show();
		gparams = new HashMap<String, String>();
		gparams.put("recordid", recordid);
		gparams.put("libid", GlobleData.LIBIRY_ID);
		requestVolley(GlobleData.SERVER_URL + "/library/bookquery/detail.aspx",
				back_ls, Method.POST);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_book, menu);
		return true;
	}

	LinearLayout mll;

	private void add2gc(List<BookLoc> list) {
		if (list == null || list.isEmpty())
			return;
		for (BookLoc bl : list) {
			// LinearLayout mll=new LinearLayout(this);
			mll = (LinearLayout) getLayoutInflater().inflate(
					R.layout.item_location_book, loc_list_ll, false);
			// mll.inflate(this, R.layout.item_location_book, null);
			TextView barcode = (TextView) mll
					.findViewById(R.id.loc_barcode_txt);
			TextView callno = (TextView) mll.findViewById(R.id.loc_callno_txt);
			TextView location = (TextView) mll
					.findViewById(R.id.loc_location_txt);
			TextView cirtype = (TextView) mll
					.findViewById(R.id.loc_cirtype_txt);
			TextView status = (TextView) mll.findViewById(R.id.loc_status_txt);

			barcode.setText(context.getString(R.string.item_barcode)
					+ bl.getBarcode());
			callno.setText(context.getString(R.string.item_callno)
					+ bl.getCallno());
			location.setText(context.getString(R.string.item_loc)
					+ bl.getLocal());
			cirtype.setText(context.getString(R.string.item_cirtype)
					+ bl.getCirtype());
			status.setText(context.getString(R.string.item_status)
					+ bl.getStatus());
			handler.sendEmptyMessage(0);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			loc_list_ll.addView(mll, 0);
		};
	};

}
