package com.cqvip.moblelib.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.net.BookException;

/**
 * 
 * @author luojiang
 *
 */
public class ShortBook {

	private String sucesss;//成功失败
	private String id;//字段1
	private String date;//字段2
	private String message;//提示信息
	
	public ShortBook(int type,String result) throws BookException{
		switch(type){
		case Task.TASK_BOOK_RENEW:
			try {
				JSONObject js = new JSONObject(result);
				JSONObject json = js.getJSONObject("renewinfo");
				sucesss = js.getString("success");
				id = json.getString("barcode");
				date = json.getString("returndate");
				message = json.getString("returnmessage");
			} catch (JSONException e) {
				throw new BookException(e);
			}
			break;
		case Task.TASK_EBOOK_DOWN:
			try {
				JSONObject json = new JSONObject(result);
				date = json.getString("svrurl");
				message = json.getString("svrname");
			} catch (JSONException e) {
				throw new BookException(e);
			}
			break;
		}
		
	}
	
	public static List<ShortBook> formList(int type,String result) throws BookException{
		
	    List<ShortBook> books = null;
	try {
		JSONObject json = new JSONObject(result);
	     if(!json.getBoolean("success")){
	    	 return null;
	     }
		JSONArray ary = json.getJSONArray("serverinfo");
		 int count = ary.length();
		 if(count <=0){
			 return null;
		 }
		 books = new ArrayList<ShortBook>(count);
		 for(int i = 0;i<count;i++){
			 books.add(new ShortBook(type,ary.getJSONObject(i).toString()));
		 }
		 return books;
	} catch (JSONException e) {
		throw new BookException(e);
	}
	
		
	}
	
	
	public String getSucesss() {
		return sucesss;
	}

	public String getId() {
		return id;
	}

	public String getDate() {
		return date;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "ShortBook [sucesss=" + sucesss + ", id=" + id + ", date="
				+ date + ", message=" + message + "]";
	}
	
	
	
}
