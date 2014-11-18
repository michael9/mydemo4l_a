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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.sychild.R;
import com.cqvip.moblelib.adapter.GridViewImgAdapter;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.db.MUserDao;
import com.cqvip.moblelib.entity.MUser;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.view.StableGridView;
import com.cqvip.utils.Phinfo;
import com.cqvip.utils.Tool;

/**
 * <p>
 * �ļ����: MainMenuActivity.java �ļ�����: ���˵��������� ��Ȩ����: ��Ȩ����(C)2013-2020 �� ˾:
 * ����ά����ѯ���޹�˾ ����ժҪ: ����˵��: ������ڣ� 201��5��10�� �޸ļ�¼:
 * </p>
 * 
 * @author LHP,LJ
 */
public class MainMenuActivity extends BaseActivity_Main implements
		OnClickListener {

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
	// private WebView adwebview;
	String updata_url;
	private GridViewImgAdapter adapter;
	private Timer mtimer;
	private int mtimern;

	private ImageView iv_top, iv_EntanceGuide, iv02,main08_iv;

	private final Class[] activities = { EntanceGuideActivity.class,
			BookSearchActivity.class, EBookActiviy.class,
			SuggestedReadingActivity.class, PersonalCenterActivity.class,
			MyFavorActivity.class, AnnounceActivity.class,
			BorrowAndOrderActivity.class, GroupOfReadersActivity.class };
	// ����
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
				// ������

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

		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
		setContentView(R.layout.activity_main);
		context = this;
		// adwebview.getSettings().setSupportZoom(true);
		// adwebview
		// .loadUrl("http://www.szlglib.com.cn/uploads/Image/2013/06/24/20130624154214468.jpg");
		dao = new MUserDao(this);
		gridview = (StableGridView) findViewById(R.id.grid_main);
		adapter = new GridViewImgAdapter(this, activities);
		gridview.setAdapter(adapter);
		init_login();
		mtimer = new Timer();
		mtimer.schedule(new time_check_task(), 8 * 1000, 6 * 1000);
		init();
		ActivityManager activityManager = (ActivityManager) this
				.getSystemService("activity");
		Log.i("MemoryClass", "" + activityManager.getMemoryClass());

		// ��ȡ�ֻ���Ϣ
		Phinfo phinfo = new Phinfo();
		phinfo.fullbaseinfo(this);
		String info = phinfo.tojson(phinfo);
		Log.i("phinfo", info);
	}

	private void init_login() {
		if (dao == null) {
			dao = new MUserDao(context);
		}
		try {
			MUser user = dao.queryInfo();
			if (user != null) {
				// �û��Ѿ���½
				// ��̨�Զ���½
				Log.i("database",
						"��ݿ��ȡ�鼮�ɹ�" + user.getCardno() + user.getReaderno());
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

//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		if (hasFocus) {
//			int margintop=main09_iv.getHeight()-main08_iv.getHeight()*2;
//			Log.i("MainMenuAct", "height:"+main09_iv.getHeight()+"--"+main08_iv.getHeight());
//			//main08_iv.
//		}
//	}
	
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

	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// // TODO Auto-generated method stub
	// if (keyCode == 4) {
	// Intent intent = new Intent(MainMenuActivity.this, ActivityDlg.class);
	// intent.putExtra("ACTIONID", 0);
	// intent.putExtra("MSGBODY", "ȷ���˳����ͼ�����");
	// intent.putExtra("BTN_CANCEL", 1);
	// startActivityForResult(intent, 104);
	//
	// }
	// return super.onKeyDown(keyCode, event);
	// }

	@Override
	protected void onResume() {
		super.onResume();
		cantouch = true;
	}

	public void init() {
		// ��ʼ�� service
		// if (!ManagerService.isrun) {
		// ManagerService.isrun = true;
		// Intent it = new Intent(this, ManagerService.class);
		// this.startService(it);
		// }
		// ��������Ƿ����
		// if (Tool.checkNetWork(context)) {
		//
		// }
		iv_EntanceGuide = (ImageView) findViewById(R.id.main02_iv);
		iv_top = (ImageView) findViewById(R.id.main01_iv);
		main08_iv=(ImageView) findViewById(R.id.main08_iv);
		// iv_EntanceGuide.setOnClickListener(this);
		// iv_top.setOnClickListener(this);
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
					// �Ƚϰ汾�����ظ���
					int versioncode = 0;
					try {
						versioncode = MainMenuActivity.this.getPackageManager()
								.getPackageInfo("com.cqvip.moblelib.sychild", 0).versionCode;
						Log.i("mainmenu", "versioncode=" + versioncode);
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (remoteversion > versioncode) {
						Intent intent = new Intent(MainMenuActivity.this,
								ActivityDlg.class);
						intent.putExtra("ACTIONID", 0);
						intent.putExtra("MSGBODY", "�и��°汾��ȷ���Ƿ���£�");
						intent.putExtra("BTN_CANCEL", 1);
						startActivityForResult(intent, 105);
					}
				}
			}
		}
	};

	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			arg0.printStackTrace();
		}
	};

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_fade_out);
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
	// // �Ƚϰ汾�����ظ���
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
	// intent.putExtra("MSGBODY", "�и��°汾��ȷ���Ƿ���£�");
	// intent.putExtra("BTN_CANCEL", 1);
	// startActivityForResult(intent, 105);
	// }
	// }
	// }

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ֹͣ����
		// �˳�Service
		// context.stopService(new Intent("com.cqvip.moblelib.mainbiz"));
		// �ر����߳�
		// ManagerService.isrun = false;
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.main02_iv:
			intent.setClass(MainMenuActivity.this, activities[0]);
			startActivity(intent);
			break;
		case R.id.main03_iv:
			intent.setClass(MainMenuActivity.this, activities[1]);
			startActivity(intent);
			break;
		case R.id.main04_iv:
			intent.setClass(MainMenuActivity.this, activities[2]);
			startActivity(intent);
			break;
		case R.id.main05_iv:
			intent.setClass(MainMenuActivity.this, activities[3]);
			startActivity(intent);
			break;
		case R.id.main06_iv:// ��������
			if (GlobleData.islogin) {
				intent.setClass(context, activities[4]);
				startActivity(intent);
			} else {
				showLoginDialog(4);
			}
			break;
		case R.id.main07_iv:// ���Ĺ���
			if (GlobleData.islogin) {
				intent.setClass(context, activities[7]);
				startActivity(intent);
			} else {
				showLoginDialog(7);
			}
			break;
		case R.id.main08_iv:// �ο���ѯ
