package com.cqvip.moblelib.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.scan.CaptureActivity;

/**
 * <p>
 * �ļ�����: EBookActiviy.java
 * �ļ�����: ������
 * ��Ȩ����: ��Ȩ����(C)2013-2020
 * ��          ˾: ����ά����ѯ���޹�˾
 * ����ժҪ: 
 * ����˵��:
 * ������ڣ� 201��5��10��
 * �޸ļ�¼: 
 * </p>
 * 
 * @author LHP,LJ
 */
public class EBookActiviy extends BaseActivity {

//	 private static final String[] EBOOKTYPE = new String[] {
//	        "�ݲ���Ŀ", "ά���ڿ�", "���ڿ�","����ͼ��","����ͼ��"
//	    };
	private static final String[] EBOOKTYPE = new String[] {
       "ά���ڿ�"
    };
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_ebook_activiy);
		View v = findViewById(R.id.ebook_title);
		TextView title = (TextView)v.findViewById(R.id.txt_header);
		title.setText(R.string.main_ebook);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		
//		SearchView sc = (SearchView)findViewById(R.id.search_view);
	
		
		ListView lv = (ListView)findViewById(R.id.ebook_search_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,EBOOKTYPE);
		lv.setAdapter(adapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setItemsCanFocus(false);
		lv.setItemChecked(0, true);	
		lv.setEnabled(false);
		lv.setFocusable(false);
		
		ImageView scan_iv=(ImageView)findViewById(R.id.ebook_seach_img);
		scan_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(EBookActiviy.this, EBookSearchActivity.class);
				startActivity(intent);
			}
		});
//		InitImageView();
//		InitTextView();
//		InitViewPager();
//		
		 final EditText et = (EditText)findViewById(R.id.ebook_edit);
		    et.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
					Intent intent = new Intent(EBookActiviy.this,EBookSearchActivity.class) ;
					startActivity(intent);
				}
			});
		
		
	}


}
