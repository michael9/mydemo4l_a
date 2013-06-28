package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.Timer;

import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.R;
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
	static boolean islogin = false;
	private StableGridView gridview;
	static public boolean cantouch;
	private MUserDao dao;
	private WebView adwebview;
	String updata_url;
	
	private Timer mtimer;
	private int mtimern;
	// 抽屉
		private SlidingDrawer sd;
//		private ImageView iv;
	Handler handler=new Handler(){
		  public void handleMessage(Message msg) {
			  
			switch (msg.what) {
			
			case 0:
				sd.open();
			
				break;
			case 1:
				sd.close();
				//检查更新	
				if (Tool.checkNetWork(context)) {
		        	startCheckUpdate();
				}
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

	// private SurfaceView main_anim_background;
	// private SurfaceHolder sh;
	// private Timer mtr;
	// private Bitmap b1, b2;
	// private float postx;
	// private int drawbackgroud = 0;
	// Dialog dialog;

	// TimerTask task = new TimerTask() {
	// public void run() {
	// drawbackgroud();
	// }
	// };

	// private void drawbackgroud() {
	// if (main_anim_background.isShown()) {
	// try {
	// Canvas canvas = sh.lockCanvas();
	// postx += 0.5;
	// postx = postx % (b1.getWidth() - width);
	// Paint bmpp = new Paint();
	// Matrix ma = new Matrix();
	// // ma.postScale(3, 3);
	// ma.postTranslate(postx * -1, 0);
	//
	// canvas.drawBitmap(b1, ma, bmpp);
	// sh.unlockCanvasAndPost(canvas);
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
	// }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.);

		Intent intent = new Intent(MainMenuActivity.this, WelcomeActivity.class);
		startActivity(intent);

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
		// 读取SharedPreferences中需要的数据

		// preferences = getSharedPreferences("count",MODE_PRIVATE);
		//
		// int count = preferences.getInt("count", 0);
		//
		// //判断程序与第几次运行，如果是第一次运行则跳转到引导页面
		//
		// if (count == 0) {
		//
		// Editor editor = preferences.edit();
		// //存入数据
		// editor.putInt("count", ++count);
		// //提交修改
		// editor.commit();
		//
		// Intent intent = new Intent();
		// intent.setClass(getApplicationContext(),WelcomeActivity.class);
		// startActivity(intent);
		// finish();
		// }
		//
		// b1 = BitmapFactory.decodeResource(this.getResources(),
		// R.drawable.m_3);
		// main_anim_background = (SurfaceView)
		// findViewById(R.id.main_anim_background);
		// sh = main_anim_background.getHolder();
		// mtr = new Timer();
		// mtr.schedule(task, 1000, 50);
		gridview = (StableGridView) findViewById(R.id.grid_main);
		final GridViewImgAdapter adapter = new GridViewImgAdapter(this);
		gridview.setAdapter(adapter);
		// gridview.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1,
		// int position, long arg3) {
		// Intent intent = new Intent();
		//
		// // adapter.setSeclection(position);
		// // adapter.notifyDataSetChanged();
		//
		// // Animation animation =
		// // AnimationUtils.loadAnimation(MainMenuActivity.this,
		// // R.anim.wave_scale);
		// // arg1.startAnimation(animation);
		// switch (position) {
		// // 入馆指南
		// case 0:
		// intent.setClass(context, EntanceGuideActivity.class);
		// startActivity(intent);
		// overridePendingTransition(R.anim.slide_right_in,
		// R.anim.slide_left_out);
		// break;
		// // 馆藏查询
		// case 1:
		// intent.setClass(context, BookSearchActivity.class);
		// startActivity(intent);
		// // overridePendingTransition(R.anim.slide_right_in,
		// // R.anim.slide_left_out);
		// break;
		// // 读者服务
		// case 2:
		// intent.setClass(context, EBookActiviy.class);
		// startActivity(intent);
		// // overridePendingTransition(R.anim.slide_right_in,
		// // R.anim.slide_left_out);
		// break;
		// // 推荐阅读
		// case 3:
		// intent.setClass(context, SuggestedReadingActivity.class);
		// startActivity(intent);
		// // overridePendingTransition(R.anim.slide_right_in,
		// // R.anim.slide_left_out);
		// break;
		// // 参考咨询
		// case 4:
		// intent.setClass(context, RefServiceActivity.class);
		// startActivity(intent);
		// // overridePendingTransition(R.anim.slide_right_in,
		// // R.anim.slide_left_out);
		// break;
		// // 个人中心
		// case 5:
		// // 弹出登陆对话框
		// if (islogin) {
		// intent.setClass(context, PersonalCenterActivity.class);
		// startActivity(intent);
		// } else {
		// showLoginDialog();
		// }
		// //
		// // intent.setClass(context, PersonalCenterActivity.class);
		// // startActivity(intent);
		// break;
		// // 馆内公告
		// case 6:
		// intent.setClass(context, AnnounceActivity.class);
		// startActivity(intent);
		// // overridePendingTransition(R.anim.slide_right_in,
		// // R.anim.slide_left_out);
		// break;
		// // 借阅管理
		// case 7:
		// intent.setClass(context, BorrowAndOrderActivity.class);
		// startActivity(intent);
		// // overridePendingTransition(R.anim.slide_right_in,
		// // R.anim.slide_left_out);
		// break;
		// // 书友圈
		// case 8:
		// intent.setClass(context, GroupOfReadersActivity.class);
		// startActivity(intent);
		// // overridePendingTransition(R.anim.slide_right_in,
		// // R.anim.slide_left_out);
		//
		// break;
		// // 其它
		// default:
		// break;
		// }
		// }
		//
		// });
		init_login();
		mtimer=new Timer();
		mtimer.schedule(new time_check_task(), 8*1000,6*1000);
		
		init();
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
				islogin = true;

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
			if (resultCode == 0&&islogin) {
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

	private void showLoginDialog(int id) {
		cantouch = true;

		// View
		// view=LayoutInflater.from(context).inflate(R.layout.activity_login,
		// null);
		// dialog = new AlertDialog.Builder(this).setView(view).create();
		// Button btn = (Button) view.findViewById(R.id.sign_in_button);
		// _id = (AutoCompleteTextView) view.findViewById(R.id.ed_loginname);
		// _pwd = (EditText) view.findViewById(R.id.ed_loginpwd);
		// login_status_ll = (LinearLayout)
		// view.findViewById(R.id.login_status);
		// login_form_sv = (ScrollView) view.findViewById(R.id.login_form);
		//
		// ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
		// android.R.layout.simple_dropdown_item_1line,
		// new String[] { "0441200001098","0440061012345"});
		// _id.setThreshold(0);
		// _id.setAdapter(adapter);
		//
		// btn.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // TODO Auto-generated method stub
		// name = _id.getText().toString().trim();
		// pwd = _pwd.getText().toString();
		// Log.i("MainMenuActivity", name+"--"+pwd);
		// HashMap map = new HashMap();
		// map.put("id", name);
		// map.put("pwd", pwd);
		// Task tsHome = new Task(Task.TASK_LOGIN, map);
		// ManagerService.allActivity.add(MainMenuActivity.this);
		// ManagerService.addNewTask(tsHome);
		//
		// // 显示进度条
		// login_status_ll.setVisibility(View.VISIBLE);
		// login_form_sv.setVisibility(View.GONE);
		// }
		// });
		// dialog.show();
		Intent intent = new Intent(MainMenuActivity.this, ActivityDlg.class);
		intent.putExtra("ACTIONID", id);
		startActivityForResult(intent, id);
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
		// Log.i("MainMenuActivity", "onResume");			
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

	private final Class[] activities = { EntanceGuideActivity.class,
			BookSearchActivity.class, EBookActiviy.class,
			SuggestedReadingActivity.class, PersonalCenterActivity.class,
			MyFavorActivity.class, AnnounceActivity.class,
			BorrowAndOrderActivity.class, GroupOfReadersActivity.class };

	public class GridViewImgAdapter extends BaseAdapter {

		private Context mContext;

		// 定义整型数组 即图片源

		private Integer[] mImageIds = { R.drawable.sy_anniu_03,
				R.drawable.sy_anniu_05, R.drawable.sy_anniu_07,
				R.drawable.sy_anniu_12, R.drawable.sy_anniu_14,
				R.drawable.sy_anniu_21, R.drawable.sy_anniu_18,
				R.drawable.sy_anniu_19, R.drawable.sy_anniu_20 };

		private Integer[] mImageIds_big = { R.drawable.sy_anniu_03big,
				R.drawable.sy_anniu_05big, R.drawable.sy_anniu_07big,
				R.drawable.sy_anniu_12big, 
				R.drawable.sy_anniu_14big,R.drawable.sy_anniu_21big, R.drawable.sy_anniu_18big,
				R.drawable.sy_anniu_19big, R.drawable.sy_anniu_20big };

		private int[] mTitle = { R.string.main_guide, R.string.main_search,
				R.string.main_ebook, R.string.main_readingguide,
				R.string.main_ebookstore,//R.string.main_bookcomment
				R.string.serv_favorite,
				R.string.main_notice, R.string.main_borrow, R.string.main_order

		};

		public GridViewImgAdapter(Context c) {
			mContext = c;
		}

		// private int clickTemp = -1;
		//
		// // 标识选择的Item
		// public void setSeclection(int position) {
		// clickTemp = position;
		// }

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mImageIds.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		// 获取图片ID
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final ImageView img;
//			final int temp_position = position;
			if (convertView == null) {
				// 给ImageView设置资源
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_main_menu, null);
				// DisplayMetrics dm = new DisplayMetrics();
				// WindowManager vm =
				// (WindowManager)mContext.getSystemService(Context.WINDOW_SERVICE);
				// vm.getDefaultDisplay().getMetrics(dm);
				// int width = dm.widthPixels ;
				// int widthAndHeight = width/ 5 ;
				// imageView.setLayoutParams(new
				// GridView.LayoutParams(widthAndHeight, widthAndHeight));
				// imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

			}
			img = (ImageView) convertView.findViewById(R.id.img_main);
			TextView tx = (TextView) convertView.findViewById(R.id.txt_main);
			tx.setText(getString(mTitle[position]));
			img.setImageResource(mImageIds[position]);
			img.setTag(position);

			img.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					int tag=(Integer)v.getTag();
					
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						img.setImageResource(mImageIds_big[tag]);
						Log.i("MainMenuActivity", "ACTION_DOWN");
						break;
					case MotionEvent.ACTION_UP:
						img.setImageResource(mImageIds[tag]);
						if (!MainMenuActivity.cantouch)
							break;
						MainMenuActivity.cantouch = false;
						Intent intent = new Intent();						
						switch (tag) {
						case 0:
						case 1:
						case 2:
							intent.setClass(context, activities[tag]);
							startActivity(intent);
							break;

						case 3:
							intent.setClass(context, ActivityDlg.class);
							intent.putExtra("ACTIONID", 0);
							intent.putExtra("MSGBODY", "该项正在紧张开发中...");
							intent.putExtra("BTN_CANCEL", 0);
							startActivity(intent);
							break;
						case 4:
						case 5:
						case 7:
						case 8:
							if (islogin) {
								intent.setClass(context, activities[tag]);
								startActivity(intent);
							} else {
								showLoginDialog(tag);
							}
							break;
						case 6:
							intent.setClass(context, activities[tag]);
							startActivity(intent);
							break;

						default:
							break;
						}
						break;
					default:
						break;
					}
					return true;
				}
			});

			// if (clickTemp == position) {
			// img.setImageResource(mImageIds_big[position]);
			// } else {
			// img.setImageResource(mImageIds[position]);
			// }
			return convertView;
		}

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
