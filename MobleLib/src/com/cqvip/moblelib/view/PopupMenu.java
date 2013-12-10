package com.cqvip.moblelib.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cqvip.moblelib.szy.R;

public class PopupMenu {
	
	private String[] groups;
	private static PopupWindow popupWindow;
	private static PoupWindowAdapter groupAdapter;
	private static ListView lv_group;
	private Context context;
	
	public PopupMenu(Context context){
		this.context = context;
	}
	
	 private onMyItemOnClickListener mMyItemClickListener;
	
	public interface onMyItemOnClickListener{
		
		  public void onMyItemClick(ListView view,PopupWindow pop);
	}
	
	  public void setonMyItemOnClickListener(onMyItemOnClickListener listener) {
		  mMyItemClickListener = listener;
	    }
	  
	  
	public  void setGroups(String[] groups) {
		this.groups = groups;
	}


	/**
	 * 鏄剧ず
	 * 
	 * @param parent
	 */
	public void showWindow(View parent) {
		// 鍔犺浇鏁版嵁
		if(groups==null){
			return;
		}
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View view = layoutInflater.inflate(R.layout.popup_window_list, null);
			//view.getBackground().setAlpha(200);
			//view.findViewById(1);
			 lv_group = (ListView) view.findViewById(R.id.lvGroup);
			
			
			 groupAdapter = new PoupWindowAdapter(context, groups);
			lv_group.setAdapter(groupAdapter);
			// 鍒涘缓涓�釜PopuWidow瀵硅薄
			popupWindow = new PopupWindow(view,context.getResources().getDimensionPixelSize(R.dimen.popwind_width), WindowManager.LayoutParams.WRAP_CONTENT);
		}
		
		//groupAdapter.notifyDataSetChanged();

		// 浣垮叾鑱氶泦
		popupWindow.setFocusable(true);
		// 璁剧疆鍏佽鍦ㄥ鐐瑰嚮娑堝け
		popupWindow.setOutsideTouchable(true);

		// 杩欎釜鏄负浜嗙偣鍑烩�杩斿洖Back鈥濅篃鑳戒娇鍏舵秷澶憋紝骞朵笖骞朵笉浼氬奖鍝嶄綘鐨勮儗鏅�
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		// 鏄剧ず鐨勪綅缃负:灞忓箷鐨勫搴︾殑涓�崐-PopupWindow鐨勯珮搴︾殑涓�崐
		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
				- popupWindow.getWidth() / 2;

		/*System.out.println(xPos);
		System.out.println(parent.getWidth());
		xPos = xPos - parent.getWidth(); 
		System.out.println(xPos);*/
		
		
		popupWindow.showAsDropDown(parent, 0, 0);

		if(mMyItemClickListener!=null){
			mMyItemClickListener.onMyItemClick(lv_group,popupWindow);
		}
		
//		lv_group.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> adapterView, View view,
//					int position, long id) {
//				    Intent intent = new Intent();
//					System.out.println("onItemClick");
//					switch(position){
//					case 0:
//						 Toast.makeText(context, "鐐瑰嚮浜�", Toast.LENGTH_SHORT).show();
//						break;
//					case 1:
//						Toast.makeText(context, "鐐瑰嚮浜�", Toast.LENGTH_SHORT).show();
//						break;
//					case 2:
//						Toast.makeText(context, "鐐瑰嚮浜�", Toast.LENGTH_SHORT).show();
//						break;
//					case 3:
//						Toast.makeText(context, "鐐瑰嚮浜�", Toast.LENGTH_SHORT).show();
//						break;
//					}
//
//				if (popupWindow != null) {
//					popupWindow.dismiss();
//				}
//			}
//		});
	}

}
