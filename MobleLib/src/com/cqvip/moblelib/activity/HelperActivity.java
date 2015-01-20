package com.cqvip.moblelib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cqvip.moblelib.sm.R;
import com.cqvip.moblelib.view.ScrollLayout;
import com.cqvip.moblelib.view.listener.OnViewChangeListener;

public class HelperActivity extends Activity implements OnViewChangeListener {

	private ScrollLayout mScrollLayout;
	private ImageView[] imgs;
	private int count;
	private int currentItem;
	private Button startBtn;
	private RelativeLayout mainRLayout;
	private LinearLayout pointLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.helper);
		initView();
	}

	private void initView() {
		mScrollLayout = (ScrollLayout) findViewById(R.id.ScrollLayout);
		pointLayout = (LinearLayout) findViewById(R.id.llayout);
		mainRLayout = (RelativeLayout) findViewById(R.id.main_rl);
//		startBtn = (Button) findViewById(R.id.startBtn);
//		startBtn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				switch (v.getId()) {
//				case R.id.startBtn:
//					Intent intent = new Intent(HelperActivity.this, MainMenuActivity.class);
//					HelperActivity.this.startActivity(intent);
//					HelperActivity.this.finish();
//					break;
//				}
//
//			}
//		});
		count = mScrollLayout.getChildCount();
		imgs = new ImageView[count];
		for (int i = 0; i < count; i++) {
			imgs[i] = (ImageView) pointLayout.getChildAt(i);
			imgs[i].setEnabled(true);
			imgs[i].setTag(i);
		}
		currentItem = 0;
		imgs[currentItem].setEnabled(false);
		mScrollLayout.SetOnViewChangeListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.welcome, menu);
		return true;
	}

	@Override
	public void OnViewChange(int position) {
		setcurrentPoint(position);
	}

	private void setcurrentPoint(int position) {
		if (position < 0 || position > count - 1 || currentItem == position) {
			return;
		}
		imgs[currentItem].setEnabled(true);
		imgs[position].setEnabled(false);
		currentItem = position;
	}

}
