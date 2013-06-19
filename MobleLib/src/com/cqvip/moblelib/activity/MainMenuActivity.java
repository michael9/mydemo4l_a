package com.cqvip.moblelib.activity;

import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SlidingDrawer;
import android.widget.TextView;

import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.model.User;
import com.cqvip.moblelib.view.StableGridView;
import com.cqvip.utils.Tool;

/**
 * <p>
 * �ļ�����: MainMenuActivity.java �ļ�����: ���˵��������� ��Ȩ����: ��Ȩ����(C)2013-2020 �� ˾:
 * ����ά����ѯ���޹�˾ ����ժҪ: ����˵��: ������ڣ� 201��5��10�� �޸ļ�¼:
 * </p>
 * 
 * @author LHP,LJ
 */
public class MainMenuActivity extends Activity implements IBookManagerActivity {

	private SharedPreferences preferences;
	private Context context;
	private String name;
	private String pwd;
	private AutoCompleteTextView _id;
	private EditText  _pwd;
	private LinearLayout login_status_ll;
	private ScrollView login_form_sv;
	private boolean islogin = false;

	private int width,height;
//	private SurfaceView main_anim_background;
//	private SurfaceHolder sh;
//	private Timer mtr;
//	private Bitmap b1, b2;
//	private float postx;
//	private int drawbackgroud = 0;
	private Dialog dialog;
//	TimerTask task = new TimerTask() {
//		public void run() {
//			drawbackgroud();
//		}
//	};

//	private void drawbackgroud() {
//		if (main_anim_background.isShown()) {
//			try {
//				Canvas canvas = sh.lockCanvas();
//				postx += 0.5;
//				postx = postx % (b1.getWidth() - width);
//				Paint bmpp = new Paint();
//				Matrix ma = new Matrix();
//				// ma.postScale(3, 3);
//				ma.postTranslate(postx * -1, 0);
//
//				canvas.drawBitmap(b1, ma, bmpp);
//				sh.unlockCanvasAndPost(canvas);
//			} catch (Exception e) {
//				// TODO: handle exception
//			}
//		}
//	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		Intent intent = new Intent(MainMenuActivity.this,
				WelcomeActivity.class);
		startActivity(intent);
		
		Display display = getWindowManager().getDefaultDisplay();
		width = display.getWidth();
		height=display.getHeight();				
		setContentView(R.layout.activity_main);
		context = this;
		ManagerService.allActivity.add(this);
		 init_drawer();
		// ��ȡSharedPreferences����Ҫ������

		// preferences = getSharedPreferences("count",MODE_PRIVATE);
		//
		// int count = preferences.getInt("count", 0);
		//
		// //�жϳ�����ڼ������У�����ǵ�һ����������ת������ҳ��
		//
		// if (count == 0) {
		//
		// Editor editor = preferences.edit();
		// //��������
		// editor.putInt("count", ++count);
		// //�ύ�޸�
		// editor.commit();
		//
		// Intent intent = new Intent();
		// intent.setClass(getApplicationContext(),WelcomeActivity.class);
		// startActivity(intent);
		// finish();
		// }
		//
