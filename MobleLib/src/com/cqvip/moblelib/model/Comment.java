package com.cqvip.moblelib.model;

import java.util.ArrayList;
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


private String commentid;//id
private String typeid;//类型
private String keyid;//书籍id
private String userid;//用户id
private String nickname;//名字
private String contents;//内容
private String commenttime;//评论时间
	
public Comment(JSONObject json) throws BookException{
	try {
	commentid = json.getString("commentid");
	typeid = json.getString("typeid");
	keyid = json.getString("keyid");
	userid = json.getString("userid");
	nickname = json.getString("nickname");
	contents = json.getString("contents");
	commenttime = json.getString("commenttime");
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

	public String getCommentid() {
	return commentid;
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



public String getCommenttime() {
	return commenttime;
}



@Override
public String toString() {
	return "Comment [commentid=" + commentid + ", typeid=" + typeid
			+ ", keyid=" + keyid + ", userid=" + userid + ", nickname="
			+ nickname + ", contents=" + contents + ", commenttime="
			+ commenttime + "]";
}



	
}
