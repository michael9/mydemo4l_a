package com.cqvip.moblelib.view;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;


public class PinchableImageView extends ImageView {
	static final int NONE = 0;
	// �϶���
	static final int DRAG = 1;
	// ������
	static final int ZOOM = 2;
	// �Ŵ�ing
	static final int BIGGER = 3;
	// ��Сing
	static final int SMALLER = 4;
	// �ر����Ŷ���
	static final int OPENSCALE = 1;
	// �ر��ƶ�����
	static final int OPENTRANS = 2;
	// ��ǰ���¼�
	private int mode = NONE;
	// ���������
	private float beforeLenght;
	// ���������
	private float afterLenght;
	// ���ŵı��� X Y���������ֵ Խ�����ŵ�Խ��
	private float scale = 0.06f;
	/* �����϶� ���� */
	private int screenW;
	private int screenH;
	private int start_x;
	private int start_y;
	private int stop_x;
	private int stop_y;
	private TranslateAnimation trans;
	/* Bitmap�Ŀ�� */
	private int bmWidth;
	private int bmHeight;
	// �������߽�Ķ���
	private Bitmap bitmap;
	private float maxScale = 2.0f;
	private float minScale = 1.0f;
	// ��¼��ʼ��ȣ��������Żص�
	private int startWidth = 0;
	private float piovtX = 0.5f;
	private float piovtY = 0.5f;
	// Ĭ�Ͽ������ж���
	private int AnimSwitch = OPENSCALE | OPENTRANS;
	
	private int ViewHeight;

	public int getViewHeight() {
		return ViewHeight;
	}

	/**
	 * ���캯��
	 * 
	 * @param context
	 *            ���������
	 * @param w
	 *            �����Ŀ�
	 * @param h
	 *            �����ĸ�
	 */
	public PinchableImageView(Context context) {
		super(context);
	}

	public void setWH(int w, int h){
		screenW = w;
		screenH = h;
	}
	
	 public PinchableImageView(Context context, AttributeSet attrs) {
		 super(context,attrs);
	    }
	    
