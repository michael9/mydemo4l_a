package com.cqvip.moblelib.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cqvip.moblelib.ahcm.R;
import com.cqvip.moblelib.model.BookLoc;

public class BookLocAdapter extends BaseAdapter{

	private Context context;
	private List<BookLoc> lists;
	
	public BookLocAdapter(Context context,List<BookLoc> lists){
		this.context = context;
		this.lists = lists;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_location_book, null);
			
			TextView barcode = (TextView) convertView.findViewById(R.id.loc_barcode_txt);
			TextView callno = (TextView) convertView.findViewById(R.id.loc_callno_txt);
			TextView location = (TextView) convertView.findViewById(R.id.loc_location_txt);
			TextView cirtype = (TextView) convertView.findViewById(R.id.loc_cirtype_txt);
			TextView status = (TextView) convertView.findViewById(R.id.loc_status_txt);

				barcode.setText(context.getString(R.string.item_barcode)+lists.get(position).getBarcode());
				callno.setText(context.getString(R.string.item_callno)+lists.get(position).getCallno());
				location.setText(context.getString(R.string.item_loc)+lists.get(position).getLocal());
				cirtype.setText(context.getString(R.string.item_cirtype)+lists.get(position).getCirtype());
				status.setText(context.getString(R.string.item_status)+lists.get(position).getStatus());
		}
		return convertView;
	}
	

}
