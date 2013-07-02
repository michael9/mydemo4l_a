package com.cqvip.moblelib.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.net.BookException;

public class Favorite {

	private String lngid;
	private String title;
	private String writer;
	private String organ;
	private String keyword;
	private String remark;
	private String years;
	private int num;
	private int pagecount;
	private String price;
	private String vote;
	private int commentcount;
	private String imgurl;
	private String weburl;
	
	public Favorite(JSONObject jsonObject) throws BookException{
		try {
			JSONObject json = jsonObject.getJSONObject("favoriteinfo");
			lngid = json.getString("lngid");
			title = json.getString("title");
			writer = json.getString("writer");
			organ = json.getString("organ");
			keyword = json.getString("keyword");
			remark = json.getString("remark");
			years = json.getString("years");
			num = getInt("num", json);
			pagecount = getInt("pagecount", json);
			price = json.getString("price");
			vote = json.getString("vote");
			commentcount = getInt("commentcount", json);
			imgurl = json.getString("imgurl");
			weburl = json.getString("weburl");
		} catch (JSONException e) {
			e.printStackTrace();
			throw new BookException(e);
		}
		
	}
	   protected static int getInt(String key, JSONObject json) throws JSONException {
	        String str = json.getString(key);
	        if(null == str || "".equals(str)||"null".equals(str)){
	            return 0;
	        }
	        return Integer.parseInt(str);
	    }
	    
	
	public static Map<Integer,List<Favorite>> formList(String result) throws BookException{
		Map<Integer,List<Favorite>>  map = new HashMap<Integer,List<Favorite>>();
		 List<Favorite> favor_sz = null;
		 List<Favorite> favor_zk = null;
	try {
		JSONObject json = new JSONObject(result);
	     if(!json.getBoolean("success")){
	    	 return null;
	     }
	     if(json.getInt("recordcount")>0){
		JSONArray ary = json.getJSONArray("grouplist");
		 int count = ary.length();
		 if(count <=0){
			 return null;
		 }
		 favor_sz = new ArrayList<Favorite>();
		 favor_zk = new ArrayList<Favorite>();
		 for(int i = 0;i<count;i++){
			 JSONObject js = ary.getJSONObject(i);
			 int type = js.getInt("typeid");
			 JSONArray array = js.getJSONArray("favoritelist");
			 switch(type){
			 case GlobleData.BOOK_SZ_TYPE:
				 for(int j=0;j<array.length();j++){
				 favor_sz.add(new Favorite(array.getJSONObject(j))); 
				 }
				 break;
			 case GlobleData.BOOK_ZK_TYPE:
				 for(int j=0;j<array.length();j++){
					 favor_zk.add(new Favorite(array.getJSONObject(j))); 
					 }
				 break;
			 }
		 }
		map.put(GlobleData.BOOK_SZ_TYPE, favor_sz);	 
		map.put(GlobleData.BOOK_ZK_TYPE, favor_zk);	 
		 return map;
	     }else{
	    	 return null;
	     }
	} catch (JSONException e) {
		e.printStackTrace();
		throw new BookException(e);
	}
	}
	public String getLngid() {
		return lngid;
	}
	public String getTitle() {
		return title;
	}
	public String getWriter() {
		return writer;
	}
	public String getOrgan() {
		return organ;
	}
	public String getKeyword() {
		return keyword;
	}
	public String getRemark() {
		return remark;
	}
	public String getYears() {
		return years;
	}
	public int getNum() {
		return num;
	}
	public int getPagecount() {
		return pagecount;
	}
	public String getPrice() {
		return price;
	}
	public String getVote() {
		return vote;
	}
	public int getCommentcount() {
		return commentcount;
	}
	public String getImgurl() {
		return imgurl;
	}
	public String getWeburl() {
		return weburl;
	}
	@Override
	public String toString() {
		return "Favorite [lngid=" + lngid + ", title=" + title + ", writer="
				+ writer + ", organ=" + organ + ", keyword=" + keyword
				+ ", remark=" + remark + ", years=" + years + ", num=" + num
				+ ", pagecount=" + pagecount + ", price=" + price + ", vote="
				+ vote + ", commentcount=" + commentcount + ", imgurl="
				+ imgurl + ", weburl=" + weburl + "]";
	}
	
}
