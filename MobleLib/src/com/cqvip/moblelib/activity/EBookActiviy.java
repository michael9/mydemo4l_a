package com.cqvip.moblelib.activity;

import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.moblelib.longgang.R;

/**
 * <p>
 * 文件名称: EBookActiviy.java 文件描述: 电子书 版权所有: 版权所有(C)2013-2020 公 司: 重庆维普咨询有限公司
 * 内容摘要: 其他说明: 完成日期： 201年5月10日 修改记录:
 * </p>
 * 
 * @author LHP,LJ
 */
public class EBookActiviy extends BaseActivity {

	// private static final String[] EBOOKTYPE = new String[] {
	// "馆藏书目", "维普期刊", "万方期刊","超星图书","内置图书"
	// };
	private String[] EBOOKTYPE;
	private ViewGroup searchbar;
	private int[] drawableids = { R.drawable.sign_vip, R.drawable.sign_chaoxing,
			R.drawable.sign_fangzheng };
	private  int currentID=-1;
	private EditText editText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ebook_activiy);
		editText=(EditText) findViewById(R.id.ebook_edit);
		View v = findViewById(R.id.ebook_title);
		TextView title = (TextView) v.findViewById(R.id.txt_header);
		title.setText(R.string.main_ebook);
		ImageView back = (ImageView) v.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		hideinputmethod();
		
		// SearchView sc = (SearchView)findViewById(R.id.search_view);
		EBOOKTYPE = getResources().getStringArray(R.array.ebooktype);

		ListView lv = (ListView) findViewById(R.id.ebook_search_list);
		final MyAdapter  adapter = new MyAdapter(this, EBOOKTYPE, drawableids);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position==0){
//					adapter.setCurrentID(position);
//					adapter.notifyDataSetChanged();
					startActivity(new Intent(EBookActiviy.this,
					PeriodicalClassfyActivity.class));
				}
			}

		});

		searchbar = (ViewGroup) findViewById(R.id.searchbar);
		searchbar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EBookActiviy.this,
						EBookSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});

		final EditText et = (EditText) findViewById(R.id.ebook_edit);
		et.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
				Intent intent = new Intent(EBookActiviy.this,
						EBookSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}

	public class MyAdapter extends BaseAdapter {
		private String[] eBookTypes;
		private Context context;
		private int[] drawableids;
		int  currentID=-1;

		public MyAdapter(Context context, String[] eBookTypes, int[] drawableids) {
			this.eBookTypes = eBookTypes;
			this.context = context;
			this.drawableids = drawableids;
		}

		public void setCurrentID(int currentID) {
			this.currentID = currentID;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return eBookTypes.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater;
			if (convertView == null) {
				inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.item_ebooktype, null);
				CheckBox checkBox=(CheckBox) convertView.findViewById(R.id.checkbox);
				if(position!=0){
					checkBox.setEnabled(false);
				}
			}
			ImageView iv = (ImageView) convertView
					.findViewById(R.id.booktype_img);
			TextView tv=(TextView) convertView
					.findViewById(android.R.id.text1);
//			CheckedTextView checkedTextView = (CheckedTextView) convertView
//					.findViewById(android.R.id.text1);
			CheckBox checkBox=(CheckBox) convertView.findViewById(R.id.checkbox);
			if(position==0){
				checkBox.setChecked(true);
			}
			iv.setImageResource(drawableids[position]);
			tv.setText(eBookTypes[position]);
//			  if(position==this.currentID)
//				  checkBox.setChecked(true);
//			  else
//				  checkBox.setChecked(false);
			return convertView;
		}
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
