package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.adapter.GridViewImgAdapter;
import com.cqvip.moblelib.ahslsd.R;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.MUserDao;
import com.cqvip.moblelib.entity.MUser;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.utils.HttpUtils;
import com.cqvip.moblelib.view.StableGridView;
import com.cqvip.utils.Phinfo;
import com.cqvip.utils.Tool;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

/**
 * <p>
 * 文件名称: MainMenuActivity.java 文件描述: 主菜单，主界面 版权所有: 版权所有(C)2013-2020 公 司:
 * 重庆维普咨询有限公司 内容摘要: 其他说明: 完成日期： 201年5月10日 修改记录:
 * </p>
 * 
 * @author LHP,LJ
 */
public class MainMenuActivity extends BaseActivity {

	private SharedPreferences preferences;
	private Context context;
	private String name;
	private String pwd;
	private AutoCompleteTextView _id;
	private EditText _pwd;
	private LinearLayout login_status_ll;
	private ScrollView login_form_sv;
	private StableGridView gridview;
	static public boolean cantouch;// 防止多次点击
	private MUserDao dao;
	// private WebView adwebview;
	String updata_url;
	private GridViewImgAdapter adapter;
	private Timer mtimer;
	private int mtimern;
	private final Class[] activities = { EntanceGuideActivity.class,
			BookSearchActivity.class, EBookActiviy.class,
			SuggestedReadingActivity.class, PersonalCenterActivity.class,
			MessageInfoActivity.class, WebViewAnnouceActivity.class,
			RefServiceActivity.class, GroupOfReadersActivity.class };
	// 抽屉
	// private SlidingDrawer sd;
	// private ImageView iv;
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {

			switch (msg.what) {

			case 0:
				// sd.open();
				if (Tool.checkNetWork(context)) {
					startCheckUpdate();
				}
				break;
			case 1:
				// sd.close();
				// 检查更新

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

	public static int width, height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.);
		// Log.i("MainMenuActivity", "onCreate");
		Intent intent = new Intent(MainMenuActivity.this, WelcomeActivity.class);
		startActivity(intent);

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		setContentView(R.layout.activity_main);
		context = this;
		// sd = (SlidingDrawer) findViewById(R.id.sd);
		// adwebview = (WebView) findViewById(R.id.adwebview);
		// adwebview.getSettings().setSupportZoom(true);
		// adwebview
		// .loadUrl("http://www.szlglib.com.cn/uploads/Image/2013/06/24/20130624154214468.jpg");
		dao = new MUserDao(this);
		gridview = (StableGridView) findViewById(R.id.grid_main);
		adapter = new GridViewImgAdapter(this, activities);
		gridview.setAdapter(adapter);
		init_login();
//		mtimer = new Timer();
//		mtimer.schedule(new time_check_task(), 8 * 1000, 6 * 1000);
//		init();
		ActivityManager activityManager = (ActivityManager) this
				.getSystemService("activity");
		Log.i("MemoryClass", "" + activityManager.getMemoryClass());

		// 获取手机信息
		Phinfo phinfo = new Phinfo();
		phinfo.fullbaseinfo(this);
		String info = phinfo.tojson(phinfo);
		Log.i("phinfo", info);

		gridview.setOnItemClickListener(onItemClickListener);
		
		umeng();
	}
	
	private void umeng() {
		// 更新
			UmengUpdateAgent.setUpdateCheckConfig(false);
			UmengUpdateAgent.update(this);
		// 统计
		// MobclickAgent.setDebugMode(true);//打开调试模式（debug）后，数据实时发送，不受发送策略控制
		MobclickAgent.updateOnlineConfig(this);// 发送策略定义了用户由统计分析SDK产生的数据发送回友盟服务器的频率。在没有取到在线配置的发送策略的情况下，会使用默认的发送策略：启动时发送。
												// 你可以在友盟后台“设置->发送策略”页面自定义数据发送的频率。
	}

	OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (!MainMenuActivity.cantouch) {
				MainMenuActivity.cantouch = false;
				return;
			}
			Intent intent = new Intent();
			switch (position) {
			case 0:
			case 1:
			case 2:
			case 3:
				intent.setClass(MainMenuActivity.this, activities[position]);
				startActivity(intent);
				break;
			case 4:// 个人中心,需登录

			case 8:// 书友圈
					// 判断是否登录
//				if (GlobleData.islogin) {
//					intent.setClass(MainMenuActivity.this, activities[position]);
//					startActivity(intent);
//				} else {
//					showLoginDialog(position);
//				}
				MainMenuActivity.cantouch = true;
				Toast.makeText(MainMenuActivity.this, getString(R.string.tips_undo),
						Toast.LENGTH_SHORT).show();
				break;
			case 6:// 馆内公告
				intent.setClass(MainMenuActivity.this, activities[position]);
				startActivity(intent);
				break;

			case 5:// 信息素养，暂不开放
				break;
			case 7:// 参考咨询，暂不开放
				MainMenuActivity.cantouch = true;
				Toast.makeText(MainMenuActivity.this, "该功能正紧张开发中..",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	// 显示对话框
	private void showLoginDialog(int id) {
		MainMenuActivity.cantouch = true;
		Intent intent = new Intent(MainMenuActivity.this, ActivityDlg.class);
		intent.putExtra("ACTIONID", id);
		startActivityForResult(intent, id);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Log.i("MainMenuActivity", "onStart");
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
			if (resultCode == 0 && GlobleData.islogin) {
				Intent intent = new Intent();
				intent.setClass(context, activities[requestCode]);
				startActivity(intent);
			}
			break;

		case 104:
			if (resultCode == 0) {
				finish();
				android.os.Process.killProcess(android.os.Process.myPid());
			}
			break;

		case 105:
			if (resultCode == 0) {
				Uri uri = Uri.parse(updata_url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
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
		if (keyCode == 4) {
			Intent intent = new Intent(MainMenuActivity.this, ActivityDlg.class);
			intent.putExtra("ACTIONID", 0);
			intent.putExtra("MSGBODY", "确定退出"+getString(R.string.app_name)+"吗？");
			intent.putExtra("BTN_CANCEL", 1);
			startActivityForResult(intent, 104);

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		cantouch = true;
		// Log.i("MainMenuActivity", "onResume");
	}

	public void init() {
		// 初始化 service
		// if (!ManagerService.isrun) {
		// ManagerService.isrun = true;
		// Intent it = new Intent(this, ManagerService.class);
		// this.startService(it);
		// }
		// 检查网络是否可用
		if (Tool.checkNetWork(context)) {

		}

	}

	private void startCheckUpdate() {
		HashMap<String, String> map = new HashMap<String, String>();
		requestVolley(map,
				GlobleData.SERVER_URL + "/library/base/version.aspx",
				backlistener_version, Method.POST);
	}

	private void requestVolley(HashMap<String, String> gparams, String url,
			Listener<String> listener, int post) {
		final HashMap<String, String> gparams_t = gparams;
		StringRequest mys = new StringRequest(post, url, listener, el) {
			protected Map<String, String> getParams()
					throws com.android.volley.AuthFailureError {
				return gparams_t;
			};
		};
		mys.setRetryPolicy(HttpUtils.setTimeout());
		mQueue.add(mys);
		mQueue.start();

	}

	Listener<String> backlistener_version = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			ShortBook shortBook = null;
			try {
				shortBook = new ShortBook(Task.TASK_REFRESH, response);
			} catch (BookException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (shortBook != null) {
				if (shortBook.getSucesss().equals("true")) {
					int remoteversion = Integer.parseInt(shortBook.getId());
					updata_url = shortBook.getDate();
					// 比较版本号下载更新
					int versioncode = 0;
					try {
						versioncode = MainMenuActivity.this.getPackageManager()
								.getPackageInfo(getPackageName(), 0).versionCode;
						Log.i("mainmenu", "versioncode=" + versioncode);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (remoteversion > versioncode) {
						Intent intent = new Intent(MainMenuActivity.this,
								ActivityDlg.class);
						intent.putExtra("ACTIONID", 0);
						intent.putExtra("MSGBODY", "有更新版本，确定是否更新？");
						intent.putExtra("BTN_CANCEL", 1);
						startActivityForResult(intent, 105);
					}
				}
			}
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// Log.i("MainMenuActivity", "onPause");
		// overridePendingTransition(R.anim.slide_fade_in,
		// R.anim.slide_fade_out);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		// Log.i("MainMenuActivity", "onStop");
	}

	// @Override
	// public void refresh(Object... obj) {
	// ShortBook shortBook = (ShortBook) obj[0];
	// if (shortBook.getSucesss().equals("true")) {
	// int remoteversion = Integer.parseInt(shortBook.getId());
	// updata_url = shortBook.getDate();
	// // 比较版本号下载更新
	// int versioncode = 0;
	// try {
	// versioncode = this.getPackageManager().getPackageInfo(
	// "com.cqvip.moblelib", 0).versionCode;
	// Log.i("mainmenu", "versioncode=" + versioncode);
	// } catch (NameNotFoundException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// if (remoteversion > versioncode) {
	// Intent intent = new Intent(MainMenuActivity.this,
	// ActivityDlg.class);
	// intent.putExtra("ACTIONID", 0);
	// intent.putExtra("MSGBODY", "有更新版本，确定是否更新？");
	// intent.putExtra("BTN_CANCEL", 1);
	// startActivityForResult(intent, 105);
	// }
	// }
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 停止服务
		// 退出Service
		// context.stopService(new Intent("com.cqvip.moblelib.mainbiz"));
		// 关闭子线程
		// ManagerService.isrun = false;

	}

}
