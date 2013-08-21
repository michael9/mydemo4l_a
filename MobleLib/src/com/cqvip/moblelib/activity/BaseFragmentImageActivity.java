package com.cqvip.moblelib.activity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.mobelib.imgutils.ImageCache.ImageCacheParams;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.constant.GlobleData;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class BaseFragmentImageActivity  extends FragmentActivity{
	
	
	protected ImageFetcher mImageFetcher;
	protected RequestQueue mQueue;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		ImageCacheParams cacheParams = new ImageCacheParams(this, GlobleData.IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f); // Set memory cache to 12.5% of app memory
		mImageFetcher = new ImageFetcher(this, getResources().getDimensionPixelSize(R.dimen.bookicon_width),
				   getResources().getDimensionPixelSize(R.dimen.bookicon_height));
	    mImageFetcher.setLoadingImage(R.drawable.defaut_book);
	    mImageFetcher.addImageCache(cacheParams);
	    mImageFetcher.setImageFadeIn(false);
	    mQueue = Volley.newRequestQueue(this);
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
	
	

}
