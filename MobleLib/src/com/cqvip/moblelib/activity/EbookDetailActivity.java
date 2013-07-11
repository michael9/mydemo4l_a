package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;

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
import com.cqvip.utils.Tool;

public class EbookDetailActivity extends BaseActivity implements IBookManagerActivity {

	public static final int ADD_COMMENT = 1;
	public static final int ADD_FORVORITE = 2;
	public static final int GET_DETAIL = 3;
	
	private Context context;
	private EBook dBook;
	private TextView author,from,type,page,title,content,time;
	private String download_url = null;
	private Button btn_ebook_detail_great,
					btn_ebook_detail_buzz,
					btn_ebook_detail_share,
					btn_ebook_detail_collect,
					btn_ebook_detail_download;
	private View title_bar,book_action_bar;
	private ImageView img_book;
	private ImageFetcher mImageFetcher;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ebook_detail);
		context = this;
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		dBook = (EBook)bundle.getSerializable("book");
		author = (TextView) findViewById(R.id.ebook_author_txt);
		from = (TextView) findViewById(R.id.ebook_from_txt);
		type =(TextView)findViewById(R.id.ebook_type_txt);
		page =(TextView)findViewById(R.id.ebook_page_txt);
		title =(TextView)findViewById(R.id.ebook_title_txt);
		content =(TextView)findViewById(R.id.ebook_content_abst);
		time = (TextView) findViewById(R.id.ebook_time_txt);
		img_book = (ImageView)findViewById(R.id.ebook_icon_img);
//		share =(Button)findViewById(R.id.ebook_share_txt);
//		favor =(Button)findViewById(R.id.ebook_favorite_txt);
//		comment =(Button)findViewById(R.id.ebook_comment_txt);
//		download =(Button)findViewById(R.id.ebook_down_txt);
	
		book_action_bar=findViewById(R.id.book_action_bar);
		btn_ebook_detail_great=(Button)book_action_bar.findViewById(R.id.btn_item_great);
		btn_ebook_detail_buzz=(Button)book_action_bar.findViewById(R.id.btn_item_buzz);
		btn_ebook_detail_share=(Button)book_action_bar.findViewById(R.id.btn_item_share);
		btn_ebook_detail_collect=(Button)book_action_bar.findViewById(R.id.btn_item_collect);
		btn_ebook_detail_download=(Button)book_action_bar.findViewById(R.id.btn_item_download);
		
		ManagerService.allActivity.add(this);
		if(dBook.getLngid()!=null){
			getLocalinfo(dBook.getLngid());
		}
		   String author1 = getResources().getString(R.string.item_author);
		   String from1 = getResources().getString(R.string.ebook_orang);
		   String time1 = getResources().getString(R.string.ebook_time);
		   String page1 =  getResources().getString(R.string.ebook_page);
		   String describe1 = getResources().getString(R.string.ebook_abstrac);
		   String type1 = getResources().getString(R.string.ebook_type);
	   ImageCacheParams cacheParams = new ImageCacheParams(context, GlobleData.IMAGE_CACHE_DIR);
       cacheParams.setMemCacheSizePercent(0.125f); // Set memory cache to 12.5% of app memory
	   mImageFetcher = new ImageFetcher(context, getResources().getDimensionPixelSize(R.dimen.bookicon_width),
			   getResources().getDimensionPixelSize(R.dimen.bookicon_height));
	   mImageFetcher.setLoadingImage(R.drawable.defaut_book);
	   mImageFetcher.addImageCache(cacheParams);
	   mImageFetcher.setImageFadeIn(false);
	   mImageFetcher.loadImage(dBook.getImgurl(), img_book);	
		title.setText(dBook.getTitle_c());
		author.setText(author1+dBook.getWriter());
		from.setText(from1+dBook.getName_c());
		time.setText(time1+dBook.getYears()+"年,"+"第"+dBook.getNum()+"期");
		page.setText(page1+dBook.getPagecount());
		if(dBook.getPdfsize()!=0){
		type.setText(type1+"PDF,"+dBook.getPdfsize()/1024+"KB");
		}else {
		type.setVisibility(View.GONE);
		}
		content.setText(describe1+dBook.getRemark_c());
//		//判断是否已经收藏
//		btn_ebook_detail_collect.setText(isFavorite(dBook.isIsfavorite()));
		
		
		btn_ebook_detail_buzz.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Tool.bookEbuzz(EbookDetailActivity.this, dBook);
				if (GlobleData.islogin) {
				Intent _intent = new Intent(context,CommentActivity.class);
				Book book = new Book(null,dBook.getOrgan(),dBook.getTitle_c(),
						dBook.getWriter(),dBook.getLngid(),null,null,dBook.getRemark_c(),"");
				Bundle bundle = new Bundle();
				bundle.putSerializable("book", book);
				_intent.putExtra("detaiinfo", bundle);
				_intent.putExtra("type", GlobleData.BOOK_ZK_TYPE);
				startActivity(_intent);
				} else {
					//只是登陆而已
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
				Tool.bookEfavorite(EbookDetailActivity.this, dBook);
				customProgressDialog.show();
				} else {
					//只是登陆而已
					showLoginDialog(4);
				}
			}
		});
		
		btn_ebook_detail_download.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(download_url!=null){
					//弹出对话框
					Intent  intent=new Intent(Intent.ACTION_VIEW,Uri.parse(download_url));
					startActivity(intent);
					//下载
				}
				
			}
		});
		
		title_bar=findViewById(R.id.head_bar);
		TextView title = (TextView)title_bar.findViewById(R.id.txt_header);
		title.setText(R.string.book_detail);
		ImageView back = (ImageView)title_bar.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	//显示对话框
	private void showLoginDialog(int id) {
		MainMenuActivity.cantouch = true;
		Intent intent = new Intent(context, ActivityDlg.class);
		intent.putExtra("ACTIONID", id);
		startActivityForResult(intent, id);
	}
	
//	//判断是否收藏
//	private String isFavorite(boolean isfavorite) {
//		if(isfavorite||EBookSearchActivity.favors.containsKey(dBook.getLngid())){
//			return getResources().getString(R.string.already_favoriate);
//		}else{
//			return getResources().getString(R.string.add_favorite);
//		}
//	}

	private void getLocalinfo(String recordid) {
		
		HashMap map=new HashMap();
		map.put("lngid", recordid);
		ManagerService.addNewTask(new Task(Task.TASK_EBOOK_DOWN,map));
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
    @Override
    public void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
    }
	@Override
	public void refresh(Object... obj) {
		customProgressDialog.dismiss();
		int type = (Integer)obj[0];
		switch(type){
		case ADD_FORVORITE:
			//更新界面
			Result result = (Result)obj[1];
			if(result.getSuccess()){
			//提示
				Tool.ShowMessages(context,getResources().getString(R.string.favorsucess));
//			//设置已收藏
//			btn_ebook_detail_collect.setText(getResources().getString(R.string.already_favoriate));
//			btn_ebook_detail_collect.setClickable(false);
//			btn_ebook_detail_collect.setTextColor(getResources().getColor(R.drawable.silvergray));
//			//加入hashMap
//			EBookSearchActivity.favors.put(dBook.getLngid(), true);
			}else{
				Tool.ShowMessages(context, getResources().getString(R.string.already_favoriate));
				}
			break;
		case GET_DETAIL:
			List<ShortBook> book = (List<ShortBook>)obj[1];
			if(book!=null){
				download_url = book.get(0).getDate();
				Log.i("url","download_url"+download_url);
			}
			break;
		}
		
		
	}

}
