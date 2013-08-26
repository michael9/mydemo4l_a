package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookLocAdapter;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BookLoc;
import com.cqvip.moblelib.model.Result;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_book);
		context = this;
		loc_list_ll = (LinearLayout) findViewById(R.id.loc_list_ll);
		imgview = (ImageView) findViewById(R.id.book_big_img);
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (Book) bundle.getSerializable("book");
		ismyfavor = getIntent().getBooleanExtra("ismyfavor", false);
		booktitle_tv = (TextView) findViewById(R.id.booktitle_tv);
		textView9 = (TextView) findViewById(R.id.textView9);
		textView10 = (TextView) findViewById(R.id.textView10);
		textView11 = (TextView) findViewById(R.id.textView11);
		
		ImageLoader mImageLoader = new ImageLoader(mQueue, new BitmapCache(Tool.getCachSize()));
		ImageListener listener = ImageLoader.getImageListener(imgview, R.drawable.defaut_book, R.drawable.defaut_book);
		mImageLoader.get(dBook.getCover_path(), listener);

		imgview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(dBook.getCover_path())) {
					return;
				}
				String bigimg = Tool.getBigImg(dBook.getCover_path());
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

		textView10.setText(getString(R.string.item_author) + dBook.getAuthor()
				+ "\n" + getString(R.string.item_publish)
				+ dBook.getPublisher() + "\n" + timeortheme + "\n"
				// +getString(R.string.item_callno)+dBook.getCallno()+"\n"
				// +getString(R.string.item_classno)+dBook.getClassno()+"\n"
				+ isbn + getString(R.string.item_price) + dBook.getU_price());
		textView11.setText(dBook.getU_abstract());
		// listview.setAdapter(adapter);
		title_bar = findViewById(R.id.head_bar);
		TextView title = (TextView) title_bar.findViewById(R.id.txt_header);
		title.setText(R.string.book_detail);
		ImageView back = (ImageView) title_bar
				.findViewById(R.id.img_back_header);
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
							gparams=new HashMap<String, String>();
							gparams.put("libid",  GlobleData.LIBIRY_ID);
							gparams.put("vipuserid", GlobleData.cqvipid);
							gparams.put("typeid", ""+GlobleData.BOOK_SZ_TYPE);
							gparams.put("keyid", Tool.formSZbookID(dBook.getCallno(),dBook.getRecordid()));
							customProgressDialog.show();
							requestVolley(GlobleData.SERVER_URL+"/cloud/favorite.aspx",bookfavorite_ls,Method.POST);
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
				Tool.bookshare(DetailBookActivity.this, dBook);
			}
		});

		// 评论
		btn_item_result_search_buzz = (TextView) book_action_bar
				.findViewById(R.id.btn_item_buzz);
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
			customProgressDialog.dismiss();
			try {
				Result r=new Result(response);
				Tool.ShowMessagel(DetailBookActivity.this, r.getMessage());
			} catch (Exception e) {
				// TODO: handle exception
				return;
			}
		}
	};
	
	private Listener<String> back_ls = new Listener<String>() {

		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
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
				// TODO: handle exception
				return;
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
			// TODO: handle exception
		}
	}

	private void getLocalinfo(String recordid) {
		customProgressDialog.show();
		gparams = new HashMap<String, String>();
		gparams.put("recordid", recordid);
		gparams.put("tablename", "bibliosm");// 书籍
		gparams.put("library", GlobleData.SZLG_LIB_ID);
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
					R.layout.item_location_book, loc_list_ll,false);
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
	
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			loc_list_ll.addView(mll,0);
		};
	};

}
