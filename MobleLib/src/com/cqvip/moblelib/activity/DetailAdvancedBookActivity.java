package com.cqvip.moblelib.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.view.CustomProgressDialog;

public class DetailAdvancedBookActivity extends BaseActivity implements IBookManagerActivity {

	private TextView content;
	private int type;
	private String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_advanced_book);
		
		content = (TextView)findViewById(R.id.ad_book_content);
		type = getIntent().getIntExtra("type",1);
		id = getIntent().getStringExtra("id");
		if(type == Constant.QUESTION){
			content.setText(id);
			setheadbar(getResources().getString(R.string.title_FAQ));
		}else{
		getContent(id);
		setheadbar(getResources().getString(R.string.title_moredetail));
		}
	}
	
	private void setheadbar(String title)
	{
		View headbar,btn_back;
		TextView bar_title;
		customProgressDialog=CustomProgressDialog.createDialog(this);
		headbar=findViewById(R.id.head_bar);
		bar_title=(TextView)headbar.findViewById(R.id.txt_header);
		bar_title.setText(title);
		btn_back=headbar.findViewById(R.id.img_back_header);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	}

	private void getContent(String id2) {
//		customProgressDialog.show();
		ManagerService.allActivity.add(this);
		HashMap map=new HashMap();
		map.put("id", id2);
		ManagerService.addNewTask(new Task(Task.TASK_SUGGEST_DETAIL,map));
			
	}


	@Override
	public void init() {
	
		
	}

	@Override
	public void refresh(Object... obj) {		
		String res =(String)obj[0];
		content.setText(Html.fromHtml(res));
//		customProgressDialog.dismiss();
	}

}
