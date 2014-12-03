package com.cqvip.moblelib.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cqvip.moblelib.ahcm.R;

public class PopupMenu  {
	
	private String[] groups;
	private  PopupWindow popupWindow;
	private  PoupWindowAdapter groupAdapter;
	private  ListView lv_group;
	private Context context;
	
	public PopupMenu(Context context){
		this.context = context;
	}
	public PopupMenu(Context context,String[] groups){
		this.context = context;
		this.groups = groups;
		this.popupWindow = creatPopWindow(groups);
		
	}
	
	 private PopupWindow creatPopWindow(String[] items) {
		 PopupWindow pw;
			LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.popup_window_list, null);
			//view.getBackground().setAlpha(200);
			//view.findViewById(1);
			 lv_group = (ListView) view.findViewById(R.id.lvGroup);
			 groupAdapter = new PoupWindowAdapter(context, items);
			lv_group.setAdapter(groupAdapter);
			pw = new PopupWindow(view,context.getResources().getDimensionPixelSize(R.dimen.popwind_width), WindowManager.LayoutParams.WRAP_CONTENT);
			return pw;
	}
	public boolean isShowing(){
		if(popupWindow!=null&&popupWindow.isShowing()){
		return true;
		}
		return false;
	} 
	 
	public void  closeWindow(){
		if(isShowing()){
			popupWindow.dismiss();
		}
		
	}
	 
	public  PopupWindow getPopupWindow() {
		return popupWindow;
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
	 * 闁哄嫬澧介妵锟�	 * 
	 * @param parent
	 */
	public void showWindow(View parent) {
		if (popupWindow == null) {
			LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layoutInflater.inflate(R.layout.popup_window_list, null);
			//view.getBackground().setAlpha(200);
			//view.findViewById(1);
			 lv_group = (ListView) view.findViewById(R.id.lvGroup);
			 groupAdapter = new PoupWindowAdapter(context, groups);
			lv_group.setAdapter(groupAdapter);
			popupWindow = new PopupWindow(view,context.getResources().getDimensionPixelSize(R.dimen.popwind_width), WindowManager.LayoutParams.WRAP_CONTENT);
		}		
		//groupAdapter.notifyDataSetChanged();
		popupWindow.setOutsideTouchable(true);
		popupWindow.showAsDropDown(parent, 0, 0);
//		int xPos = windowManager.getDefaultDisplay().getWidth() / 2
//				- popupWindow.getWidth() / 2;
		/*System.out.println(xPos);
		System.out.println(parent.getWidth());
		xPos = xPos - parent.getWidth(); 
		System.out.println(xPos);*/
		if(mMyItemClickListener!=null){
			mMyItemClickListener.onMyItemClick(lv_group,popupWindow);
		}
	}
}
