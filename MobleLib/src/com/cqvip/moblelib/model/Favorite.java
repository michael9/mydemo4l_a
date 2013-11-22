package com.cqvip.moblelib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.net.BookException;

public class Favorite implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3219034814224955778L;
	private String lngid;//为5时，是isbn号
	private String title;
	private String writer;
	private String organ;// 出版社
	private String keyword;
	private String remark;
	private String years;
	private String num;
	private int pagecount;
	private String price;
	private String vote;
	private int commentcount;
	private String imgurl;
	private String weburl;
	private String favoritekeyid;// 索书号
	private String typeid;// 书类型
	private String favoritetime;// 收藏时间
	
	public int recordcount;//条数
	public Map<Integer, List<Favorite>> map;
	public String getTypeid() {
		return typeid;
	}

	public String getFavoritekeyid() {
		return favoritekeyid;
	}

	public Favorite() {
		// TODO Auto-generated constructor stub
	}
	
	public Favorite(JSONObject jsonObject) throws BookException {
		try {
			favoritekeyid = jsonObject.getString("favoritekeyid");
			favoritetime=jsonObject.getString("favoritetime");
			JSONObject json = jsonObject.getJSONObject("favoriteinfo");
			typeid = lngid = json.getString("typeid");
			lngid = json.getString("lngid");
			title = json.getString("title");
			writer = json.getString("writer");
			organ = json.getString("organ");
			keyword = json.getString("keyword");
			remark = json.getString("remark");
			years = json.getString("years");
			num =json.getString("num");
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

	public Favorite(int type, JSONObject json) throws BookException {
		if (type == Task.TASK_COMMENT_BOOKLIST) {
			try {
				typeid = lngid = json.getString("typeid");
				lngid = json.getString("lngid");
				title = json.getString("title");
				writer = json.getString("writer");
				organ = json.getString("organ");
				keyword = json.getString("keyword");
				remark = json.getString("remark");
				years = json.getString("years");
				num = json.getString("num");
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

	}

	protected static int getInt(String key, JSONObject json)
			throws JSONException {
		String str = json.getString(key);
		if (null == str || "".equals(str) || "null".equals(str)) {
			return 0;
		}
		return Integer.parseInt(str);
	}

	public static Favorite formList(int task, String result)
			throws BookException {
		Favorite favorite=new Favorite();
		Map<Integer, List<Favorite>> map = new HashMap<Integer, List<Favorite>>();
		favorite.map=map;
		List<Favorite> favor_sz = null;
		List<Favorite> favor_zk = null;
		switch (task) {
		case Task.TASK_GET_FAVOR:
			try {
				JSONObject json = new JSONObject(result);
				if (!json.getBoolean("success")) {
					return null;
				}
				if ((favorite.recordcount=json.getInt("recordcount")) >0) {
					JSONArray ary = json.getJSONArray("grouplist");
					int count = ary.length();
					if (count <= 0) {
						return null;
					}
					favor_sz = new ArrayList<Favorite>();
					favor_zk = new ArrayList<Favorite>();
					for (int i = 0; i < count; i++) {
						JSONObject js = ary.getJSONObject(i);
						int type = js.getInt("typeid");
						JSONArray array = js.getJSONArray("favoritelist");
						switch (type) {
						case GlobleData.BOOK_SZ_TYPE:
							for (int j = 0; j < array.length(); j++) {
								favor_sz.add(new Favorite(array
										.getJSONObject(j)));
							}
							break;
						case GlobleData.BOOK_ZK_TYPE:
							for (int j = 0; j < array.length(); j++) {
								favor_zk.add(new Favorite(array
										.getJSONObject(j)));
							}
							break;
						}
					}
					map.put(GlobleData.BOOK_SZ_TYPE, favor_sz);
					map.put(GlobleData.BOOK_ZK_TYPE, favor_zk);
					return favorite;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				throw new BookException(e);
			}
			break;
		case Task.TASK_COMMENT_BOOKLIST:
			try {
				JSONObject json = new JSONObject(result);
				if (!json.getBoolean("success")) {
					return null;
				}
				if ((favorite.recordcount=json.getInt("recordcount")) >0) {
					if(hasObject(json,"zkbooks")){
					JSONArray ary = json.getJSONArray("zkbooks");
					int count = ary.length();
					if (count <= 0) {
						map.put(GlobleData.BOOK_ZK_TYPE, null);
					} else {
						favor_zk = new ArrayList<Favorite>(count);
						for (int i = 0; i < count; i++) {
							JSONObject js = ary.getJSONObject(i);
							favor_zk.add(new Favorite(task, js));
						}
						map.put(GlobleData.BOOK_ZK_TYPE, favor_zk);
					}
					}else{
						map.put(GlobleData.BOOK_ZK_TYPE, null);
					}
					if(hasObject(json,"szybooks")){
					JSONArray sz = json.getJSONArray("szybooks");
					int szcount = sz.length();
					if (szcount <= 0) {
						map.put(GlobleData.BOOK_SZ_TYPE, null);
					} else {
						favor_sz = new ArrayList<Favorite>(szcount);
						for (int i = 0; i < szcount; i++) {
							JSONObject js = sz.getJSONObject(i);
							favor_sz.add(new Favorite(task, js));
						}
						map.put(GlobleData.BOOK_SZ_TYPE, favor_sz);
					}
					}else{
						map.put(GlobleData.BOOK_SZ_TYPE, null);
					}
					return favorite;
				}
			} catch (JSONException e) {
				e.printStackTrace();
				throw new BookException(e);
			}
			break;
		}
		return null;

	}

	protected static boolean hasObject(JSONObject object, String key) {
		try {
			if (object.isNull(key)) {
				return false;
			}
			String strTmp = object.getString(key);
			if (null == strTmp) {
				return false;
			} else if (strTmp.equals("null")) {
				return false;
			} else if (strTmp.equals("")) {
				return false;
			} else {
				return true;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
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

	public String getNum() {
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
	
	public String getFavoritetime() {
		return favoritetime;
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
