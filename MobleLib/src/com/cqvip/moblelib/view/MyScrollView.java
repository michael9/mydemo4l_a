package com.cqvip.moblelib.view;

import com.cqvip.moblelib.R;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

public class MyScrollView extends ScrollView
{
	Context context;
	MyImageView myImageView;
	public static MyScrollView myScrollView;
	public MyScrollView(Context context)
	{
		super(context);
		init(context);
	}

	public MyScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}
	
	public MyScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context){
		this.context=context;
		myScrollView=this;
	}
	public void setImageView(ImageView imageView){
		myImageView=(MyImageView) imageView;
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i("MyScrollView_dispatchTouchEvent", "ACTION_DOWN");
			if(imageView==null){
			imageView=(ImageView) findViewById(R.id.finger_iv);
			imageViewhalfwidth=imageView.getDrawable().getBounds().width()/2;
			imageViewhalfheight=imageView.getDrawable().getBounds().height()/2;
			}
//			imageView.setTranslationY((int)event.getY()-prey);
//			imageView.setTranslationX((int)event.getX()-prex);
			imageView.setPaddingRelative((int)event.getX()-imageViewhalfwidth, (int)event.getY()-imageViewhalfheight+this.getScrollY(), 0, 0);
				imageView.setVisibility(View.VISIBLE);
				imageView.invalidate();
			break;
		case MotionEvent.ACTION_UP:
			Log.i("MyScrollView_dispatchTouchEvent", "ACTION_UP");
			if(imageView!=null)
			imageView.setVisibility(View.INVISIBLE);
			if(myImageView!=null)
			myImageView.doup();
			break;
		default:
			break;
		}
		
		return super.dispatchTouchEvent(event);
	}
//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent event)   //这个方法如果返回 true 的话 两个手指移动，启动一个按下的手指的移动不能被传播出去。
//	{
//		super.onInterceptTouchEvent(event);
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			Log.i("MyScrollView_onInterceptTouchEvent", "ACTION_DOWN");
//			if(imageView==null){
//			imageView=(ImageView) findViewById(R.id.finger_iv);
//			imageViewhalfwidth=imageView.getDrawable().getBounds().width()/2;
//			imageViewhalfheight=imageView.getDrawable().getBounds().height()/2;
//			}
////			imageView.setTranslationY((int)event.getY()-prey);
////			imageView.setTranslationX((int)event.getX()-prex);
//			imageView.setPaddingRelative((int)event.getX()-imageViewhalfwidth, (int)event.getY()-imageViewhalfheight+this.getScrollY(), 0, 0);
//				imageView.setVisibility(View.VISIBLE);
//				imageView.invalidate();
////				prey=(int)event.getY();
////				prex=(int)event.getX();
//			break;
//		case MotionEvent.ACTION_UP:
//			if(imageView!=null)
//			imageView.setVisibility(View.INVISIBLE);
//			Log.i("MyScrollView_onInterceptTouchEvent", "ACTION_UP");
//		default:
//			break;
//		}
//		return false;
//	}
	
	ImageView imageView;
	FrameLayout  frameLayout;
	int prex,prey;
	int imageViewhalfwidth,imageViewhalfheight;
//	@Override
//	public boolean onTouchEvent(MotionEvent event)//这个方法如果 true 则整个Activity 的 onTouchEvent() 不会被系统回调
//	{
//		switch (event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			Log.i("MyScrollView_onTouchEvent", "ACTION_DOWN");
//			if(imageView==null){
//			imageView=(ImageView) findViewById(R.id.finger_iv);
//			imageViewhalfwidth=imageView.getDrawable().getBounds().width()/2;
//			imageViewhalfheight=imageView.getDrawable().getBounds().height()/2;
//			}
////			imageView.setTranslationY((int)event.getY()-prey);
////			imageView.setTranslationX((int)event.getX()-prex);
//			imageView.setPaddingRelative((int)event.getX()-imageViewhalfwidth, (int)event.getY()-imageViewhalfheight+this.getScrollY(), 0, 0);
//				imageView.setVisibility(View.VISIBLE);
//				imageView.invalidate();
////				prey=(int)event.getY();
////				prex=(int)event.getX();
//			break;
//		case MotionEvent.ACTION_UP:
//			if(imageView!=null)
//			imageView.setVisibility(View.INVISIBLE);
//			Log.i("MyScrollView_onTouchEvent", "ACTION_UP");
//		default:
//			break;
//		}
//		Log.i("MyScrollView_onTouchEvent", ""+super.onTouchEvent(event));
//		return super.onTouchEvent(event);     
//	}
}

