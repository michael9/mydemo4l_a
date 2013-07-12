package com.cqvip.moblelib.service;

import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BookLoc;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.moblelib.model.Comment;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.EbookDetail;
import com.cqvip.moblelib.model.Favorite;
import com.cqvip.moblelib.model.Reader;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.model.User;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.net.BookParameters;
import com.cqvip.moblelib.net.HttpClientNewWork;
import com.cqvip.utils.Tool;

/**
 * ����ӿڲ�
 * 
 * @author luojiang
 * 
 */
public class BookManager {

	private String baseget = HttpClientNewWork.HTTPMETHOD_GET;
	private String basepost = HttpClientNewWork.HTTPMETHOD_POST;

	private String baseURL = GlobleData.SERVER_URL;
	protected HttpClientNewWork http = new HttpClientNewWork();

	public BookManager() {

	}
	/**
	 * ��½
	 * 
	 * @param name
	 *            �û���
	 * @param pwd
	 *            ����
	 * @param libid
	 *            ͼ���id,1��������
	 * @return
	 * @throws BookException
	 * @throws JSONException 
	 */
	public Result login(String name, String pwd,String libid) throws BookException {
		BookParameters params = new BookParameters();
		params.add("username", name);
		params.add("password", pwd);
		params.add("libid", "1");
		String result = http.requestUrl(baseURL
				+ "/library/user/login.aspx", basepost, params);
		
		Result res = new Result(result);
		if(res.getSuccess()==true){
			return new User(result);
		}else{
			return res;
		}
	}
	
	/**
	 * ��ȡ������Ϣ
	 * 
	 * @return
	 * @throws BookException
	 */
	public Reader getReaderInfo(String id) throws BookException {
		BookParameters params = new BookParameters();
		params.add("userid", id);
		String result = http.requestUrl(baseURL
				+ "/library/user/readerinfo.aspx", baseget, params);
		return Reader.formReaderInfo(result);
	}



	/**
	 * ��ѯ��ǰ�û�����ͼ���б�ӿ�
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 * @throws BookException
	 */
	public List<BorrowBook> getLoanList(String id) throws BookException {
		BookParameters params = new BookParameters();
		params.add("userid", id);
		
		String result = http.requestUrl(baseURL + "/library/user/borrowlist.aspx",baseget, params);
		return BorrowBook.formList(result);
	}

	/**
	 * �û�������Ϣ��ѯ�ӿ�
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 * @throws BookException
	 */
	public String getreaderByIdx(String url, String method,
			BookParameters params) throws BookException {
		return http.requestUrl(baseURL, baseget, params);
	}



