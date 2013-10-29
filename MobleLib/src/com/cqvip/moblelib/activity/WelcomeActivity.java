package com.cqvip.moblelib.activity;

import java.util.Timer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;

import com.cqvip.moblelib.longgang.R;

public class WelcomeActivity extends Activity {

//	private ImageView welcomimg = null;
//	private AnimationDrawable animator = null;
	private Timer timer_sys_check;
//	private int n = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
            	   timer_sys_check.cancel();
            	   finish();
            	   overridePendingTransition(R.anim.welcome_slide_fade_in, R.anim.welcome_slide_fade_out);
                   startHelperActivity();
                break;
            }
       
        }
    };
    
	void startHelperActivity() {
		// 读取SharedPreferences中需要的数据

		SharedPreferences preferences = getSharedPreferences("count",
				MODE_PRIVATE);

		int count = preferences.getInt("count", 0);

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导页面

		if (count == 0) {

			Editor editor = preferences.edit();
			// 存入数据
			editor.putInt("count", ++count);
			// 提交修改
			editor.commit();

			Intent intent = new Intent();
			intent.setClass(WelcomeActivity.this, HelperActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.welcome_slide_fade_in, R.anim.welcome_slide_fade_out);
		}
	}

	class Page_check_task extends java.util.TimerTask {
		@Override
		public void run() {
			Message ms = new Message();
			ms.what = 1;
			handler.sendMessage(ms);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		timer_sys_check = new Timer();
		timer_sys_check.schedule(new Page_check_task(), 1000);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//overridePendingTransition(0, R.anim.welcome_slide_fade_out);
	}

}
