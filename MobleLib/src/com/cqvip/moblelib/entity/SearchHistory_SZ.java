package com.cqvip.moblelib.entity;

import java.sql.Date;

import com.cqvip.dao.Model;
import com.cqvip.dao.PrimaryKey;

/**
 * 用户实体类
 * @author luojiang
 *
 */
public class SearchHistory_SZ extends Model{
	private String name;//用户名
	private Date date;
	public SearchHistory_SZ() {
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
