package com.cqvip.moblelib.entity;


import java.util.Date;

import com.cqvip.dao.Model;
import com.cqvip.dao.PrimaryKey;

/**
 * 搜索历史――sz
 * @author luojiang
 *
 */
public class SearchHistory_SZ extends Model{
	private Integer id;
	private String name;
	private Date date=new Date();
	
	public SearchHistory_SZ() {
		super(new PrimaryKey("id",true));
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}

}
