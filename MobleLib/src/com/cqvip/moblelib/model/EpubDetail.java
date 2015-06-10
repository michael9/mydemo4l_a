package com.cqvip.moblelib.model;

import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.net.BookException;

public class EpubDetail implements Serializable{

	private String id;
	private String title;
	private String writer;
	private String downloadurl;
	private String detail;
	private String imgurl;
	
	public EpubDetail(String m_id, String m_title,String m_writer,String m_downloadurl,String m_detail,String m_imgurl)
	{
		id=m_id;
		title=m_title;
		writer=m_writer;
		downloadurl=m_downloadurl;
		detail=m_detail;
		imgurl=m_imgurl;
	}
	
	public EpubDetail(JSONObject json)
	{
		try {
		id=json.getString("编号");
		title=json.getString("题名");
		writer=json.getString("作者");
		detail=json.getString("简介");
		imgurl=GlobleData.EPUB_HOME_URL+"image/"+json.getString("封面图片文件名");
		String filename=URLEncoder.encode(title,"UTF-8");
		downloadurl=GlobleData.EPUB_HOME_URL+"book/"+filename+".epub";
		}
		catch(Exception e)
		{
			
		}
	}
	
	
	 public static List<EpubDetail> formList(String result) throws BookException{
			
			
		    List<EpubDetail> books = null;
		try {
			JSONObject json=new JSONObject(result);
			JSONArray ary = json.getJSONArray("Table");
			 int count = ary.length();
			 if(count <=0){
				 return null;
			 }
			 books = new ArrayList<EpubDetail>(count);
			 for(int i = 0;i<count;i++){
				 books.add(new EpubDetail(ary.getJSONObject(i)));
			 }
			 return books;
		   
		   
		} catch (JSONException e) {
			e.printStackTrace();
			throw new BookException(e);
		}
		
		
		
	}
	
	 public String getId()
	 {
		 return id;
	 }
	public String getTitle()
	{
		return title;
	}
	
	public String getWriter()
	{
		return writer;
	}
	
	public String getImgurl() {
		return imgurl;
	}
	
	public String getDownloadurl(){
		return downloadurl;
	}
	
	public String getDetail(){
		return detail;
	}
	
}