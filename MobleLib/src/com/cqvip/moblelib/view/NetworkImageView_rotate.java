/**
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cqvip.moblelib.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cqvip.moblelib.bate1.R;
import com.cqvip.moblelib.utils.Rotate3dAnimation;

/**
 * Handles fetching an image from a URL as well as the life-cycle of the
 * associated request.
 */
public class NetworkImageView_rotate extends ImageView {
	/** The URL of the network image to load */
	private String mUrl;

	/**
	 * Resource ID of the image to be used as a placeholder until the network
	 * image is loaded.
	 */
	private int mDefaultImageId;

	/**
	 * Resource ID of the image to be used if the network response fails.
	 */
	private int mErrorImageId;

	/** Local copy of the ImageLoader. */
	private ImageLoader mImageLoader;

	/** Current ImageContainer. (either in-flight or finished) */
	private ImageContainer mImageContainer;

	private ViewGroup mContainer;
	private ImageView readerinfo_f_lay, readerinfo_b_lay;
	private boolean isrotate = false;

	public void setIsrotate(boolean isrotate) {
		this.isrotate = isrotate;
	}

	public NetworkImageView_rotate(Context context) {
		this(context, null);
	}

	public NetworkImageView_rotate(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public NetworkImageView_rotate(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Sets URL of the image that should be loaded into this view. Note that
	 * calling this will immediately either set the cached image (if available)
	 * or the default image specified by
	 * {@link NetworkImageView_rotate#setDefaultImageResId(int)} on the view.
	 * 
	 * NOTE: If applicable,
	 * {@link NetworkImageView_rotate#setDefaultImageResId(int)} and
	 * {@link NetworkImageView_rotate#setErrorImageResId(int)} should be called
	 * prior to calling this function.
	 * 
	 * @param url
	 *            The URL that should be loaded into this ImageView.
	 * @param imageLoader
	 *            ImageLoader that will be used to make the request.
	 */
	public void setImageUrl(String url, ImageLoader imageLoader) {
		mUrl = url;
		mImageLoader = imageLoader;
		// The URL has potentially changed. See if we need to load it.
		loadImageIfNecessary(false);
	}

	/**
	 * Sets the default image resource ID to be used for this view until the
	 * attempt to load it completes.
	 */
	public void setDefaultImageResId(int defaultImage) {
		mDefaultImageId = defaultImage;
	}

	/**
	 * Sets the error image resource ID to be used for this view in the event
	 * that the image requested fails to load.
	 */
	public void setErrorImageResId(int errorImage) {
		mErrorImageId = errorImage;
	}

	/**
	 * Loads the image for the view if it isn't already loaded.
	 * 
	 * @param isInLayoutPass
	 *            True if this was invoked from a layout pass, false otherwise.
	 */
	private void loadImageIfNecessary(final boolean isInLayoutPass) {
		int width = getWidth();
		int height = getHeight();

		boolean isFullyWrapContent = getLayoutParams() != null
				&& getLayoutParams().height == LayoutParams.WRAP_CONTENT
				&& getLayoutParams().width == LayoutParams.WRAP_CONTENT;
		// if the view's bounds aren't known yet, and this is not a
		// wrap-content/wrap-content
		// view, hold off on loading the image.
		if (width == 0 && height == 0 && !isFullyWrapContent) {
			return;
		}

		// if the URL to be loaded in this view is empty, cancel any old
		// requests and clear the
		// currently loaded image.
		if (TextUtils.isEmpty(mUrl)) {
			if (mImageContainer != null) {
				mImageContainer.cancelRequest();
				mImageContainer = null;
			}
			// setImageBitmap(null);
			return;
		}

		// if there was an old request in this view, check if it needs to be
		// canceled.
		if (mImageContainer != null && mImageContainer.getRequestUrl() != null) {
			if (mImageContainer.getRequestUrl().equals(mUrl)) {
				// if the request is from the same URL, return.
				return;
			} else {
				// if there is a pre-existing request, cancel it if it's
				// fetching a different URL.
				mImageContainer.cancelRequest();
				setImageBitmap(null);
			}
		}

		// The pre-existing content of this view didn't match the current URL.
		// Load the new image
		// from the network.
		ImageContainer newContainer = mImageLoader.get(mUrl,
				new ImageListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						if (mErrorImageId != 0) {
							setImageResource(mErrorImageId);
						}
					}

					@Override
					public void onResponse(final ImageContainer response,
							boolean isImmediate) {
						// If this was an immediate response that was delivered
						// inside of a layout
						// pass do not set the image immediately as it will
						// trigger a requestLayout
						// inside of a layout. Instead, defer setting the image
						// by posting back to
						// the main thread.
						if (isImmediate && isInLayoutPass) {
							post(new Runnable() {
								@Override
								public void run() {
									onResponse(response, false);
								}
							});
							return;
						}

						if (response.getBitmap() != null) {
							 Log.i("NetworkImageView_rotate",
							 "response.getBitmap()"+isrotate);
							if (isrotate) {
								mContainer = (ViewGroup) getParent();
								if (mContainer != null) {
									readerinfo_f_lay = (ImageView) mContainer
											.findViewById(R.id.defaut_book_img);
								}
								readerinfo_b_lay = NetworkImageView_rotate.this;
								applyRotation(0, 0, 90);
								isrotate=false;
							}
							setImageBitmap(response.getBitmap());

						} else if (mDefaultImageId != 0) {
							setImageResource(mDefaultImageId);
						}
					}
				});

		// update the ImageContainer to be the new bitmap container.
		mImageContainer = newContainer;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		loadImageIfNecessary(true);
	}

	@Override
	protected void onDetachedFromWindow() {
		if (mImageContainer != null) {
			// If the view was bound to an image request, cancel it and clear
			// out the image from the view.
			mImageContainer.cancelRequest();
			setImageBitmap(null);
			// also clear out the container so we can reload the image if
			// necessary.
			mImageContainer = null;
		}
		this.clearAnimation();
		super.onDetachedFromWindow();
	}

	@Override
	protected void drawableStateChanged() {
		super.drawableStateChanged();
		invalidate();
	}

	private void applyRotation(int position, float start, float end) {
		// Find the center of the container
		final float centerX, centerY;
		if (mContainer != null && readerinfo_f_lay != null) {
			centerX = mContainer.getWidth() / 2.0f;
			centerY = mContainer.getHeight() / 2.0f;
		} else {
			centerX = getWidth() / 2.0f;
			centerY = getHeight() / 2.0f;
		}

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, 0.0f, true);
		rotation.setDuration(200);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new LinearInterpolator());
		rotation.setAnimationListener(new DisplayNextView(position));

			readerinfo_f_lay.startAnimation(rotation);
	}

	/**
	 * This class listens for the end of the first half of the animation. It
	 * then posts a new action that effectively swaps the views when the
	 * container is rotated 90 degrees and thus invisible.
	 */
	private final class DisplayNextView implements Animation.AnimationListener {
		private final int mPosition;

		private DisplayNextView(int position) {
			mPosition = position;
		}

		public void onAnimationStart(Animation animation) {
		}

		public void onAnimationEnd(Animation animation) {
			if (!(readerinfo_b_lay.getVisibility()==View.VISIBLE)){
				readerinfo_b_lay.post(new SwapViews(mPosition));
			}
		}

		public void onAnimationRepeat(Animation animation) {
		}
	}

	/**
	 * This class is responsible for swapping the views and start the second
	 * half of the animation.
	 */
	private final class SwapViews implements Runnable {
		private final int mPosition;

		public SwapViews(int position) {
			mPosition = position;
		}

		public void run() {
			final float centerX, centerY;
			if (mContainer != null && readerinfo_f_lay != null) {
				centerX = mContainer.getWidth() / 2.0f;
				centerY = mContainer.getHeight() / 2.0f;
			} else {
				centerX = getWidth() / 2.0f;
				centerY = getHeight() / 2.0f;
			}
			Rotate3dAnimation rotation;

			if (mPosition > -1) {
				if (readerinfo_f_lay != null)
					readerinfo_f_lay.setVisibility(View.GONE);
				readerinfo_b_lay.setVisibility(View.VISIBLE);
				readerinfo_b_lay.requestFocus();

				rotation = new Rotate3dAnimation(-90, 0, centerX, centerY,
						0.0f, false);
			} else {
				readerinfo_b_lay.setVisibility(View.GONE);
				if (readerinfo_f_lay != null) {
					readerinfo_f_lay.setVisibility(View.VISIBLE);
					readerinfo_f_lay.requestFocus();
				}
				rotation = new Rotate3dAnimation(90, 0, centerX, centerY, 0.0f,
						false);
			}

			rotation.setDuration(200);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new LinearInterpolator());
	
				readerinfo_b_lay.startAnimation(rotation);
		}
	}
}
