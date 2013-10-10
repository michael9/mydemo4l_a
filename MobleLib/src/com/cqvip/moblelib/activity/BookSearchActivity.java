package com.cqvip.moblelib.activity;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqvip.moblelib.nanshan.R;
import com.cqvip.moblelib.scan.CaptureActivity;
import com.cqvip.moblelib.view.mylinearlayout;

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
public class BookSearchActivity extends BaseActivity {
	
	private Context context;
	private ImageView scan_iv;
	private EditText editText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_search1);
		context = this;
		ImageView back = (ImageView)findViewById(R.id.return_iv);
		ImageView search = (ImageView)findViewById(R.id.search_seach_btn);
		scan_iv=(ImageView)findViewById(R.id.scan_iv);
		editText = (EditText)findViewById(R.id.search_et);
		hideinputmethod();
		
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(BookSearchActivity.this, ResultOnSearchActivity.class);
				startActivity(intent);
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
		
		editText.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
					Intent intent = new Intent(context,ResultOnSearchActivity.class) ;
					startActivity(intent);
					finish();
				}
			});
		
	}


	private void hideinputmethod() {
		if (android.os.Build.VERSION.SDK_INT <= 10) { 
			editText.setInputType(InputType.TYPE_NULL);
			} else {
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
			Class<EditText> cls = EditText.class;
			Method setSoftInputShownOnFocus = cls.getMethod("setSoftInputShownOnFocus", boolean.class);
			setSoftInputShownOnFocus.setAccessible(true);
			setSoftInputShownOnFocus.invoke(editText, false);
			} catch (Exception e) {}
			}
	}	
}
