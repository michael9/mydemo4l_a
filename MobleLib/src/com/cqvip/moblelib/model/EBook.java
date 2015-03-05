package com.cqvip.moblelib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.cqvip.moblelib.net.BookException;

public class EBook implements Serializable{
	
		    /**
	 * 
	 */
	private static final long serialVersionUID = -7393037851968299461L;
			private String lngid;//id
		    private String gch;//
		    private String years;//年
		    private String num;//期数 第几期
		    private String vol;//
		    private String title_c;//标题（中文）
		    private String title_e;//标题（英文）
		    private String keyword_c;//
		    private String keyword_e;
		    private String name_c;//来源杂志
		    private String name_e;
		    private String remark_c;//简介
		    private String remark_e;
		    private String classtype;
		    private String writer;//作者
		    private String organ;//来源
		    private String beginpage;//开始页
		    private String endpage;//结束页
		    private int pagecount;//页数
		    private long pdfsize;//大小
		    private String imgurl;//图片
		    private boolean isfavorite;//是否收藏
		    private boolean allowdown;//是否
	
		    
		    public EBook(String lngid, String years, String num, String title_c,
					String name_c, String remark_c, String writer,
					int pagecount, long pdfsize, String imgurl) {
				super();
				this.lngid = lngid;
				this.years = years;
				this.num = num;
				this.title_c = title_c;
				this.name_c = name_c;
				this.remark_c = remark_c;
				this.writer = writer;
				this.pagecount = pagecount;
				this.pdfsize = pdfsize;
				this.imgurl = imgurl;
			}

			public String getImgurl() {
				return imgurl;
			}

			public String getLngid() {
				return lngid;
			}

			public String getGch() {
				return gch;
			}

			public String getYears() {
				return years;
			}

			public String getNum() {
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
		    				num = json.getString("num");
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
		    				imgurl = json.getString("imgurl");
		    				isfavorite = json.getBoolean("isfavorite");
		    				allowdown = json.getBoolean("allowdown");
		    			} catch (JSONException e) {
		    				e.printStackTrace();
		    				throw new BookException(e);
		    			}
		    
		    	
		    }
			public static int ebookCount(String result) throws BookException{
				JSONObject json;
				try {
					json = new JSONObject(result);
					if(!json.getBoolean("success")){
						return 0;
					}
					int resultCount = json.getInt("recordcount");
					if(resultCount>0){
						return resultCount;
					}
					return 0;
				} catch (JSONException e) {
					e.printStackTrace();
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
			     if(json.getInt("recordcount")>0){
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
			     }else{
			    	 return null;
			     }
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

			public boolean isIsfavorite() {
				return isfavorite;
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
						+ pagecount + ", pdfsize=" + pdfsize + ", imgurl="
						+ imgurl + ", isfavorite=" + isfavorite + "]";
			}

			
}
