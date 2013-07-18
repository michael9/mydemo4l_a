package com.cqvip.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.cqvip.moblelib.activity.CommentActivity;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.EBook;

/**
 * �����ڣ������ַ����������ַ������ַ���ת��
 * 
 * @author lj 20121128
 * 
 */
public class Tool {

	
	public static void bookEshare(Context mcontext, EBook mbook) {
		if (mbook != null) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_SUBJECT, "ͼ�����");
			intent.putExtra(Intent.EXTRA_TEXT,
					("�������:" + mbook.getTitle_c() + "\r\n����:" + mbook.getWriter()
							+ "\r\n��������:"
							+ mbook.getYears() + "\r\nISBN:" ));
			intent.putExtra(Intent.EXTRA_STREAM,
					Uri.decode("http://www.szlglib.com.cn/images/logo.jpg")); // ����ͼƬ"http://www.szlglib.com.cn/images/logo.jpg"
			mcontext.startActivity(Intent.createChooser(intent, "����"));
		}
	}

	// ����
	public static void bookEbuzz(Context mcontext, EBook mbook) {
		if (mbook != null) {
//			Intent intent = new Intent(mcontext, CommentActivity.class);
//			Bundle bundle = new Bundle();
//			bundle.putSerializable("book", mbook);
//			intent.putExtra("detaiinfo", bundle);
//			mcontext.startActivity(intent);
		}
	}


	// ����
	public static void bookshare(Context mcontext, Book mbook) {
		if (mbook != null) {
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("image/*");
			intent.putExtra(Intent.EXTRA_SUBJECT, "ͼ�����");
			intent.putExtra(Intent.EXTRA_TEXT,
					("�������:" + mbook.getTitle() + "\r\n����:" + mbook.getAuthor()
							+ "\r\n������:" + mbook.getPublisher() + "\r\n��������:"
							+ mbook.getPublishyear() + "\r\nISBN:" + mbook
							.getIsbn()));
			intent.putExtra(Intent.EXTRA_STREAM,
					Uri.decode("http://www.szlglib.com.cn/images/logo.jpg")); // ����ͼƬ"http://www.szlglib.com.cn/images/logo.jpg"
			mcontext.startActivity(Intent.createChooser(intent, "����"));
		}
	}

	// ����
	public static void bookbuzz(Context mcontext, Book mbook) {
		if (mbook != null) {
			Intent intent = new Intent(mcontext, CommentActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("book", mbook);
			intent.putExtra("detaiinfo", bundle);
			mcontext.startActivity(intent);
		}
	}
	
	// ��ȡĳ���鼮�������������
	public static void getCommentList(Context mcontext, Book mbook,int type) {
		if (mbook != null) {
			Intent intent = new Intent(mcontext, CommentActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("book", mbook);
			intent.putExtra("detaiinfo", bundle);
			intent.putExtra("type", type);
			mcontext.startActivity(intent);
		}
	}
//	// �ղ�
//	public static Map<String, String> bookfavorite(Map<String, String> params,Book mbook) {
//			params=new HashMap<String, String>();
//			params.put("libid",  GlobleData.LIBIRY_ID);
//			params.put("vipuserid", GlobleData.cqvipid);
//			params.put("typeid", ""+GlobleData.BOOK_SZ_TYPE);
//			params.put("keyid", Tool.formSZbookID(mbook.getCallno(),mbook.getRecordid()));
//		return params;
//	}
//	// �ղ�
//	public static  Map<String, String> bookEfavorite(Map<String, String> params, EBook mbook) {
//		params=new HashMap<String, String>();
//		params.put("libid",  GlobleData.LIBIRY_ID);
//		params.put("vipuserid", GlobleData.cqvipid);
//		params.put("typeid", ""+GlobleData.BOOK_ZK_TYPE);
//		params.put("keyid", mbook.getLngid());
//		return params;
//	}
	/**
	 * Toast��ʾ
	 * 
	 * @param context
	 * @param msg
	 */
	public static void ShowMessages(Context context, String msg) {
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
	}

	public static void ShowMessagel(Context context, String msg) {
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
		if (networkInfo != null && networkInfo.isConnected()) {
			netWorkIsOK = true;
		} else {
			Toast.makeText(context, "����ʧ�ܣ��������磡", Toast.LENGTH_LONG).show();
		}
		return netWorkIsOK;
	}

	/**
	 * MD5����
	 * 
	 * @param plainText
	 * @return
	 */
	public static String md5Add(String plainText) {
		StringBuffer buf = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			int i;
			buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	/**
	 * isbn������ʽƥ��
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isbnMatch(String str) {
		Pattern pattern = Pattern
				.compile("[0-9]{3}\\-?[0-9]{1}\\-?[0-9]{4}\\-?[0-9]{4}\\-?[0-9]{1}");
		Matcher matcher = pattern.matcher(str);
		return matcher.find();
	}
	/**
	 * ƴ�������ַ���
	 * @param callno
	 * @param recordid
	 * @return
	 */
	public static String formSZbookID(String callno,String recordid){
		String result = callno+","+recordid;
		return result;
	}
	/**
	 * ���� ʱ����ʽ ����Сʱǰ
	 * @param date
	 * @return
	 */
	public static String GetTime(Date date){
		long   lSubTime = 0;
		String strTime = "";
		Date curTime = new Date();
		lSubTime = curTime.getTime() - date.getTime();
		lSubTime = lSubTime/(60 * 1000);
		if(lSubTime < 1){
			lSubTime = 1;
		}
		if(lSubTime < 60){
			strTime = "" + lSubTime + "����ǰ";
		}else if(lSubTime >= 60 && lSubTime < (24*60)){
			strTime = "" + lSubTime/60 + "Сʱǰ";
		}else{
			try {
				SimpleDateFormat df = (SimpleDateFormat) new SimpleDateFormat(
						"MM-dd hh:mm");
				strTime = df.format(date);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return strTime;
	}

	/**
	 *  ��ȡ��ͼƬurl
	 * @param strr
	 * @return
	 */
	 public static String getBigImg(String strr){
		   int i = strr.indexOf("spic",0);
		  return strr.substring(0,i) +"l"+ strr.substring(i+1);
	   }
}
