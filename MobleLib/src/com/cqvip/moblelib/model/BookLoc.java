package com.cqvip.moblelib.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.cqvip.moblelib.net.BookException;

/**
 * �鼮�ݲ���Ϣ
 * @author luojiang
 *
 */
public class BookLoc {

	
	private String barcode;//�����
	private String callno;//�����
	private String local;//λ��
	private String status;//״̬
	private String volume;//�ھ�
	private String cirtype;//����
	
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
		 if(ary.length()<=0){
			 return null;
		 }
		 JSONArray array = ary.getJSONObject(0).getJSONArray("serviceaddrlist");
		 if(array.length()<=0){
			 return null;
		 }
		 JSONArray json = array.getJSONObject(0).getJSONArray("articlelist");
		 int count = json.length();
		 if(count <=0){
			 return null;
		 }
		 books = new ArrayList<BookLoc>(count);
		 for(int i = 0;i<count;i++){
			 books.add(new BookLoc(json.getJSONObject(i)));
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
