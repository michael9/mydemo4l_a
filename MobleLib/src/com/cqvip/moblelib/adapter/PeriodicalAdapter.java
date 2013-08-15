package com.cqvip.moblelib.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import com.cqvip.moblelib.adapter.PeriodicalAdapter.ViewHolder;
import com.cqvip.moblelib.model.Comment;
import com.cqvip.moblelib.model.Periodical;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;

public class PeriodicalAdapter extends BaseAdapter{

	private Context context;
	private List<Periodical> lists;
	private ImageFetcher fetch;
	public PeriodicalAdapter(Context context){
		this.context = context;
	}
	public PeriodicalAdapter(Context context,List<Periodical> periodicals){
		this.context = context;
		this.lists = periodicals;
	}
	public PeriodicalAdapter(Context context,List<Periodical> periodicals,ImageFetcher fetch){
		this.context = context;
		this.lists = periodicals;
		this.fetch = fetch;
	}
	public List<Periodical> getLists(){
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
	public void addMoreData(List<Periodical> moreStatus)
	{
		this.lists.addAll(moreStatus);//�����������ӵ�ԭ�м���
		this.notifyDataSetChanged();
	}

	static class ViewHolder{
		ImageView icon;
		TextView name;
		TextView ename;
		TextView cnno;
		TextView issn;
		
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		//����
		if (position == this.getCount() - 1) {
			convertView = LayoutInflater.from(context).inflate(R.layout.moreitemsview, null);
			return convertView;
		}
		if(convertView==null||convertView.findViewById(R.id.linemore) != null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_periodical, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.re_book_img);
			holder.name = (TextView) convertView.findViewById(R.id.re_name_txt);
			holder.ename = (TextView) convertView.findViewById(R.id.re_author_txt);
			holder.cnno = (TextView) convertView.findViewById(R.id.re_addr_txt);
			holder.issn = (TextView) convertView.findViewById(R.id.re_time_txt);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		// ͼƬ
		    Periodical periodical = lists.get(position);
		    if (!TextUtils.isEmpty(periodical.getImgurl())) {
		    	fetch.loadImage(periodical.getImgurl(), holder.icon);
		    } else {
		    	holder.icon.setImageDrawable(context.getResources().getDrawable(
		    			R.drawable.defaut_book));
		    }
	        holder.name.setText(periodical.getName());
	        holder.ename.setText(periodical.getEname());
	        holder.cnno.setText(context.getResources().getString(R.string.title_cnno)+periodical.getCnno());
	        holder.issn.setText(context.getResources().getString(R.string.title_issn)+periodical.getIssn());
	        //����
		return convertView;
	}
}