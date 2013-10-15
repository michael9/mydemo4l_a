package com.cqvip.moblelib.activity;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
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

import com.cqvip.moblelib.R;

/**
 * <p>
 * �ļ�����: EBookActiviy.java �ļ�����: ������ ��Ȩ����: ��Ȩ����(C)2013-2020 �� ˾: ����ά����ѯ���޹�˾
 * ����ժҪ: ����˵��: ������ڣ� 201��5��10�� �޸ļ�¼:
 * </p>
 * 
 * @author LHP,LJ
 */
public class EBookActiviy extends BaseActivity {

	// private static final String[] EBOOKTYPE = new String[] {
	// "�ݲ���Ŀ", "ά���ڿ�", "���ڿ�","����ͼ��","����ͼ��"
	// };
	private String[] EBOOKTYPE;
	private ViewGroup searchbar;
	private int[] drawableids = { R.drawable.sign_vip, R.drawable.sign_chaoxing,
			R.drawable.sign_fangzheng };
	private  int currentID=-1;
	private EditText editText;
	public static Map<Integer, Boolean> isSelected;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ebook_activiy);
		context = this;
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
				if(position!=2){//������ʱ������
				View v = parent.getChildAt(position);
				CheckBox check = (CheckBox) v.findViewById(R.id.checkbox);
				if(check!=null){
					unSelectAll();
				    check.toggle();
				    isSelected.put(position, check.isChecked());
				    adapter.notifyDataSetChanged();
				}
			}
			}

		});

		searchbar = (ViewGroup) findViewById(R.id.searchbar);
		searchbar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!validateChoosed()){
					Toast.makeText(context, "��ѡ��һ����Դ��", 1).show();
				}else{
					int searchtype = 1;
					if(isSelected.get(0)){
						searchtype = 1;//ά��
					}else if(isSelected.get(1)){
						searchtype = 2;//������
					}
					Intent intent = new Intent(EBookActiviy.this,
							EBookSearchActivity.class);
					intent.putExtra("type",searchtype);
					startActivity(intent);
					finish();
				}
			}
		});

		final EditText et = (EditText) findViewById(R.id.ebook_edit);
		et.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
				//û��ѡ�п���ʾ
				if(!validateChoosed()){
					Toast.makeText(context, "��ѡ��һ����Դ��", 1).show();
				}else{
					int searchtype = 1;
					if(isSelected.get(0)){
						searchtype = 1;//ά��
					}else if(isSelected.get(1)){
						searchtype = 2;//������
					}
					Intent intent = new Intent(EBookActiviy.this,
							EBookSearchActivity.class);
					intent.putExtra("type",searchtype);
					startActivity(intent);
					finish();
				}
				
			}
		});
	}
	
	/**
	 * ȫ��
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
     * �ж��Ƿ���ѡ��
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

		
		private void init() {
			isSelected = new HashMap<Integer, Boolean>();
			for (int i = 0; i < eBookTypes.length; i++) {
				if(i==0){
					isSelected.put(i, true);
				}else{
				isSelected.put(i, false);
				}
			}
		}
		public MyAdapter(Context context, String[] eBookTypes, int[] drawableids) {
			this.eBookTypes = eBookTypes;
			this.context = context;
			this.drawableids = drawableids;
			init();
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
				if(position==eBookTypes.length-1){
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
//			if(position==0){
//				checkBox.setChecked(true);
//			}
			checkBox.setChecked(isSelected.get(position));
			iv.setImageResource(drawableids[position]);
			tv.setText(eBookTypes[position]);
//			  if(position==this.currentID)
//				  checkBox.setChecked(true);
//			  else
//				  checkBox.setChecked(false);
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
}
