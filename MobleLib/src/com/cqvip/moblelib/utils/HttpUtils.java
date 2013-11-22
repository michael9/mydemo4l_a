package com.cqvip.moblelib.utils;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RetryPolicy;
import com.cqvip.moblelib.constant.Constant;

public class HttpUtils {

	/**
	 * vollery���ó�ʱ20s
	 * @return
	 */
	public static RetryPolicy setTimeout(){
		RetryPolicy retryPolicy = new DefaultRetryPolicy(Constant.SOCKET_TIMEOUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		return retryPolicy;
	}
}
