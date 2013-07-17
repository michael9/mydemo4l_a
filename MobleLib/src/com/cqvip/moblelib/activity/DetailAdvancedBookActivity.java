package com.cqvip.moblelib.activity;

import java.util.Map;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cqvip.moblelib.R;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.constant.Constant;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.view.CustomProgressDialog;

public class DetailAdvancedBookActivity extends BaseActivity{

	private TextView content;
	private int type;
	private String id;
	private Map<String, String> gparams;
	private Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_advanced_book);
		context = this;
		content = (TextView)findViewById(R.id.ad_book_content);
		type = getIntent().getIntExtra("type",1);
		id = getIntent().getStringExtra("id");
		if(type == Constant.QUESTION){
			content.setText(id);
			setheadbar(getResources().getString(R.string.title_FAQ));
		}else{
		customProgressDialog.show();
		getContent(id);
		setheadbar(getResources().getString(R.string.title_moredetail));
		}
	}
	
	private void setheadbar(String title)
	{
		View headbar,btn_back;
		TextView bar_title;
		//customProgressDialog=CustomProgressDialog.createDialog(context);
		headbar=findViewById(R.id.head_bar);
		bar_title=(TextView)headbar.findViewById(R.id.txt_header);
		bar_title.setText(title);
		btn_back=headbar.findViewById(R.id.img_back_header);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
	}

	private void getContent(String id2) {
	
		String serverurl = GlobleData.SERVER_URL + "/library/announce/detail.aspx?libid=1&announceid="+id2;
		JsonObjectRequest myReq = new JsonObjectRequest(Method.GET,serverurl,null,createDetailListener(),createMyReqErrorListener());
		mQueue.add(myReq);
	}
	private Response.Listener<JSONObject> createDetailListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject json) {
            	customProgressDialog.dismiss();
            	try {
    				if (json.getString("success").equalsIgnoreCase("true")) {
    					content.setText(Html.fromHtml(json.getString("contents")));
    				}
    			} catch (Exception e) {
    				content.setText(getResources()
    						.getString(R.string.loadfail));
    			}

            	
            	
            }
        };
    }
    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
        		customProgressDialog.dismiss();
        		content.setText(getResources()
						.getString(R.string.loadfail));
            }
        };
    }
}