	    public PinchableImageView(Context context, AttributeSet attrs, int defStyle) {
	    	super(context,attrs,defStyle);
	    }
	
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		if(bm!=null){
		// ����startWidth
		startWidth = 0;
		bmWidth = bm.getWidth();
		bmHeight = bm.getHeight();
		if (bitmap != null && !bitmap.isRecycled())
			bitmap.recycle();
		bitmap = bm;
		}
	}

	/**
	 * �ͷ�ImageView��Bitmap
	 */
	public void recycle() {
		setImageBitmap(null);
		if (bitmap != null && !bitmap.isRecycled())
			bitmap.recycle();
	}

	/**
	 * ���������ľ���
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private float[] center;

	/**
	 * �������������ĵ�
	 */
	private float[] centerPostion(MotionEvent event) {
		float[] center = new float[2];
		float x = event.getX(0);
		float y = event.getY(0);
		float x1 = event.getX(1);
		float y1 = event.getY(1);
		/* x,y�ֱ�ľ��� */
		center[0] = Math.abs((x1 - x) / 2);
		center[1] = Math.abs((y1 - y) / 2);
		center[0] = Math.max(x, x1) - center[0];
		center[1] = Math.max(y, y1) - center[1];
		return center;
	}

	/**
	 * ����ImageView��С������ʾ�����ݴ�С
	 */
	public void setRect() {
		float scale = Math.min((float) getWidth() / (float) bmWidth,
				(float) getHeight() / (float) bmHeight);
		int w = (int) ((float) bmWidth * scale) + 1;
		int h = (int) ((float) bmHeight * scale) + 1;
		// int t=(screenH-h)/2;
		// int l=(screenW-w)/2;
		int t = getTop();
		int l = getLeft();
		Log.i("setRect", l+"--"+t+"--"+w+"--"+h+"    "+getWidth()+"--"+getHeight());
		layout(l, t, l + w, t + h);
	}

	/**
	 * ��������ƶ��ص�
	 * 
	 * @param disX
	 *            X��ƫ��
	 * @param disY
	 *            Y��ƫ��
	 */
	public void Rebound(int disX, int disY) {
		this.layout(getLeft() + disX, getTop() + disY, getLeft() + disX
				+ getWidth(), getTop() + disY + getHeight());
		if ((AnimSwitch & OPENTRANS) == 0)
			return;
		trans = new TranslateAnimation(-disX, 0, -disY, 0);
		trans.setInterpolator(new AccelerateInterpolator());
		trans.setDuration(300);
		this.startAnimation(trans);
	}

	/**
	 * ����������Żص�
	 */
	public boolean ReScale() {
		float scaleX = 1f;
		float scaleY = 1f;
		int width = getWidth();
		int height = getHeight();
		int l, t, r, b;
		if (center == null)
			return false;
		if (getWidth() > startWidth * maxScale) {
			while (getWidth() > startWidth * maxScale) {
				l = this.getLeft() + (int) (center[0] * this.getWidth());
				t = this.getTop() + (int) (center[1] * this.getHeight());
				r = this.getRight()
						- (int) ((scale - center[0]) * this.getWidth());
				b = this.getBottom()
						- (int) ((scale - center[1]) * this.getHeight());
				this.setFrame(l, t, r, b);
			}
			scaleX = (float) width / (float) getWidth();
			scaleY = (float) height / (float) getHeight();
		}
		if (getWidth() < startWidth * minScale) {
			while (getWidth() < startWidth * minScale) {
				l = this.getLeft() - (int) (center[0] * this.getWidth());
				t = this.getTop() - (int) (center[1] * this.getHeight());
				r = this.getRight()
						+ (int) ((scale - center[0]) * this.getWidth());
				b = this.getBottom()
						+ (int) ((scale - center[1]) * this.getHeight());
				this.setFrame(l, t, r, b);
			}
			scaleX = (float) width / (float) getWidth();
			scaleY = (float) height / (float) getHeight();
		}

		if (scaleX == 1f && scaleY == 1f)
			return false;
		if ((AnimSwitch & OPENSCALE) == 0) {
			setRect();
			onRebound();
			return true;
		}
		ScaleAnimation scaleanim = new ScaleAnimation(scaleX, 1f, scaleY, 1f,
				ScaleAnimation.RELATIVE_TO_SELF, piovtX,
				ScaleAnimation.RELATIVE_TO_SELF, piovtY);
		scaleanim.setDuration(300);
		scaleanim.setInterpolator(new AccelerateInterpolator());
		scaleanim.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation paramAnimation) {
			}

			@Override
			public void onAnimationRepeat(Animation paramAnimation) {
			}

			@Override
			public void onAnimationEnd(Animation paramAnimation) {
				setRect();
				onRebound();
			}

		});
		this.startAnimation(scaleanim);
		return true;
	}

	/**
	 * ������Χ�ص�
	 */
	private void onRebound() {
		/* �ж��Ƿ񳬳���Χ ������ */
		int disX = 0, disY = 0;
		if (getHeight() < screenH) {
			disY = (screenH - getHeight()) / 2 - getTop();
		}
		if (getWidth() < screenW) {
			disX = (screenW - getWidth()) / 2 - getLeft();
		}
		if (getHeight() >= screenH) {
			if (getTop() > 0)
				disY = -getTop();
			if (getBottom() < screenH)
				disY = screenH - getBottom();
		}
		if (getWidth() >= screenW) {
			if (getLeft() > 0)
				disX = -getLeft();
			if (getRight() < screenW)
				disX = screenW - getRight();
		}
		// ��ʼ�ص�
		Rebound(disX, disY);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
		if (startWidth == 0) {
			startWidth = right - left;
			setRect();
			if(ViewHeight==0){
				ViewHeight=getHeight();
				screenH=ViewHeight;
				}
			Log.i("pinchable", ""+screenH);
			AnimSwitch = 0;
			onRebound();
			AnimSwitch = OPENSCALE | OPENTRANS;
		}
	}

	/**
	 * ������..
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			mode = DRAG;
			stop_x = (int) event.getRawX();
			stop_y = (int) event.getRawY();
			start_x = (int) event.getX();
			start_y = stop_y - this.getTop();
			if (event.getPointerCount() == 2)
				beforeLenght = spacing(event);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			/** �����������ڼ����������ĵ�λ�� **/
			center = centerPostion(event);
			piovtX = center[0] / getWidth();
			piovtY = center[1] / getHeight();

			center[0] = (center[0] / getWidth()) * scale;
			center[1] = (center[1] / getHeight()) * scale;
			if (spacing(event) > 10f) {
				mode = ZOOM;
				beforeLenght = spacing(event);
			}
			break;
		case MotionEvent.ACTION_UP:
			mode = NONE;
			setRect();
			/* �ж��Ƿ񳬹����Ž��� */
			if (!ReScale())
				onRebound();
			break;
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			/* �����϶� */
			if (mode == DRAG) {
				this.setPosition(stop_x - start_x, stop_y - start_y, stop_x
						+ this.getWidth() - start_x,
						stop_y - start_y + this.getHeight());
				stop_x = (int) event.getRawX();
				stop_y = (int) event.getRawY();
			}
			/* �������� */
			else if (mode == ZOOM) {
				if (spacing(event) > 10f) {
					afterLenght = spacing(event);
					float gapLenght = afterLenght - beforeLenght;
					if (gapLenght == 0) {
						break;
					} else if (Math.abs(gapLenght) > 5f) {
						if (gapLenght > 0) {
							this.setScale(scale, BIGGER);
						} else {
							this.setScale(scale, SMALLER);
						}
						beforeLenght = afterLenght;
					}
				}
			}
			break;
		}
		return true;
	}

	/**
	 * ʵ�ִ�������
	 */
	private void setScale(float temp, int flag) {
		int l = 0, t = 0, r = 0, b = 0;
		if (flag == BIGGER) {
			l = this.getLeft() - (int) (center[0] * this.getWidth());
			t = this.getTop() - (int) (center[1] * this.getHeight());
			r = this.getRight() + (int) ((scale - center[0]) * this.getWidth());
			b = this.getBottom()
					+ (int) ((scale - center[1]) * this.getHeight());
		} else if (flag == SMALLER) {
			l = this.getLeft() + (int) (center[0] * this.getWidth());
			t = this.getTop() + (int) (center[1] * this.getHeight());
			r = this.getRight() - (int) ((scale - center[0]) * this.getWidth());
			b = this.getBottom()
					- (int) ((scale - center[1]) * this.getHeight());
		}
		this.setFrame(l, t, r, b);
	}

	/**
	 * ʵ�ִ����϶�
	 */
	private void setPosition(int left, int top, int right, int bottom) {
		this.layout(left, top, right, bottom);
	}
}