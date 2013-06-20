package com.cqvip.moblelib.model;

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
	
	
	
}
