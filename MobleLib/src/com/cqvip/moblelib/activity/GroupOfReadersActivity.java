package com.cqvip.moblelib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookAdapter;

/**
 * <p>
 * 文件名称: GroupOfReadersActivity.java 文件描述: 书友圈 版权所有: 版权所有(C)2013-2020 公 司:
 * 重庆维普咨询有限公司 内容摘要: 其他说明: 完成日期： 201年5月10日 修改记录:
 * </p>
 * 
 * @author LHP,LJ
 */
public class GroupOfReadersActivity extends BaseActivity implements OnClickListener{
	private LinearLayout mycomments_ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_group_of_readers);
		mycomments_ll=(LinearLayout) findViewById(R.id.mycomments_ll);
		mycomments_ll.setOnClickListener(this);
		TextView title = (TextView) findViewById(R.id.txt_header);
		title.setText(R.string.main_order);
		ImageView back = (ImageView) findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.mycomments_ll:
			Intent intent=new Intent(GroupOfReadersActivity.this, MyCommentByBooktypeActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

}
