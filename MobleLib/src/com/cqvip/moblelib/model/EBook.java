package com.cqvip.moblelib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.cqvip.moblelib.net.BookException;

public class EBook implements Serializable{
	
		    /**
	 * 
	 */
	private static final long serialVersionUID = -7393037851968299461L;
			private String lngid;
		    private String gch;
		    private String years;
		    private int num;
		    private String vol;
		    private String title_c;
		    private String title_e;
		    private String keyword_c;
		    private String keyword_e;
		    private String name_c;
		    private String name_e;
		    private String remark_c;
		    private String remark_e;
		    private String classtype;
		    private String writer;
		    private String organ;
		    private String beginpage;
		    private String endpage;
		    private int pagecount;
		    private long pdfsize;
		    
		    public String getLngid() {
				return lngid;
			}

			public String getGch() {
				return gch;
			}

			public String getYears() {
				return years;
			}

			public int getNum() {
				return num;
			}

			public String getVol() {
				return vol;
			}

			public String getTitle_c() {
				return title_c;
			}

			public String getTitle_e() {
				return title_e;
			}

			public String getKeyword_c() {
				return keyword_c;
			}

			public String getKeyword_e() {
				return keyword_e;
			}

			public String getName_c() {
				return name_c;
			}

			public String getName_e() {
				return name_e;
			}

			public String getRemark_c() {
				return remark_c;
			}

			public String getRemark_e() {
				return remark_e;
			}

			public String getClasstype() {
				return classtype;
			}

			public String getWriter() {
				return writer;
			}

			public String getOrgan() {
				return organ;
			}

			public String getBeginpage() {
				return beginpage;
			}

			public String getEndpage() {
				return endpage;
			}

			public int getPagecount() {
				return pagecount;
			}

			public long getPdfsize() {
				return pdfsize;
			}

			public EBook(JSONObject json)throws BookException{
		    			try {
		    				lngid = json.getString("lngid");
		    				gch = json.getString("gch");
		    				years = json.getString("years");
		    				num = getInt("pagecount", json);
		    				vol = json.getString("vol");
		    				title_c = json.getString("title_c");
		    				title_e = json.getString("title_e");
		    				keyword_c = json.getString("keyword_c");
		    				keyword_e = json.getString("keyword_e");
		    				name_c = json.getString("name_c");
		    				name_e = json.getString("name_e");
		    				remark_c = json.getString("remark_c");
		    				remark_e = json.getString("remark_e");
		    				classtype = json.getString("classtype");
		    				writer = json.getString("writer");
		    				organ = json.getString("organ");
		    				beginpage = json.getString("beginpage");
		    				endpage = json.getString("endpage");
		    				pagecount = getInt("pagecount", json);
		    				pdfsize = getInt("pdfsize",json);
		    			} catch (JSONException e) {
		    				throw new BookException(e);
		    			}
		    
		    	
		    }
	    
		    public static List<EBook> formList(String result) throws BookException{
				
				
			    List<EBook> books = null;
			try {
				JSONObject json = new JSONObject(result);
			     if(!json.getBoolean("success")){
			    	 return null;
			     }
				JSONArray ary = json.getJSONArray("articlelist");
				 int count = ary.length();
				 if(count <=0){
					 return null;
				 }
				 books = new ArrayList<EBook>(count);
				 for(int i = 0;i<count;i++){
					 books.add(new EBook(ary.getJSONObject(i)));
				 }
				 return books;
			} catch (JSONException e) {
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
		    
			@Override
			public String toString() {
				return "EBook [lngid=" + lngid + ", gch=" + gch + ", years="
						+ years + ", num=" + num + ", vol=" + vol
						+ ", title_c=" + title_c + ", title_e=" + title_e
						+ ", keyword_c=" + keyword_c + ", keyword_e="
						+ keyword_e + ", name_c=" + name_c + ", name_e="
						+ name_e + ", remark_c=" + remark_c + ", remark_e="
						+ remark_e + ", classtype=" + classtype + ", writer="
						+ writer + ", organ=" + organ + ", beginpage="
						+ beginpage + ", endpage=" + endpage + ", pagecount="
						+ pagecount + ", pdfsize=" + pdfsize + "]";
			}
	
			
}
