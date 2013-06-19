package com.cqvip.moblelib.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

public class Book {

	private String id;//ͼ��id
	private String isbn;//isbn��
//	private String isbn;
	private String publisher;//������
	private String publishyear;//����ʱ��
	private String title;//����
	private String author;//����
	
//	private String isbn;
	private String u_cover;//ͼƬ
	private String u_page;//ҳ��
	private String u_price;//�۸�
	private String u_abstract;//���
	private String u_title;
//	private String isbn;
//	private String isbn;
//	private String isbn;
//	private String isbn;
//		private String isbn;
	
	public Book(JSONObject json) throws BookException{
		try {
			id = json.getString("id");
			isbn = json.getString("isbn");
			publisher = json.getString("publisher");
			publishyear = json.getString("publishyear");
			title = json.getString("title");
			author = json.getString("author");
			u_page = json.getString("u_page");
			u_price = json.getString("u_price");
			u_abstract = json.getString("u_abstract");
			u_title = json.getString("u_title");
		} catch (JSONException e) {
			throw new BookException(e);
		}
	}
	public String getAuthor() {
		return author;
	}
	public String getU_title() {
		return u_title;
	}
	public String getId() {
		return id;
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
		return "Book [id=" + id + ", isbn=" + isbn + ", publisher=" + publisher
				+ ", publishyear=" + publishyear + ", title=" + title
				+ ", u_cover=" + u_cover + ", u_page=" + u_page + ", u_price="
				+ u_price + ", u_abstract=" + u_abstract + ", u_title="
				+ u_title + "]";
	}
	
}
