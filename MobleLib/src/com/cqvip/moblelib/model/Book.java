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
	private String publisher;//出版社
	private String publishyear;//出版时间
	private String title;//书名
	private String author;//书名	
	private String callno;//索书号
	private String classno;//分类号
	private String cover_path;//封面图片
	private String cover_path_big;//封面图片
	private String subject;//关键字
	private String u_page;//页数
	private String u_price;//价格
	private String u_abstract;//简介
	private boolean isfavorite;

	
	public String getCover_path() {
		return cover_path;
	}

	//Favorite转换为Book
	public Book(String recordid, String publisher, String title, String author,
			String callno, String subject, String u_price,String u_abstract,String imageurl) {
		this.recordid = recordid;
		this.publisher = publisher;
		this.title = title;
		this.author = author;
		this.callno = callno;
		this.subject = subject;
		this.u_price = u_price;
		this.u_abstract=u_abstract;
		this.cover_path=imageurl;
	}
	//Favorite转换为Book
	public Book(String recordid, String publisher, String title, String author,
			String callno,String u_abstract,
			String imageurl,String pulishyear,String page,String classno,String subject) {
		this.recordid = recordid;
		this.publisher = publisher;//getName_c())
		this.title = title; //getTitle_c())
		this.author = author; //getWriter())
		this.callno = callno; //getLngid()
		this.u_abstract=u_abstract; //getRemark_c());
		this.cover_path=imageurl;//getImgurl()
		this.publishyear = pulishyear; //getYears() 
		this.u_page = page; //getPagecount());
		this.classno = classno;//getNum()
		this.subject = subject; //getPdfsize()
	}
	
	public boolean isIsfavorite() {
		return isfavorite;
	}
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
	public String getCover_path_big() {
		return cover_path_big;
	}
	
	public void setCover_path_big(String cover_path_big) {
		this.cover_path_big = cover_path_big;
	}
	public String getSubject() {
		return subject;
	}
	public Book(JSONObject json) throws BookException{
		try {
			recordid = json.getString("recordid");
			isbn = json.getString("isbn");
			publisher = json.getString("publisher");
			publishyear = json.getString("publishyear");
			title = json.getString("title");
			author = json.getString("author");
			subject = json.getString("keyword");
			classno = json.getString("classno");
			callno = json.getString("callno");
			cover_path = json.getString("imgsmallurl");
			cover_path_big = json.getString("imgbigurl");
			u_page = json.getString("pagecount");
			u_price = json.getString("price");
			u_abstract = json.getString("remark");
			isfavorite = json.getBoolean("isfavorite");
		} catch (JSONException e) {
			throw new BookException(e);
		}
	}
	public String getAuthor() {
		return author;
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
	public String getU_page() {
		return u_page;
	}
	public String getU_price() {
		return u_price;
	}
	public String getU_abstract() {
		return u_abstract;
	}
	/**
	 * 搜索返回记录数
	 * @param result
	 * @return
	 * @throws BookException
	 */
	public static int bookCount(String result) throws BookException{
		JSONObject json;
		try {
			json = new JSONObject(result);
			if(!json.getBoolean("success")){
				return 0;
			}
			JSONObject dateObj = json.getJSONObject("articlelist");
			int resultCount = dateObj.getInt("loadnum");
			if(resultCount>0){
				return resultCount;
			}
			return 0;
		} catch (JSONException e) {
			e.printStackTrace();
			throw new BookException(e);
		}
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
			if(dateObj.getInt("loadnum")>0){
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
			}else{
				return null;
			}
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
				+   ", u_page=" + u_page + ", u_price=" + u_price
				+ ", u_abstract=" + u_abstract +  ", isfavorite=" + isfavorite + "]";
	}
	
	
}