//		b1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.m_3);
//		main_anim_background = (SurfaceView) findViewById(R.id.main_anim_background);
//		sh = main_anim_background.getHolder();
//		mtr = new Timer();
//		mtr.schedule(task, 1000, 50);

		StableGridView gridview = (StableGridView) findViewById(R.id.grid_main);
		gridview.setAdapter(new GridViewImgAdapter(this));
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent();
				// Animation animation =
				// AnimationUtils.loadAnimation(MainMenuActivity.this,
				// R.anim.wave_scale);
				// arg1.startAnimation(animation);
				switch (position) {
				// ���ָ��
				case 0:
					intent.setClass(context, EntanceGuideActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.slide_right_in,
							R.anim.slide_left_out);
					break;
				// �ݲز�ѯ
				case 1:
					intent.setClass(context, BookSearchActivity.class);
					startActivity(intent);
					// overridePendingTransition(R.anim.slide_right_in,
					// R.anim.slide_left_out);
					break;
				// ���߷���
				case 2:
					intent.setClass(context, EBookActiviy.class);
					startActivity(intent);
					// overridePendingTransition(R.anim.slide_right_in,
					// R.anim.slide_left_out);
					break;
				// �Ƽ��Ķ�
				case 3:
					intent.setClass(context, SuggestedReadingActivity.class);
					startActivity(intent);
					// overridePendingTransition(R.anim.slide_right_in,
					// R.anim.slide_left_out);
					break;
				// �ο���ѯ
				case 4:
					intent.setClass(context, RefServiceActivity.class);
					startActivity(intent);
					// overridePendingTransition(R.anim.slide_right_in,
					// R.anim.slide_left_out);
					break;
				// ��������
				case 5:
					// ������½�Ի���
					 if (islogin) {
					 intent.setClass(context, PersonalCenterActivity.class);
					 startActivity(intent);
					 } else {
					 showLoginDialog();
					 }
//
//					intent.setClass(context, PersonalCenterActivity.class);
//					startActivity(intent);
					break;
				// ���ڹ���
				case 6:
					intent.setClass(context, AnnounceActivity.class);
					startActivity(intent);
					// overridePendingTransition(R.anim.slide_right_in,
					// R.anim.slide_left_out);
					break;
				// ���Ĺ���
				case 7:
					intent.setClass(context, BorrowAndOrderActivity.class);
					startActivity(intent);
					// overridePendingTransition(R.anim.slide_right_in,
					// R.anim.slide_left_out);
					break;
				// ����Ȧ
				case 8:
					intent.setClass(context, GroupOfReadersActivity.class);
					startActivity(intent);
					// overridePendingTransition(R.anim.slide_right_in,
					// R.anim.slide_left_out);

					break;
				// ����
				default:
					break;
				}
			}

		});

	}

	private void showLoginDialog() {
		dialog = new Dialog(context);
		dialog.setContentView(R.layout.activity_login);
		Button btn = (Button) dialog.findViewById(R.id.sign_in_button);
		_id = (AutoCompleteTextView) dialog.findViewById(R.id.ed_loginname);
		_pwd = (EditText) dialog.findViewById(R.id.ed_loginpwd);
		login_status_ll=(LinearLayout) dialog.findViewById(R.id.login_status);
		login_form_sv= (ScrollView) dialog.findViewById(R.id.login_form);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line,
				new String[]{"0441200001098"});
		_id.setThreshold(0);
		_id.setAdapter(adapter);
		
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name = _id.getText().toString().trim();
				pwd = _pwd.getText().toString();
				HashMap map = new HashMap();
				map.put("id", name);
				map.put("pwd", pwd);
				Task tsHome = new Task(Task.TASK_LOGIN, map);
				ManagerService.addNewTask(tsHome);
				// ��ʾ������
				login_status_ll.setVisibility(View.VISIBLE);
				login_form_sv.setVisibility(View.GONE);
			}
		});
		dialog.show();
	}

	@Override
	public void init() {
		// ��ʼ�� service
		// ��������Ƿ����
		if (Tool.checkNetWork(context)) {
			if (!ManagerService.isrun) {
				ManagerService.isrun = true;
				Intent it = new Intent(this, ManagerService.class);
				this.startService(it);
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	public class GridViewImgAdapter extends BaseAdapter {

		private Context mContext;

		// ������������ ��ͼƬԴ

		private Integer[] mImageIds = { R.drawable.sy_anniu_03,
				R.drawable.sy_anniu_05, R.drawable.sy_anniu_07,
				R.drawable.sy_anniu_12, R.drawable.sy_anniu_13,
				R.drawable.sy_anniu_14, R.drawable.sy_anniu_18,
				R.drawable.sy_anniu_19, R.drawable.sy_anniu_20 };

		private int[] mTitle = { R.string.main_guide, R.string.main_search,
				R.string.main_ebook, R.string.main_readingguide,
				R.string.main_bookcomment, R.string.main_ebookstore,
				R.string.main_notice, R.string.main_borrow, R.string.main_order

		};

		public GridViewImgAdapter(Context c) {
			mContext = c;
		}

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

		// ��ȡͼƬID
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("NewApi")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				// ��ImageView������Դ
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.item_main_menu, null);
				ImageView img = (ImageView) convertView
						.findViewById(R.id.img_main);
				TextView tx = (TextView) convertView
						.findViewById(R.id.txt_main);
				tx.setText(getString(mTitle[position]));
				img.setImageResource(mImageIds[position]);
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

			return convertView;
		}

	}

	@Override
	public void refresh(Object... obj) {
		// ȡ��������
		Result res = (Result) obj[0];
		if (res.getSuccess()) {
			islogin = true;
			if(dialog.isShowing()){
				dialog.dismiss();
			}
			User user =(User)obj[0];
			GlobleData.userid = user.getCardno();
			// ��ʾ��½�ɹ�
			Tool.ShowMessages(context,"��½�ɹ�");
		} else {
			islogin = false;
			// ��ʾ��½ʧ��
			Tool.ShowMessages(context, res.getMessage());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// ֹͣ����
		// �˳�Service
		context.stopService(new Intent("com.cqvip.moblelib.mainbiz"));
		// �ر����߳�
		ManagerService.isrun = false;

	}

	// ����
	private SlidingDrawer sd;
	private ImageView iv;
	private ListView lv;
	private static final String[] PHOTOS_NAMES = new String[] { "Lyon",
			"Livermore", "Tahoe Pier", "Lake Tahoe", "Grand Canyon", "Bodie" };

	private void init_drawer() {
		lv = (ListView) findViewById(R.id.myContent);
		sd = (SlidingDrawer) findViewById(R.id.sd);
		iv = (ImageView) findViewById(R.id.iv);
		lv.setLayoutParams(new ListView.LayoutParams(width, height/2));
		// MyAdapter adapter=new
		// MyAdapter(this,items,icons);//�Զ���MyAdapter��ʵ��ͼ���item����ʾЧ��
		lv.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, PHOTOS_NAMES));
		sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// ������
		{
			@Override
			public void onDrawerOpened() {
				iv.setImageResource(R.drawable.person_new_icon);// ��Ӧ�������¼�
																// ����ͼƬ��Ϊ���µ�
			}
		});
		sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				iv.setImageResource(R.drawable.person_new_icon);// ��Ӧ�س����¼�
			}
		});
	}

}
