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
			//content.setText("1、进入外借处、阅览室（区）须持重庆图书馆读者证。\n 2、请注意仪表，勿穿背心、拖鞋入馆。\n 3、随身携带的包及其它与借阅无关的物品请寄放存包处。请勿携带书包、个人书刊资料及食品等进入阅览室（读者自修室除外）。");
			break;
		case 2:
			t1.setText(R.string.guide_cardguide);
			getContent(E_CARDGUID);
			//content.setText("一、申办对象 \n本馆的读者卡面向所有公民发放。14岁以下的少年儿童办理少儿借阅证。 \n二、办证所需证件 \n身份证（含港澳台）、护照、台胞回乡证、军官证等有效证件，未领身份证的少儿可持户口簿或出生证办理。 \n三、办证步骤 \n1、领取申请表，按本人实际情况填写（外地及外籍读者需填写本市住址及电话），根据个人所需选择读者卡类型； \n2、仔细阅读《重庆图书馆读者服务协议》，并签名； \n3、持有效证件，办理登记手续。 ");
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
