package com.cqvip.moblelib.net;


import java.net.URLEncoder;


import android.util.Log;

public class NetUtil {

	/**
	 * get请求url
	 * @param param
	 * @return
	 */
	public static String encodeUrl(BookParameters param){
		if(param == null){
			return "";
		}
		
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(int i = 0;i<param.size();i++){
			if(first){
				first = false;
			}else{
			builder.append("&");
			}
			String key = param.getKey(i);
			String value = param.getValue(i);
			if(value==null){
			    Log.i("encodeUrl", "key:"+key+"value is null");
			}
			else{
			    builder.append(key + "="
                        + value);
			}
			
		}
		return builder.toString();
	}
	
	/**
	 * post请求参数
	 * @param param
	 * @return
	 */
	public static String encodeParameters(BookParameters param) {
		if (null == param) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		boolean first = true;
		for(int i = 0;i<param.size();i++){
			String key = param.getKey(i);
			if(first){
				first = false;
			}else{
			builder.append("&");
			}
			//				builder.append(URLEncoder.encode(key, "UTF-8")).append("=")
//						.append(URLEncoder.encode(param.getValue(key), "UTF-8"));
			builder.append(key).append("=")
			.append(param.getValue(key));
		}
		return builder.toString();

	}
}
