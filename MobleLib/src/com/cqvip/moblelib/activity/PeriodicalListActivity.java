package com.cqvip.moblelib.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.R.layout;
import com.cqvip.moblelib.R.menu;
import com.cqvip.moblelib.adapter.EbookAdapter;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.utils.Tool;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ģ���ڿ��б���ʾ
 * @author luojiang
 *
 */
public class PeriodicalListActivity extends BaseActivity implements
		OnItemClickListener {

	public static final int GETFIRSTPAGE = 1;
	public static final int GETNEXTPAGE = 2;
	public static final int FAVOR = 3;
	public static final int DEFAULT_COUNT = 10;
	private TextView searchCount;
	private Context context;
	private ListView listview;
	// private String editekey;
	private int page = 1;
	private EbookAdapter adapter;
	private RelativeLayout noResult_rl;
	private View title_bar;
	// private ImageFetcher mImageFetcher;
	private Map<String, String> gparams;
	public static HashMap<String, Boolean> favors = new HashMap<String, Boolean>();// �����ղ�״̬�����½���

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_periodical_list);
		context = this;
		searchCount = (TextView) findViewById(R.id.txt_total_esearch);
		listview = (ListView) findViewById(R.id.search_res_lv);
		listview.setOnItemClickListener((OnItemClickListener) this);
		noResult_rl = (RelativeLayout) findViewById(R.id.noresult_rl);

		title_bar = findViewById(R.id.head_bar);
		TextView title = (TextView) title_bar.findViewById(R.id.txt_header);
		title.setText(R.string.main_ebook);
		ImageView back = (ImageView) title_bar
				.findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		getHomePage("������Щ��", page, DEFAULT_COUNT, 0);
	}


	Listener<String> backlistener = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			try {
				// ��ȡ���ؼ�¼��
				int count = EBook.ebookCount(response);
				if (count > 0) {
					searchCount.setVisibility(View.VISIBLE);
					searchCount.setText("����������" + count + "����¼");
				} else {
					searchCount.setVisibility(View.GONE);
				}
				// JSONObject mj=new JSONObject(response);
				List<EBook> lists = EBook.formList(response);
				if (lists != null && !lists.isEmpty()) {
					listview.setVisibility(View.VISIBLE);
					noResult_rl.setVisibility(View.GONE);
					adapter = new EbookAdapter(context, lists, mQueue);
					listview.setAdapter(adapter);
				} else {
					listview.setVisibility(View.GONE);
					noResult_rl.setVisibility(View.VISIBLE);
				}
			} catch (Exception e) {

				e.printStackTrace();
			}

		}
	};

	Listener<String> backlistenermore = new Listener<String>() {
		@Override
		public void onResponse(String response) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();
			moreprocess.setVisibility(View.GONE);
			try {
				// JSONObject mj=new JSONObject(response);
				List<EBook> lists = EBook.formList(response);
				if (lists != null && !lists.isEmpty()) {
					adapter.addMoreData(lists);
				} else {
					Tool.ShowMessages(context, "û�и������ݿɹ�����");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

		}
	};

	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			customProgressDialog.dismiss();

		}
	};

	private void requestVolley(String addr, Listener<String> bl, int method) {
		try {
			StringRequest mys = new StringRequest(method, addr, bl, el) {
				protected Map<String, String> getParams()
						throws com.android.volley.AuthFailureError {
					return gparams;
				};
			};
			mQueue.add(mys);
			mQueue.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �������磬��ȡ����
	 * 
	 * @param key
	 * @param page
	 * @param count
	 */
	private void getHomePage(String key, int page, int count, int type) {
		gparams = new HashMap<String, String>();
		gparams.put("title", key);
		gparams.put("curpage", "" + page);// ��ǰҳ��
		gparams.put("perpage", "" + count);// ����

		if (type == 0) {
			requestVolley(GlobleData.SERVER_URL + "/zk/search.aspx",
					backlistener, Method.POST);
		} else {
			requestVolley(GlobleData.SERVER_URL + "/zk/search.aspx",
					backlistenermore, Method.POST);
		}
	}

	// @Override
	// public void refresh(Object... obj) {
	// customProgressDialog.dismiss();
	// hideKeybord();
	// //��ʾ
	// int type = (Integer)obj[0];
	//
	// //�ж��ղ��Ƿ�ɹ�
	// if(type == FAVOR){
	// Result res = (Result) obj[1];
	// if (res.getSuccess()) {
	// Tool.ShowMessages(context, "�ղسɹ�");
	// }else{
	// Tool.ShowMessages(context, "�ղ�ʧ��");
	// }
	// return;
	// }
	//
	// List<EBook> lists = (List<EBook>)obj[1];
	// if(type == GETFIRSTPAGE ){
	// if(lists!=null&&!lists.isEmpty()){
	// listview.setVisibility(View.VISIBLE);
	// noResult_rl.setVisibility(View.GONE);
	// adapter = new EbookAdapter(context,lists,mImageFetcher);
	// listview.setAdapter(adapter);
	//
	// }else{
	// listview.setVisibility(View.GONE);
	// noResult_rl.setVisibility(View.VISIBLE);
	// }
	// }else if(type == GETNEXTPAGE){
	// if(lists!=null&&!lists.isEmpty()){
	// adapter.addMoreData(lists);
	// }else{
	// Tool.ShowMessages(context, "û�и������ݿɹ�����");
	// }
	// }
	// }
	View moreprocess;

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int positon, long id) {
		if (id == -2) // ����
		{
			// ������
			moreprocess = arg1.findViewById(R.id.footer_progress);
			moreprocess.setVisibility(View.VISIBLE);
			// �����������
			getHomePage("������Щ��", page + 1,
					DEFAULT_COUNT, 1);
			page = page + 1;
		} else {
			EBook book = adapter.getLists().get(positon);
			context.startActivity(new Intent(context,PeriodicalContentActivity.class));
			// Book book = lists.get(position-1);
			// if(book!=null){
			// Bundle bundle = new Bundle();
			// bundle.putSerializable("book", book);
			// _intent.putExtra("detaiinfo", bundle);
			// startActivityForResult(_intent, 1);
		}
	}

}