package com.cqvip.moblelib.activity;

import java.util.HashMap;

import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;

public class DetailAdvancedBookActivity extends BaseActivity implements IBookManagerActivity {

	private TextView content;
	private int type;
	private String id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail_advanced_book);
		
		content = (TextView)findViewById(R.id.ad_book_content);
		type = getIntent().getIntExtra("type",1);
		id = getIntent().getStringExtra("id");
		getContent(id);
	}

	private void getContent(String id2) {
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
		
	}

}
