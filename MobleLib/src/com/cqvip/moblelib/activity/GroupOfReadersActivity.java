package com.cqvip.moblelib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.BookAdapter;

/**
 * <p>
 * �ļ�����: GroupOfReadersActivity.java
 * �ļ�����: ����Ȧ
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
public class GroupOfReadersActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_group_of_readers);
		View v = findViewById(R.id.group_title);
		TextView title = (TextView)v.findViewById(R.id.txt_header);
		title.setText(R.string.main_order);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ListView lv1 = (ListView)findViewById(R.id.group_list);
		lv1.setAdapter(new BookAdapter(this));
		
		
	}


}
