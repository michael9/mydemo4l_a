package com.cqvip.moblelib.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.net.BookException;

/**
 * 评论
 * @author luojiang
 *
 */
public class Comment {


private String recordid;//id
private String typeid;//类型
private String keyid;//书籍id
private String userid;//用户id
private String nickname;//名字
private String contents;//内容
private Date commenttime;//评论时间
	
public Comment(JSONObject json) throws BookException{
	try {
		recordid = json.getString("recordid");
	typeid = json.getString("typeid");
	keyid = json.getString("keyid");
	userid = json.getString("userid");
	nickname = json.getString("nickname");
	contents = json.getString("contents");
	commenttime = parseDate(json.getString("commenttime"), "yyyy-MM-dd HH:mm:ss");
	} catch (JSONException e) {
		throw new BookException(e);
	}
	
}



public static List<Comment> formList(String result) throws BookException{
	
    List<Comment> comment = null;
try {
	JSONObject json = new JSONObject(result);
     if(!json.getBoolean("success")){
    	 return null;
     }
     if(json.getInt("recordcount")>0){
	JSONArray ary = json.getJSONArray("commentlist");
	 int count = ary.length();
	 if(count <=0){
		 return null;
	 }
	 comment = new ArrayList<Comment>(count);
	 for(int i = 0;i<count;i++){
		 comment.add(new Comment(ary.getJSONObject(i)));
	 }
	 return comment;
     }else{
    	 return null;
     }
} catch (JSONException e) {
	throw new BookException(e);
}

}


private Date parseDate(String str, String format) throws BookException {
	 if(str==null||"".equals(str)){
        	return null;
        }
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
	    Date time;
		try {
			time = sdf.parse(str);
			return time;
		} catch (ParseException e) {
			throw new BookException("Unexpected format(" + str + ") date Exception");
		}
}

	public String getCommentid() {
	return recordid;
}



public String getTypeid() {
	return typeid;
}



public String getKeyid() {
	return keyid;
}



public String getUserid() {
	return userid;
}



public String getNickname() {
	return nickname;
}



public String getContents() {
	return contents;
}



public Date getCommenttime() {
	return commenttime;
}



@Override
public String toString() {
	return "Comment [commentid=" + recordid + ", typeid=" + typeid
			+ ", keyid=" + keyid + ", userid=" + userid + ", nickname="
			+ nickname + ", contents=" + contents + ", commenttime="
			+ commenttime + "]";
}



	
}
