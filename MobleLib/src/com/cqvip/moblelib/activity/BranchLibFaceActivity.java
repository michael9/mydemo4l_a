package com.cqvip.moblelib.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cqvip.moblelib.longgang.R;


public class BranchLibFaceActivity extends BaseActivity {
	private WebView webView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.branch_lib_face);
		
		TextView title = (TextView)findViewById(R.id.txt_header);
		title.setText(R.string.branch_lib_face);
		ImageView back = (ImageView) findViewById(R.id.img_back_header);
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				// overridePendingTransition(R.anim.slide_left_in,
				// R.anim.slide_right_out);
			}
		});
		
		webView=(WebView) findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setSupportZoom(true);
		webView.getSettings().setBuiltInZoomControls(true);
		webView.clearCache(true);
		webView.getSettings().setUseWideViewPort(true);
		 webView.getSettings().setLoadWithOverviewMode(true);
		 webView.getSettings().setPluginState(WebSettings.PluginState.ON);
		webviewState(webView);
		webView.loadUrl("http://www.szlglib.com.cn/part/");
	}
	
	void webviewState(WebView webView) {
		webView.setWebViewClient(new WebViewClient() {
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				customProgressDialog.dismiss();
				Toast.makeText(BranchLibFaceActivity.this, "Oh no! " + description,
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// TODO Auto-generated method stub
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				Log.d("ZLL", "web onPageStarted");
				customProgressDialog.show();
				//showDialog(0);
//				flag = false;
//				web_btns.setVisibility(View.GONE);
//				mstop.setVisibility(View.VISIBLE);
//				mstop.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						webView.stopLoading();
//						Log.d("ZLL", "web stop");
//					}
//				});
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				Log.d("ZLL", "web onPageFinished");
				try {
					customProgressDialog.dismiss();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				super.onPageFinished(view, url);
//				draw_open.setVisibility(View.VISIBLE);
//				mstop.setVisibility(View.GONE);
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK&&webView.canGoBack()){
			webView.goBack();
			return true;
		}
		//if (!flag) {
			return super.onKeyDown(keyCode, event);
		//}
		//return false;
	}
}
