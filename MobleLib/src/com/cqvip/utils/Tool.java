package com.cqvip.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * �����ڣ������ַ����������ַ������ַ���ת��
 * @author lj 20121128
 *
 */
public class Tool {
	/**
	 * Toast��ʾ
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
	 * ��������Ƿ����
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
			Toast.makeText(context, "����ʧ�ܣ��������磡", Toast.LENGTH_LONG)
					.show();
		}
		return netWorkIsOK;
	}
   
   /**
  	 * MD5����
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
  
}