//			intent.setClass(MainMenuActivity.this, activities[5]);
//			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void showLoginDialog(int id) {
		MainMenuActivity.cantouch = true;
		Intent intent = new Intent(context, ActivityDlg.class);
		intent.putExtra("ACTIONID", id);
		startActivityForResult(intent, id);
	}

	// ���������˳�
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
//		switch (ev.getAction()) {
//		case MotionEvent.ACTION_UP:
		Log.e("MainMenuAct", "dispatchTouchEvent"+ev.getAction());
			if (mGestureDetector.onTouchEvent(ev)) {
				Log.e("MainMenuAct", "dispatchTouchEvent_true");
				if(ev.getAction()==MotionEvent.ACTION_UP)
				exit(getString(R.string.exit_swipe_tips));
				return true;
			} else {
				boolean temp = super.dispatchTouchEvent(ev);
				// Log.e("mylinearlayout", "dispatchTouchEvent_"+temp);
				return temp;
			}
//			default:return super.dispatchTouchEvent(ev);
//		}
	}

	private static boolean isExit = false;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit(getString(R.string.exit_tips));
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit(String tips) {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), tips, Toast.LENGTH_SHORT)
					.show();
			// ����handler�ӳٷ��͸��״̬��Ϣ
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			// System.exit(0);
		}
	}
}
