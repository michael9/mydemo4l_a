package com.cqvip.moblelib.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

/**
 * �鼮�ݲ���Ϣ
 * @author luojiang
 *
 */
public class BookLoc {

	
	private String barcode;//�����
	private String callno;//�����
	private String local;//λ��
	private String status;//״̬
	private String volume;//�ھ�
	private String cirtype;//����
	
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
