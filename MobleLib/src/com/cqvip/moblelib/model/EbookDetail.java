package com.cqvip.moblelib.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

public class EbookDetail extends EBook{
	private String coverurl; 
	private String weburl; 
	private boolean allowdown; 
	public EbookDetail(JSONObject json) throws BookException {
		super(json);
		try {
			coverurl = json.getString("coverurl");
			weburl = json.getString("weburl");
			allowdown = getBoolean("allowdown", json);
			
		} catch (JSONException e) {
			throw new BookException(e);
		}
	}
	public static EbookDetail formObject(String str) throws BookException{
		try {
			JSONObject json = new JSONObject(str);
		     if(!json.getBoolean("success")){
		    	 return null;
		     }
		     JSONObject ary = json.getJSONObject("articleinfo");
			 return new EbookDetail(ary);
		} catch (JSONException e) {
			throw new BookException(e);
		}
		
	}
	protected static boolean getBoolean(String key, JSONObject json) throws JSONException {
        String str = json.getString(key);
        if(null == str || "".equals(str)||"null".equals(str)){
            return false;
        }
        return Boolean.valueOf(str);
    }
	public String getCoverurl() {
		return coverurl;
	}
	public String getWeburl() {
		return weburl;
	}
	public boolean isAllowdown() {
		return allowdown;
	}
	@Override
	public String toString() {
		return "EbookDetail [coverurl=" + coverurl + ", weburl=" + weburl
				+ ", allowdown=" + allowdown + ", getLngid()=" + getLngid()
				+ ", getGch()=" + getGch() + ", getYears()=" + getYears()
				+ ", getNum()=" + getNum() + ", getVol()=" + getVol()
				+ ", getTitle_c()=" + getTitle_c() + ", getTitle_e()="
				+ getTitle_e() + ", getKeyword_c()=" + getKeyword_c()
				+ ", getKeyword_e()=" + getKeyword_e() + ", getName_c()="
				+ getName_c() + ", getName_e()=" + getName_e()
				+ ", getRemark_c()=" + getRemark_c() + ", getRemark_e()="
				+ getRemark_e() + ", getClasstype()=" + getClasstype()
				+ ", getWriter()=" + getWriter() + ", getOrgan()=" + getOrgan()
				+ ", getBeginpage()=" + getBeginpage() + ", getEndpage()="
				+ getEndpage() + ", getPagecount()=" + getPagecount()
				+ ", getPdfsize()=" + getPdfsize() + "]";
	}
	

	
}
