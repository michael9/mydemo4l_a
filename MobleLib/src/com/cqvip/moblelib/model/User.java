package com.cqvip.moblelib.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

/**
 * 用户类，登陆
 * @author luojiang
 *
 */
public class User extends Result{
	
	private String cardno;//卡号
	private String name;//姓名
	private String dept;//部门
	private String cardtype;//卡类型
	private String email;//邮件地址
	private String username;//卡号
	private String userid;//用户id
	private String readerno;//读者id
	private String phone;//电话
	private String mobile;//手机
	private String updatedate;//更新日期
	private int vipuserid;//更新日期
	
	
	
	
	public int getVipuserid() {
		return vipuserid;
	}

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
			vipuserid = getInt("vipuserid",json);
		} catch (JSONException e) {
			throw new BookException(e);
		}
		
	}
	   protected static int getInt(String key, JSONObject json) throws JSONException {
	        String str = json.getString(key);
	        if(null == str || "".equals(str)||"null".equals(str)){
	            return 0;
	        }
	        return Integer.parseInt(str);
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
