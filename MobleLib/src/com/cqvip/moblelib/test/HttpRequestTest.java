package com.cqvip.moblelib.test;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;
import android.util.Log;

import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.moblelib.model.Reader;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.net.BookParameters;
import com.cqvip.moblelib.service.BookManager;

public class HttpRequestTest extends AndroidTestCase {
	
	/**
	 * 测试登陆
	 * @throws BookException 
	 */
	public void testLogin() throws BookException{
		BookManager man = new BookManager();
		
		String result = man.login("0441200001098", "88888888",null);
		try {
			JSONObject json = new JSONObject(result);
			Log.i("mobile","获取登陆信息"+json.toString());
		} catch (JSONException e) {
			Log.i("mobile","获取登陆信息错误");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 测试获取读者信息
	 * @throws BookException 
	 */
	public void testGetReaderInfo() throws BookException{
		BookManager man = new BookManager();
		String result = man.getReaderInfo("0441200001098",null);
		try {
			JSONObject json = new JSONObject(result);
			Log.i("mobile","获取读者信息"+json.toString());
		} catch (JSONException e) {
			Log.i("mobile","获取读者信息错误");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试获取读者信息
	 * @throws BookException 
	 */
	public void testGetReaderInfo2() throws BookException{
		BookManager man = new BookManager();
		Reader r = man.getReaderInfo("0441200001098");
		//r.toString();
		Log.i("mobile2","获取读者信息"+r.toString());
//		try {
//			JSONObject json = new JSONObject(result);
//			Log.i("mobile","获取读者信息"+json.toString());
//		} catch (JSONException e) {
//			Log.i("mobile","获取读者信息错误");
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	/**
	 * 测试借阅图书列表
	 * @throws BookException 
	 */
	public void testBorrowList() throws BookException{
		String id = "857660";
		BookManager man = new BookManager();
		String result = man.getLoanList(id,null);
		try {
			JSONObject json = new JSONObject(result);
			Log.i("mobile","获取借阅信息"+json.toString());
		} catch (JSONException e) {
			Log.i("mobile","获取借阅信息");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	/**
	 * 测试借阅图书列表
	 * @throws BookException 
	 */
	public void testBorrowList2() throws BookException{
		String id = "857660";
		BookManager man = new BookManager();
		List<BorrowBook> result = man.getLoanList(id);
		if(result!=null){
			for(int i = 0;i<result.size();i++){
				Log.i("mobile",result.get(i).toString());
			}
		}
		
		
	}
	/**
	 * 测试续借
	 */
	
	public void testRenew(){}
	/**
	 * 测试获取读者信息
	 */
	public void testReaderInfo(){
		
	}
	/**
	 * 馆藏关键字查询
	 */
	
	public void testKeyquery(){
		BookManager man = new BookManager();
		BookParameters params = new BookParameters();
		params.add("keyword", "cad");
		params.add("curpage", "2");
		String result=null;
		try {
			result = man.getBookSearch(params,null);
		} catch (BookException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			JSONObject json = new JSONObject(result);
			Log.i("mobile","获取馆藏关键字查询信息"+json.toString());
		} catch (JSONException e) {
			Log.i("mobile","获取馆藏关键字查询信息错误");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 馆藏关键字查询 json
	 */
	
	public void testKeyquery2(){
		BookManager man = new BookManager();
		
		List<Book> result=null;
		try {
			result = man.getBookSearch("cad",1,10);
		} catch (BookException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(result!=null){
			for(int i = 0;i<result.size();i++){
				Log.i("mobile",result.get(i).toString());
			}
		}
	}
	/**
	 * 馆藏详细查询
	 */
	public void testBookDetail(){
		
		
	}
	public void testGetContent(){
		BookManager man = new BookManager();
		try {
			String res = man.getContent(Task.TASK_E_NOTICE);
			
			Log.i("mobile",res);
		} catch (BookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
