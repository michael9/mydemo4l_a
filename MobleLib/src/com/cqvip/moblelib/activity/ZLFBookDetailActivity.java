package com.cqvip.moblelib.activity;

import java.util.Map;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
	private int zlftype; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_zlfbook_detail);
		context = this;
		//���ñ��⣬��ʾ���ذ�ť
		View v = findViewById(R.id.head_bar);
		Bundle bundle = getIntent().getBundleExtra("detaiinfo");
		zlfbook = (ZLFBook) bundle.getSerializable("book");
		zlftype = getIntent().getIntExtra("type", 1);
		
		
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

//		ImageLoader mImageLoader = new ImageLoader(mQueue, new BitmapCache(Tool.getCachSize()));
//		ImageListener listener = ImageLoader.getImageListener(img_book,
//				R.drawable.defaut_book, R.drawable.defaut_book);
//		mImageLoader.get(zlfbook.getImgurl(), listener);
		String ztitle = zlfbook.getTitle_c()==null?"":zlfbook.getTitle_c();
		String zdesc = zlfbook.getRemark_c()==null?"":zlfbook.getRemark_c();
		String zauthor = zlfbook.getShowwriter()==null?"":zlfbook.getShowwriter();
		title.setText(ztitle);//����
		content.setText(zdesc);//���
		switch (zlftype){
		case 1:
			String zpublish = zlfbook.getTspress()==null?"":zlfbook.getTspress();
			String zyear = zlfbook.getTspubdate()==null?"":zlfbook.getTspubdate();
			String zisbn = zlfbook.getTsisbn()==null?"":zlfbook.getTsisbn();
			author.setText(author1 + zauthor);
			from.setText(publish + zpublish+","+zyear);
			if(zisbn.equals("")){
				type.setVisibility(View.GONE);
			}else{
			type.setText("ISBN:" +zisbn);
			}
			break;
		case 2://ѧλ����
			author.setText(author1 + zauthor);
			String acadmic = context.getResources().getString(R.string.zlf_acdemic_organ);
			from.setText(acadmic + zlfbook.getShoworgan()+","+zlfbook.getYears());
			type.setVisibility(View.GONE);
			break;
		case 3://��������
			author.setText(author1 + zauthor);
			String conference_name = context.getResources().getString(R.string.zlf_conference_name);
			String conference_date = context.getResources().getString(R.string.zlf_conference_date);
			from.setText(conference_name + zlfbook.getMedia_c());
			type.setText(conference_date+zlfbook.getYears());
			break;
		case 4://ר��
			String author_patent = context.getResources().getString(R.string.zlf_author_patent);
			String patent_set = context.getResources().getString(R.string.zlf_author_patent_set);
			author.setText(author_patent + zauthor);
			if(!TextUtils.isEmpty(zlfbook.getShoworgan())){
			from.setText(patent_set+zlfbook.getShoworgan());
			}else{
				from.setVisibility(View.GONE);
			}
			type.setText("ר�����ͣ�"+zlfbook.getZlmaintype()+"\n"+"�����գ�"+zlfbook.getZlapplicationdata()+"\n"+"�����գ�"+zlfbook.getZlopendata());
			break;
		case 5:
			
			author.setText("��׼���ͣ�" + zlfbook.getBzmaintype());
			from.setText("��׼״̬��"+zlfbook.getBzstatus());
			type.setText("��ݵ�λ��"+zlfbook.getShoworgan()+"\n"+"�������ڣ�"+zlfbook.getBzpubdate());
			break;
		case 6:
			String author_achivement = context.getResources().getString(R.string.zlf_author_achivment);
			if(!TextUtils.isEmpty(zauthor)){
				author.setText(author_achivement + zauthor);
			}else{
				author.setVisibility(View.GONE);
			}
			from.setText("��ɵ�λ��"+zlfbook.getCgcontactunit());
			if(!TextUtils.isEmpty(zlfbook.getYears())){
			type.setText("������ݣ�"+zlfbook.getYears());
			}else{
				type.setVisibility(View.GONE);
			}
			break;
			default:
				break;
		}
		
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

