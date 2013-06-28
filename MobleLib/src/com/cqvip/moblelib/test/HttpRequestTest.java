package com.cqvip.moblelib.test;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.DTDHandler;

import android.test.AndroidTestCase;
import android.util.Log;

import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BookLoc;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.EbookDetail;
import com.cqvip.moblelib.model.Reader;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.net.BookParameters;
import com.cqvip.moblelib.service.BookManager;

public class HttpRequestTest extends AndroidTestCase {
	
	/**
	 * ���Ե�½
	 * @throws BookException 
	 */
	public void testLogin() throws BookException{
		BookManager man = new BookManager();
		
		Result result = man.login("0441200001098", "88888888",GlobleData.LIBIRY_ID);
			
			Log.i("mobile","��ȡ��½��Ϣ"+result.toString());
	}
	
	/**
	 * ���Ի�ȡ������Ϣ
	 * @throws BookException 
	 */
	public void testGetReaderInfo2() throws BookException{
		BookManager man = new BookManager();
		Reader r = man.getReaderInfo("0441200001098");
		//r.toString();
		Log.i("mobile2","��ȡ������Ϣ"+r.toString());
//		try {
//			JSONObject json = new JSONObject(result);
//			Log.i("mobile","��ȡ������Ϣ"+json.toString());
//		} catch (JSONException e) {
//			Log.i("mobile","��ȡ������Ϣ����");
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	/**
	 * ���Խ���ͼ���б�
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
	 * ���Ի�ȡ������Ϣ
	 */
	public void testReaderInfo(){
		
	}
	/**
	 * �ݲعؼ��ֲ�ѯ json
	 */
	
	public void testKeyquery2(){
		BookManager man = new BookManager();
		
		List<Book> result=null;
		try {
			result = man.getBookSearch("�ɶ�",1,10);
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
	 * �ݲ���ϸ��ѯ
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
	/**
	 * ��������
	 */
	public void testReNew(){
		BookManager man = new BookManager();
		String userid = "0440061012345"	;
		String barcode = "04400696111860" ;
		try {
			ShortBook res = man.reNew(userid,barcode);
			Log.i("mobile",res.getMessage());
		} catch (BookException e) {
			e.printStackTrace();
		}
		
		
	}
	public void testChangePwd(){
		BookManager man = new BookManager();
		String userid = "0441200001098"	;
		String newpwd = "888888" ;
		String oldpwd = "88888888" ;
		try {
			String res = man.changPassword(userid, newpwd, oldpwd);
			Log.i("mobile",res);
		} catch (BookException e) {
			e.printStackTrace();
		}
		
		
	}
	
	public void testGetBookDetail(){
		BookManager man = new BookManager();
		List<BookLoc> result = null;
		try {
			 result = man.getBookDetail("1402882");
			
			 if(result!=null){
				 for(int i = 0;i<result.size();i++){
					 Log.i("mobile",result.get(i).toString());
				 }
			 }
			//Log.i("mobile",b.toString());
		} catch (BookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public void testQueryEbook(){
	BookManager man = new BookManager();
		
		List<EBook> result=null;
		try {
			result = man.queryEBook("cad",1,10);
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
	public void testArticleDown(){
		BookManager man = new BookManager();
		
		List<ShortBook> result=null;
		try {
			result = man.articledown("45487094");
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
	public void queryEbookDetail(){
BookManager man = new BookManager();
		
		EbookDetail result=null;
		try {
			result = man.queryEBookDetail("45487094");
		} catch (BookException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(result!=null){
				Log.i("mobile",result.toString());
		}
	}
	
	/**
	 * ���Ը���
	 */
	public void testRefresh(){
		BookManager man = new BookManager();
		
		try {
			ShortBook res = man.getVerionCode();
			Log.i("mobile",res.toString());
		} catch (BookException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * �������ָ��
	 */
	public void testentrancelib(){
		BookManager man = new BookManager();	
		try {
			String res = man.getContent(6);
			Log.i("mobile",res);
		} catch (BookException e) {
			Log.e("mobile", "error");
			e.printStackTrace();
		}	
	}
}
