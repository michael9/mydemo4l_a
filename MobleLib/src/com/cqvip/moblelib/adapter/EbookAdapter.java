package com.cqvip.moblelib.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.EBook;

public class EbookAdapter extends BaseAdapter{
	private Context context;
	private List<EBook> lists;
	private ImageFetcher fetch;
	public EbookAdapter(Context context){
		this.context = context;
	}
	public EbookAdapter(Context context,List<EBook> lists){
		this.context = context;
		this.lists = lists;
	}
	public EbookAdapter(Context context,List<EBook> lists,ImageFetcher fetch){
		this.context = context;
		this.lists = lists;
		this.fetch = fetch;
	}
	public List<EBook> getLists(){
		return lists;
	}
	/**
	 * 底部更多按钮，返回+1
	 */
	@Override
	public int getCount() {
      if(lists!=null){
				
				return lists.size()+1;
			}
			return 1;
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
		if((this.getCount()-1)>0&&position < (this.getCount()-1)){
			return position;
		}else{
			return -2;
		}
	}
	/**
	 * 增加更多数据
	 * @param moreStatus
	 */
	public void addMoreData(List<EBook> moreStatus)
	{
		this.lists.addAll(moreStatus);//把新数据增加到原有集合
		this.notifyDataSetChanged();
	}
	  static class ViewHolder{
			
		
			TextView title;//书名
			TextView author;//作者
			TextView publisher;//来源,时间和期数
			LinearLayout l_abst;//时间和期数
			TextView u_abstract;//简介
			ImageView img;//时间图片 不用修改
			TextView u_page;//页数
			TextView type;//格式
			
			Button btn_comment,btn_item_result_search_share,favorite;
			
			}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder ;
		final long id;
		//更多
		if (position == this.getCount() - 1) {
			convertView = LayoutInflater.from(context).inflate(R.layout.moreitemsview, null);
			return convertView;
		}
		if(convertView==null||convertView.findViewById(R.id.linemore) != null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_result_search, null);
			holder = new ViewHolder();
			
			holder.title = (TextView) convertView.findViewById(R.id.re_name_txt);
			holder.author = (TextView) convertView.findViewById(R.id.re_author_txt);
			holder.publisher = (TextView) convertView.findViewById(R.id.re_addr_txt);
			holder.u_page = (TextView) convertView.findViewById(R.id.re_time_txt);
			holder.img = (ImageView) convertView.findViewById(R.id.re_book_img);
			holder.u_abstract = (TextView) convertView.findViewById(R.id.txt_abst);
			holder.type=(TextView) convertView.findViewById(R.id.re_hot_txt);
			holder.l_abst = (LinearLayout)convertView.findViewById(R.id.ll_abstract);
			holder.btn_item_result_search_share = (Button)convertView.findViewById(R.id.btn_item_result_search_share);
		    holder.favorite = (Button)convertView.findViewById(R.id.btn_item_result_search_collect);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		   String author = context.getResources().getString(R.string.item_author);
		   String from = context.getResources().getString(R.string.ebook_orang);
		   String page =  context.getResources().getString(R.string.ebook_time);//刊号
		   String describe = context.getResources().getString(R.string.ebook_abstrac);
		   String type = context.getResources().getString(R.string.ebook_type);
		   
			final EBook book = lists.get(position);
	        holder.title.setText(book.getTitle_c());
	        holder.author.setText(author+book.getWriter());
	        holder.publisher.setText(from+book.getName_c());
	        holder.u_page.setText(page+book.getYears()+"年,"+"第"+book.getNum()+"期");
//	        holder.u_abstract.setText(describe+book.getU_abstract());
	        holder.type.setText(type+book.getPagecount()+"页,"+"PDF");
	        holder.l_abst.setVisibility(View.VISIBLE);
	        holder.u_abstract.setText(describe+book.getRemark_c());
	      //  holder.favorite.setVisibility(View.VISIBLE);
	        fetch.loadImage(book.getImgurl(), holder.img);
	        holder.btn_item_result_search_share.setTag(position);
	        holder.btn_item_result_search_share.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				int pos=(Integer)v.getTag();
				Intent intent=new Intent(Intent.ACTION_SEND);    
				intent.setType("image/*");  
				intent.putExtra(Intent.EXTRA_SUBJECT, "图书分享");    				
				intent.putExtra(Intent.EXTRA_TEXT, ("好书分享:"+(lists.get(pos)).getTitle_c()+
						"\r\n作者:"+(lists.get(pos)).getWriter()+
						"\r\n出版日期:"+(lists.get(pos)).getYears()));    
				intent.putExtra(Intent.EXTRA_STREAM, Uri.decode("http://www.szlglib.com.cn/images/logo.jpg")); //分享图片"http://www.szlglib.com.cn/images/logo.jpg"
				context.startActivity(Intent.createChooser(intent, "分享到"));  
				
				}
			});
	        //收藏
	       // holder.favorite .setTag(position);
	        holder.favorite .setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {		
					//int pos=(Integer)v.getTag();
					//Book book =lists.get(pos);
					if(book!=null){
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("libid", GlobleData.LIBIRY_ID);
						map.put("vipuserid", GlobleData.cqvipid);
						Log.i("收藏",  GlobleData.cqvipid);
						map.put("keyid", book.getLngid());
						Log.i("keyid", book.getLngid());
						map.put("typeid", ""+GlobleData.BOOK_ZK_TYPE);
						ManagerService.addNewTask(new Task(Task.TASK_EBOOK_FAVOR, map));
					}
				}
			});
		
		return convertView;
	}

}