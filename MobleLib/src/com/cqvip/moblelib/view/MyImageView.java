package com.cqvip.moblelib.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.cqvip.moblelib.R;

public class MyImageView extends ScaleImageView
{
	Context context;
	private ScaleAnimation press;
	private ScaleAnimation up;
	boolean bool;
	MyScrollView myScrollView;
	public MyImageView(Context context)
	{
		super(context);
		init(context);
	}

	public MyImageView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		init(context);
	}
	
	public MyImageView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context){
		this.context=context;
	      this.press = new ScaleAnimation(1.0F, 0.95F, 1.0F, 0.95F, 1, 0.5F, 1, 0.5F);
	      this.press.setFillAfter(true);
	      this.press.setDuration(200L);
	      this.up = new ScaleAnimation(0.95F, 1.0F, 0.95F, 1.0F, 1, 0.5F, 1, 0.5F);
	      this.up.setFillAfter(true);
	      this.up.setDuration(200L);
	      myScrollView=MyScrollView.myScrollView;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i("MyImageView_dispatchTouchEvent", "ACTION_DOWN");
			break;
		case MotionEvent.ACTION_UP:
			Log.i("MyImageView_dispatchTouchEvent", "ACTION_UP");
			break;
		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}
	
	public void doup(){
		if(bool){
			startAnimation(up);
			bool=false;
			}
	}
	int x,y;
	@Override
	public boolean onTouchEvent(MotionEvent event)//这个方法如果 true 则整个Activity 的 onTouchEvent() 不会被系统回调
	{
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			Log.i("MyImageView_onTouchEvent", "ACTION_DOWN");
			bool=true;
			x=(int)event.getX();
			y=(int)event.getY();
			startAnimation(press);
			myScrollView.setImageView(this);
			break;
		case MotionEvent.ACTION_MOVE:
			Log.i("MyImageView_onTouchEvent", "ACTION_MOVE"+Math.abs((int)event.getX()-x)+"--"+Math.abs((int)event.getY()-y));
			if(bool&&(Math.abs((int)event.getX()-x)>10||Math.abs((int)event.getY()-y)>10)){
			startAnimation(up);
			bool=false;
			}
			break;
		case MotionEvent.ACTION_UP:
			Log.i("MyImageView_onTouchEvent", "ACTION_UP");
			if(bool){
				startAnimation(up);
				bool=false;
				}
			break;
		default:
			break;
		}
		Log.i("MyImageView_onTouchEvent", ""+super.onTouchEvent(event));
		return true;     
	}
		
}

