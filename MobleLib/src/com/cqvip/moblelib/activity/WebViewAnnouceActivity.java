package com.cqvip.moblelib.activity;

import com.cqvip.moblelib.szy.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;


public class WebViewAnnouceActivity extends Activity{
	
	 private WebView myWebView;  
	private static final String targenUrl = "http://webcld.duapp.com/tmp/ahcm_newslist.html";
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        myWebView = (WebView) findViewById(R.id.webview);
       myWebView.loadUrl(targenUrl);
       WebSettings webSettings = myWebView.getSettings();
       webSettings.setJavaScriptEnabled(true);
       
   }
   @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		 if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
		        myWebView.goBack();
		        return true;
		    }
		return super.onKeyDown(keyCode, event);
	}

}
