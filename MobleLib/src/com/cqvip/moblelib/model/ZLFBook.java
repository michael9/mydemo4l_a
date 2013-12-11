package com.cqvip.moblelib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.net.BookException;

public class ZLFBook implements Serializable{

    	/**
	 * 
	 */
	private static final long serialVersionUID = -2258009051431939480L;


		public String getTscoverimage() {
		return tscoverimage;
	}



	public void setTscoverimage(String tscoverimage) {
		this.tscoverimage = tscoverimage;
	}



		private String keyword_c;//中文关键词（分号分隔）:
    	private String subjects;//主题（显示）:需要检索的主题(分号分隔)
    	private String tsprice;//（图书）原书定价
    	private int type;//文献类型(1：期刊文章,2：学位论文,3：会议,4 专利,5标准,6成果,7专著，8 产品；9:科技报告；10政策法规;99引文)
    	private String tsseriesname;//（图书）丛书名
    	
    	private String title_c;//中文篇名
    	private String _id;//id 号
    	private String showwriter;//规范前作者（分号分隔）
    	private String showorgan;//规范前机构
    	private String tscatalog;//（图书）目录
    	private String pagecount;//页数

    	private String tsisbn;//书号
    	private String tspress;//出版社
    	private String tsrevision;//版次
    	private String organs;//机构
    	private String tsfoliosize;//开本大小
    	
    	private String tspubdate;//出版日期
    	private String tsprovinces;//出版地
    	private String classid;//分类号（空格分隔）class
    	private String tsrelation;//作者主要责任关系
    	private String tsimpressions;//印次
    	
    	private String classtypes;//领域（显示）: (分号分隔)
    	private String tsothernotes;//其他附注
    	private String media_c;//会议名称
    	private String remark_c;//中文摘要
    	private String bycount;//被引次数
    	private String mediaid;//传媒编号(聚类)
    	
    	private String writers;//人物
    	private String years;//年(聚类)
    	private String strreftext;//参考文献文本（参考文献信息）
    	private String tscoverimage;//图片url
    	private int language;//语言类型(1:中文2：英文….)
    	
    	private String zlmaintype;//专利类型
    	private String zlapplicationdata;//专利申请日
    	private String zlopendata;//专利公开日

    	private String bzmaintype;//行业标准
    	private String bzstatus;//标准状态
    	private String bzpubdate;//标准实行日
    
    	private String cgcontactunit;//科技成果单位
    	public static final int ZLF_BOOK = 7;//书籍
    	
