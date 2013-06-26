package com.cqvip.moblelib.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

public class BorrowBook{

    private String recordno;//记录编号
	public void setReturndate(String returndate) {
		this.returndate = returndate;
	}

	private String barcode;
	private String title;//标题
	private String callno;//取书号
	private String volumeno;
	private String loandate;//借阅日期
	private String returndate;//归还
	//private int loannum;

	private int renew;
	public int getRenew() {
		return renew;
	}

	private String local;//地址
	private String cirtype;//图书馆分类
	private String price;
	private String servicetype;
	
	
	public BorrowBook(JSONObject json) throws BookException{
		try {
			barcode = json.getString("barcode");
			callno = json.getString("callno");
			title = json.getString("title");
			loandate = json.getString("loandate");
			returndate = json.getString("returndate");
			//local = json.getString("local");
			price = json.getString("price");
			renew = json.getInt("renew");
			
		} catch (JSONException e) {
			throw new BookException(e);
		}
	}
	
	public String getRecordno() {
		return recordno;
	}
	public String getBarcode() {
		return barcode;
	}
	public String getTitle() {
		return title;
	}
	public void setRenew(int renew) {
		this.renew = renew;
	}

	public String getCallno() {
		return callno;
	}
	public String getVolumeno() {
		return volumeno;
	}
	public String getLoandate() {
		return loandate;
	}
	public String getReturndate() {
		return returndate;
	}
	public String getLocal() {
		return local;
	}
	public String getCirtype() {
		return cirtype;
	}
	public String getPrice() {
		return price;
	}
	public String getServicetype() {
		return servicetype;
	}
	
	public static List<BorrowBook> formList(String result) throws BookException{
		
		
	    List<BorrowBook> books = null;
	    JSONObject dateObj;
	try {
		JSONObject json = new JSONObject(result);
	     if(!json.getBoolean("success")){
	    	 return null;
	     }
		dateObj = json.getJSONObject("loanlist");	
		if(dateObj.getInt("loannum")>0){
		JSONArray ary = dateObj.getJSONArray("recordlist");
		 int count = ary.length();
		 if(count <=0){
			 return null;
		 }
		 books = new ArrayList<BorrowBook>(count);
		 for(int i = 0;i<count;i++){
			 books.add(new BorrowBook(ary.getJSONObject(i)));
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
		return "BorrowBook [recordno=" + recordno + ", barcode=" + barcode
				+ ", title=" + title + ", callno=" + callno + ", volumeno="
				+ volumeno + ", loandate=" + loandate + ", returndate="
				+ returndate + ", renew=" + renew + ", local=" + local
				+ ", cirtype=" + cirtype + ", price=" + price
				+ ", servicetype=" + servicetype + "]";
	}
	
}
