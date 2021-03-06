package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.Timer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;

import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.adapter.GridViewImgAdapter;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.MUserDao;
import com.cqvip.moblelib.entity.MUser;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.view.StableGridView;
import com.cqvip.utils.Tool;

/**
 * <p>
 * 文件名称: MainMenuActivity.java 文件描述: 主菜单，主界面 版权所有: 版权所有(C)2013-2020 公 司:
 * 重庆维普咨询有限公司 内容摘要: 其他说明: 完成日期： 201年5月10日 修改记录:
 * </p>
 * 
 * @author LHP,LJ
 */
public class MainMenuActivity extends BaseActivity implements IBookManagerActivity {

	private SharedPreferences preferences;
	private Context context;
	private String name;
	private String pwd;
	private AutoCompleteTextView _id;
	private EditText _pwd;
	private LinearLayout login_status_ll;
	private ScrollView login_form_sv;
	private StableGridView gridview;
	static public boolean cantouch;
	private MUserDao dao;
	private WebView adwebview;
	String updata_url;
	private GridViewImgAdapter adapter;
	private Timer mtimer;
	private int mtimern;
	private final Class[] activities = { EntanceGuideActivity.class,
			BookSearchActivity.class, EBookActiviy.class,
			SuggestedReadingActivity.class, PersonalCenterActivity.class,
			MyFavorActivity.class, AnnounceActivity.class,
			BorrowAndOrderActivity.class, GroupOfReadersActivity.class };
	// 抽屉
		private SlidingDrawer sd;
//		private ImageView iv;
	Handler handler=new Handler(){
		  public void handleMessage(Message msg) {
			  
			switch (msg.what) {
			
			case 0:
//				sd.open();
				if (Tool.checkNetWork(context)) {
		        	startCheckUpdate();
				}
				break;
			case 1:
//				sd.close();
				//检查更新	
				
			mtimer.cancel();
				break;
			default:
				break;
			}
		  }
	};
	
	 
    class time_check_task extends java.util.TimerTask {
        @Override
        public void run() {
        	handler.sendEmptyMessage(mtimern);
        	mtimern++;
        }
    }
    
	private int width, height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.);

//		Intent intent = new Intent(MainMenuActivity.this, WelcomeActivity.class);
//		startActivity(intent);
		startHelperActivity();
		
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		setContentView(R.layout.activity_main);
		context = this;
		sd = (SlidingDrawer) findViewById(R.id.sd);
		adwebview=(WebView)findViewById(R.id.adwebview);
		adwebview.getSettings().setSupportZoom(true);
		adwebview.loadUrl("http://www.szlglib.com.cn/uploads/Image/2013/06/24/20130624154214468.jpg");
		dao = new MUserDao(this);
		gridview = (StableGridView) findViewById(R.id.grid_main);
	    adapter = new GridViewImgAdapter(this,activities);
		gridview.setAdapter(adapter);
		init_login();
		mtimer=new Timer();
		mtimer.schedule(new time_check_task(), 8*1000,6*1000);
		init();
	}
	
	 void startHelperActivity(){
		 //读取SharedPreferences中需要的数据

	   SharedPreferences  preferences = getSharedPreferences("count",MODE_PRIVATE);

       int count = preferences.getInt("count", 0);

       //判断程序与第几次运行，如果是第一次运行则跳转到引导页面

      if (count == 0) {
       	
       	 Editor editor = preferences.edit();
            //存入数据
            editor.putInt("count", ++count);
            //提交修改
            editor.commit();
            
       	Intent intent = new Intent();
           intent.setClass(MainMenuActivity.this,HelperActivity.class);
           startActivity(intent);
       }
	 }
	private void init_login() {
		if (dao == null) {
			dao = new MUserDao(context);
		}
		try {
			MUser user = dao.queryInfo();
			if (user != null) {
				// 用户已经登陆
				// 后台自动登陆
				Log.i("database",
						"数据库获取书籍成功" + user.getCardno() + user.getReaderno());
				GlobleData.userid = user.getCardno();
				GlobleData.readerid = user.getReaderno();
				GlobleData.cqvipid = user.getCqvipid();
				GlobleData.islogin = true;

			}
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case 4:
		case 5:
		case 7:
		case 8:
			if (resultCode == 0&&GlobleData.islogin) {
				Intent intent = new Intent();
				intent.setClass(context, activities[requestCode]);
				startActivity(intent);
			}
			break;
			
		case 104:
			if(resultCode==0)
			{
				finish();
				  android.os.Process.killProcess(android.os.Process.myPid());
			}
			break;
			
		case 105:
			if(resultCode==0)
			{
				Uri uri = Uri.parse(updata_url); 
				Intent intent =new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
			break;

		default:
			break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==4)
		{
			Intent intent=new Intent(MainMenuActivity.this,ActivityDlg.class);
			intent.putExtra("ACTIONID", 0);
			intent.putExtra("MSGBODY", "确定退出龙岗图书馆吗？");
			intent.putExtra("BTN_CANCEL", 1);
			startActivityForResult(intent, 104);
			
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		cantouch = true;
	}
	
	@Override
	public void init() {
		// 初始化 service
		if (!ManagerService.isrun) {
			ManagerService.isrun = true;
			Intent it = new Intent(this, ManagerService.class);
			this.startService(it);
		}
		// 检查网络是否可用
        if (Tool.checkNetWork(context)) {
        	
		}
       
	}

	
	private void startCheckUpdate() {		
		ManagerService.allActivity.add(this);
		HashMap map = new HashMap();
		Task tsHome = new Task(Task.TASK_REFRESH, map);
		ManagerService.addNewTask(tsHome);
	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 overridePendingTransition(R.anim.slide_fade_in,
                 R.anim.slide_fade_out);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// Log.i("MainMenuActivity", "onStop");
	}


	@Override
	public void refresh(Object... obj) {
		ShortBook shortBook  = (ShortBook) obj[0];
		if (shortBook.getSucesss().equals("true")) {
			int remoteversion=Integer.parseInt(shortBook.getId());
			updata_url=shortBook.getDate();
			//比较版本号下载更新
			int versioncode=0;
			try {
				versioncode=this.getPackageManager().getPackageInfo("com.cqvip.moblelib", 0).versionCode;
				Log.i("mainmenu", "versioncode="+versioncode);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(remoteversion>versioncode){
				Intent intent=new Intent(MainMenuActivity.this,ActivityDlg.class);
				intent.putExtra("ACTIONID", 0);
				intent.putExtra("MSGBODY", "有更新版本，确定是否更新？");
				intent.putExtra("BTN_CANCEL", 1);
				startActivityForResult(intent, 105);
			}
		} 
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 停止服务
		// 退出Service
		context.stopService(new Intent("com.cqvip.moblelib.mainbiz"));
		// 关闭子线程
		ManagerService.isrun = false;

	}


}
