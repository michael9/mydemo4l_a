package com.cqvip.moblelib.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

/**
 * 读者类
 * @author luojiang
 *
 */
public class Reader{
	private String username;//卡号
	private String question;//密保问题
	private String answer;//密保答案
	private String recordno;//想当与readerno
	private int cardtypeid;//类型
	private String cardtype;//
	private String status;//证状态
	private String gender;//
	private String cardno;//
	private String regdate;//注册日期
	private String name;//姓名
	private String notes;//
	private String idno;//
	private String birth;//
	private String library;//
	private String cardbegdate;//启用日期
	private String cardenddate;//终止日期
	private String address;//
	private String workunit;//
	private String phone;//
	private String mobile;//
	private String updatedate;//
	
	public Reader(JSONObject json) throws BookException {
		//super(json.toString());
		
		try {
			//JSONObject json = new JSONObject(str);
			name = json.getString("name");
			regdate = json.getString("regdate");
			username = json.getString("username");
			cardbegdate = json.getString("cardbegdate");
			status = json.getString("status");
			cardenddate = json.getString("cardenddate");
			phone = json.getString("phone");
			address = json.getString("address");
			
		} catch (JSONException e) {
			throw new BookException(e);
		}
	}

	public static Reader formReaderInfo(String result) throws BookException{
	    JSONObject dateObj;
	try {
		JSONObject json = new JSONObject(result);
	     if(!json.getBoolean("success")){
	    	 return null;
	     }
		dateObj = json.getJSONObject("readerinfo");	
		 return new Reader(dateObj);
	} catch (JSONException e) {
		throw new BookException(e);
	}
}
	
	public String getUsername() {
		return username;
	}

	public String getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	public String getRecordno() {
		return recordno;
	}

	public int getCardtypeid() {
		return cardtypeid;
	}

	public String getCardtype() {
		return cardtype;
	}

	public String getStatus() {
		return status;
	}

	public String getGender() {
		return gender;
	}

	public String getCardno() {
		return cardno;
	}

	public String getRegdate() {
		return regdate;
	}

	public String getName() {
		return name;
	}

	public String getNotes() {
		return notes;
	}

	public String getIdno() {
		return idno;
	}

	public String getBirth() {
		return birth;
	}

	public String getLibrary() {
		return library;
	}

	public String getCardbegdate() {
		return cardbegdate;
	}

	public String getCardenddate() {
		return cardenddate;
	}

	public String getAddress() {
		return address;
	}

	public String getWorkunit() {
		return workunit;
	}

	public String getPhone() {
		return phone;
	}

	public String getMobile() {
		return mobile;
	}

	public String getUpdatedate() {
		return updatedate;
	}

	@Override
	public String toString() {
		return "Reader [username=" + username + ", question=" + question
				+ ", answer=" + answer + ", recordno=" + recordno
				+ ", cardtypeid=" + cardtypeid + ", cardtype=" + cardtype
				+ ", status=" + status + ", gender=" + gender + ", cardno="
				+ cardno + ", regdate=" + regdate + ", name=" + name
				+ ", notes=" + notes + ", idno=" + idno + ", birth=" + birth
				+ ", library=" + library + ", cardbegdate=" + cardbegdate
				+ ", cardenddate=" + cardenddate + ", address=" + address
				+ ", workunit=" + workunit + ", phone=" + phone + ", mobile="
				+ mobile + ", updatedate=" + updatedate + "]";
	}

}
