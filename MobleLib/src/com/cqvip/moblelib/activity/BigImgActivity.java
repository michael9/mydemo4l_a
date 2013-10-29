package com.cqvip.moblelib.activity;

import android.app.Activity;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.cqvip.mobelib.imgutils.ImageCache;
import com.cqvip.mobelib.imgutils.ImageFetcher;
import com.cqvip.moblelib.longgang.R;

public class BigImgActivity extends Activity implements OnTouchListener {

    private static final String IMAGE_CACHE_DIR = "images";
    public static final String EXTRA_IMAGE = "extra_image";
    private ImageFetcher mImageFetcher;
	private ImageView image;
	private String big_url;
	private ProgressBar progress;
		/** Called when the activity is first created. */

		// 放大缩小
		Matrix matrix = new Matrix();
		Matrix savedMatrix = new Matrix();

		PointF start = new PointF();
		PointF mid = new PointF();
		float oldDist;

		private ImageView imageview;

		// 模式
		static final int NONE = 0;
		static final int DRAG = 1;
		static final int ZOOM = 2;
		int mode = NONE;

		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			setContentView(R.layout.image_bigview);

			 // activity runs full screen
	        final DisplayMetrics displayMetrics = new DisplayMetrics();
	        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	        final int height = displayMetrics.heightPixels;
	        final int width = displayMetrics.widthPixels;
	
	        // For this sample we'll use half of the longest width to resize our images. As the
	        // image scaling ensures the image is larger than this, we should be left with a
	        // resolution that is appropriate for both portrait and landscape. For best image quality
	        // we shouldn't divide by 2, but this will use more memory and require a larger memory
	        // cache.
	        final int longest = (height > width ? height : width)/2;
	
	        ImageCache.ImageCacheParams cacheParams =
	                new ImageCache.ImageCacheParams(this, IMAGE_CACHE_DIR);
	        cacheParams.setMemCacheSizePercent(0.125f); // Set memory cache to 25% of app memory
	
	        // The ImageFetcher takes care of loading images into our ImageView children asynchronously
	        mImageFetcher = new ImageFetcher(this, longest);
	        mImageFetcher.addImageCache(this, cacheParams);
	        mImageFetcher.setImageFadeIn(false);
	
			progress = (ProgressBar) findViewById(R.id.ProgressBar01);
			image = (ImageView) findViewById(R.id.img_big);
			Log.i("mimgurl","bigimg"+image);
			big_url = getIntent().getStringExtra("bigurl");
			mImageFetcher.loadImage(big_url, image);
			//progress.setVisibility(View.GONE);
			image.setVisibility(View.VISIBLE);
			image.setOnTouchListener(this);

		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			ImageView myImageView = (ImageView) v;
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			// 设置拖拉模式
			case MotionEvent.ACTION_DOWN:
				matrix.set(myImageView.getImageMatrix());
				savedMatrix.set(matrix);
				start.set(event.getX(), event.getY());
				mode = DRAG;
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
				mode = NONE;
				break;

			// 设置多点触摸模式
			case MotionEvent.ACTION_POINTER_DOWN:
				oldDist = spacing(event);
				if (oldDist > 10f) {
					savedMatrix.set(matrix);
					midPoint(mid, event);
					mode = ZOOM;
				}
				break;
			// 若为DRAG模式，则点击移动图片
			case MotionEvent.ACTION_MOVE:
				if (mode == DRAG) {
					matrix.set(savedMatrix);
					matrix.postTranslate(event.getX() - start.x, event.getY() - start.y);
				}
				// 若为ZOOM模式，则点击触摸缩放
				else if (mode == ZOOM) {
					float newDist = spacing(event);
					if (newDist > 10f) {
						matrix.set(savedMatrix);
						float scale = newDist / oldDist;
						// 设置硕放比例和图片的中点位置
						matrix.postScale(scale, scale, mid.x, mid.y);
					}
				}
				break;
			}
			myImageView.setImageMatrix(matrix);
			return true;
		}

		// 计算移动距离
		private float spacing(MotionEvent event) {
			float x = event.getX(0) - event.getX(1);
			float y = event.getY(0) - event.getY(1);
			return FloatMath.sqrt(x * x + y * y);
		}

		// 计算中点位置
		private void midPoint(PointF point, MotionEvent event) {
			float x = event.getX(0) + event.getX(1);
			float y = event.getY(0) + event.getY(1);
			point.set(x / 2, y / 2);
		}
	
}
