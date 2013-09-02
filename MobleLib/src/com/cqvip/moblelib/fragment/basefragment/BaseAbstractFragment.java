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

	protected CustomProgressDialog customProgressDialog;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	   // mImageFetcher.addImageCache(cacheParams);
	    //mImageFetcher.setImageFadeIn(false);
		 mQueue = Volley.newRequestQueue(getActivity());
		 customProgressDialog = CustomProgressDialog.createDialog(getActivity());
		 
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if(customProgressDialog!=null){
			customProgressDialog.dismiss();
			customProgressDialog=null;
		}
	}
}
