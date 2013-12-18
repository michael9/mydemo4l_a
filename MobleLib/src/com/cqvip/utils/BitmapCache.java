package com.cqvip.utils;

import java.io.Serializable;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

public class BitmapCache  extends LruCache<String, Bitmap> implements ImageCache {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 5561733994786475350L;


	public BitmapCache(int maxsize) {
    	super(maxsize);
      
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }


    @Override
    public Bitmap getBitmap(String url) {
        return get(url);
    }


    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}
