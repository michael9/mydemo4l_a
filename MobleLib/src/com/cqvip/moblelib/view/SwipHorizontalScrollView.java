package com.cqvip.moblelib.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class SwipHorizontalScrollView extends HorizontalScrollView {
	
	private View view;
	private ImageView leftImage;
	private ImageView rightImage;
	private int windowWitdh = 0;
	private Activity mContext;
	
	public SwipHorizontalScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public SwipHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	
	public SwipHorizontalScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @param view
	 * @param leftImage
	 * @param rightImage
	 * @param context
	 */
	public void setSomeParam(View view, ImageView leftImage,
			ImageView rightImage, Activity context) {
		this.mContext = context;
		this.view = view;
		this.leftImage = leftImage;
		this.rightImage = rightImage;
		DisplayMetrics dm = new DisplayMetrics();
		this.mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
		windowWitdh = dm.widthPixels;
	}
//	/**
//	 * shep4 ��ʾ�������������ߵļ�ͷ
//	 */
//		public void showAndHideArrow() {
//			if (!mContext.isFinishing() && view != null) {
//				this.measure(0, 0);
//				//������ڵĿ�ȴ��ڵ���SyncHorizontalScrollView�Ŀ��  ˵��������  ����Ҫ����
//				if (windowWitdh >= this.getMeasuredWidth()) {
//					leftImage.setVisibility(View.GONE);
//					rightImage.setVisibility(View.GONE);
//				} else {
//					if (this.getLeft() == 0) {//���SyncHorizontalScrollView��ߵ�ǰ�ʹ��ڶ���
//						leftImage.setVisibility(View.GONE);
//						rightImage.setVisibility(View.VISIBLE);
//					} else if (this.getRight() == this.getMeasuredWidth()
//							- windowWitdh) {
//						leftImage.setVisibility(View.VISIBLE);
//						rightImage.setVisibility(View.GONE);
//					} else {
//						leftImage.setVisibility(View.VISIBLE);
//						rightImage.setVisibility(View.VISIBLE);
//					}
//				}
//			}
//		}
	
	
}
