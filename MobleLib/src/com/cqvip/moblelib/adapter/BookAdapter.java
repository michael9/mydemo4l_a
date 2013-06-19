package com.cqvip.moblelib.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.model.Book;

public class BookAdapter extends BaseAdapter{
	private Context context;
	private List<Book> lists;
	public BookAdapter(Context context){
		this.context = context;
	}
	public BookAdapter(Context context,List<Book> lists){
		this.context = context;
		this.lists = lists;
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
	public void addMoreData(List<Book> moreStatus)
	{
		this.lists.addAll(moreStatus);//�����������ӵ�ԭ�м���
		this.notifyDataSetChanged();
	}
	  static class ViewHolder{
			
		
			TextView title;//����
			TextView author;//����
			TextView publisher;//������
			TextView publishyear;//����ʱ��
			ImageView img;//ʱ��ͼƬ �����޸�
			//TextView u_page;//ҳ��
			TextView u_abstract;//���
			
			}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder ;
		final long id;
		//����
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
			holder.publishyear = (TextView) convertView.findViewById(R.id.re_time_txt);
			holder.img = (ImageView) convertView.findViewById(R.id.re_book_img);
			holder.u_abstract = (TextView) convertView.findViewById(R.id.re_hot_txt);
		
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		   String author = context.getResources().getString(R.string.item_author);
		   String publish = context.getResources().getString(R.string.item_publish);
		   String time =  context.getResources().getString(R.string.item_time);
		   String describe = context.getResources().getString(R.string.item_describe);
			Book book = lists.get(position);
	        holder.title.setText(book.getTitle());
	        holder.author.setText(author+book.getAuthor());
	        holder.publisher.setText(publish+book.getPublisher());
	        holder.publishyear.setText(time+book.getPublishyear());
	        holder.u_abstract.setText(describe+book.getU_abstract());
		
		return convertView;
	}

}
