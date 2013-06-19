package com.cqvip.moblelib.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.model.BorrowBook;

public class BorrowBookAdapter extends BaseAdapter{

	private Context context;
	private List<BorrowBook> lists;
	public BorrowBookAdapter(Context context){
		this.context = context;
	}
	public BorrowBookAdapter(Context context,List<BorrowBook> lists){
		this.context = context;
		this.lists = lists;
	}
	/**
	 * 底部更多按钮，返回+1
	 */
	@Override
	public int getCount() {
      if(lists!=null){
				return lists.size();
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
		
			return position;
	}
	/**
	 * 增加更多数据
	 * @param moreStatus
	 */
	public void addMoreData(List<BorrowBook> moreStatus)
	{
		this.lists.addAll(moreStatus);//把新数据增加到原有集合
		this.notifyDataSetChanged();
	}
	  static class ViewHolder{
			
		
			TextView title;//书名
			TextView barcode;//条码号
			TextView callno;//索书号
			TextView borrowtime;//借书时间
			TextView returntime;//还书时间
			TextView renew;//续借
			TextView price;//价格
			
			}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder ;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.item_borrow, null);
			holder = new ViewHolder();
			
			holder.title = (TextView) convertView.findViewById(R.id.br_title_txt);
			holder.barcode = (TextView) convertView.findViewById(R.id.br_code_txt);
			holder.callno = (TextView) convertView.findViewById(R.id.br_callno_txt);
			holder.borrowtime = (TextView) convertView.findViewById(R.id.br_brtime_txt);
			holder.returntime = (TextView) convertView.findViewById(R.id.br_retime_txt);
			holder.price = (TextView) convertView.findViewById(R.id.br_price_txt);
			//holder.renew = (TextView) convertView.findViewById(R.id.re_hot_txt);
		
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		   String barcode = context.getResources().getString(R.string.item_barcode);
		   String callno = context.getResources().getString(R.string.item_callno);
		   String borrowtime =  context.getResources().getString(R.string.item_brtime);
		   String returntime = context.getResources().getString(R.string.item_retime);
		   String price = context.getResources().getString(R.string.item_price);
			BorrowBook book = lists.get(position);
	        holder.title.setText(book.getTitle());
	        holder.barcode.setText(barcode+book.getBarcode());
	        holder.callno.setText(callno+book.getCallno());
	        holder.price.setText(price+book.getPrice());
	        holder.borrowtime.setText(borrowtime+book.getLoandate());
	        holder.returntime.setText(returntime+book.getReturndate());
		
		return convertView;
	}



}
