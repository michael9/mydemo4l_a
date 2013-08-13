package com.cqvip.moblelib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

/**
 * 1.12.4	获取章节目录列表
 * 
 * @author luojiang
 * 
 */
public class PeriodicalCatalog{

	/**
	 * 
	 */
	public String express; // gch+year+num
	public String recordcount; //目录数
	
	private String lngid; // 
	private String gch; // 
	private String years;// 年
	private String num;// 
	private String vol;// 卷
	private String title_c; // 标题
	private String title_e;//
	private String keyword_c;// 关键词
	private String keyword_e; // 
	private String name_c;// 
	private String name_e; // 
	private String remark_c;// 摘要
	private String remark_e;// 
	private String classtype;// 
	private String writer;//作者
	private String organ;// 出版社
	private String beginpage; // 开始页
	private String endpage;// 结束页
	private String pagecount;// 页数
	private String pdfsize;// 
	private String imgurl;// 
	private String isfavorite;// 

	public  List<PeriodicalCatalog> articlelist;
	
	public PeriodicalCatalog() {
		
	}

	public PeriodicalCatalog(JSONObject json) throws BookException {
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
				pagecount = json.getString("pagecount");
				pdfsize = json.getString("pdfsize");
				imgurl = json.getString("imgurl");
				isfavorite = json.getString("isfavorite");
			} catch (JSONException e) {
				e.printStackTrace();
				throw new BookException(e);
			}
	}


	public static PeriodicalCatalog formObject(String str)
			throws BookException {
			try {
				JSONObject json = new JSONObject(str);
				if (!json.getBoolean("success")) {
					return null;
				}
				PeriodicalCatalog periodical=new PeriodicalCatalog();
				periodical.express=json.getString("express");
				periodical.recordcount=json.getString("recordcount");
				JSONArray array=json.getJSONArray("articlelist");
				
				int size=array.length();
				periodical.articlelist=new ArrayList<PeriodicalCatalog>();
				for (int i = 0; i < size; i++) {
					periodical.articlelist.add(new PeriodicalCatalog(array.getJSONObject(i)));
				}
				return periodical;
			} catch (JSONException e) {
				throw new BookException(e);
			}
	}

	public String getExpress() {
		return express;
	}

	public String getRecordcount() {
		return recordcount;
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

	public String getPagecount() {
		return pagecount;
	}

	public String getPdfsize() {
		return pdfsize;
	}

	public String getImgurl() {
		return imgurl;
	}

	public String getIsfavorite() {
		return isfavorite;
	}

	@Override
	public String toString() {
		return "PeriodicalCatalog [express=" + express + ", recordcount="
				+ recordcount + ", lngid=" + lngid + ", gch=" + gch
				+ ", years=" + years + ", num=" + num + ", vol=" + vol
				+ ", title_c=" + title_c + ", title_e=" + title_e
				+ ", keyword_c=" + keyword_c + ", keyword_e=" + keyword_e
				+ ", name_c=" + name_c + ", name_e=" + name_e + ", remark_c="
				+ remark_c + ", remark_e=" + remark_e + ", classtype="
				+ classtype + ", writer=" + writer + ", organ=" + organ
				+ ", beginpage=" + beginpage + ", endpage=" + endpage
				+ ", pagecount=" + pagecount + ", pdfsize=" + pdfsize
				+ ", imgurl=" + imgurl + ", isfavorite=" + isfavorite
				+ ", articlelist=" + articlelist + "]";
	}

}
