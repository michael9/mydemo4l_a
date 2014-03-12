package com.cqvip.moblelib.activity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqvip.moblelib.xxu.R;

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
//	private int[] drawableids = { R.drawable.sign_vip, R.drawable.sign_chaoxing,
//			R.drawable.icon_search_wf,R.drawable.icon_search_wf,R.drawable.icon_search_wf,R.drawable.icon_search_cnki,R.drawable.icon_search_wf };
	private int[] drawableids = { R.drawable.sign_vip, 
			R.drawable.icon_search_wf,R.drawable.icon_search_wf,R.drawable.icon_search_cnki };
	private  int currentID=-1;
	private EditText editText;
	public static Map<Integer, Boolean> isSelected;
	private Context context;
	
	private SharedPreferences userChoice;
	private Editor editor;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ebook_activiy);
		context = this;
		
		userChoice = getSharedPreferences("mobilelibbate1", MODE_PRIVATE);
		editor = userChoice.edit();
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
		int choice = userChoice.getInt("userchoice", 1);
		final MyAdapter  adapter = new MyAdapter(this, EBOOKTYPE, drawableids,choice);
		lv.setAdapter(adapter);

		lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				View v = parent.getChildAt(position);
				CheckBox check = (CheckBox) v.findViewById(R.id.checkbox);
				if(check!=null){
					unSelectAll();
				    check.toggle();
				    isSelected.put(position, check.isChecked());
				    adapter.notifyDataSetChanged();
				}
			}
		});

		searchbar = (ViewGroup) findViewById(R.id.searchbar);
		searchbar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!validateChoosed()){
					Toast.makeText(context, "请选择一个资源库", 1).show();
				}else{
					int searchtype = 0;
					for(int i=0;i<EBOOKTYPE.length;i++){
						if(isSelected.get(i)){
							searchtype = i;
						}
					}
					Intent intent = new Intent(EBookActiviy.this,
							EBookSearchActivity.class);
					intent.putExtra("type",searchtype);
					startActivity(intent);
					//finish();
				}
			}
		});

		final EditText et = (EditText) findViewById(R.id.ebook_edit);
		et.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
				//没有选中库提示
				if(!validateChoosed()){
					Toast.makeText(context, "请选择一个资源库", 1).show();
				}else{
					int searchtype = 0;
					for(int i=0;i<EBOOKTYPE.length;i++){
						if(isSelected.get(i)){
							searchtype = i;
						}
					}
					Intent intent = new Intent(EBookActiviy.this,
							EBookSearchActivity.class);
					intent.putExtra("type",searchtype);
					startActivity(intent);
					//finish();
				}
				
			}
		});
	}
	
	/**
	 * 全消
	 * 
	 * @param isok
	 *         
	 */
	private void unSelectAll() {
		if(isSelected==null){
		isSelected = new HashMap<Integer, Boolean>();
		}else{
			isSelected.clear();
		}
		for (int i = 0; i < EBOOKTYPE.length; i++) {
			isSelected.put(i, false);
		}
	}
	
	/**
     * 判断是否有选中
     * @return
     */
	private boolean validateChoosed() {
		for(int i=0;i<EBOOKTYPE.length;i++){
			if(isSelected.get(i)){
				return true;
			}
		}
		return false;
	}

	public class MyAdapter extends BaseAdapter {
		private String[] eBookTypes;
		private Context context;
		private int[] drawableids;
		int  currentID=-1;

		
		private void init(int choice) {
			isSelected = new HashMap<Integer, Boolean>();
			for (int i = 0; i < eBookTypes.length; i++) {
				if(i==choice){
					isSelected.put(i, true);
				}else{
				isSelected.put(i, false);
				}
			}
		}
		public MyAdapter(Context context, String[] eBookTypes, int[] drawableids,int choice) {
			this.eBookTypes = eBookTypes;
			this.context = context;
			this.drawableids = drawableids;
			init(choice);
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater;
			if (convertView == null) {
				inflater = LayoutInflater.from(context);
				convertView = inflater.inflate(R.layout.item_ebooktype, null);
				CheckBox checkBox=(CheckBox) convertView.findViewById(R.id.checkbox);
//				if(position==eBookTypes.length-1){
//					checkBox.setEnabled(false);
//				}
			}
			ImageView iv = (ImageView) convertView
					.findViewById(R.id.booktype_img);
			TextView tv=(TextView) convertView
					.findViewById(android.R.id.text1);
			CheckBox checkBox=(CheckBox) convertView.findViewById(R.id.checkbox);
			checkBox.setChecked(isSelected.get(position));
			iv.setImageResource(drawableids[position]);
			tv.setText(eBookTypes[position]);
			tv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(position==0){
//						adapter.setCurrentID(position);
//						adapter.notifyDataSetChanged();
						startActivity(new Intent(EBookActiviy.this,
						PeriodicalClassfyActivity.class));
					}
				}
			});
			iv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(position==0){
//						adapter.setCurrentID(position);
//						adapter.notifyDataSetChanged();
						startActivity(new Intent(EBookActiviy.this,
								PeriodicalClassfyActivity.class));
					}
					
				}
			});
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		for(int i=0;i<EBOOKTYPE.length;i++){
			if(isSelected.get(i)){
				editor.putInt("userchoice", i);
				editor.commit();
			}
		}
		
		
		
	}	
	
}
