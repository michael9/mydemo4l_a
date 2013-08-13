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
	private LinkedHashMap<String, String[]> yearsnumlist;

	// 获取期刊分类列表
	public LinkedHashMap<String, String> classfylist;

	// 获取期刊分类下的期刊列表
	private String recordcount;
	public  List<Periodical> qklist;
	
	public Periodical() {
		
	}

	public Periodical(JSONObject json, int sort) throws BookException {
		if (sort == 1) {
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
		} else if (sort == 2) {
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
		} else if (sort == 3) {
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
		if (sort == 1) {
			try {
				JSONObject json = new JSONObject(str);
				if (!json.getBoolean("success")) {
					return null;
				}
				return new Periodical(json, sort);

			} catch (JSONException e) {
				throw new BookException(e);
			}
		} else if (sort == 2) {
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
		} else if (sort == 3) {
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
		}
		return null;
	}

	private LinkedHashMap<String, String[]> formList(JSONArray array)
			throws BookException {
		int count = array.length();
		if (count <= 0) {
			return null;
		}
		LinkedHashMap<String, String[]> map = new LinkedHashMap<String, String[]>(count);
		for (int i = 0; i < count; i++) {
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
