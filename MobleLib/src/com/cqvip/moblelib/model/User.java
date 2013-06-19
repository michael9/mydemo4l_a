package com.cqvip.moblelib.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

/**
 * �û��࣬��½
 * @author luojiang
 *
 */
public class User extends Result{
	
	private String cardno;//����
	private String name;//����
	private String dept;//����
	private String cardtype;//������
	private String email;//�ʼ���ַ
	private String username;//����
	private String userid;//�û�id
	private String readerno;//����id
	private String phone;//�绰
	private String mobile;//�ֻ�
	private String updatedate;//��������
	
	
	
	
	public User(String result) throws BookException{
		super(result);
		try {
			JSONObject js = new JSONObject(result);
			JSONObject json = js.getJSONObject("userinfo");
			cardno = json.getString("cardno");
			name = json.getString("name");
			cardtype = json.getString("cardtypeid");
			userid = json.getString("userid");
			readerno = json.getString("readerno");
		} catch (JSONException e) {
			throw new BookException(e);
		}
		
	}
	
	public String getCardno() {
		return cardno;
	}
	public String getName() {
		return name;
	}
	public String getDept() {
		return dept;
	}
	public String getCardtype() {
		return cardtype;
	}
	public String getEmail() {
		return email;
	}
	public String getUsername() {
		return username;
	}
	public String getUserid() {
		return userid;
	}
	public String getReaderno() {
		return readerno;
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
	
	

}
