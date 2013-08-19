package com.cqvip.moblelib.fragment.basefragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.cqvip.mobelib.imgutils.ImageCache.ImageCacheParams;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.view.CustomProgressDialog;


public class BaseAbstractFragment extends Fragment{
	protected RequestQueue mQueue;
	protected ImageFetcher mImageFetcher;
	protected CustomProgressDialog customProgressDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ImageCacheParams cacheParams = new ImageCacheParams(getActivity(), GlobleData.IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.125f); // Set memory cache to 12.5% of app memory
		mImageFetcher = new ImageFetcher(getActivity(), getResources().getDimensionPixelSize(R.dimen.bookicon_width),
				   getResources().getDimensionPixelSize(R.dimen.bookicon_height));
	    mImageFetcher.setLoadingImage(R.drawable.defaut_book);
	    mImageFetcher.addImageCache(cacheParams);
	    mImageFetcher.setImageFadeIn(false);
		 mQueue = Volley.newRequestQueue(getActivity());
		 customProgressDialog = CustomProgressDialog.createDialog(getActivity());
		 
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
