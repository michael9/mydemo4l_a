package com.cqvip.moblelib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.model.Reader;

public class ReaderInfoAdapter extends BaseAdapter{
	private Context context;
	private String[] values;
	private String[] attrs;
	public ReaderInfoAdapter(Context context,String[] strs,String[] values){
		this.context = context;
		this.values=values;
		this.attrs=strs;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return attrs.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			convertView = LayoutInflater.from(context).inflate(R.layout.readerinfo_item,null);
		}
		TextView tv1=(TextView) convertView.findViewById(R.id.tv1);
		TextView tv2=(TextView) convertView.findViewById(R.id.tv2);
		String temp=values[position];
		if(temp.equals("null")||temp.equals("")){
			temp="无";
		}
		tv1.setText(attrs[position]);
		tv2.setText(temp);
		return convertView;
	}

}
