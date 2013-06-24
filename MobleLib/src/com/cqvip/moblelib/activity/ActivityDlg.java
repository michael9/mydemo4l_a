package com.cqvip.moblelib.activity;

import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import com.cqvip.utils.Tool;

public class ActivityDlg extends Activity implements IBookManagerActivity {

	private RelativeLayout login_layout;
	private EditText log_in_passwords;
	private AutoCompleteTextView log_in_username;
	private View login_btn, cancel_btn;
	private LinearLayout login_status_ll;
	private MUserDao dao;
	private ImageView iv_loading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dlg);

		login_layout = (RelativeLayout) findViewById(R.id.log_in_layout);
		log_in_username = (AutoCompleteTextView) findViewById(R.id.log_in_username);
		log_in_passwords = (EditText) findViewById(R.id.log_in_passwords);
		login_status_ll = (LinearLayout) findViewById(R.id.login_status);
		login_btn = findViewById(R.id.login_btn);
		cancel_btn = findViewById(R.id.cancel_btn);
		iv_loading=(ImageView) findViewById(R.id.iv_loading);
		dao = new MUserDao(this);
		switch (getIntent().getIntExtra("ACTIONID", 0)) {
		case 0:
			login() ;
			break;

		default:
			break;
		}
		 init();
	}

	String name, pwd;

	private void login() {
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

				Animation operatingAnim = AnimationUtils.loadAnimation(ActivityDlg.this, R.anim.loadingrotate);
				LinearInterpolator lin = new LinearInterpolator();
				operatingAnim.setInterpolator(lin);
				if (operatingAnim != null) {
					iv_loading.startAnimation(operatingAnim);
				}
				login_layout.setVisibility(View.GONE);
				login_status_ll.setVisibility(View.VISIBLE);
			}
		});
		
		cancel_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

@Override
protected void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
		iv_loading.clearAnimation();
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

	@Override
	public void refresh(Object... obj) {
		// 取消进度条
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
			Tool.ShowMessages(this, "登陆成功");
			finish();
		} else {
			MainMenuActivity.islogin = false;
			// dialog.dismiss();
			// 提示登陆失败
			Tool.ShowMessages(this, res.getMessage());
		}
	}
}
