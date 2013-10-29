package com.cqvip.moblelib.view;

import com.cqvip.moblelib.longgang.R;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;

public class FlipImageView extends ImageView implements View.OnClickListener,
		Animation.AnimationListener {

	private static final Interpolator[] fInterpolators = new Interpolator[] {
			new DecelerateInterpolator(), 
			new AccelerateInterpolator(),
			new AccelerateDecelerateInterpolator(),
			new BounceInterpolator(),
			new OvershootInterpolator(), 
			new AnticipateOvershootInterpolator() };
	private int duration = 1400;

	private boolean mIsFlipped;
	private boolean mIsFlipping;

	private Drawable mDrawable;

//	private Drawable mFlippedDrawable;

	private FlipAnimator mAnimation;

	public FlipImageView(Context context) {
		super(context);
		init(context, null, 0);
	}

	public FlipImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs, 0);
	}

	public FlipImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs, defStyle);
	}

	private void init(Context context, AttributeSet attrs, int defStyle) {
//		mFlippedDrawable = getResources().getDrawable(
//				R.drawable.readerinfocard2);
		mDrawable = getDrawable();

		mAnimation = new FlipAnimator();
		mAnimation.setAnimationListener(this);
		mAnimation.setInterpolator(fInterpolators[1]);
		mAnimation.setDuration(duration);

		setOnClickListener(this);

//		setImageDrawable(mIsFlipped ? mFlippedDrawable : mDrawable);

	}

	public void toggleFlip() {
//		mAnimation.setToDrawable(mIsFlipped ? mDrawable : mFlippedDrawable);
		startAnimation(mAnimation);
		mIsFlipped = !mIsFlipped;
	}

	@Override
	public void onClick(View v) {
		if(!mIsFlipping)
		toggleFlip();
	}

	@Override
	public void onAnimationStart(Animation animation) {
		mIsFlipping = true;
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		mIsFlipping = false;
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
	}

	/**
	 * Animation part All credits goes to coomar
	 */
	public class FlipAnimator extends Animation {

		private Camera camera;

		private Drawable toDrawable;

		private float centerX;

		private float centerY;

		private boolean visibilitySwapped;

		public void setToDrawable(Drawable to) {
			toDrawable = to;
			visibilitySwapped = false;
		}

		public FlipAnimator() {
			setFillAfter(true);
		}

		@Override
		public void initialize(int width, int height, int parentWidth,
				int parentHeight) {
			super.initialize(width, height, parentWidth, parentHeight);
			camera = new Camera();
			this.centerX = width / 2;
			this.centerY = height / 2;
		}

		@Override
		protected void applyTransformation(float interpolatedTime,
				Transformation t) {
			// Angle around the y-axis of the rotation at the given time. It is
			// calculated both in radians and in the equivalent degrees.
			final double radians = Math.PI * interpolatedTime;
			float degrees = (float) (180.0 * radians / Math.PI);

			// Once we reach the midpoint in the animation, we need to hide the
			// source view and show the destination view. We also need to change
			// the angle by 180 degrees so that the destination does not come in
			// flipped around. This is the main problem with SDK sample, it does
			// not
			// do this.
			Log.e("applyTransformation", "applyTransformation");
			if (interpolatedTime >= 0.5f) {
				degrees -= 180.f;
				if (!visibilitySwapped) {
					Log.e("applyTransformation", "visibilitySwapped");
					setImageDrawable(toDrawable);
					visibilitySwapped = true;
				}
			}

			final Matrix matrix = t.getMatrix();

			camera.save();
			camera.translate(0.0f, 0.0f, (float) (150.0 * Math.sin(radians)));
			camera.rotateY(degrees);
			camera.getMatrix(matrix);
			camera.restore();

			matrix.preTranslate(-centerX, -centerY);
			matrix.postTranslate(centerX, centerY);
		}
	}
}
