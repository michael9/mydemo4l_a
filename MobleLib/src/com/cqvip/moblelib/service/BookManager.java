package com.cqvip.moblelib.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
import com.cqvip.moblelib.model.User;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.net.BookParameters;
import com.cqvip.moblelib.net.HttpClientNewWork;

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

	public String getBaseget() {
		return baseget;
	}

	public String getBasepost() {
		return basepost;
	}

	public String getBaseURL() {
		return baseURL;
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
		String result = http.requestUrl(getBaseURL()
				+ "/library/user/login.aspx", getBasepost(), params);
		
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
		String result = http.requestUrl(getBaseURL()
				+ "/library/user/readerinfo.aspx", getBaseget(), params);
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
		
		String result = http.requestUrl(getBaseURL() + "/library/user/borrowlist.aspx",getBaseget(), params);
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
		return http.requestUrl(getBaseURL(), getBaseget(), params);
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
		//TODO
		BookParameters params = new BookParameters();
		params.add("keyword", key);
		params.add("curpage", page + "");
		params.add("perpage", count + "");
		params.add("library", library);
		params.add("field", field);
		String result = http.requestUrl(getBaseURL()
				+ "/library/bookquery/search.aspx", getBasepost(), params);
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
			result = http.requestUrl(getBaseURL()+"/library/guide/notice.aspx", getBaseget(), params);
			str = formResult(result);
			break;
		case Task.TASK_E_CARDGUID:
			result = http.requestUrl(getBaseURL()+"/library/guide/cardguide.aspx", getBaseget(), params);
			str = formResult(result);
			break;
		case Task.TASK_E_TIME:
			result = http.requestUrl(getBaseURL()+"/library/guide/time.aspx", getBaseget(), params);
			str = formResult(result);
			break;
		case Task.TASK_E_READER:
			result = http.requestUrl(getBaseURL()+"/library/guide/reader.aspx", getBaseget(), params);
			str = formResult(result);
			break;
		case Task.TASK_E_SERVICE:
			result = http.requestUrl(getBaseURL()+"/library/guide/service.aspx", getBaseget(), params);
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
		String result = http.requestUrl(getBaseURL()+"/library/bookquery/detail.aspx", getBasepost(), params);
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
		String result = http.requestUrl(getBaseURL()+"/library/user/renew.aspx", getBasepost(), params);
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
		String result = http.requestUrl(getBaseURL()+"/library/user/password.aspx", getBasepost(), params);
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
		params.add("curpage", page+"");//������
		params.add("perpage",count+"" );//������
		String result = http.requestUrl(getBaseURL()+"/zk/searchtemp.aspx", getBasepost(), params);
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
		String result = http.requestUrl(getBaseURL()+"/zk/detailtemp.aspx", getBasepost(), params);
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
		String result = http.requestUrl(getBaseURL()+"/zk/articledowntemp.aspx", getBasepost(), params);
		return ShortBook.formList(Task.TASK_EBOOK_DOWN, result);
	}
	
	/**
	 * ��ȡ���°汾��
	 * @return
	 * @throws BookException
	 */
	public ShortBook getVerionCode() throws BookException{
		String result = http.requestUrl(getBaseURL()+"/library/base/versiontemp.aspx", getBasepost(),null);
		return new ShortBook(Task.TASK_REFRESH, result);
	}
	
	/**
	 * ����ղ�
	 * @param libid //ͼ���id
	 * @param vipuserid //vip �û�id
	 * @param keyid //ͼ��id
	 * @param typeid //��� 1,ͼ�飬2,�ڿ� 3����ý�� ��4���ĵ�
	 * @return
	 * @throws BookException
	 */
	public Result addFavorite(String libid,String vipuserid,String keyid,String typeid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("vipuserid", vipuserid);
		params.add("keyid", keyid);
		params.add("typeid", typeid);
		String result = http.requestUrl(getBaseURL()+"/cloud/favorite.aspx", getBasepost(),params);
		return new Result(result);
	}
	
	/**
	 * 
	 * @param page ҳ��
	 * @param count ����
	 * @return
	 * @throws BookException
	 */
	public String getFavoriteList(String page,String count)throws BookException{
		//TODO
		BookParameters params = new BookParameters();
		params.add("curpage", page);
		params.add("perpage", count);
		String result = http.requestUrl(getBaseURL()+"/cloud/favorite.aspx", getBasepost(),null);
		return result;
	}
	
	/**
	 * ɾ���ղ�
	 * @param libid //ͼ���id
	 * @param vipuserid //vip �û�id
	 * @param keyid //ͼ��id
	 * @param typeid //��� 1,ͼ�飬2,�ڿ� 3����ý�� ��4���ĵ�
	 * @return
	 * @throws BookException
	 */
	public Result destroyFavorite(String libid,String vipuserid,String keyid,String typeid)throws BookException{
		//TODO
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("vipuserid", vipuserid);
		params.add("keyid", keyid);
		params.add("typeid", typeid);
		
		String result = http.requestUrl(getBaseURL()+"/cloud/favoritecancel.aspx", getBasepost(),params);
		return new Result(result);
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
