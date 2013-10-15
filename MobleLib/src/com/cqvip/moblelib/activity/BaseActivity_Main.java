package com.cqvip.moblelib.activity;

import java.util.Map;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cqvip.moblelib.nanshan.R;
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

public class BaseActivity_Main extends Activity {
	protected GestureDetector mGestureDetector;
	protected CustomProgressDialog customProgressDialog;
	protected RequestQueue mQueue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this,
				new MyGestrueListener(this));
		mQueue = Volley.newRequestQueue(this);
		customProgressDialog = CustomProgressDialog.createDialog(this);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (customProgressDialog == null)
			customProgressDialog = CustomProgressDialog.createDialog(this);
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent ev) {
	// return mGestureDetector.onTouchEvent(ev);
	//
	// }

	class MyGestrueListener extends SimpleOnGestureListener {
		private Context mContext;

		MyGestrueListener(Context context) {
			mContext = context;
		}

		private int verticalMinDistance = 150;
		private int horizontalMinDistance = 200;
		private int minVelocitx = 500;

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.e("onFling", "onFling");
			if (Math.abs(velocityX) > minVelocitx
					&& Math.abs(velocityX) > 1.5* Math.abs(velocityY) 
					&& Math.abs(e2.getY() - e1.getY())/Math.abs(e2.getX() - e1.getX()) <0.36//Ω«∂»<20∂»
					&&velocityX>0
					) {
//				finish();
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
			Tool.ShowMessages(this, getResources().getString(R.string.loadfail));
		} else if (a == 3) {// –¯ΩË ß∞‹
			Tool.ShowMessages(this, getResources()
					.getString(R.string.renewfail));
		} else if (a == 4) {// –ﬁ∏ƒ ß∞‹
			Tool.ShowMessages(this,
					getResources().getString(R.string.modifyfail));
		} else if (a == 5) {//  ’≤ÿ ß∞‹
			Tool.ShowMessages(this, getResources()
					.getString(R.string.favorfail));
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (customProgressDialog != null) {
			customProgressDialog.dismiss();
			customProgressDialog = null;
		}
	}


}
