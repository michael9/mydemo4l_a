package com.cqvip.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cqvip.moblelib.activity.CommentActivity;
import com.cqvip.moblelib.biz.ManagerService;
import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.EBook;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

	// �ղ�
	public static void bookEfavorite(Context mcontext, EBook mbook) {
		if (mbook != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("libid", GlobleData.LIBIRY_ID);
			map.put("vipuserid", GlobleData.cqvipid);
//			Log.i("�ղ�",  GlobleData.cqvipid);
			map.put("keyid", mbook.getLngid());
//			Log.i("keyid", book.getLngid());
			map.put("typeid", ""+GlobleData.BOOK_ZK_TYPE);
			ManagerService.addNewTask(new Task(Task.TASK_EBOOK_FAVOR, map));
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

	// �ղ�
	public static void bookfavorite(Context mcontext, Book mbook) {
		if (mbook != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("libid", GlobleData.LIBIRY_ID);
			map.put("vipuserid", GlobleData.cqvipid);
			// Log.i("�ղ�", GlobleData.cqvipid);
			map.put("keyid", mbook.getCallno());
			// Log.i("keyid", book.getCallno());
			map.put("typeid", "" + GlobleData.BOOK_SZ_TYPE);
			ManagerService.addNewTask(new Task(Task.TASK_LIB_FAVOR, map));
		}
	}

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

}
