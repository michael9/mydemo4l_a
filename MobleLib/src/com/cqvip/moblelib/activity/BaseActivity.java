package com.cqvip.moblelib.activity;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.moblelib.view.mylinearlayout;
import com.cqvip.utils.Tool;

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
	protected CustomProgressDialog customProgressDialog;

	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this,
				new MyGestrueListener(this));
		customProgressDialog=CustomProgressDialog.createDialog(this);
	}

//	@Override
//	public boolean onTouchEvent(MotionEvent ev) {
//		return mGestureDetector.onTouchEvent(ev);
//
//	}

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

	public void onError(int a) {
		if (customProgressDialog != null && customProgressDialog.isShowing()) {
			customProgressDialog.dismiss();
		}
		if (a == 1) {// µ«¬º ß∞‹
			Tool.ShowMessages(this, getResources()
					.getString(R.string.loginfail));
		} else if (a == 2) {// º”‘ÿ ß∞‹
			Tool.ShowMessages(this, getResources()
					.getString(R.string.loadfail));
		} else if (a == 3) {// –¯ΩË ß∞‹
			Tool.ShowMessages(this, getResources()
					.getString(R.string.renewfail));
		} else if (a == 4) {// –ﬁ∏ƒ ß∞‹
			Tool.ShowMessages(this, getResources()
					.getString(R.string.modifyfail));
		}else if (a == 5) {//  ’≤ÿ ß∞‹
			Tool.ShowMessages(this, getResources()
					.getString(R.string.favorfail));
		}
	}
}
