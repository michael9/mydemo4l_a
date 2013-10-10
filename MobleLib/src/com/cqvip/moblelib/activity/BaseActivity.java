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

public class BaseActivity extends Activity {
	protected GestureDetector mGestureDetector;
	protected CustomProgressDialog customProgressDialog;
	protected RequestQueue mQueue;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGestureDetector = new GestureDetector(this,
				new MyGestrueListener(this));
		mQueue=Volley.newRequestQueue(this);
		customProgressDialog=CustomProgressDialog.createDialog(this);
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

		private int verticalMinDistance = 150;
		private int horizontalMinDistance = 200;
    	private int minVelocitx = 500;
//		private int minVelocity = 5000;

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			Log.e("onFling", "velocityY"+velocityY+"--velocityX"+velocityX);
			if (Math.abs(velocityX) > minVelocitx
					&& Math.abs(velocityX) > 3 * Math.abs(velocityY) / 2
					&& e2.getX() - e1.getX() > verticalMinDistance
					&& e2.getY() - e1.getY() < horizontalMinDistance) {
				finish();
				return true;
			}
			return false;
		}

	}

	public void onError(int a) {
		if (customProgressDialog != null && customProgressDialog.isShowing()) {
			customProgressDialog.dismiss();
		}
		if (a == 1) {// µÇÂ¼Ê§°Ü
			Tool.ShowMessages(this, getResources()
					.getString(R.string.loginfail));
		} else if (a == 2) {// ¼ÓÔØÊ§°Ü
			Tool.ShowMessages(this, getResources()
					.getString(R.string.loadfail));
		} else if (a == 3) {// Ðø½èÊ§°Ü
			Tool.ShowMessages(this, getResources()
					.getString(R.string.renewfail));
		} else if (a == 4) {// ÐÞ¸ÄÊ§°Ü
			Tool.ShowMessages(this, getResources()
					.getString(R.string.modifyfail));
		}else if (a == 5) {// ÊÕ²ØÊ§°Ü
			Tool.ShowMessages(this, getResources()
					.getString(R.string.favorfail));
		}
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
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		if (mGestureDetector.onTouchEvent(ev)) {
		//Log.e("mylinearlayout", "dispatchTouchEvent_true");
		return true;

	} else {
		boolean temp=super.dispatchTouchEvent(ev);
		//Log.e("mylinearlayout", "dispatchTouchEvent_"+temp);
		return temp;
	}
	}
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		return mGestureDetector.onTouchEvent(event);
//	}
}
