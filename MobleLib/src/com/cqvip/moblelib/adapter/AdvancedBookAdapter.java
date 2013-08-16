package com.cqvip.moblelib.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.activity.BigImgActivity;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.utils.BitmapCache;

public class AdvancedBookAdapter extends BaseAdapter {
	private Context context;
	private List<ShortBook> lists;
	private RequestQueue mQueue;
	
	public AdvancedBookAdapter(Context context){
		this.context = context;
	}
	public AdvancedBookAdapter(Context context,List<ShortBook> lists){
		this.context = context;
		this.lists = lists;
	}
	public AdvancedBookAdapter(Context context,List<ShortBook> lists,RequestQueue mQueue){
		this.context = context;
		this.lists = lists;
		this.mQueue = mQueue;
	}
	public List<ShortBook> getLists(){
		return lists;
	}
	/**
	 * 底部更多按钮，返回+1
	 */
	@Override
	public int getCount() {
      if(lists!=null){
				return lists.size();
			}
      return 0;
		}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}
	/**
	 * 如果点击到最底部的更多按钮，返回-2
	 */
	@Override
	public long getItemId(int position) {
      return position;
	}
	/**
	 * 增加更多数据
	 * @param moreStatus
	 */
	public void addMoreData(List<ShortBook> moreStatus)
	{
		this.lists.addAll(moreStatus);//把新数据增加到原有集合
		this.notifyDataSetChanged();
	}
	  static class ViewHolder{
			
		
			TextView title;//书名
			ImageView img;//时间图片 不用修改
			
			}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder ;
		if(convertView==null||convertView.findViewById(R.id.linemore) != null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_advanced_book, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.ad_booktitle_txt);
			holder.img = (ImageView) convertView.findViewById(R.id.ad_book_img);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		    ShortBook book = lists.get(position);
	        holder.title.setText(book.getMessage());
	        
	        ImageLoader mImageLoader = new ImageLoader(mQueue, new BitmapCache());
			ImageListener listener = ImageLoader.getImageListener(holder.img,
					R.drawable.defaut_book, R.drawable.defaut_book);
			mImageLoader.get(book.getDate(), listener);
		return convertView;
	}

}
