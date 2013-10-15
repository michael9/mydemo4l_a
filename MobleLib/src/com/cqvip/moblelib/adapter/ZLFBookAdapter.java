package com.cqvip.moblelib.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cqvip.dao.GetterSetter;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookAdapter.ViewHolder;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.ZLFBook;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ZLFBookAdapter extends BaseAdapter {
	private Context context;
	private List<ZLFBook> lists;
	private ImageLoader mImageLoader;

	public ZLFBookAdapter(Context context) {
		this.context = context;
	}

	public ZLFBookAdapter(Context context, List<ZLFBook> lists) {
		this.context = context;
		this.lists = lists;
	}

	public ZLFBookAdapter(Context context, List<ZLFBook> lists, ImageLoader imageLoader) {
		this.context = context;
		this.lists = lists;
		this.mImageLoader=imageLoader;
	}

	public List<ZLFBook> getLists() {
		return lists;
	}

	
	@Override
	public int getCount() {
		if (lists != null) {
			return lists.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	
	@Override
	public long getItemId(int position) {
		if (this.getCount() > 0 && position < (this.getCount())) {
			return position;
		} else {
			return 0;
		}
	}

	/**
	 * 增加更多数据
	 * 
	 * @param moreStatus
	 */
	public void addMoreData(List<ZLFBook> moreStatus) {
		this.lists.addAll(moreStatus);// 把新数据增加到原有集合
		this.notifyDataSetChanged();
	}

	static class ViewHolder {
		TextView title;// 书名
		TextView author;// 作者
		TextView publisher;// 出版社
		NetworkImageView img;// 时间图片 不用修改
		TextView u_abstract;// 简介
		// Button btn_comment,btn_item_result_search_share,favorite;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null
				|| convertView.findViewById(R.id.linemore) != null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_book_search, null);
			holder = new ViewHolder();

			holder.title = (TextView) convertView
					.findViewById(R.id.re_name_txt);
			holder.author = (TextView) convertView
					.findViewById(R.id.re_author_txt);
			holder.publisher = (TextView) convertView
					.findViewById(R.id.re_addr_txt);
			holder.img = (NetworkImageView) convertView.findViewById(R.id.re_book_img);
			holder.img.setDefaultImageResId(R.drawable.defaut_book);
			holder.img.setErrorImageResId(R.drawable.defaut_book);
			holder.u_abstract = (TextView) convertView
					.findViewById(R.id.txt_abst);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String author = context.getResources().getString(R.string.item_author);
		String publish = context.getResources()
				.getString(R.string.item_publish);
		String describe = context.getResources().getString(
				R.string.item_describe);
		ZLFBook book = lists.get(position);
		holder.title.setText(book.getTitle_c());
		holder.author.setText(author + book.getShowwriter());
		holder.publisher.setText(publish + book.getTspress()+","+book.getTspubdate());
		holder.u_abstract.setText(describe + book.getRemark_c());

		String url=book.getTscoverimage();
        if(!TextUtils.isEmpty(url)){
        	holder.img.setImageUrl(url, mImageLoader);
        } else {
            holder.img.setImageResource(R.drawable.defaut_book);
        }
		return convertView;
	}
	
	
	
	
	
	
	

}
