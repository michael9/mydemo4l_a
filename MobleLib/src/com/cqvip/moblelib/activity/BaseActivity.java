package com.cqvip.moblelib.activity;

import com.cqvip.moblelib.view.mylinearlayout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.widget.Toast;

public class BaseActivity extends Activity {
	private GestureDetector mGestureDetector;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this,
				new MyGestrueListener(this));
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return mGestureDetector.onTouchEvent(ev);

	}

	class MyGestrueListener extends SimpleOnGestureListener {
		private Context mContext;

		MyGestrueListener(Context context) {
			mContext = context;
		}

		private int verticalMinDistance = 50;
		private int minVelocity = 0;

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.e("onFling", "onFling");
			if (e2.getX() - e1.getX() > verticalMinDistance
					&& Math.abs(velocityX) > minVelocity) {
				finish();
				Log.e("onFling", "finish");
				return true;
			}
			return false;
		}

	}

}
