package com.cqvip.moblelib.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.adapter.BookAdapter;

/**
 * <p>
 * �ļ����: ResultForSearchActivity.java
 * �ļ�����: ��ѯ���
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
public class ResultForSearchActivity extends BaseActivity {

	private ListView listview;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_result_for_search);
		View v = findViewById(R.id.result_title);
		TextView title = (TextView)v.findViewById(R.id.txt_header);
		title.setText(R.string.title_activity_result_for_search);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			//	overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
			}
		});
		
//		listview = (ListView)findViewById(R.id.re_list);
//		listview.setAdapter(new BookAdapter(this));
//		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view, int postion,
//					long id) {
//				
//				
//				
//				
//			}
//		});
	}


}
