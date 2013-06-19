package com.cqvip.moblelib.activity;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.R.layout;
import com.cqvip.moblelib.R.menu;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;

import android.os.Bundle;
import android.app.Activity;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailTextActivity extends BaseActivity implements IBookManagerActivity{
	
	public static final int E_NOTICE = 1;
	public static final int E_CARDGUID = 2;
	public static final int E_TIME = 3;
	public static final int E_READER = 4;
	public static final int E_SERVICE = 5;
	
	private int type;
	private TextView t1,content;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature( Window.FEATURE_NO_TITLE );
		setContentView(R.layout.activity_detail_text);
		View v = findViewById(R.id.seach_title);
		t1 = (TextView)v.findViewById(R.id.txt_header);
		ImageView back = (ImageView)v.findViewById(R.id.img_back_header);
		ManagerService.allActivity.add(this);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		
		//title = (TextView)findViewById(R.id.title_txt);
		content = (TextView)findViewById(R.id.content_txt);
		type = getIntent().getIntExtra("enter",0);
		switch(type){
		case 1:
			t1.setText(R.string.guide_needknow);
			getContent(E_NOTICE);
			//content.setText("1��������账�������ң������������ͼ��ݶ���֤��\n 2����ע���Ǳ��𴩱��ġ���Ь��ݡ�\n 3������Я���İ�������������޹ص���Ʒ��ķŴ����������Я������������鿯���ϼ�ʳƷ�Ƚ��������ң����������ҳ��⣩��");
			break;
		case 2:
			t1.setText(R.string.guide_cardguide);
			getContent(E_CARDGUID);
			//content.setText("һ�������� \n���ݵĶ��߿��������й��񷢷š�14�����µ������ͯ�����ٶ�����֤�� \n������֤����֤�� \n���֤�����۰�̨�������ա�̨������֤������֤����Ч֤����δ�����֤���ٶ��ɳֻ��ڲ������֤���� \n������֤���� \n1����ȡ�����������ʵ�������д����ؼ��⼮��������д����סַ���绰�������ݸ�������ѡ����߿����ͣ� \n2����ϸ�Ķ�������ͼ��ݶ��߷���Э�顷����ǩ���� \n3������Ч֤��������Ǽ������� ");
			break;
		case 3:
			t1.setText(R.string.guide_time);
			getContent(E_TIME);
			//content.setText("");
			break;
		case 4:
			t1.setText(R.string.guide_readerknow);
			getContent(E_SERVICE);
			//content.setText("");
			break;
		case 5:
			t1.setText(R.string.guide_server);
			getContent(E_SERVICE);
			break;
		case 6:
			t1.setText(R.string.guide_transport);
			//title.setText("");
			content.setText("");
			break;
//		case 7:
//			
//			break;
		case 8:
			t1.setText(R.string.guide_problem);
			content.setText("");
			break;
			
		
		
		
		
		}
		
		
		
	}

	private void getContent(int taskid) {
		switch(taskid){
		case E_NOTICE:
			ManagerService.addNewTask(new Task(Task.TASK_E_NOTICE,null));
			break;
		case E_CARDGUID:
			ManagerService.addNewTask(new Task(Task.TASK_E_CARDGUID,null));
			break;
		case E_TIME:
			ManagerService.addNewTask(new Task(Task.TASK_E_TIME,null));
			break;
		case E_READER:
			ManagerService.addNewTask(new Task(Task.TASK_E_READER,null));
			break;
		case E_SERVICE:
			ManagerService.addNewTask(new Task(Task.TASK_E_SERVICE,null));
			break;
		
		
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_text, menu);
		return true;
	}

	@Override
	public void init() {
		
		
		
	}

	@Override
	public void refresh(Object... obj) {
		Integer type = (Integer)obj[0];
		String res = (String) obj[1];
		
		switch(type){
		case E_NOTICE:
			content.setText(Html.fromHtml(res));
			break;
		case E_CARDGUID:
			content.setText(Html.fromHtml(res));
			break;
			
		case E_TIME:
			content.setText(Html.fromHtml(res));
			break;
		case E_READER:
			content.setText(Html.fromHtml(res));
			break;
		case E_SERVICE:
			content.setText(Html.fromHtml(res));
			break;
		
		}
	}

}
