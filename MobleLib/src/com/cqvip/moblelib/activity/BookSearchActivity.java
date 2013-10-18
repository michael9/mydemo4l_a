package com.cqvip.moblelib.activity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.db.SearchHistoryDao;
import com.cqvip.moblelib.entity.SearchHistory_SZ;
import com.cqvip.moblelib.nanshan.R;
import com.cqvip.moblelib.scan.CaptureActivity;
import com.cqvip.moblelib.view.KeywordsView;

/**
 * <p>
 * �ļ�����: BookSearchActivity.java �ļ�����: �ݲز�ѯ ��Ȩ����: ��Ȩ����(C)2013-2020 �� ˾:
 * ����ά����ѯ���޹�˾ ����ժҪ: �������������������ѯ ����˵��: ������ڣ� 201��5��10�� �޸ļ�¼:
 * </p>
 * 
 * @author LHP,LJ
 */
public class BookSearchActivity extends BaseActivity implements
		View.OnClickListener {

	private Context context;
	private ImageView scan_iv, refresh_iv;
	private EditText editText;

	private String[] totalKeys = null;
	private String[] key_words = new String[15];
	private KeywordsView showKeywords = null;
	private LinearLayout searchLayout = null;
	private GestureDetector mggd;
	/**
	 * �ж�������ҳ�滹����ҳ��
	 */
	private boolean isOutter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_search1);
		context = this;
		ImageView back = (ImageView) findViewById(R.id.return_iv);
		ImageView search = (ImageView) findViewById(R.id.search_seach_btn);
		scan_iv = (ImageView) findViewById(R.id.scan_iv);
		editText = (EditText) findViewById(R.id.search_et);
		hideinputmethod();

		//������ʷ
		searchLayout = (LinearLayout) this.findViewById(R.id.searchContent);
		refresh_iv = (ImageView) findViewById(R.id.refresh_iv);
		showKeywords = (KeywordsView) this.findViewById(R.id.word);
		showKeywords.setDuration(2000l);
		showKeywords.setOnClickListener(this);
		this.mggd = new GestureDetector(new Mygdlinseter());
		showKeywords.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return mggd.onTouchEvent(event); // ע�����¼�
			}
		});
		isOutter = true;
		handler.sendEmptyMessage(Msg_Start_Load);

		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BookSearchActivity.this,
						ResultOnSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});

		scan_iv.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BookSearchActivity.this,
						CaptureActivity.class);
				startActivityForResult(intent, 100);
			}
		});

		editText.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				Intent intent = new Intent(context,
						ResultOnSearchActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}

	List<SearchHistory_SZ> list_SearchHistory_SZ;

	private void getSearchHistoryfromDatabase() {
		SearchHistory_SZ searchHistory_SZ = new SearchHistory_SZ();
		SearchHistoryDao<SearchHistory_SZ> searchHistoryDao = new SearchHistoryDao<SearchHistory_SZ>(
				this, searchHistory_SZ);
		try {
			list_SearchHistory_SZ = searchHistoryDao.queryInfobydate();
			if (list_SearchHistory_SZ != null) {
				int i = 0;
				for (SearchHistory_SZ element : list_SearchHistory_SZ) {
					Log.i("BookSearchAct" + i++, element.getName());
				}
			}
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	private String[] getRandomArray() {
		List<String> list_str = new ArrayList<String>();
		if(list_SearchHistory_SZ!=null)
		for (SearchHistory_SZ sh : list_SearchHistory_SZ) {
			list_str.add(sh.getName());
		}
		int size = list_str.size();
		return list_str.toArray(new String[size]);
	}

	private static final int Msg_Start_Load = 0x0102;
	private static final int Msg_Load_End = 0x0203;

	private LoadKeywordsTask task = null;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Msg_Start_Load:

				task = new LoadKeywordsTask();
				new Thread(task).start();

				break;
			case Msg_Load_End:
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				break;
			}

		}
	};

	private class LoadKeywordsTask implements Runnable {
		@Override
		public void run() {
			try {
				getSearchHistoryfromDatabase();
				key_words = getRandomArray();
				if (key_words.length > 0)
					handler.sendEmptyMessage(Msg_Load_End);
			} catch (Exception e) {
			}
		}
	}

	private void feedKeywordsFlow(KeywordsView keyworldFlow, String[] arr) {
		for (int i = 0; i < arr.length; i++) {
			String tmp = arr[i];
			keyworldFlow.feedKeyword(tmp);
		}
	}

	class Mygdlinseter implements OnGestureListener {
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (e2.getX() - e1.getX() > 100) { // �һ�
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_OUT);
				return true;
			}
			if (e2.getX() - e1.getX() < -100) {// ��
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				return true;
			}
			if (e2.getY() - e1.getY() < -100) {// �ϻ�
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_IN);
				return true;
			}
			if (e2.getY() - e1.getY() > 100) {// �»�
				key_words = getRandomArray();
				showKeywords.rubKeywords();
				feedKeywordsFlow(showKeywords, key_words);
				showKeywords.go2Shwo(KeywordsView.ANIMATION_OUT);
				return true;
			}
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		// System.out.println("V"+v);
		// // TODO Auto-generated method stub
		// if(isOutter){
		// isOutter = false;
		//
		// String kw = ((TextView) v).getText().toString();
		// // Log.i(TAG, "keywords = "+kw);
		// if (!kw.trim().equals("")) {
		// searchLayout.removeAllViews();
		//
		// }
		// Toast.makeText(this, "ѡ�е������ǣ�" + ((TextView) v).getText().toString(),
		// 1)
		// .show();
		// }
		switch (v.getId()) {
		case R.id.refresh_iv:
			key_words = getRandomArray();
			showKeywords.rubKeywords();
			feedKeywordsFlow(showKeywords, key_words);
			showKeywords.go2Shwo(KeywordsView.ANIMATION_OUT);
			break;

		default:
			Intent intent = new Intent(context, ResultOnSearchActivity.class);
			intent.putExtra("isfromDetailAdvancedBookActivity", true);
			intent.putExtra("bookname", ((TextView) v).getText().toString());
			startActivity(intent);
			// finish();
			break;
		}

	}

	private void hideinputmethod() {
		if (android.os.Build.VERSION.SDK_INT <= 10) {
			editText.setInputType(InputType.TYPE_NULL);
		} else {
			this.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setSoftInputShownOnFocus = cls.getMethod(
						"setSoftInputShownOnFocus", boolean.class);
				setSoftInputShownOnFocus.setAccessible(true);
				setSoftInputShownOnFocus.invoke(editText, false);
			} catch (Exception e) {
			}
		}
	}
}
