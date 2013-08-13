package com.cqvip.moblelib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

/**
 * �ڿ���ϸ
 * @author luojiang
 *
 */
public class Periodical implements Serializable {
	                     
	     /**
	 * 
	 */
	private static final long serialVersionUID = -3844606467162617902L;
		private String gch; //id
	     private String name; //����
	     private String ename;//Ӣ������
	     private String imgurl;//ͼƬ
	     private String isrange;//����
	     private String cnno; //ͳһ����
	     
	     private String issn;//����ͳһ����
	     private String changestate;//����
	     private String remark; //���
	     private String directordept;//���ܵ�λ
	     private String publisher; //���쵥λ
	     private String chiefeditor;//����
	     private String pubcycle;//�¿�
	     private String size;//���ٿ�
	     private HashMap<String,String[]> yearsnumlist;
	
	     public Periodical(JSONObject json) throws BookException{
	 		try {
	 			gch = json.getString("gch");
	 			name = json.getString("name");
	 			ename = json.getString("ename");
	 			imgurl = json.getString("imgurl");
	 			isrange = json.getString("isrange");
	 			cnno = json.getString("cnno");
	 			
	 			issn = json.getString("issn");
	 			changestate = json.getString("changestate");
	 			remark = json.getString("remark");
	 			directordept = json.getString("directordept");
	 			publisher = json.getString("publisher");
	 			chiefeditor = json.getString("chiefeditor");
	 			pubcycle = json.getString("pubcycle");
	 			size = json.getString("size");
	 			yearsnumlist = formList(json.getJSONArray("yearsnumlist"));
	 			
	 		} catch (JSONException e) {
	 			e.printStackTrace();
	 			throw new BookException(e);
	 		}
	 	}
	     
	    public Periodical formObject(String str) throws BookException{
	    	try {
	 		JSONObject json = new JSONObject(str);
	 		if(!json.getBoolean("success")){
				return null;
			}
	 		return new Periodical(json);
	 			
	 		} catch (JSONException e) {
	 			throw new BookException(e);
	 		}
	    } 

		private HashMap<String, String[]> formList(JSONArray array) throws BookException {
			 int count = array.length();
			 if(count <=0){
				 return null;
			 }
			 HashMap<String, String[]> map = new HashMap<String, String[]>(count);
			 for(int i = 0;i<count;i++){
				JSONObject js;
				try {
					js = (JSONObject) array.get(i);
					String year = js.getString("year");
					String tempnum = js.getString("num");
					String[] num = tempnum.split(",");
					map.put(year, num);
				} catch (JSONException e) {
					e.printStackTrace();
					throw new BookException(e);
				}
				
			 }
			
			return map;
		}

		public String getGch() {
			return gch;
		}

		public String getName() {
			return name;
		}

		public String getEname() {
			return ename;
		}

		public String getImgurl() {
			return imgurl;
		}

		public String getIsrange() {
			return isrange;
		}

		public String getCnno() {
			return cnno;
		}

		public String getIssn() {
			return issn;
		}

		public String getChangestate() {
			return changestate;
		}

		public String getRemark() {
			return remark;
		}

		public String getDirectordept() {
			return directordept;
		}

		public String getPublisher() {
			return publisher;
		}

		public String getChiefeditor() {
			return chiefeditor;
		}

		public String getPubcycle() {
			return pubcycle;
		}

		public String getSize() {
			return size;
		}

		public HashMap<String, String[]> getYearsnumlist() {
			return yearsnumlist;
		}
	
	
}
