package com.cqvip.moblelib.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

/**
 * 书籍馆藏信息
 * @author luojiang
 *
 */
public class BookLoc {

	
	private String barcode;//条码号
	private String callno;//索书号
	private String local;//位置
	private String status;//状态
	private String volume;//期卷
	private String cirtype;//类型
	
	public BookLoc(String result) throws BookException{
		try {
			JSONObject js = new JSONObject(result);
			JSONObject jso = js.getJSONObject("article");
			JSONArray ary = jso.getJSONArray("districtlist");
			JSONObject json = ary.getJSONObject(0).getJSONArray("serviceaddrlist").getJSONObject(0).getJSONArray("articlelist").getJSONObject(0);
			
			barcode = json.getString("barcode");
			callno = json.getString("callno");
			local = json.getString("local");
			status = json.getString("status");
			volume = json.getString("volume");
			cirtype = json.getString("cirtype");
		} catch (JSONException e) {
			throw new BookException(e);
		}
	}
	
	public String getBarcode() {
		return barcode;
	}
	public String getCallno() {
		return callno;
	}
	public String getLocal() {
		return local;
	}
	public String getStatus() {
		return status;
	}
	public String getVolume() {
		return volume;
	}
	public String getCirtype() {
		return cirtype;
	}

	@Override
	public String toString() {
		return "BookLoc [barcode=" + barcode + ", callno=" + callno
				+ ", local=" + local + ", status=" + status + ", volume="
				+ volume + ", cirtype=" + cirtype + "]";
	}
	
	
}
