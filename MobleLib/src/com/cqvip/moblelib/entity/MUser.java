package com.cqvip.moblelib.entity;

import com.cqvip.dao.Model;
import com.cqvip.dao.PrimaryKey;

/**
 * 用户实体类
 * @author luojiang
 *
 */
public class MUser extends Model{

	private Integer id;//自增id
	private String cardno;//用户id
	private String pwd;//密码
	private String readerno;//读者id
	private String cqvipid;//读者vipid

	private String name;//用户名
	
	public MUser() {
		super(new PrimaryKey("id",true));
	}

	public Integer getId() {
		return id;
	}

	public String getCqvipid() {
		return cqvipid;
	}
	
	public void setCqvipid(String cqvipid) {
		this.cqvipid = cqvipid;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	public String getCardno() {
		return cardno;
	}

	public void setCardno(String cardno) {
		this.cardno = cardno;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getReaderno() {
		return readerno;
	}

	public void setReaderno(String readerno) {
		this.readerno = readerno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
