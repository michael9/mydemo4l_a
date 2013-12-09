package com.cqvip.moblelib.activity;

import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.cqvip.moblelib.scan.CaptureActivity;
import com.cqvip.moblelib.szy.R;
import com.cqvip.moblelib.view.PopupMenu;
import com.cqvip.moblelib.view.PopupMenu.onMyItemOnClickListener;

/**
 * <p>
 * �ļ�����: BookSearchActivity.java
 * �ļ�����: �ݲز�ѯ
 * ��Ȩ����: ��Ȩ����(C)2013-2020
 * ��          ˾: ����ά����ѯ���޹�˾
 * ����ժҪ: �������������������ѯ
 * ����˵��:
 * ������ڣ� 201��5��10��
 * �޸ļ�¼: 
 * </p>
 * 
 * @author LHP,LJ
 */
public class BookSearchActivity extends BaseActivity implements  OnClickListener {
	
	private Context context;
	private ImageView scan_iv;
	private EditText edit_search;
	private ImageView img;
	private ImageView icon_search_down;//��ť
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_search1);
		context = this;
		icon_search_down = (ImageView) findViewById(R.id.iv_search_down_arrow);
		icon_search_down.setVisibility(View.GONE);
		edit_search = (EditText) findViewById(R.id.et_search_keyword);
		
		TextView title = (TextView)findViewById(R.id.txt_header);
		title.setText(R.string.main_search);

		ImageView back = (ImageView)findViewById(R.id.img_back_header);
		scan_iv=(ImageView)findViewById(R.id.scan_iv);
		//hideinputmethod();
		
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
		    edit_search.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(edit_search.getWindowToken(), 0);
					Intent intent = new Intent(context,ResultOnSearchActivity.class) ;
					startActivity(intent);
					finish();
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
		case R.id.tv_search_condition:
			Intent intent=new Intent(BookSearchActivity.this, ResultOnSearchActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		
	}	
}
