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

import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.activity.BigImgActivity;
import com.cqvip.moblelib.model.ShortBook;

public class AdvancedBookAdapter extends BaseAdapter {
	private Context context;
	private List<ShortBook> lists;
	private ImageFetcher fetch;
	public AdvancedBookAdapter(Context context){
		this.context = context;
	}
	public AdvancedBookAdapter(Context context,List<ShortBook> lists){
		this.context = context;
		this.lists = lists;
	}
	public AdvancedBookAdapter(Context context,List<ShortBook> lists,ImageFetcher fetch){
		this.context = context;
		this.lists = lists;
		this.fetch = fetch;
	}
	public List<ShortBook> getLists(){
		return lists;
	}
	/**
	 * �ײ����ఴť������+1
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
	 * ����������ײ��ĸ��ఴť������-2
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
	 * ���Ӹ�������
	 * @param moreStatus
	 */
	public void addMoreData(List<ShortBook> moreStatus)
	{
		this.lists.addAll(moreStatus);//�����������ӵ�ԭ�м���
		this.notifyDataSetChanged();
	}
	  static class ViewHolder{
			
		
			TextView title;//����
			ImageView img;//ʱ��ͼƬ �����޸�
			
			}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder ;
		//����
		if (position == this.getCount() - 1) {
			convertView = LayoutInflater.from(context).inflate(R.layout.moreitemsview, null);
			return convertView;
		}
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
	      //ͼƬ
	        if(!TextUtils.isEmpty(book.getDate())){
	        	fetch.loadImage(book.getDate(), holder.img);
	        	final String bigimg = book.getSucesss();
	        	holder.img.setOnClickListener(new View.OnClickListener() {
	        		
	        		@Override
	        		public void onClick(View v) {
	        			if(TextUtils.isEmpty(bigimg)){
	        				return;
	        			}
	        			Intent  intent = new Intent(context,BigImgActivity.class);
	        			intent.putExtra("bigurl", bigimg);
	        			context.startActivity(intent);
	        			
	        		}
	        	});
	        }else{
	        	holder.img.setBackgroundResource(R.drawable.defaut_book);
	        }
	        //����
		return convertView;
	}

}
