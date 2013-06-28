package com.cqvip.moblelib.model;

/**
 * ½á¹ûÀà
 */
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

public class Result {

	private boolean success;
	private String message;
	
	public Result(String str) throws BookException{
		try {
			JSONObject json = new JSONObject(str);
			success = json.getBoolean("success");
			message = json.getString("message");
		} catch (JSONException e) {
			throw new BookException(e);
		}
	}
	
	public boolean getSuccess() {
		return success;
	}
	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "Result [success=" + success + ", message=" + message + "]";
	}
	
	
}
