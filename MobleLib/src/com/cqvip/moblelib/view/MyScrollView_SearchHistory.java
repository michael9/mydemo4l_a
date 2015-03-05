package com.cqvip.moblelib.view;

import com.cqvip.moblelib.sychild.R;

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

public class MyScrollView_SearchHistory extends ScrollView {
	Context context;

	public MyScrollView_SearchHistory(Context context) {
		super(context);
		init(context);
	}

	public MyScrollView_SearchHistory(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MyScrollView_SearchHistory(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
	}

	// 滑动距离及坐标
	private float xDistance, yDistance, xLast, yLast;

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			if (xDistance > yDistance) {
				Log.i("MyScrollView_SearchHistory", "0");
				return false;
			}
		}

		return super.onInterceptTouchEvent(ev);
	}

}
