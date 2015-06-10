package com.cqvip.moblelib.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cqvip.moblelib.sm.R;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.EpubDetail;

public class EpubAdapter extends BaseAdapter {
	private Context context;
	private List<EpubDetail> lists;
	private ImageLoader mImageLoader;
	private LayoutInflater layoutInflater;

	public EpubAdapter(Context context) {
		this.context = context;
		layoutInflater = LayoutInflater.from(context);
	}

	public EpubAdapter(Context context, List<EpubDetail> lists) {
		this.context = context;
		this.lists = lists;
		layoutInflater = LayoutInflater.from(context);
	}

	public EpubAdapter(Context context, List<EpubDetail> lists, ImageLoader imageLoader) {
		this.context = context;
		this.lists = lists;
		layoutInflater = LayoutInflater.from(context);
        this.mImageLoader=imageLoader;
	}

	public List<EpubDetail> getLists() {
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
	 * ���Ӹ�������
	 * 
	 * @param moreStatus
	 */
	public void addMoreData(List<EpubDetail> moreStatus) {
		this.lists.addAll(moreStatus);// �����������ӵ�ԭ�м���
		this.notifyDataSetChanged();
	}

//	static class ViewHolder {
//
//		TextView title;// ����
////		TextView author;// ����
////		TextView publisher;// ��Դ
////		TextView u_abstract;// ���
//		NetworkImageView  img;// ʱ��ͼƬ �����޸�
//		// Button btn_comment,btn_item_result_search_share,favorite;
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		 if(convertView == null)
			 {    
			         convertView = layoutInflater.inflate(R.layout.epub_item, null);
			   }
		 
		 TextView epub_item_name=(TextView)convertView.findViewById(R.id.epub_item_name);
		 NetworkImageView  epub_item_img=(NetworkImageView)convertView.findViewById(R.id.epub_item_img);
		 
		 final EpubDetail book = this.lists.get(position);
		 epub_item_name.setText( book.getTitle());
		 epub_item_img.setImageUrl(book.getImgurl(),mImageLoader);
		return convertView;
	}

}