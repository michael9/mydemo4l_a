package com.cqvip.moblelib.activity;

import java.util.HashMap;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;

public class LoginActivity extends Activity implements IBookManagerActivity{
	
	private String username,password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		username = "1";
		password = "1";
		HashMap map=new HashMap();
		map.put("username", username);
		map.put("username", password);
		Task tsHome=new Task(Task.TASK_LOGIN,map);
		ManagerService.addNewTask(tsHome);
		//ManagerService.allActivity.add(this);
	}

	@Override
	public void refresh(Object... obj) {
		String result = (String)obj[0];
		//ÅÐ¶ÏµÇÂ½³É¹¦
		if(result.equals("True")){
			
		}
	}

}
