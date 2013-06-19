package com.cqvip.moblelib.activity;

import java.util.Timer;

import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;

import com.cqvip.moblelib.R;

public class WelcomeActivity extends Activity {

    ImageView welcomimg=null;
    AnimationDrawable animator = null;
    Timer timer_sys_check;
    int n=0;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case 1:
               n++;
               if(n==5)
                   animator.start();     
               if(n>24)
               {
                   finish();
//               overridePendingTransition(R.anim.activty_fade,
//                       R.anim.activty_fade_out_immediately);
               }
                break;
            }
        }
    };
    
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.welcome);
		welcomimg=(ImageView)findViewById(R.id.welcome_img);
		welcomimg.setBackgroundResource(R.anim.welcome_anim);
		animator = (AnimationDrawable) welcomimg.getBackground();
	
	}
	
	@Override
	protected void onResume() {
	    // TODO Auto-generated method stub
	    super.onResume();	    	    
        timer_sys_check=new Timer();
        timer_sys_check.schedule(new Page_check_task(), 0,100);  
	}

}
