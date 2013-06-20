package com.cqvip.moblelib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

public class Book implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6159142206069737907L;
	private String recordid;//图书id
	private String isbn;//isbn号
//	private String isbn;
	private String publisher;//出版社
	private String publishyear;//出版时间
	private String title;//书名
	private String author;//书名	
	private String callno;//分类号
	private String classno;
	
	private String subject;//关键字
	private String u_publish;//关键字
	private String u_cover;//图片
	private String u_page;//页数
	private String u_price;//价格
	private String u_abstract;//简介
	private String u_title;
	private String u_isbn;

	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getRecordid() {
		return recordid;
	}
	public String getCallno() {
		return callno;
	}
	public String getClassno() {
		return classno;
	}
	public String getSubject() {
		return subject;
	}
	public String getU_isbn() {
		return u_isbn;
	}
	public Book(JSONObject json) throws BookException{
		try {
			recordid = json.getString("recordid");
			isbn = json.getString("isbn");
			publisher = json.getString("publisher");
			publishyear = json.getString("publishyear");
			title = json.getString("title");
			author = json.getString("author");
			subject = json.getString("subject");
			classno = json.getString("classno");
			callno = json.getString("callno");
			u_page = json.getString("u_page");
			u_price = json.getString("u_price");
			u_abstract = json.getString("u_abstract");
			u_isbn = json.getString("u_isbn");
			u_cover = json.getString("u_cover");
			u_title = json.getString("u_title");
			u_publish = json.getString("u_publish");
		} catch (JSONException e) {
			throw new BookException(e);
		}
	}
	public String getU_publish() {
		return u_publish;
	}
	public String getAuthor() {
		return author;
	}
	public String getU_title() {
		return u_title;
	}
	public String getIsbn() {
		return isbn;
	}
	public String getPublisher() {
		return publisher;
	}
	public String getPublishyear() {
		return publishyear;
	}
	public String getTitle() {
		return title;
	}
	public String getU_cover() {
		return u_cover;
	}
	public String getU_page() {
		return u_page;
	}
	public String getU_price() {
		return u_price;
	}
	public String getU_abstract() {
		return u_abstract;
	}
	
	public static List<Book> formList(String result) throws BookException{
		
		
		    List<Book> books = null;
		    JSONObject dateObj;
		try {
			JSONObject json = new JSONObject(result);
		     if(!json.getBoolean("success")){
		    	 return null;
		     }
			dateObj = json.getJSONObject("articlelist");	
			JSONArray ary = dateObj.getJSONArray("recordlist");
			 int count = ary.length();
			 if(count <=0){
				 return null;
			 }
			 books = new ArrayList<Book>(count);
			 for(int i = 0;i<count;i++){
				 books.add(new Book(ary.getJSONObject(i)));
			 }
			 return books;
		} catch (JSONException e) {
			throw new BookException(e);
		}
		
		
		
	}
	@Override
	public String toString() {
		return "Book [recordid=" + recordid + ", isbn=" + isbn + ", publisher="
				+ publisher + ", publishyear=" + publishyear + ", title="
				+ title + ", author=" + author + ", callno=" + callno
				+ ", classno=" + classno + ", subject=" + subject
				+ ", u_cover=" + u_cover + ", u_page=" + u_page + ", u_price="
				+ u_price + ", u_abstract=" + u_abstract + ", u_title="
				+ u_title + ", u_isbn=" + u_isbn + "]";
	}
	
}
