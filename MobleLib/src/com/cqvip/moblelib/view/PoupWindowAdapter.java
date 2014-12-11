package com.cqvip.moblelib.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cqvip.moblelib.ahslsd.R;

public class PoupWindowAdapter extends BaseAdapter {

	private Context context;

	private  String[] list;

	public PoupWindowAdapter(Context context, String[] list) {

		this.context = context;
		this.list = list;

	}

	@Override
	public int getCount() {
		return list.length;
	}

	@Override
	public Object getItem(int position) {

		return list[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		
		ViewHolder holder;
		if (convertView==null) {
			convertView=LayoutInflater.from(context).inflate(R.layout.popup_window_item, null);
			holder=new ViewHolder();
			
			convertView.setTag(holder);
			
			holder.groupItem=(TextView) convertView.findViewById(R.id.popup_window_Item);
			
		}
		else{
			holder=(ViewHolder) convertView.getTag();
		}
		holder.groupItem.setText(list[position]);
		
		return convertView;
	}

	static class ViewHolder {
		TextView groupItem;
	}

}
