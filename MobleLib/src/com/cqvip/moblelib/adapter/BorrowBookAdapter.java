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
	 * �ײ����ఴť������+1
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
	 * ����������ײ��ĸ��ఴť������-2
	 */
	@Override
	public long getItemId(int position) {
		
			return position;
	}
	/**
	 * ���Ӹ�������
	 * @param moreStatus
	 */
	public void addMoreData(List<BorrowBook> moreStatus)
	{
		this.lists.addAll(moreStatus);//�����������ӵ�ԭ�м���
		this.notifyDataSetChanged();
	}
	  static class ViewHolder{
			
		
			TextView title;//����
			TextView barcode;//�����
			TextView callno;//�����
			TextView borrowtime;//����ʱ��
			TextView returntime;//����ʱ��
			TextView renew;//����
			TextView price;//�۸�
			
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
