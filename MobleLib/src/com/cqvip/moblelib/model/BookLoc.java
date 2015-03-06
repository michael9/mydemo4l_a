package com.cqvip.moblelib.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

/**
 * 书籍馆藏信息
 * @author luojiang
 *
 */
public class BookLoc {

	
	private String barcode;//条码号
	private String callno;//索书号
	private String local;//位置
	private String status;//状态
	private String volume;//期卷
	private String cirtype;//类型
	
	public BookLoc(JSONObject json) throws BookException{
		try {
			
			barcode = json.getString("barcode");
			callno = json.getString("callno");
			local = json.getString("local");
			status = json.getString("status");
			volume = json.getString("volume");
			cirtype = json.getString("cirtype");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new BookException(e);
		}
	}
	
	
    public static List<BookLoc> formList(String result) throws BookException{
		
		
	    List<BookLoc> books = null;
	try {
		JSONObject js = new JSONObject(result);
	     if(!js.getBoolean("success")){
	    	 return null;
	     }
	     JSONObject jso = js.getJSONObject("article");
		 JSONArray ary = jso.getJSONArray("districtlist");
		 if(ary!=null&&ary.length()<=0){
			 return null;
		 }
		 books = new ArrayList<BookLoc>();
		 for(int i = 0;i<ary.length();i++){
			 JSONArray array = ary.getJSONObject(i).getJSONArray("articlelist");
			 int count = array.length();
			 if(count<=0){
				 continue;
			 }else{
					 if(count <=0){
						 return null;
					 }
					 for(int j= 0;j<count;j++){
						 books.add(new BookLoc(array.getJSONObject(j)));
				}
			 }
		 }
		 return books;
	} catch (JSONException e) {
		throw new BookException(e);
	}
	
	
	
}
	
	public String getBarcode() {
		return barcode;
	}
	public String getCallno() {
		return callno;
	}
	public String getLocal() {
		return local;
	}
	public String getStatus() {
		return status;
	}
	public String getVolume() {
		return volume;
	}
	public String getCirtype() {
		return cirtype;
	}

	@Override
	public String toString() {
		return "BookLoc [barcode=" + barcode + ", callno=" + callno
				+ ", local=" + local + ", status=" + status + ", volume="
				+ volume + ", cirtype=" + cirtype + "]";
	}
	
	
}
