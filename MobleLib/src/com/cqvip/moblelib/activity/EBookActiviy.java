package com.cqvip.moblelib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.moblelib.R;

/**
 * <p>
 * 文件名称: EBookActiviy.java
 * 文件描述: 电子书
 * 版权所有: 版权所有(C)2013-2020
 * 公          司: 重庆维普咨询有限公司
 * 内容摘要: 
 * 其他说明:
 * 完成日期： 201年5月10日
 * 修改记录: 
 * </p>
 * 
 * @author LHP,LJ
 */
public class EBookActiviy extends BaseActivity {

//	 private static final String[] EBOOKTYPE = new String[] {
//	        "馆藏书目", "维普期刊", "万方期刊","超星图书","内置图书"
//	    };
	private  String[] EBOOKTYPE;
	private ViewGroup searchbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		EBOOKTYPE = getResources().getStringArray(R.array.ebooktype);
		
		ListView lv = (ListView)findViewById(R.id.ebook_search_list);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,EBOOKTYPE);
		lv.setAdapter(adapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv.setItemsCanFocus(false);
		lv.setItemChecked(0, true);	
		//lv.setEnabled(false);
		lv.setFocusable(false);
		
		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				startActivity(new Intent(EBookActiviy.this,PeriodicalClassfyActivity.class));
				
			}
		
		});
		
		searchbar=(ViewGroup)findViewById(R.id.searchbar);
		searchbar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(EBookActiviy.this, EBookSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});
		
		 final EditText et = (EditText)findViewById(R.id.ebook_edit);
		    et.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
					Intent intent = new Intent(EBookActiviy.this,EBookSearchActivity.class) ;
					startActivity(intent);
					finish();
				}
			});
	}
}
