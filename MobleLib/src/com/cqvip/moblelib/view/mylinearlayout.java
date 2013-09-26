package com.cqvip.moblelib.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class mylinearlayout extends LinearLayout {
	private Activity activity;
	private GestureDetector mGestureDetector;
	private Context context;

	public mylinearlayout(Context context) {
		super(context);
		initial(context);
	}

	public mylinearlayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initial(context);
	}

	public mylinearlayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initial(context);
	}

	private void initial(Context context) {
		this.context = context;
		if (mGestureDetector == null) {
			mGestureDetector = new GestureDetector(this.context,
					new MyGestrueListener(this.context));
			Log.e("mylinearlayout", "new mGestureDetector");
		}
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	private boolean istouchleft = false;

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (mGestureDetector.onTouchEvent(ev)) {
			//Log.e("mylinearlayout", "dispatchTouchEvent_true");
			istouchleft = true;
			return true;

		} else {
			boolean temp=super.dispatchTouchEvent(ev);
			//Log.e("mylinearlayout", "dispatchTouchEvent_"+temp);
			return temp;
		}
	}

//	@Override
//	public boolean onInterceptTouchEvent(MotionEvent ev) {
//		boolean temp=super.onInterceptTouchEvent(ev);
//		Log.e("mylinearlayout", "onInterceptTouchEvent_"+temp);
//		return temp;
//	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (istouchleft) {
			istouchleft = false;
			Log.e("mylinearlayout", "onTouchEvent_true");
			return true;			
		} else
			Log.e("mylinearlayout", "onTouchEvent_false");
			return false;
	}

	class MyGestrueListener extends SimpleOnGestureListener {
		private Context mContext;

		MyGestrueListener(Context context) {
			mContext = context;
		}

		private int verticalMinDistance = 50;
		private int minVelocity = 1000;

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.e("onFling", "onFling");
			if (e2.getX() - e1.getX() > verticalMinDistance
					&& Math.abs(velocityX) > minVelocity) {
				activity.finish();
				Log.e("onFling", "finish");
				return true;
			}
			return false;
		}
	}
}
