package com.cqvip.moblelib.activity;

import java.util.Map;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqvip.moblelib.bate1.R;
import com.cqvip.moblelib.model.ZLFBook;
public class ZLFBookDetailActivity extends BaseActivity {

	private ZLFBook zlfbook;
	private TextView author, from, type, page, title, content, time;
	private View title_bar, book_action_bar;
	private ImageView img_book;
	private Context context;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_zlfbook_detail);
		context = this;
		//设置标题，显示下载按钮
		View v = findViewById(R.id.head_bar);
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		zlfbook = (ZLFBook) bundle.getSerializable("book");
		
		author = (TextView) findViewById(R.id.ebook_author_txt);
		from = (TextView) findViewById(R.id.ebook_from_txt);
		type = (TextView) findViewById(R.id.ebook_type_txt);//isbn
		//page = (TextView) findViewById(R.id.ebook_page_txt);
		title = (TextView) findViewById(R.id.ebook_title_txt);
		content = (TextView) findViewById(R.id.ebook_content_abst);
		//time = (TextView) findViewById(R.id.ebook_time_txt);
		img_book = (ImageView) findViewById(R.id.ebook_icon_img);

		//book_action_bar = findViewById(R.id.book_action_bar);

		String author1 = getResources().getString(R.string.item_author);
		//String from1 = getResources().getString(R.string.ebook_orang);
		String publish = getResources().getString(R.string.item_publish);
		String page1 = getResources().getString(R.string.ebook_page);
		String describe1 = getResources().getString(R.string.ebook_abstrac);

//		ImageLoader mImageLoader = new ImageLoader(mQueue, new BitmapCache(Tool.getCachSize()));
//		ImageListener listener = ImageLoader.getImageListener(img_book,
//				R.drawable.defaut_book, R.drawable.defaut_book);
//		mImageLoader.get(zlfbook.getImgurl(), listener);

		
		String ztitle = zlfbook.getTitle_c()==null?"":zlfbook.getTitle_c();
		String zauthor = zlfbook.getShowwriter()==null?"":zlfbook.getShowwriter();
		String zpublish = zlfbook.getTspress()==null?"":zlfbook.getTspress();
		String zyear = zlfbook.getTspubdate()==null?"":zlfbook.getTspubdate();
		String zisbn = zlfbook.getTsisbn()==null?"":zlfbook.getTsisbn();
		String zdesc = zlfbook.getRemark_c()==null?"":zlfbook.getRemark_c();
		
		title.setText(ztitle);
		author.setText(author1 + zauthor);
		from.setText(publish + zpublish+","+zyear);
		type.setText("ISBN:" +zisbn);
		content.setText(zdesc);
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
}

