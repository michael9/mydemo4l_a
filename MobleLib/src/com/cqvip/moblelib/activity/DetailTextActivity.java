package com.cqvip.moblelib.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cqvip.moblelib.longgang.R;
import com.cqvip.moblelib.constant.GlobleData;

public class DetailTextActivity extends BaseActivity {
	private int type;
	private TextView t1, content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_text);
		View v = findViewById(R.id.seach_title);
		t1 = (TextView) v.findViewById(R.id.txt_header);
		ImageView back = (ImageView) v.findViewById(R.id.img_back_header);
		// ManagerService.allActivity.add(this);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// title = (TextView)findViewById(R.id.title_txt);
		content = (TextView) findViewById(R.id.content_txt);
		type = getIntent().getIntExtra("enter", 0);

		customProgressDialog.show();
		switch (type) {
		case 1:
			t1.setText(R.string.guide_needknow);
			requestVolley(GlobleData.SERVER_URL
					+ "/library/guide/notice.aspx?libid=1", mj, null,
					Method.GET);
			break;
		case 2:
			t1.setText(R.string.guide_cardguide);
			requestVolley(GlobleData.SERVER_URL
					+ "/library/guide/cardguide.aspx?libid=1", mj, null,
					Method.GET);
			break;
		case 3:
			t1.setText(R.string.guide_time);
			requestVolley(GlobleData.SERVER_URL
					+ "/library/guide/time.aspx?libid=1", mj, null, Method.GET);
			break;
		case 4:
			t1.setText(R.string.guide_readerknow);
			requestVolley(GlobleData.SERVER_URL
					+ "/library/guide/reader.aspx?libid=1", mj, null,
					Method.GET);
			break;
		case 5:
			t1.setText(R.string.guide_server);
			requestVolley(GlobleData.SERVER_URL
					+ "/library/guide/service.aspx?libid=1", mj, null,
					Method.GET);
			break;
		case 6:
			t1.setText(R.string.guide_transport);
			// title.setText("");
			// content.setText("");
			break;
		// case 7:
		//
		// break;
		case 8:
			t1.setText(R.string.guide_problem);
			content.setText("");
			break;

		}

	}

	Listener<JSONObject> mj = new Listener<JSONObject>() {
		@Override
		public void onResponse(JSONObject arg0) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			try {
				if (arg0.getString("success").equalsIgnoreCase("true")) {
					content.setText(Html.fromHtml(arg0.getString("contents")));
				}
			} catch (Exception e) {
				// TODO: handle exception
				content.setText(DetailTextActivity.this.getResources()
						.getString(R.string.loadfail));
			}

		}
	};

	ErrorListener el = new ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError arg0) {
			// TODO Auto-generated method stub
			if(customProgressDialog!=null&&customProgressDialog.isShowing())
			customProgressDialog.dismiss();
			content.setText(DetailTextActivity.this.getResources().getString(
					R.string.loadfail));
		}
	};

	private void requestVolley(String addr, Listener<JSONObject> mj,
			JSONObject js, int method) {

		try {
			JsonObjectRequest myjson = new JsonObjectRequest(method, addr, js,
					mj, el);
			mQueue.add(myjson);
			mQueue.start();
		} catch (Exception e) {
			onError(2);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detail_text, menu);
		return true;
	}
}
