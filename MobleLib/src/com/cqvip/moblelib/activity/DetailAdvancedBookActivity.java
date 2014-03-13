package com.cqvip.moblelib.activity;

import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cqvip.moblelib.xxu.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.view.CustomProgressDialog;

public class DetailAdvancedBookActivity extends BaseActivity {

	private TextView title, content;
	private int type;
	private String id, bookname;
	private Map<String, String> gparams;
	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_advanced_book);
		context = this;
		content = (TextView) findViewById(R.id.ad_book_content);
		title = (TextView) findViewById(R.id.ad_book_title);
		type = getIntent().getIntExtra("type", 1);
		id = getIntent().getStringExtra("id");
		// bookname=getIntent().getStringExtra("bookname");
		
		switch (type) {
		case  Constant.QUESTION:
			content.setText(id);
			setheadbar(getResources().getString(R.string.title_FAQ));
			break;
			
		case  Constant.INNERPAY:
			content.setText(Html.fromHtml(id));
			setheadbar(getResources().getString(R.string.title_moredetail));
			break;

		default:
			customProgressDialog.show();
			getContent(id);
			setheadbar(getResources().getString(R.string.title_moredetail));
			break;
		}
//		if (type == Constant.QUESTION) {
//			content.setText(id);
//			setheadbar(getResources().getString(R.string.title_FAQ));
//		} else {
//			customProgressDialog.show();
//			getContent(id);
//			setheadbar(getResources().getString(R.string.title_moredetail));
//		}
	}

	private void setheadbar(String title) {
		View headbar, btn_back;
		TextView bar_title;
		// customProgressDialog=CustomProgressDialog.createDialog(context);
		headbar = findViewById(R.id.head_bar);
		bar_title = (TextView) headbar.findViewById(R.id.txt_header);

		if (type == Constant.NEWBOOK || type == Constant.HOTBOOK) {
			ImageView btn_right_header = (ImageView) findViewById(R.id.btn_right_header);
			btn_right_header.setImageResource(R.drawable.btn_selector_sreach);
			btn_right_header.setVisibility(View.VISIBLE);
			btn_right_header.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!bookname.isEmpty()) {
						Intent intent = new Intent(
								DetailAdvancedBookActivity.this,
								ResultOnSearchActivity.class);
						intent.putExtra("isfromDetailAdvancedBookActivity",
								true);
						intent.putExtra("bookname", bookname);
						startActivity(intent);
					}
				}
			});
		}

		bar_title.setText(title);
		btn_back = headbar.findViewById(R.id.img_back_header);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void getContent(String id2) {

		String serverurl = GlobleData.SERVER_URL
				+ "/library/announce/detail.aspx?libid=1&announceid=" + id2;
		JsonObjectRequest myReq = new JsonObjectRequest(Method.GET, serverurl,
				null, createDetailListener(), createMyReqErrorListener());
		mQueue.add(myReq);
	}

	private Response.Listener<JSONObject> createDetailListener() {
		return new Response.Listener<JSONObject>() {
			/**
			 * @param json
			 */
			@Override
			public void onResponse(JSONObject json) {
				if (customProgressDialog != null
						&& customProgressDialog.isShowing())
					customProgressDialog.dismiss();
				try {
					if (json.getString("success").equalsIgnoreCase("true")) {
						content.setText(Html.fromHtml(json
								.getString("contents")));
//						title.setText(bookname = json.getString("title"));
					}
				} catch (Exception e) {
					content.setText(getResources().getString(R.string.loadfail));
				}

			}
		};
	}

	private Response.ErrorListener createMyReqErrorListener() {
		return new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				if (customProgressDialog != null
						&& customProgressDialog.isShowing())
					customProgressDialog.dismiss();
				content.setText(getResources().getString(R.string.loadfail));
			}
		};
	}
}
