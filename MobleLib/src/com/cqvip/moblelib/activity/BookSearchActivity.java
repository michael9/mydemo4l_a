package com.cqvip.moblelib.activity;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqvip.moblelib.ahcm.R;
import com.cqvip.moblelib.scan.CaptureActivity;

/**
 * <p>
 * 文件名称: BookSearchActivity.java
 * 文件描述: 馆藏查询
 * 版权所有: 版权所有(C)2013-2020
 * 公          司: 重庆维普咨询有限公司
 * 内容摘要: 包含条件搜索，分类查询
 * 其他说明:
 * 完成日期： 201年5月10日
 * 修改记录: 
 * </p>
 * 
 * @author LHP,LJ
 */
public class BookSearchActivity extends BaseActivity implements  OnClickListener {
	
	private Context context;
	private ImageView scan_iv;
	private EditText edit_search;
	private ImageView icon_search_down;//按钮
	private View searchLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_search1);
		context = this;
		icon_search_down = (ImageView) findViewById(R.id.iv_search_down_arrow);
		icon_search_down.setVisibility(View.GONE);
		edit_search = (EditText) findViewById(R.id.et_search_keyword);
		searchLayout = findViewById(R.id.search_bar_layout);
		searchLayout.setOnClickListener(this);
		TextView title = (TextView)findViewById(R.id.txt_header);
		title.setText(R.string.main_search);

		ImageView back = (ImageView)findViewById(R.id.img_back_header);
		scan_iv=(ImageView)findViewById(R.id.scan_iv);
		//hideinputmethod();
		
		edit_search.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				Intent intent=new Intent(BookSearchActivity.this, ResultOnSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		scan_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(BookSearchActivity.this, CaptureActivity.class);
				startActivityForResult(intent, 100);
			}
		});
	}


	private void hideinputmethod() {
		if (android.os.Build.VERSION.SDK_INT <= 10) { 
			edit_search.setInputType(InputType.TYPE_NULL);
			} else {
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
			Class<EditText> cls = EditText.class;
			Method setSoftInputShownOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
			setSoftInputShownOnFocus.setAccessible(true);
			setSoftInputShownOnFocus.invoke(edit_search, false);
			} catch (Exception e) {
			}
			}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.search_bar_layout:
			Intent intent=new Intent(BookSearchActivity.this, ResultOnSearchActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		
	}	
}
