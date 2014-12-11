package com.cqvip.mobelib.exception;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.cqvip.moblelib.ahslsd.BuildConfig;

public class ErrorVolleyThrow implements ErrorListener{

	private Context context;
	private Dialog dialog;
	public ErrorVolleyThrow(Context context,Dialog dialog){
		this.context = context;
		this.dialog = dialog;
	}
	@Override
	public void onErrorResponse(VolleyError arg0) {
			if(BuildConfig.DEBUG){
				Log.i("VolleyError","VolleyError"+arg0.toString());
			}
				if(dialog!=null&&dialog.isShowing())
					dialog.dismiss();
				if((arg0.toString()).equals("VolleyErrorcom.android.volley.TimeoutError")){
					Toast.makeText(context, "网络请求超时！", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(context, "服务器无响应", Toast.LENGTH_SHORT).show();
				}
		}
		
}