    	public ZLFBook(JSONObject json)throws BookException{
			try {
				
				
				if(!json.isNull("_id")){
					_id = json.getString("_id");
				}
				
				if(!json.isNull("language")){
					language = json.getInt("language");
				}
				
				title_c = judgeTilte(language,json);//标题
				showwriter = judgeWirter(language,json);//作者
				remark_c = judgeRemark(language,json);//介绍
				if(!json.isNull("years")){
					years = json.getString("years");
				}
				type = json.getInt("type");
				switch(type){
				case ZLF_BOOK:
						if(!json.isNull("tspress")){
							tspress = json.getString("tspress");
							}
						if(!json.isNull("tsisbn")){
							tsisbn = json.getString("tsisbn");
							}
						if(!json.isNull("tspubdate")){
							tspubdate = json.getString("tspubdate");
							}				
						if(!json.isNull("class")){
							classid = json.getString("class");
							}
						if(!json.isNull("pagecount")){
							pagecount = json.getString("pagecount");
							}
						if(!json.isNull("keyword_c")){
							keyword_c = json.getString("keyword_c");
							}				
						if(!json.isNull("tscoverimage")){
							tscoverimage = json.getString("tscoverimage");
							}
					break;
					default:
						break;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				throw new BookException(e);
			}
}
    	
    	
    	public String getShoworgan() {
			return showorgan;
		}



		public String getMedia_c() {
			return media_c;
		}



		public String getZlmaintype() {
			return zlmaintype;
		}



		public String getZlapplicationdata() {
			return zlapplicationdata;
		}



		public String getZlopendata() {
			return zlopendata;
		}



		public String getBzmaintype() {
			return bzmaintype;
		}



		public String getBzstatus() {
			return bzstatus;
		}



		public String getBzpubdate() {
			return bzpubdate;
		}



		public String getCgcontactunit() {
			return cgcontactunit;
		}



		/**
		 * 根据语言获取介绍
		 * @param language2
		 * @param json
		 * @return
		 */
    	private String judgeRemark(int language2, JSONObject json) {
    		String remark = "";
			if(language2== 1){
				if(!json.isNull("remark_c")){
					try {
						remark = json.getString("remark_c");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			}else if(language2 == 2){
				if(!json.isNull("remark_e")){
					try {
						remark = json.getString("remark_e");
					} catch (JSONException e) {
						e.printStackTrace();
					}				
								}
			}
			return remark;
		}


    	/**
		 * 根据语言获取标题
		 * @param language2
		 * @param json
		 * @return
		 */
		private String judgeWirter(int language2, JSONObject json) {
			String title = "";
			if(language2== 1){
				if(!json.isNull("showwriter")){
					try {
						title = json.getString("showwriter");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			}else if(language2 == 2){
				if(!json.isNull("author_e")){
					try {
						title = json.getString("author_e");
					} catch (JSONException e) {
						e.printStackTrace();
					}				
								}
			}
			return title;
		}


		/**
		 * 根据语言获取作者
		 * @param language2
		 * @param json
		 * @return
		 */
		private String judgeTilte(int language2, JSONObject json) {
			String autor = "";
    		if(language2== 1){
				if(!json.isNull("title_c")){
					try {
						autor = json.getString("title_c");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			}else if(language2 == 2){
				if(!json.isNull("title_e")){
					try {
						autor = json.getString("title_e");
					} catch (JSONException e) {
						e.printStackTrace();
					}				
								}
			}
			return autor;
		}



		public static List<ZLFBook> formList(String result) throws BookException{
    		
    		Log.i("ZLFBook","=======result=="+result);
		    List<ZLFBook> books = null;
		try {
			JSONArray jsonArray = new JSONArray(result);
		   
			 int count = jsonArray.length();
			 if(count <=0){
				 return null;
			 }
			 books = new ArrayList<ZLFBook>(count);
			 for(int i = 0;i<count;i++){
				 books.add(new ZLFBook(jsonArray.getJSONObject(i)));
			 }
			 return books;
		} catch (JSONException e) {
			throw new BookException(e);
		}
		
	}



		public String getKeyword_c() {
			return keyword_c;
		}



		public void setKeyword_c(String keyword_c) {
			this.keyword_c = keyword_c;
		}



		public String getSubjects() {
			return subjects;
		}



		public void setSubjects(String subjects) {
			this.subjects = subjects;
		}



		public String getTsprice() {
			return tsprice;
		}



		public void setTsprice(String tsprice) {
			this.tsprice = tsprice;
		}



		public int getType() {
			return type;
		}



		public void setType(int type) {
			this.type = type;
		}



		public String getTsseriesname() {
			return tsseriesname;
		}



		public void setTsseriesname(String tsseriesname) {
			this.tsseriesname = tsseriesname;
		}



		public String getTitle_c() {
			return title_c;
		}



		public void setTitle_c(String title_c) {
			this.title_c = title_c;
		}



		public String get_id() {
			return _id;
		}



		public void set_id(String _id) {
			this._id = _id;
		}



		public String getShowwriter() {
			return showwriter;
		}



		public void setShowwriter(String showwriter) {
			this.showwriter = showwriter;
		}



		public String getTscatalog() {
			return tscatalog;
		}



		public void setTscatalog(String tscatalog) {
			this.tscatalog = tscatalog;
		}



		public String getPagecount() {
			return pagecount;
		}



		public void setPagecount(String pagecount) {
			this.pagecount = pagecount;
		}



		public String getTsisbn() {
			return tsisbn;
		}



		public void setTsisbn(String tsisbn) {
			this.tsisbn = tsisbn;
		}



		public String getTspress() {
			return tspress;
		}



		public void setTspress(String tspress) {
			this.tspress = tspress;
		}



		public String getTsrevision() {
			return tsrevision;
		}



		public void setTsrevision(String tsrevision) {
			this.tsrevision = tsrevision;
		}



		public String getOrgans() {
			return organs;
		}



		public void setOrgans(String organs) {
			this.organs = organs;
		}



		public String getTsfoliosize() {
			return tsfoliosize;
		}



		public void setTsfoliosize(String tsfoliosize) {
			this.tsfoliosize = tsfoliosize;
		}



		public String getTspubdate() {
			return tspubdate;
		}



		public void setTspubdate(String tspubdate) {
			this.tspubdate = tspubdate;
		}



		public String getTsprovinces() {
			return tsprovinces;
		}



		public void setTsprovinces(String tsprovinces) {
			this.tsprovinces = tsprovinces;
		}



		public String getClassid() {
			return classid;
		}



		public void setClassid(String classid) {
			this.classid = classid;
		}



		public String getTsrelation() {
			return tsrelation;
		}



		public void setTsrelation(String tsrelation) {
			this.tsrelation = tsrelation;
		}



		public String getTsimpressions() {
			return tsimpressions;
		}



		public void setTsimpressions(String tsimpressions) {
			this.tsimpressions = tsimpressions;
		}



		public String getClasstypes() {
			return classtypes;
		}



		public void setClasstypes(String classtypes) {
			this.classtypes = classtypes;
		}



		public String getTsothernotes() {
			return tsothernotes;
		}



		public void setTsothernotes(String tsothernotes) {
			this.tsothernotes = tsothernotes;
		}



		public String getRemark_c() {
			return remark_c;
		}



		public void setRemark_c(String remark_c) {
			this.remark_c = remark_c;
		}



		public String getBycount() {
			return bycount;
		}



		public void setBycount(String bycount) {
			this.bycount = bycount;
		}



		public String getMediaid() {
			return mediaid;
		}



		public void setMediaid(String mediaid) {
			this.mediaid = mediaid;
		}



		public String getWriters() {
			return writers;
		}



		public void setWriters(String writers) {
			this.writers = writers;
		}



		public String getYears() {
			return years;
		}



		public void setYears(String years) {
			this.years = years;
		}



		public String getStrreftext() {
			return strreftext;
		}



		public void setStrreftext(String strreftext) {
			this.strreftext = strreftext;
		}



		public int getLanguage() {
			return language;
		}



		public void setLanguage(int language) {
			this.language = language;
		}



		@Override
		public String toString() {
			return "ZLFBook [keyword_c=" + keyword_c + ", title_c=" + title_c
					+ ", remark_c=" + remark_c + "]";
		}

    	
    	
}