	/**
	 * �ݲعؼ��ֲ�ѯ�ӿ�
	 * 
	 * @param key ��ѯ�ؼ���
	 * @param page ҳ��
	 * @param count ����
	 * @return
	 * @throws BookException
	 */
	public List<Book> getBookSearch(String key, int page, int count,String library,String field)
			throws BookException {
		BookParameters params = new BookParameters();
		params.add("keyword", key);
		params.add("curpage", page + "");
		params.add("perpage", count + "");
		params.add("library", library);
		params.add("field", field);
		String result = http.requestUrl(baseURL
				+ "/library/bookquery/search.aspx", basepost, params);
		return Book.formList(result);
	}
	/**
	 * ��ȡ���ָ��
	 * @param type
	 * @return
	 * @throws BookException
	 */
	public String getContent(int type) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", GlobleData.LIBIRY_ID);
		String result =null;
		String str = null;
		switch(type){
		case Task.TASK_E_NOTICE:
			result = http.requestUrl(baseURL+"/library/guide/notice.aspx", baseget, params);
			str = formResult(result);
			break;
		case Task.TASK_E_CARDGUID:
			result = http.requestUrl(baseURL+"/library/guide/cardguide.aspx", baseget, params);
			str = formResult(result);
			break;
		case Task.TASK_E_TIME:
			result = http.requestUrl(baseURL+"/library/guide/time.aspx", baseget, params);
			str = formResult(result);
			break;
		case Task.TASK_E_READER:
			result = http.requestUrl(baseURL+"/library/guide/reader.aspx", baseget, params);
			str = formResult(result);
			break;
		case Task.TASK_E_SERVICE:
			result = http.requestUrl(baseURL+"/library/guide/service.aspx", baseget, params);
			str = formResult(result);
			break;
		}
		return str;
	}
	
	/**
	 * ��ȡ�鼮�Ĺݲ���Ϣ
	 * @param recordid
	 * @return
	 * @throws BookException
	 */
	public List<BookLoc> getBookDetail(String recordid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("recordid", recordid);
		params.add("tablename", "bibliosm");//�鼮
		params.add("library", GlobleData.SZLG_LIB_ID);
		String result = http.requestUrl(baseURL+"/library/bookquery/detail.aspx", basepost, params);
		return BookLoc.formList(result);
		
	}
	
	/**
	 * ����
	 * @param userind
	 * @param barcode
	 * @return
	 * @throws BookException
	 */
	public ShortBook reNew(String userind,String barcode) throws BookException{
		BookParameters params = new BookParameters();
		params.add("userind", userind);
		params.add("barcode", barcode);//�鼮
		String result = http.requestUrl(baseURL+"/library/user/renew.aspx", basepost, params);
		return new ShortBook(Task.TASK_BOOK_RENEW,result);
	}
	
	/**
	 * �޸�����
	 * @param userid 
	 * @param newcode
	 * @param oldcode
	 * @return
	 * @throws BookException 
	 */
	public String changPassword(String userid,String newcode,String oldcode) throws BookException{
		BookParameters params = new BookParameters();
		params.add("userid", userid);
		params.add("newcode", newcode);//������
		params.add("oldcode", oldcode);//������
		String result = http.requestUrl(baseURL+"/library/user/password.aspx", basepost, params);
		return result;
		
	}
	
	/**
	 * ��ѯ��������
	 * @param key
	 * @param type
	 * @return
	 * @throws BookException
	 */
	public List<EBook> queryEBook(String key,int page,int count) throws BookException{
		BookParameters params = new BookParameters();
		params.add("title", key);
		params.add("curpage", page+"");//��ǰҳ��
		params.add("perpage",count+"" );//����
		String result = http.requestUrl(baseURL+"/zk/search.aspx", basepost, params);
		return EBook.formList(result);
		
	}
	/**
	 * ��ѯ������Դ��ϸ
	 * @param lngid
	 * @return
	 * @throws BookException
	 */
	public EbookDetail queryEBookDetail(String lngid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("lngid", lngid);
		String result = http.requestUrl(baseURL+"/zk/detail.aspx", basepost, params);
		return EbookDetail.formObject(result);
	}
	/**
	 * ��ȡ�����������ص�ַ
	 * @param lngid
	 * @return ��ַ����
	 * @throws BookException
	 */
	public List<ShortBook> articledown(String lngid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("lngid", lngid);
		String result = http.requestUrl(baseURL+"/zk/articledown.aspx", basepost, params);
		return ShortBook.formList(Task.TASK_EBOOK_DOWN, result);
	}
	
	/**
	 * ��ȡ���°汾��
	 * @return
	 * @throws BookException
	 */
	public ShortBook getVerionCode() throws BookException{
		String result = http.requestUrl(baseURL+"/library/base/version.aspx", basepost,null);
		return new ShortBook(Task.TASK_REFRESH, result);
	}
	
	/**
	 * ����ղ�
	 * @param libid //ͼ���id
	 * @param vipuserid //vip �û�id
	 * @param keyid //�����
	 * @param typeid //��� //4���п���5����
	 * @return
	 * @throws BookException
	 */
	public Result addFavorite(String libid,String vipuserid,String callno,String typeid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("vipuserid", vipuserid);
		params.add("keyid", callno);
		params.add("typeid", typeid);
		String result = http.requestUrl(baseURL+"/cloud/favorite.aspx", basepost,params);
		return new Result(result);
	}
	/**
	 * ����ղ�
	 * @param libid //ͼ���id
	 * @param vipuserid //vip �û�id
	 * @param keyid //�����
	 * @param typeid //��� //4���п���5����
	 * @return
	 * @throws BookException
	 */
	public Result addFavorite(String libid,String vipuserid,String callno,String typeid,String recordid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("vipuserid", vipuserid);
		if(!TextUtils.isEmpty(recordid)){
		params.add("keyid", Tool.formSZbookID(callno,recordid));
		}else{
		params.add("keyid", callno);
		}
		params.add("typeid", typeid);
		String result = http.requestUrl(baseURL+"/cloud/favorite.aspx", basepost,params);
		return new Result(result);
	}
	
	/**
	 * ��ȡ�ղ��б�
	 * @param page ҳ��
	 * @param count ����
	 * @return
	 * @throws BookException
	 */
	public Map<Integer,List<Favorite>> getFavoriteList(String libid,String vipuserid,String page,String count,String typeid)throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("vipuserid", vipuserid);
		params.add("curpage", page);
		params.add("perpage", count);
		params.add("typeid", typeid);
		String result = http.requestUrl(baseURL+"/cloud/favoritelistuser.aspx", basepost,params);
		return Favorite.formList(Task.TASK_GET_FAVOR,result);
	}
	
	/**
	 * ȡ���ղ�
	 * @param libid //ͼ���id
	 * @param vipuserid //vip �û�id
	 * @param keyid //�����
	 * @param typeid //��� 4�п���5��ͼ
	 * @return
	 * @throws BookException
	 */
	public Result destroyFavorite(String libid,String vipuserid,String keyid,String typeid)throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("vipuserid", vipuserid);
		params.add("keyid", keyid);
		params.add("typeid", typeid);
		
		String result = http.requestUrl(baseURL+"/cloud/favoritecancel.aspx", basepost,params);
		return new Result(result);
	}
	/**
	 * ��ȡ�û��������۹����鼮
	 * @param libid
	 * @param vipuserid
	 * @param page
	 * @param count
	 * @return
	 * @throws BookException
	 */
	public Map<Integer,List<Favorite>> getUserCommentBook(String libid,String vipuserid,String page,String count,String typeid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("vipuserid", vipuserid);
		params.add("curpage", page);
		params.add("perpage", count);
		params.add("typeid", typeid);
		String result = http.requestUrl(baseURL+"/cloud/commentlistuser.aspx", baseget,params);
		return Favorite.formList(Task.TASK_COMMENT_BOOKLIST, result);
	}
	
	/**
	 * ��ȡĳ���鼮�������������
	 * @param bookid
	 * @param vipuserid
	 * @param page
	 * @param count
	 * @return
	 * @throws BookException
	 */
	public List<Comment> getCommentList(String typeid,String keyid,String page,String count) throws BookException{
		BookParameters params = new BookParameters();
		params.add("typeid", typeid);
		params.add("keyid", keyid);
		params.add("curpage", page);
		params.add("perpage", count);
		String result = http.requestUrl(baseURL+"/cloud/commentlist.aspx", basepost,params);
		return Comment.formList(result);
	}
	
	/**
	 * �������
	 * @param libid //ͼ���id
	 * @param vipuserid //vip �û�id
	 * @param keyid //����ͼ��id,�п�Ϊlngid������Ϊcallno
	 * @param typeid //4���п���5����
	 * @param content //��������
	 * @return
	 * @throws BookException
	 */
	public Result addComment(String libid,String vipuserid,String keyid,String typeid,String content) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("vipuserid", vipuserid);
		params.add("keyid", keyid);
		params.add("typeid", typeid);
		params.add("info", content);
		String result = http.requestUrl(baseURL+"/cloud/comment.aspx", basepost,params);
		return new Result(result);
		
	}
	/**
	 * �������
	 * @param libid //ͼ���id
	 * @param vipuserid //vip �û�id
	 * @param keyid //����ͼ��id,�п�Ϊlngid������Ϊcallno
	 * @param typeid //4���п���5����
	 * @param content //��������
	 * @param recordid //��¼id,bookΨһ��ʶ
	 * @return
	 * @throws BookException
	 */
	public Result addComment(String libid,String vipuserid,String keyid,String typeid,String content,String recordid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("vipuserid", vipuserid);
		if(keyid.contains(",")){//�����ҵ��ղص�����ۣ�����ֻ�����lngid��J228.5/1:4,863174���ɹ�ʹ�ã����������ж�һ��
			params.add("keyid", keyid);
		}else if(!TextUtils.isEmpty(recordid)){
			params.add("keyid", Tool.formSZbookID(keyid,recordid));
			}else{
				params.add("keyid", keyid);
			}
		params.add("typeid", typeid);
		params.add("info", content);
		String result = http.requestUrl(baseURL+"/cloud/comment.aspx", basepost,params);
		return new Result(result);
		
	}
	/**
	 * ɾ������
	 * @param libid //ͼ���id
	 * @param vipuserid //vip �û�id
	 * @param keyid //����ͼ��id,�п�Ϊlngid������Ϊcallno
	 * @param typeid //��� //4���п���5����
	 * @return
	 * @throws BookException
	 */
	public Result destoryComment(String libid,String vipuserid,String keyid,String typeid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("vipuserid", vipuserid);
		params.add("keyid", keyid);
		params.add("typeid", typeid);
		String result = http.requestUrl(baseURL+"/cloud/comment.aspx", basepost,params);
		return new Result(result);
	}
	/**
	 * ��ȡ������Ϣ
	 * @param libid
	 * @param announcetypeid
	 * @return
	 * @throws BookException
	 */
	//TODO
	public List<ShortBook> getAnnouce(int type,String libid,String announcetypeid,String curpage,String perpage) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("announcetypeid", announcetypeid);
		params.add("curpage", curpage);
		params.add("perpage", perpage);
		String result = http.requestUrl(baseURL+"/library/announce/html.aspx", baseget,params);
		return ShortBook.formList(type,result);
	}
	/**
	 * �����鼮,����ͨ��
	 * @param libid
	 * @param announcetypeid
	 * @param curpage
	 * @param perpage
	 * @return
	 * @throws BookException
	 */
	public List<ShortBook> getSuggestHotBook(int type,String libid,String announcetypeid,String curpage,String perpage) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("announcetypeid", announcetypeid);
		params.add("curpage", curpage);
		params.add("perpage", perpage);
		String result = http.requestUrl(baseURL+"/library/announce/list.aspx", baseget,params);
		return ShortBook.formList(type, result);
	}
	/**
	 * �Ƽ�ģ���鼮��ϸ
	 * @param libid
	 * @param announceid
	 * @return
	 * @throws BookException 
	 */
	public String getSuggestDetail(String libid,String announceid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("announceid", announceid);
		String result = http.requestUrl(baseURL+"/library/announce/detail.aspx", baseget,params);
		return formResult(result);
		
	}
	
	/**
	 * ���Ŷ�̬�����潲��
	 * @param type
	 * @param libid
	 * @param announcetypeid
	 * @param curpage
	 * @param perpage
	 * @return
	 * @throws BookException
	 */
	public List<ShortBook> getAnnounceNews(int type,String libid,String announcetypeid,String curpage,String perpage) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("announcetypeid", announcetypeid);
		params.add("curpage", curpage);
		params.add("perpage", perpage);
		String result = http.requestUrl(baseURL+"/library/announce/list.aspx", baseget,params);
		return ShortBook.formList(type, result);
		
	}
	/**
	 * ����������ϸ
	 * @param libid
	 * @param announceid
	 * @return
	 * @throws BookException
	 */
	public String getAnnounceDetail(String libid,String announceid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("announceid", announceid);
		String result = http.requestUrl(baseURL+"/library/announce/detail.aspx", baseget,params);
		return formResult(result);
	}
	
	private String formResult(String result) throws BookException {
		try {
			JSONObject json = new JSONObject(result);
			return json.getString("contents");
		} catch (JSONException e) {
			throw new BookException(e);
		}
	}
}
