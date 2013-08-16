package com.cqvip.moblelib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.net.BookException;

/**
 * 期刊详细
 * 
 * @author luojiang
 * 
 */
public class Periodical implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3844606467162617902L;
	private String gch; // id
	private String name; // 题名
	private String ename;// 英文题名
	private String imgurl;// 图片
	private String isrange;// 排序
	private String cnno; // 统一刊号

	private String issn;// 国际统一刊号
	private String changestate;// 改名
	private String remark; // 简介
	private String directordept;// 主管单位
	private String publisher; // 主办单位
	private String chiefeditor;// 主编
	private String pubcycle;// 月刊
	private String size;// 多少开
	private ArrayList<PeriodicalYear> yearsnumlist;

	// 获取期刊分类列表
	public LinkedHashMap<String, String> classfylist;

	// 获取期刊分类下的期刊列表
	private String recordcount;
	public  List<Periodical> qklist;
	
	public Periodical() {
		
	}

	public Periodical(JSONObject json, int sort) throws BookException {
		if (sort == Task.TASK_PERIODICAL_TYPE) {
			classfylist = new LinkedHashMap<String, String>();
			try {
				JSONArray array = json.getJSONArray("classlist");
				int size=array.length();
				for (int i = 0; i < size; i++) {
					JSONObject js;
					js = (JSONObject) array.get(i);
					String classid = js.getString("classid");
					String classname = js.getString("classname");
					classfylist.put(classid, classname);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				throw new BookException(e);
			}
		} else if (sort == Task.TASK_PERIODICAL_SUBTYPE) {
			try {
				gch = json.getString("gch");
				name = json.getString("name");
				ename = json.getString("ename");
				imgurl = json.getString("imgurl");
				isrange = json.getString("isrange");
				cnno = json.getString("cnno");
				issn = json.getString("issn");
				changestate = json.getString("changestate");
			} catch (JSONException e) {
				e.printStackTrace();
				throw new BookException(e);
			}
		} else if (sort == Task.TASK_PERIODICAL_DETAIL) {
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
	}

	// sort --1: 获取期刊分类列表 2: 获取期刊分类下的期刊列表 3:获取期期刊详细
	public static Periodical formObject(String str, int sort)
			throws BookException {
		if (sort == Task.TASK_PERIODICAL_TYPE) {
			try {
				JSONObject json = new JSONObject(str);
				if (!json.getBoolean("success")) {
					return null;
				}
				return new Periodical(json, sort);

			} catch (JSONException e) {
				throw new BookException(e);
			}
		} else if (sort == Task.TASK_PERIODICAL_SUBTYPE) {
			try {
				JSONObject json = new JSONObject(str);
				if (!json.getBoolean("success")) {
					return null;
				}
				Periodical periodical=new Periodical();
				periodical.recordcount=json.getString("recordcount");
				JSONArray array=json.getJSONArray("qklist");
				
				int size=array.length();
				periodical.qklist=new ArrayList<Periodical>();
				for (int i = 0; i < size; i++) {
					periodical.qklist.add(new Periodical(array.getJSONObject(i), sort));
				}
				return periodical;

			} catch (JSONException e) {
				throw new BookException(e);
			}
		} else if (sort == Task.TASK_PERIODICAL_DETAIL) {
			try {
				JSONObject json = new JSONObject(str);
				if (!json.getBoolean("success")) {
					return null;
				}
				JSONObject json_qkinfo = json.getJSONObject("qkinfo");
				return new Periodical(json_qkinfo, sort);

			} catch (JSONException e) {
				throw new BookException(e);
			}
		}else if (sort == Task.TASK_PERIODICAL_SPECIAL) {
			try {
				JSONObject json = new JSONObject(str);
				if (!json.getBoolean("success")) {
					return null;
				}
				
				JSONArray ary = json.getJSONArray("classlist");
				 int count = ary.length();
				 if(count <=0){
					 return null;
				 }
				 Periodical periodical=new Periodical();
				 periodical.qklist=new ArrayList<Periodical>();
				 for(int i = 0;i<count;i++){
					 
					 JSONArray array = ary.getJSONObject(i).getJSONArray("qklist");
					 int tcount = array.length();
					 for(int j = 0;j<tcount;j++){
					 periodical.qklist.add(new Periodical(array.getJSONObject(j), Task.TASK_PERIODICAL_SUBTYPE));
					 }
				 }
				 return periodical;
			} catch (JSONException e) {
				throw new BookException(e);
			}
		}  
		return null;
	}

	private ArrayList<PeriodicalYear> formList(JSONArray array)
			throws BookException {
		ArrayList<PeriodicalYear> mlists = new ArrayList<PeriodicalYear>();
		int count = array.length();
		if (count <= 0) {
			return null;
		}
		for (int i = 0; i < count; i++) {
			
			PeriodicalYear map = new PeriodicalYear();
			try {
				JSONObject js = (JSONObject) array.get(i);
				String year = js.getString("year");
				String tempnum = js.getString("num");
				String[] num = tempnum.split(",");
				map.setYear(year);
				map.setNum(num);
				mlists.add(map);
			} catch (JSONException e) {
				e.printStackTrace();
				throw new BookException(e);
			}

		}

		return mlists;
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

	public ArrayList<PeriodicalYear> getYearsnumlist() {
		return yearsnumlist;
	}

	public String getRecordcount() {
		return recordcount;
	}
	
	@Override
	public String toString() {
		return "Periodical [gch=" + gch + ", name=" + name + ", ename=" + ename
				+ ", imgurl=" + imgurl + ", isrange=" + isrange + ", cnno="
				+ cnno + ", issn=" + issn + ", changestate=" + changestate
				+ ", remark=" + remark + ", directordept=" + directordept
				+ ", publisher=" + publisher + ", chiefeditor=" + chiefeditor
				+ ", pubcycle=" + pubcycle + ", size=" + size
				+ ", yearsnumlist=" + yearsnumlist + ", classfylist="
				+ classfylist + ", recordcount=" + recordcount + ", qklist="
				+ qklist + "]";
	}
}
