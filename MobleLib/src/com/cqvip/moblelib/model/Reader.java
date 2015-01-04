package com.cqvip.moblelib.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

/**
 * ������
 * @author luojiang
 *
 */
public class Reader{
	private String username;//����
	private String question;//�ܱ�����
	private String answer;//�ܱ���
	private String recordno;//�뵱��readerno
	private int cardtypeid;//����
	private String cardtype;//
	private String status;//֤״̬
	private String gender;//
	private String cardno;//
	private String regdate;//ע������
	private String name;//����
	private String notes;//
	private String idno;//
	private String birth;//
	private String library;//
	private String cardbegdate;//��������
	private String cardenddate;//��ֹ����
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
