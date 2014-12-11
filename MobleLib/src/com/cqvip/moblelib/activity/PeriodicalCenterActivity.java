package com.cqvip.moblelib.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cqvip.moblelib.ahslsd.R;
import com.cqvip.moblelib.fragment.BranchPeriodicalFragment;
import com.cqvip.moblelib.fragment.ConcernPeriodicalFragment;

/**
 * 电子期刊中心，主页面
 * 提供关注分类，和关注期刊 功能
 * @author luojiang
 *
 */
public class PeriodicalCenterActivity extends FragmentActivity {
	 private FragmentTabHost mTabHost;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_periodical_center);
	        
	        //title
	        View v = findViewById(R.id.head_bar);
	        TextView tv = (TextView) v.findViewById(R.id.txt_concern_title);
	        tv.setVisibility(View.VISIBLE);
	        
	        tv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					startActivity(new Intent(PeriodicalCenterActivity.this,PeriodicalClassfyActivity.class));
				}
			});
	        
	      //  getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.header);
	        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
	        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

	        mTabHost.addTab(mTabHost.newTabSpec("关注分类").setIndicator("关注分类"),
	        		BranchPeriodicalFragment.class, null);
	        mTabHost.addTab(mTabHost.newTabSpec("关注期刊").setIndicator("关注期刊"),
	        		ConcernPeriodicalFragment.class, null);
	    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.periodical_center, menu);
		return true;
	}

}
