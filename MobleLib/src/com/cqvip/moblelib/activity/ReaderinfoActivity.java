package com.cqvip.moblelib.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.ReaderInfoAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Reader;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.utils.Rotate3dAnimation;
import com.cqvip.utils.Tool;

public class ReaderinfoActivity extends Activity implements
		IBookManagerActivity {

	private Context context;
	private ListView mList;
	private ViewGroup mContainer;
	private RelativeLayout readerinfo_f_lay, readerinfo_b_lay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.readerinfo);
		TextView title = (TextView) findViewById(R.id.txt_header);
		title.setText(R.string.serv_readerinfo);
		ImageView back = (ImageView) findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				// overridePendingTransition(R.anim.slide_left_in,
				// R.anim.slide_right_out);
			}
		});

		init();

		mList = (ListView) findViewById(android.R.id.list);
		mContainer = (ViewGroup) findViewById(R.id.container);

		readerinfo_f_lay = (RelativeLayout) findViewById(R.id.readerinfo_f_lay);
		readerinfo_f_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				applyRotation(0, 0, 90);
			}
		});

		readerinfo_b_lay = (RelativeLayout) findViewById(R.id.readerinfo_b_lay);
		readerinfo_b_lay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				applyRotation(-1, 0, -90);
			}
		});

		readerinfo_b_lay.setVisibility(View.GONE);

		mList.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				applyRotation(-1, 0, -90);
				return false;
			}
		});

		// Since we are caching large views, we want to keep their cache
		// between each animation
		mContainer
				.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
	}

	/**
	 * Setup a new 3D rotation on the container view.
	 * 
	 * @param position
	 *            the item that was clicked to show a picture, or -1 to show the
	 *            list
	 * @param start
	 *            the start angle at which the rotation must begin
	 * @param end
	 *            the end angle of the rotation
	 */
	private void applyRotation(int position, float start, float end) {
		// Find the center of the container
		final float centerX = mContainer.getWidth() / 2.0f;
		final float centerY = mContainer.getHeight() / 2.0f;

		// Create a new 3D rotation with the supplied parameter
		// The animation listener is used to trigger the next animation
		final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,
				centerX, centerY, 310.0f, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(new DisplayNextView(position));

		mContainer.startAnimation(rotation);
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
			mContainer.post(new SwapViews(mPosition));
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
			final float centerX = mContainer.getWidth() / 2.0f;
			final float centerY = mContainer.getHeight() / 2.0f;
			Rotate3dAnimation rotation;

			if (mPosition > -1) {
				readerinfo_f_lay.setVisibility(View.GONE);
				readerinfo_b_lay.setVisibility(View.VISIBLE);
				readerinfo_b_lay.requestFocus();

				rotation = new Rotate3dAnimation(-90, 0, centerX, centerY,
						310.0f, false);
			} else {
				readerinfo_b_lay.setVisibility(View.GONE);
				readerinfo_f_lay.setVisibility(View.VISIBLE);
				readerinfo_f_lay.requestFocus();
				rotation = new Rotate3dAnimation(90, 0, centerX, centerY,
						310.0f, false);
			}

			rotation.setDuration(500);
			rotation.setFillAfter(true);
			rotation.setInterpolator(new DecelerateInterpolator());

			mContainer.startAnimation(rotation);
		}
	}

	// 获取读者信息
	@Override
	public void init() {
		if (GlobleData.userid != null) {
			ManagerService.allActivity.add(this);
			HashMap map = new HashMap();
			map.put("userid", GlobleData.userid);
			Task tsHome;
			tsHome = new Task(Task.TASK_GET_READERINFO, map);
			ManagerService.addNewTask(tsHome);
		}
	}

	private String[] values;
	private final String[] attrs = { "姓名：", "注册日期：", "证号：", "启用日期：", "终止日期：",
			"证状态：", "电话：", "地址：" };

	@Override
	public void refresh(Object... obj) {
		Reader reader = (Reader) obj[0];
		String[] values = { reader.getName(), reader.getRegdate(),
				reader.getUsername(), reader.getCardbegdate(),
				reader.getCardenddate(), reader.getStatus(), reader.getPhone(),
				reader.getAddress() };
		this.values = values;
		mList.setAdapter(new ReaderInfoAdapter(this, attrs, values));
	}

}
