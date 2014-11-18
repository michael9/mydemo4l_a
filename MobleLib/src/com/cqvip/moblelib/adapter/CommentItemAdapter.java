package com.cqvip.moblelib.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.model.Comment;
import com.cqvip.utils.Tool;

public class CommentItemAdapter extends BaseAdapter{

	private Context context;
	private List<Comment> lists;
	public CommentItemAdapter(Context context){
		this.context = context;
	}
	public CommentItemAdapter(Context context,List<Comment> comments){
		this.context = context;
		this.lists = comments;
	}
	public List<Comment> getLists(){
		return lists;
	}
	/**
	 * �ײ���ఴť������+1
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
	 * ���������ײ��ĸ�ఴť������-2
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
	 * ���Ӹ�����
	 * @param moreStatus
	 */
	public void addMoreData(List<Comment> moreStatus)
	{
		this.lists.addAll(moreStatus);//����������ӵ�ԭ�м���
		this.notifyDataSetChanged();
	}

	static class ViewHolder{
		ImageView icon;
		TextView name;
		TextView date;
		TextView content;
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		//���
		if (position == this.getCount() - 1) {
			convertView = LayoutInflater.from(context).inflate(R.layout.moreitemsview, null);
			return convertView;
		}
		if(convertView==null||convertView.findViewById(R.id.linemore) != null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_comment, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.commentItemPortrait);
			holder.name = (TextView) convertView.findViewById(R.id.tvcommentItemName);
			holder.content = (TextView) convertView.findViewById(R.id.tvcommentItemContent);
			holder.date = (TextView) convertView.findViewById(R.id.tvcommentItemDate);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		    Comment comment = lists.get(position);
	        holder.name.setText(comment.getNickname());
	        holder.content.setText(comment.getContents());
	        holder.icon.setBackgroundResource(R.drawable.portrait);
	        holder.date.setText(Tool.GetTime(comment.getCommenttime()));
	        //����
		return convertView;
	}
}
