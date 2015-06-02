package com.cqvip.moblelib.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cqvip.moblelib.ahslsd.R;


public class WebViewAnnouceActivity extends BaseActivity{
	
	 private WebView myWebView;  
	 private ProgressDialog dialog;
//	private static final String targenUrl = "http://webcld.duapp.com/tmp/ahsdsl_newslist.html";
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_webview);
       customProgressDialog.show();
        myWebView = (WebView) findViewById(R.id.webview);
        myWebView.setWebViewClient(new WebViewClient(){
        	  @Override
              public void onPageFinished(WebView view,String url)
              {
        		  if(customProgressDialog!=null&&customProgressDialog.isShowing()){
        		  customProgressDialog.dismiss();
        		  }
              }
             });
        	
       myWebView.loadUrl(getIntent().getStringExtra("urlstr"));
       WebSettings webSettings = myWebView.getSettings();
       webSettings.setJavaScriptEnabled(true);
       
   }
   
   public void loadUrl(String url) 
   { 
       if(myWebView != null) 
       { 
    	   myWebView.loadUrl(url); 
           myWebView.reload(); 
       } 
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
