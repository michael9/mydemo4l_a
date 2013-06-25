package com.cqvip.moblelib.activity;

import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.MUserDao;
import com.cqvip.moblelib.entity.MUser;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.model.User;
import com.cqvip.moblelib.view.CustomProgressDialog;
import com.cqvip.utils.Tool;

public class ActivityDlg extends Activity implements IBookManagerActivity {

	private RelativeLayout login_layout,msg_box_layout;
	private EditText log_in_passwords;
	private AutoCompleteTextView log_in_username;
	private Button login_btn, cancel_btn,ok_btn;
//	private LinearLayout login_status_ll;
	private MUserDao dao;
	private TextView msg_box_txt;
	private  CustomProgressDialog customProgressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dlg);

		msg_box_layout=(RelativeLayout)findViewById(R.id.msg_box_layout);
		login_layout = (RelativeLayout) findViewById(R.id.log_in_layout);
//		login_status_ll = (LinearLayout) findViewById(R.id.login_status);
		msg_box_layout.setVisibility(View.GONE);
		login_layout.setVisibility(View.GONE);
		customProgressDialog = CustomProgressDialog.createDialog(this);
		//customProgressDialog.setMessage("正在加载中...");
//		login_status_ll.setVisibility(View.GONE);
		
		switch (getIntent().getIntExtra("ACTIONID", 0)) {
		
		case 0:
			showmsg();
			break;
		
		case 5:
		case 7:
		case 8:
			login() ;
			break;
		}
	}

	String name, pwd;

	private void login() {
		login_layout.setVisibility(View.VISIBLE);
		log_in_username = (AutoCompleteTextView) findViewById(R.id.log_in_username);
		log_in_passwords = (EditText) findViewById(R.id.log_in_passwords);
//		login_status_ll = (LinearLayout) findViewById(R.id.login_status);
		login_btn = (Button)findViewById(R.id.login_ok_btn);
		cancel_btn = (Button)findViewById(R.id.login_cancel_btn);
		
		dao = new MUserDao(this);
				
		login_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				HashMap map = new HashMap();
				name = log_in_username.getText().toString().trim();
				pwd = log_in_passwords.getText().toString();
				map.put("id", name);
				map.put("pwd", pwd);
				Task tsHome = new Task(Task.TASK_LOGIN, map);
				ManagerService.allActivity.add(ActivityDlg.this);
				ManagerService.addNewTask(tsHome);

				customProgressDialog.show();
//				login_layout.setVisibility(View.GONE);
//				login_status_ll.setVisibility(View.VISIBLE);
			}
		});
		
		cancel_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				winexit(1);
			}
		});
		init();
	}

	private void showmsg(){
		msg_box_layout.setVisibility(View.VISIBLE);
		 cancel_btn=(Button)findViewById(R.id.dlg_cancel_btn);
		 ok_btn=(Button)findViewById(R.id.dlg_ok_btn);
		 msg_box_txt=(TextView)findViewById(R.id.msg_box_txt);
		 
		 cancel_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				winexit(1);
			}
		});
		 ok_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				winexit(0);
			}
		});
		 
		 msg_box_txt.setText(getIntent().getStringExtra("MSGBODY"));
		 if(getIntent().getIntExtra("BTN_CANCEL", 0)==0){
			 cancel_btn.setVisibility(View.GONE);			 
		 }
				 
	}
	
	@Override
	public void init() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line, new String[] {
						"0441200001098", "0440061012345" });
		log_in_username.setThreshold(0);
		log_in_username.setAdapter(adapter);
		// 初始化 service
		// 检查网络是否可用
		if (Tool.checkNetWork(this)) {
			if (!ManagerService.isrun) {
				ManagerService.isrun = true;
				Intent it = new Intent(this, ManagerService.class);
				this.startService(it);
			}
		}
	}

	void winexit(int flag)
	{
		ActivityDlg.this.setResult(flag);
		finish();
	}
	
	@Override
	public void refresh(Object... obj) {
		// 取消进度条
		customProgressDialog.dismiss();
		Result res = (Result) obj[0];
		if (res.getSuccess()) {
			MainMenuActivity.islogin = true;

			User user = (User) obj[0];
			GlobleData.userid = user.getCardno();
			GlobleData.readerid = user.getReaderno();
			MUser muser = new MUser();
			muser.setCardno(user.getCardno());
			muser.setReaderno(user.getReaderno());
			muser.setPwd(pwd);
			muser.setName(user.getName());
			if (dao == null) {
				dao = new MUserDao(this);
			}
			try {
				// dao.delInfo(muser.getCardno());
				dao.saveInfo(muser);
				Log.i("database", "存储成功");
			} catch (DaoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// if (dialog.isShowing()) {
			// dialog.dismiss();
			// }
			// 提示登陆成功
//			Tool.ShowMessages(this, "登陆成功");
			winexit(0);
		} else {
			MainMenuActivity.islogin = false;
			// dialog.dismiss();
			// 提示登陆失败
			Tool.ShowMessages(this, res.getMessage());
		}
	}
}
