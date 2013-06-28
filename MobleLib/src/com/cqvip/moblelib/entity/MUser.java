package com.cqvip.moblelib.entity;

import com.cqvip.dao.Model;
import com.cqvip.dao.PrimaryKey;

/**
 * �û�ʵ����
 * @author luojiang
 *
 */
public class MUser extends Model{

	private Integer id;//����id
	private String cardno;//�û�id
	private String pwd;//����
	private String readerno;//����id
	private String cqvipid;//����vipid

	private String name;//�û���
	
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
