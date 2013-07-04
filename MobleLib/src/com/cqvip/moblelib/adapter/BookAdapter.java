package com.cqvip.moblelib.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.activity.CommentActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;

/**
 * 
 * @author luojiang
 *
 */
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
	public List<Book> getLists(){
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
//			TextView u_abstract;//���
			TextView isbn;
			
			Button btn_comment,btn_item_result_search_share,favorite;
			
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
			convertView=LayoutInflater.from(context).inflate(R.layout.item_result_search, null);
			holder = new ViewHolder();
			
			holder.title = (TextView) convertView.findViewById(R.id.re_name_txt);
			holder.author = (TextView) convertView.findViewById(R.id.re_author_txt);
			holder.publisher = (TextView) convertView.findViewById(R.id.re_addr_txt);
			holder.publishyear = (TextView) convertView.findViewById(R.id.re_time_txt);
			holder.img = (ImageView) convertView.findViewById(R.id.re_book_img);
//			holder.u_abstract = (TextView) convertView.findViewById(R.id.re_hot_txt);
			holder.isbn=(TextView) convertView.findViewById(R.id.re_hot_txt);
			holder.btn_item_result_search_share=(Button)convertView.findViewById(R.id.btn_item_result_search_share);
			holder.favorite = (Button)convertView.findViewById(R.id.btn_item_result_search_collect);
			holder.btn_comment = (Button)convertView.findViewById(R.id.btn_comment);
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
//	        holder.u_abstract.setText(describe+book.getU_abstract());
	        holder.isbn.setText("ISBN:"+book.getIsbn());
	        //ͼƬ
	        holder.img.setBackgroundResource(R.drawable.defaut_book);
	        //����
	        holder.btn_item_result_search_share.setTag(position);
	        holder.btn_item_result_search_share.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
				int pos=(Integer)v.getTag();
				Intent intent=new Intent(Intent.ACTION_SEND);    
				intent.setType("image/*");  
				intent.putExtra(Intent.EXTRA_SUBJECT, "ͼ�����");    				
				intent.putExtra(Intent.EXTRA_TEXT, ("�������:"+(lists.get(pos)).getTitle()+
						"\r\n����:"+(lists.get(pos)).getAuthor()+
						"\r\n������:"+(lists.get(pos)).getPublisher()+
						"\r\n��������:"+(lists.get(pos)).getPublishyear()+
						"\r\nISBN:"+(lists.get(pos)).getIsbn()));    
				intent.putExtra(Intent.EXTRA_STREAM, Uri.decode("http://www.szlglib.com.cn/images/logo.jpg")); //����ͼƬ"http://www.szlglib.com.cn/images/logo.jpg"
				context.startActivity(Intent.createChooser(intent, "����"));  
				
				}
			});
		//����
	        holder.btn_comment.setTag(position);
	        holder.btn_comment.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {		
					int pos=(Integer)v.getTag();
					Book book =lists.get(pos);
					if(book!=null){
						Intent intent=new Intent(context, CommentActivity.class);						
						Bundle bundle = new Bundle();
						bundle.putSerializable("book", book);
						intent.putExtra("detaiinfo", bundle);
						context.startActivity(intent);
					}
				}
			});
	      //�ղ�
	        holder.favorite .setTag(position);
	        holder.favorite .setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {		
					int pos=(Integer)v.getTag();
					Book book =lists.get(pos);
					if(book!=null){
						HashMap<String, String> map = new HashMap<String, String>();
						map.put("libid", GlobleData.LIBIRY_ID);
						map.put("vipuserid", GlobleData.cqvipid);
						Log.i("�ղ�",  GlobleData.cqvipid);
						map.put("keyid", book.getCallno());
						Log.i("keyid", book.getCallno());
						map.put("typeid", ""+GlobleData.BOOK_SZ_TYPE);
						ManagerService.addNewTask(new Task(Task.TASK_LIB_FAVOR, map));
					}
				}
			});
	        
		return convertView;
	}

}
