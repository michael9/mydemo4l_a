package com.cqvip.moblelib.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.view.NetworkImageView_rotate;

public class AdvancedBookAdapter extends BaseAdapter {
	private Context context;
	private List<ShortBook> lists;
	private ImageLoader mImageLoader;
	Animation operatingAnim;
	
	public AdvancedBookAdapter(Context context){
		this.context = context;
	}
	public AdvancedBookAdapter(Context context,List<ShortBook> lists){
		this.context = context;
		this.lists = lists;
	}
	public AdvancedBookAdapter(Context context,List<ShortBook> lists,ImageLoader imageLoader){
		this.context = context;
		this.lists = lists;
		this.mImageLoader=imageLoader;
	  	operatingAnim = AnimationUtils.loadAnimation(context, R.anim.loadingrotate);
	  	LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
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
			NetworkImageView_rotate img;//时间图片 不用修改
			
			}
	  private int rotate_position=-1;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder ;
		if(convertView==null||convertView.findViewById(R.id.linemore) != null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_advanced_book, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.ad_booktitle_txt);
			holder.img = (NetworkImageView_rotate) convertView.findViewById(R.id.loaded_book_img);
			 convertView.findViewById(R.id.defaut_book_img).startAnimation(operatingAnim);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		    ShortBook book = lists.get(position);
	        holder.title.setText(book.getMessage());
	        
//	        ImageLoader mImageLoader = new ImageLoader(mQueue, new BitmapCache());
//			ImageListener listener = ImageLoader.getImageListener(holder.img,
//					R.drawable.defaut_book, R.drawable.defaut_book);
//			mImageLoader.get(book.getDate(), listener);
        	if(position>rotate_position){
        		holder.img.setIsrotate(true);
        		//Log.i("AdvancedBookAdapter", "rotate_position");
        	}
        	Log.i("AdvancedBookAdapter", rotate_position+"rotate_position+positon"+position);
	        rotate_position=position>rotate_position?position:rotate_position;  
	        
			String url=book.getDate();
	        if(!TextUtils.isEmpty(url)){
	        	holder.img.setImageUrl(url, mImageLoader);
	        } else {
	            holder.img.setImageResource(R.drawable.defaut_book);
	        }
		return convertView;
	}

}
