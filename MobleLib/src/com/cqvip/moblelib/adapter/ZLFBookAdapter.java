package com.cqvip.moblelib.adapter;

import java.util.List;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cqvip.dao.GetterSetter;
import com.cqvip.moblelib.xxu.R;
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
	private int type;

	public ZLFBookAdapter(Context context) {
		this.context = context;
	}

	public ZLFBookAdapter(Context context, List<ZLFBook> lists) {
		this.context = context;
		this.lists = lists;
	}

	public ZLFBookAdapter(Context context, List<ZLFBook> lists, ImageLoader imageLoader,int type) {
		this.context = context;
		this.lists = lists;
		this.mImageLoader=imageLoader;
		this.type = type;
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
					R.layout.item_zlfbook_search, null);
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
		
		
		ZLFBook book = lists.get(position);
		holder.title.setText(book.getTitle_c());
		String describe = context.getResources().getString(
				R.string.item_describe);
		holder.u_abstract.setText(describe + book.getRemark_c());
		String author = context.getResources().getString(R.string.item_author);
		switch (type){
		case 1:
			String publish = context.getResources()
			.getString(R.string.item_publish);
			holder.publisher.setText(publish + book.getTspress()+","+book.getTspubdate());
			holder.author.setText(author + book.getShowwriter());
			break;
		case 2://学位论文
			String acadmic = context.getResources().getString(R.string.zlf_academic);
			holder.publisher.setText(acadmic + book.getShoworgan()+","+book.getYears());
			holder.author.setText(author + book.getShowwriter());
			break;
		case 3:
			holder.author.setText(author + book.getShowwriter());
			String conference = context.getResources().getString(R.string.zlf_conference);
			holder.publisher.setText(conference + book.getMedia_c()+","+book.getYears());
			break;
		case 4://专利
			String patent = context.getResources().getString(R.string.zlf_patent);
			String author_patent = context.getResources().getString(R.string.zlf_author_patent);
			String zlmaintype ="";
			if(!TextUtils.isEmpty(book.getZlmaintype())){
				zlmaintype ="["+book.getZlmaintype()+"]";
			}
			holder.publisher.setText(patent+zlmaintype+book.getShoworgan()+","+book.getZlapplicationdata());
			holder.author.setText(author_patent + book.getShowwriter());
			break;
		case 5://标准
			String standard = context.getResources().getString(R.string.zlf_standard);
			holder.publisher.setText(standard + "["+book.getBzmaintype()+"]"+"["+book.getBzstatus()+"]"+book.getShoworgan()+book.getBzpubdate());
			holder.author.setVisibility(View.GONE);
			break;
		case 6:
			String achivement = context.getResources().getString(R.string.zlf_achivment);
			String author_achivement = context.getResources().getString(R.string.zlf_author_achivment);
			holder.publisher.setText(achivement + book.getCgcontactunit()+","+book.getYears());
			if(!TextUtils.isEmpty(book.getShowwriter())){
			holder.author.setText(author_achivement+book.getShowwriter());
			}else{
				holder.author.setVisibility(View.GONE);
			}
			break;
			default:
				break;
		}
		
//		String url=book.getTscoverimage();
//        if(!TextUtils.isEmpty(url)){
//        	holder.img.setImageUrl(url, mImageLoader);
//        } else {
//            holder.img.setImageResource(R.drawable.defaut_book);
//        }
		holder.img.setVisibility(View.GONE);
		return convertView;
	}
	
	
	
	
	
	
	

}
