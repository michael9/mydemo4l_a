package com.cqvip.moblelib.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.Volley;
import com.cqvip.mobelib.exception.ErrorVolleyThrow;
import com.cqvip.moblelib.ahslsd.R;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;
import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends Activity {
	private GestureDetector mGestureDetector;
	protected CustomProgressDialog customProgressDialog;
	protected RequestQueue mQueue;
	public static BitmapCache cache;
	protected ErrorListener el;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this,
				new MyGestrueListener(this));
		mQueue=Volley.newRequestQueue(this);
		customProgressDialog=CustomProgressDialog.createDialog(this);
		el = new  ErrorVolleyThrow(this, customProgressDialog);
		Log.i("BaseActivity", getClass().getSimpleName());
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(customProgressDialog==null)
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
		if (a == 1) {// 
			Tool.ShowMessages(this, getResources()
					.getString(R.string.loginfail));
		} else if (a == 2) {// 
			Tool.ShowMessages(this, getResources()
					.getString(R.string.loadfail));
		} else if (a == 3) {// 
			Tool.ShowMessages(this, getResources()
					.getString(R.string.renewfail));
		} else if (a == 4) {// 
			Tool.ShowMessages(this, getResources()
					.getString(R.string.modifyfail));
		}else if (a == 5) {// 
			Tool.ShowMessages(this, getResources()
					.getString(R.string.favorfail));
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(customProgressDialog!=null){
			customProgressDialog.dismiss();
			customProgressDialog=null;
		}
	}
}
