package com.cqvip.moblelib.activity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.mobelib.imgutils.ImageCache.ImageCacheParams;
import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.activity.BaseActivity.MyGestrueListener;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.view.CustomProgressDialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.GestureDetector.SimpleOnGestureListener;


public class BaseFragmentImageActivity  extends FragmentActivity{
	
	
	protected ImageFetcher mImageFetcher;
	protected RequestQueue mQueue;
	protected CustomProgressDialog customProgressDialog;
	protected GestureDetector mGestureDetector;
	protected boolean isLeftFragment=true;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		customProgressDialog=CustomProgressDialog.createDialog(this);
		mGestureDetector = new GestureDetector(this,
				new MyGestrueListener(this));
		
		ImageCacheParams cacheParams = new ImageCacheParams(this, GlobleData.IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.125f); // Set memory cache to 12.5% of app memory
		mImageFetcher = new ImageFetcher(this, getResources().getDimensionPixelSize(R.dimen.bookicon_width),
				   getResources().getDimensionPixelSize(R.dimen.bookicon_height));
	    mImageFetcher.setLoadingImage(R.drawable.defaut_book);
	    //mImageFetcher.addImageCache(getSupportFragmentManager(), cacheParams);
	    mImageFetcher.addImageCache(this, cacheParams);
	    mImageFetcher.setImageFadeIn(false);
	    mQueue = Volley.newRequestQueue(this);
	    Log.i("BaseActivity", getClass().getSimpleName());
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(customProgressDialog==null)
		customProgressDialog=CustomProgressDialog.createDialog(this);
	}
	
	@Override
	protected void onResume() {
        super.onResume();
        mImageFetcher.setExitTasksEarly(false);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mImageFetcher.setPauseWork(false);
        mImageFetcher.setExitTasksEarly(true);
        mImageFetcher.flushCache();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageFetcher.closeCache();
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
			if (isLeftFragment&&Math.abs(velocityX) > minVelocitx
					&& Math.abs(velocityX) > 1.5* Math.abs(velocityY) 
					&& Math.abs(e2.getY() - e1.getY())/Math.abs(e2.getX() - e1.getX()) <0.36//�Ƕ�<20��
					&&velocityX>0
					) {
				finish();
				Log.e("onFling", "finish");
				return true;
			}
			return false;
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
}
