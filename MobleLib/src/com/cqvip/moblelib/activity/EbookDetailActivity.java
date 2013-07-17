package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cqvip.mobelib.imgutils.ImageCache.ImageCacheParams;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ebook_detail);
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
								+ "/library/bookquery/search.aspx",
								add_forvorite_ls, Method.POST);
					}
				} else {
					// 只是登陆而已
					showLoginDialog(4);
				}
			}
		});

		btn_ebook_detail_download.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (download_url != null) {
					// 弹出对话框
					Intent intent = new Intent(Intent.ACTION_VIEW, Uri
							.parse(download_url));
					startActivity(intent);
					// 下载
				}

			}
		});

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
			try {
				List<ShortBook> book = ShortBook.formList(Task.TASK_EBOOK_DOWN,
						response);
				if (book != null) {
					download_url = book.get(0).getDate();
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
				// TODO: handle exception
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

}
