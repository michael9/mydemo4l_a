package com.cqvip.moblelib.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.model.Book;
import com.cqvip.utils.BitmapCache;

/**
 * 
 * @author luojiang
 * 
 */
public class BookAdapter extends BaseAdapter {
	private Context context;
	private List<Book> lists;
	private List<ImageLoader>illist;
	private RequestQueue mQueue;

	public BookAdapter(Context context) {
		this.context = context;
	}

	public BookAdapter(Context context, List<Book> lists) {
		this.context = context;
		this.lists = lists;
	}

	public BookAdapter(Context context, List<Book> lists, RequestQueue mQueue) {
		this.context = context;
		this.lists = lists;
		illist=new ArrayList<ImageLoader>();
		for(Book mb:lists)
		{
			illist.add(new ImageLoader(mQueue, new BitmapCache()));
		}
		this.mQueue = mQueue;
	}

	public List<Book> getLists() {
		return lists;
	}

	/**
	 * �ײ����ఴť������+1
	 */
	@Override
	public int getCount() {
		if (lists != null) {

			return lists.size() + 1;
		}
		return 1;
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	/**
	 * ����������ײ��ĸ��ఴť������-2
	 */
	@Override
	public long getItemId(int position) {
		if ((this.getCount() - 1) > 0 && position < (this.getCount() - 1)) {
			return position;
		} else {
			return -2;
		}
	}

	/**
	 * ���Ӹ�������
	 * 
	 * @param moreStatus
	 */
	public void addMoreData(List<Book> moreStatus) {
		this.lists.addAll(moreStatus);// �����������ӵ�ԭ�м���
		this.notifyDataSetChanged();
	}

	static class ViewHolder {
		TextView title;// ����
		TextView author;// ����
		TextView publisher;// ������
		TextView publishyear;// ����ʱ��
		ImageView img;// ʱ��ͼƬ �����޸�
		// TextView u_page;//ҳ��
		TextView u_abstract;// ���
		TextView isbn;
		// Button btn_comment,btn_item_result_search_share,favorite;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		// ����
		if (position == this.getCount() - 1) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.moreitemsview, null);
			return convertView;
		}
		if (convertView == null
				|| convertView.findViewById(R.id.linemore) != null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_result_search, null);
			holder = new ViewHolder();

			holder.title = (TextView) convertView
					.findViewById(R.id.re_name_txt);
			holder.author = (TextView) convertView
					.findViewById(R.id.re_author_txt);
			holder.publisher = (TextView) convertView
					.findViewById(R.id.re_addr_txt);
			holder.publishyear = (TextView) convertView
					.findViewById(R.id.re_time_txt);
			holder.img = (ImageView) convertView.findViewById(R.id.re_book_img);
			holder.u_abstract = (TextView) convertView
					.findViewById(R.id.txt_abst);
			holder.isbn = (TextView) convertView.findViewById(R.id.re_hot_txt);
			// holder.btn_item_result_search_share=(Button)convertView.findViewById(R.id.btn_item_result_search_share);
			// holder.favorite =
			// (Button)convertView.findViewById(R.id.btn_item_result_search_collect);
			// holder.btn_comment =
			// (Button)convertView.findViewById(R.id.btn_comment);
			convertView.setTag(holder);
			// Log.i("BookAdapter", "convertView==null");
		} else {
			holder = (ViewHolder) convertView.getTag();
			// Log.i("BookAdapter", "convertView==null_else");
		}
		// Log.i("BookAdapter", "getview");
		String author = context.getResources().getString(R.string.item_author);
		String publish = context.getResources()
				.getString(R.string.item_publish);
		String time = context.getResources().getString(R.string.item_time);
		String describe = context.getResources().getString(
				R.string.item_describe);
		Book book = lists.get(position);
		holder.title.setText(book.getTitle());
		holder.author.setText(author + book.getAuthor());
		holder.publisher.setText(publish + book.getPublisher());
		holder.publishyear.setText(time + book.getPublishyear());
		holder.u_abstract.setText(describe + book.getU_abstract());
		holder.isbn.setText("ISBN:" + book.getIsbn());

		if (lists.size()>illist.size()) {
			illist.add( new ImageLoader(mQueue, new BitmapCache()));
		}
		// if(book.listener==null)
		// {
		// book.listener=book. mImageLoader .getImageListener(holder.img,
		// R.drawable.defaut_book, R.drawable.defaut_book);
		// }
		// ImageLoader mImageLoader = new ImageLoader(mQueue, new
		// BitmapCache());
		ImageListener listener = ImageLoader.getImageListener(holder.img,
				R.drawable.defaut_book, R.drawable.defaut_book);
		(illist.get(position)).get(book.getCover_path(), listener);

		// //ͼƬ
		// if(!TextUtils.isEmpty(book.getCover_path())){
		// fetch.loadImage(book.getCover_path(), holder.img);
		// final String bigimg = Tool.getBigImg(book.getCover_path());
		// holder.img.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// if(TextUtils.isEmpty(bigimg)){
		// return;
		// }
		// Intent intent = new Intent(context,BigImgActivity.class);
		// intent.putExtra("bigurl", bigimg);
		// context.startActivity(intent);
		// }
		// });
		// }else{
		// holder.img.setImageDrawable(context.getResources().getDrawable(R.drawable.defaut_book));
		// }

		// //����
		// holder.btn_item_result_search_share.setTag(position);
		// holder.btn_item_result_search_share.setOnClickListener(new
		// OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// int pos=(Integer)v.getTag();
		// Tool.bookshare(context, lists.get(pos));
		// }
		// });
		// //����
		// holder.btn_comment.setTag(position);
		// holder.btn_comment.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// int pos=(Integer)v.getTag();
		// Tool.bookbuzz(context, lists.get(pos));
		// }
		// });
		// //�ղ�
		// holder.favorite .setTag(position);
		// holder.favorite .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// int pos=(Integer)v.getTag();
		// Tool.bookfavorite(context, lists.get(pos));
		// }
		// });

		return convertView;
	}

}
