package com.cqvip.moblelib.activity;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.szy.R;
import com.cqvip.utils.BitmapCache;
import com.cqvip.utils.Tool;

public class DetailTextActivity extends BaseActivity {
	private int type;
	private TextView t1, content;
	private LinearLayout piclayouy;
	private Context context;
	private ImageLoader mImageLoader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_text);
		context = this;
		View v = findViewById(R.id.seach_title);
		t1 = (TextView) v.findViewById(R.id.txt_header);
		ImageView back = (ImageView) v.findViewById(R.id.img_back_header);
		piclayouy = (LinearLayout) findViewById(R.id.ll_pic_layout);
		if(cache==null){
			cache = new BitmapCache(Tool.getCachSize());
		}
	    mImageLoader = new ImageLoader(mQueue, cache);
		
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
					+ "/library/guide/notice.aspx?libid=2", mj, null,
					Method.GET);
			break;
		case 3:
			t1.setText(R.string.guide_time);
			requestVolley(GlobleData.SERVER_URL
					+ "/library/guide/time.aspx?libid=2", mj, null, Method.GET);
			break;
		case 5:
			t1.setText(R.string.guide_server);
			requestVolley(GlobleData.SERVER_URL
					+ "/library/guide/service.aspx?libid=2", mj, null,
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
					if(!arg0.isNull("contentImgUrl")){
					String [] imgs = getImgs(arg0.getString("contentImgUrl"));
					if(imgs!=null&&imgs.length>0){
						for(int i=0;i<imgs.length;i++){
							ImageView img = new ImageView(context);
							ImageListener listener = ImageLoader.getImageListener(img,
									R.drawable.defaut_book, R.drawable.defaut_book);
					      	ImageContainer imageContainer=mImageLoader.get(imgs[i], listener);
					      	Bitmap bitmap = imageContainer.getBitmap();
					      	if(bitmap!=null){
							img.setImageBitmap(imageContainer.getBitmap());
							piclayouy.addView(img);
					      	}
						}
					  }
					}
				}
			} catch (Exception e) {
			    e.printStackTrace();
				content.setText(DetailTextActivity.this.getResources()
						.getString(R.string.loadfail));
			}
		}

		private String[] getImgs(String string) {
			String[] array = null ;
			if(!TextUtils.isEmpty(string)){
				array = string.split(";");
				return array;
			}
			return array;
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
