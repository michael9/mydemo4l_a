package com.cqvip.moblelib.activity;

import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.mobelib.imgutils.ImageCache.ImageCacheParams;
import com.cqvip.moblelib.bate1.R;
import com.cqvip.moblelib.constant.GlobleData;

import android.os.Bundle;
import android.widget.ImageView;

public class BaseImageActivity extends BaseActivity{

	protected ImageFetcher mImageFetcher;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//内存占用整个app1/8
		ImageCacheParams cacheParams = new ImageCacheParams(this, GlobleData.IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.125f); // Set memory cache to 12.5% of app memory
		mImageFetcher = new ImageFetcher(this, getResources().getDimensionPixelSize(R.dimen.bookicon_width),
				   getResources().getDimensionPixelSize(R.dimen.bookicon_height));
	    mImageFetcher.setLoadingImage(R.drawable.defaut_book);
	    mImageFetcher.addImageCache(cacheParams);
	    mImageFetcher.setImageFadeIn(false);
		
	}
	 @Override
	    public void onResume() {
	        super.onResume();
	        mImageFetcher.setExitTasksEarly(false);
	    }

	    @Override
	    public void onPause() {
	        super.onPause();
	        mImageFetcher.setPauseWork(false);
	        mImageFetcher.setExitTasksEarly(true);
	        mImageFetcher.flushCache();
	    }

	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        mImageFetcher.closeCache();
	    }
	

	
}
