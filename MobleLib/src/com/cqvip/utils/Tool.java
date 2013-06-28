package com.cqvip.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * 工具内，构造字符串，解析字符串，字符串转换
 * @author lj 20121128
 *
 */
public class Tool {
	/**
	 * Toast提示
	 * @param context
	 * @param msg
	 */
   public static void ShowMessages(Context context,String msg){
	   Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
   }
   public static void ShowMessagel(Context context,String msg){
	   Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
   }
   /**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean checkNetWork(Context context) {
		boolean netWorkIsOK = false;
		ConnectivityManager connectManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectManager.getActiveNetworkInfo();
		if (networkInfo != null&&networkInfo.isConnected()) {
			netWorkIsOK = true;
		} else {
			Toast.makeText(context, "联网失败，请检查网络！", Toast.LENGTH_LONG)
					.show();
		}
		return netWorkIsOK;
	}
   
   /**
  	 * MD5加密
  	 * @param plainText
  	 * @return
  	 */
  	public static String md5Add(String plainText) { 
  		StringBuffer buf=null;
  		try { 
  			MessageDigest md = MessageDigest.getInstance("MD5"); 
  			md.update(plainText.getBytes()); 
  			byte b[] = md.digest(); 
  			int i; 
  			buf = new StringBuffer(""); 
  			for (int offset = 0; offset < b.length; offset++) { 
  				i = b[offset]; 
  				if(i<0) i+= 256; 
  				if(i<16) 
  				buf.append("0"); 
  				buf.append(Integer.toHexString(i)); 
  			} 
  		} catch (NoSuchAlgorithmException e) { 
  			e.printStackTrace(); 
  		} 
  		return buf.toString();
  	}
  	/**
  	 * isbn正则表达式匹配
  	 * @param str
  	 * @return
  	 */
  	 public static  boolean isbnMatch(String str)
     {
  	   Pattern pattern = Pattern.compile("[0-9]{3}\\-?[0-9]{1}\\-?[0-9]{4}\\-?[0-9]{4}\\-?[0-9]{1}");
  		Matcher matcher = pattern.matcher(str);
  		return matcher.find();
     }
  
}

