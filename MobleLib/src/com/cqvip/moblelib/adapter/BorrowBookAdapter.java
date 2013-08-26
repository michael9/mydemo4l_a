package com.cqvip.moblelib.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.activity.ActivityDlg;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.utils.Tool;

public class BorrowBookAdapter extends BaseAdapter{

	private Context context;
	private List<BorrowBook> lists;
	private RequestQueue mQueue;
	private Response.Listener<String> createRenewSuccessListener;
	private Response.ErrorListener createMyReqErrorListener;
	public BorrowBookAdapter(Context context){
		this.context = context;
	}
	public BorrowBookAdapter(Context context,List<BorrowBook> lists){
		this.context = context;
		this.lists = lists;
	}
	public BorrowBookAdapter(Context context,List<BorrowBook> lists,RequestQueue mQueue,
			Response.Listener<String> createRenewSuccessListener,Response.ErrorListener createMyReqErrorListener){
		this.context = context;
		this.lists = lists;
		this.mQueue = mQueue;
		this.createRenewSuccessListener = createRenewSuccessListener;
		this.createMyReqErrorListener = createMyReqErrorListener;
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
			holder.renew = (TextView) convertView.findViewById(R.id.re_brrenew_txt);
		
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		   String barcode = context.getResources().getString(R.string.item_barcode);
		   String callno = context.getResources().getString(R.string.item_callno);
		   String borrowtime =  context.getResources().getString(R.string.item_brtime);
		   String returntime = context.getResources().getString(R.string.item_retime);
		   String price = context.getResources().getString(R.string.item_price);
			final BorrowBook book = lists.get(position);
	        holder.title.setText(book.getTitle());
	        holder.barcode.setText(barcode+book.getBarcode());
	        holder.callno.setText(callno+book.getCallno());
	        holder.price.setText(price+book.getPrice());
	        holder.borrowtime.setText(borrowtime+book.getLoandate());
	        holder.renew.setTag(position);
	        if(book.getRenew()!=0){
	        holder.returntime.setText(returntime+book.getReturndate()+context.getResources().getString(R.string.alreadyrenew));
	        holder.renew.setTextColor(context.getResources().getColor(R.color.txt_red));
	        }else{
	        	holder.returntime.setText(returntime+book.getReturndate());
	        	holder.renew.setTextColor(context.getResources().getColor(R.color.green_light));
	        }
	        //判断是否续借过
//	        if(book.getRenew()!=0){
//	        	holder.renew.setVisibility(View.GONE);
//	        }else{
//	        	if(holder.renew.getVisibility()==View.GONE){
//	        		holder.renew.setVisibility(View.VISIBLE);
//	        	}
	        		holder.renew.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							//发送续借请求
							int p=(Integer)v.getTag();
							if(lists.get(p).getRenew()==0){								
						   //续借
						    StringRequest myReq = new StringRequest(Method.POST,GlobleData.SERVER_URL+"/library/user/renew.aspx",
                                         createRenewSuccessListener,
                                         createMyReqErrorListener) {
							     protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
							         Map<String, String> params = new HashMap<String, String>();
							         params.put("userind", GlobleData.readerid);
							         params.put("barcode", book.getBarcode());
							         Log.i("mobile",GlobleData.readerid+","+book.getBarcode());
							         return params;
							     }; 
							 };
 							mQueue.add(myReq);
 							mQueue.start();
							Tool.ShowMessages(context,context.getString(R.string.beginrenew));
							}
							else
							{
								Intent intent=new Intent(context, ActivityDlg.class);
								intent.putExtra("ACTIONID", 0);
								intent.putExtra("MSGBODY", "该本图书已经续借过了。\r\n请注意到期归还。\r\n谢谢！");
								intent.putExtra("BTN_CANCEL", 0);
								context.startActivity(intent);
							}
						}
					});
		return convertView;
	}



}
