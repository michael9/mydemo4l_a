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



		private String keyword_c;//���Ĺؼ��ʣ��ֺŷָ���:
    	private String subjects;//���⣨��ʾ��:��Ҫ����������(�ֺŷָ�)
    	private String tsprice;//��ͼ�飩ԭ�鶨��
    	private int type;//��������(1���ڿ�����,2��ѧλ����,3������,4 ר��,5��׼,6�ɹ�,7ר����8 ��Ʒ��9:�Ƽ����棻10���߷���;99����)
    	private String tsseriesname;//��ͼ�飩������
    	
    	private String title_c;//����ƪ��
    	private String _id;//id ��
    	private String showwriter;//�淶ǰ���ߣ��ֺŷָ���
    	private String showorgan;//�淶ǰ����
    	private String tscatalog;//��ͼ�飩Ŀ¼
    	private String pagecount;//ҳ��

    	private String tsisbn;//���
    	private String tspress;//������
    	private String tsrevision;//���
    	private String organs;//����
    	private String tsfoliosize;//������С
    	
    	private String tspubdate;//��������
    	private String tsprovinces;//�����
    	private String classid;//����ţ��ո�ָ���class
    	private String tsrelation;//������Ҫ���ι�ϵ
    	private String tsimpressions;//ӡ��
    	
    	private String classtypes;//������ʾ��: (�ֺŷָ�)
    	private String tsothernotes;//������ע
    	private String media_c;//��������
    	private String remark_c;//����ժҪ
    	private String bycount;//��������
    	private String mediaid;//��ý���(����)
    	
    	private String writers;//����
    	private String years;//��(����)
    	private String strreftext;//�ο������ı����ο�������Ϣ��
    	private String tscoverimage;//ͼƬurl
    	private int language;//��������(1:����2��Ӣ�ġ�.)
    	
    	private String zlmaintype;//ר������
    	private String zlapplicationdata;//ר��������
    	private String zlopendata;//ר��������

    	private String bzmaintype;//��ҵ��׼
    	private String bzstatus;//��׼״̬
    	private String bzpubdate;//��׼ʵ����
    
    	private String cgcontactunit;//�Ƽ��ɹ���λ
    	public static final int ZLF_BOOK = 7;//�鼮
    	
    	public ZLFBook(JSONObject json)throws BookException{
			try {
				
				
				if(!json.isNull("_id")){
					_id = json.getString("_id");
				}
				
				if(!json.isNull("language")){
					language = json.getInt("language");
				}
				
				title_c = judgeTilte(language,json);//����
				showwriter = judgeWirter(language,json);//����
				remark_c = judgeRemark(language,json);//����
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
		 * �������Ի�ȡ����
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
		 * �������Ի�ȡ����
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
		 * �������Ի�ȡ����
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
