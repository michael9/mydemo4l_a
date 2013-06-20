package com.cqvip.moblelib.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.cqvip.moblelib.biz.Task;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BookLoc;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.moblelib.model.Reader;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.model.User;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.net.BookParameters;
import com.cqvip.moblelib.net.HttpClientNewWork;

/**
 * 定义接口层
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
	 * 登陆
	 * 
	 * @param name
	 *            用户名
	 * @param pwd
	 *            密码
	 * @return
	 * @throws BookException
	 * @throws JSONException 
	 */
	public Result login(String name, String pwd) throws BookException, JSONException {
		BookParameters params = new BookParameters();
		params.add("username", name);
		params.add("password", pwd);
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
	 * 测试登陆
	 * 
	 * @param name
	 *            用户名
	 * @param pwd
	 *            密码
	 * @return
	 * @throws BookException
	 */
	public String login(String name, String pwd, String test)
			throws BookException {
		BookParameters params = new BookParameters();
		params.add("username", name);
		params.add("password", pwd);
		return http.requestUrl(getBaseURL() + "/library/user/login.aspx",
				getBasepost(), params);
	}

	/**
	 * 获取读者信息
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
	 * 测试获取读者信息
	 * 
	 * @return
	 * @throws BookException
	 */
	public String getReaderInfo(String id, String test) throws BookException {
		BookParameters params = new BookParameters();
		params.add("userid", id);
		return http.requestUrl(getBaseURL() + "/library/user/readerinfo.aspx",
				getBaseget(), params);
	}

	/**
	 * 查询当前用户借阅图书列表接口
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 * @throws BookException
	 */
	public String getLoanList(String id,String test) throws BookException {
		BookParameters params = new BookParameters();
		params.add("userid", id);
		return http.requestUrl(getBaseURL() + "/library/user/borrowlist.aspx",
				getBaseget(), params);
	}
	/**
	 * 查询当前用户借阅图书列表接口
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
	 * 单本续借图书接口
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 * @throws BookException
	 */
	public String Renew(String bookId) throws BookException {
		BookParameters params = new BookParameters();
		params.add("id", bookId);
		return http.requestUrl(getBaseURL(), getBasepost(), params);
	}

	/**
	 * 用户基本信息查询接口
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
	 * 馆藏关键字查询接口
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 * @throws BookException
	 */
	public String getBookSearch(BookParameters params, String test)
			throws BookException {
		return http.requestUrl(getBaseURL() + "/library/bookquery/search.aspx",
				getBasepost(), params);
	}

	/**
	 * 馆藏关键字查询接口
	 * 
	 * @param url
	 * @param method
	 * @param params
	 * @return
	 * @throws BookException
	 */
	public List<Book> getBookSearch(String key, int page, int count)
			throws BookException {
		BookParameters params = new BookParameters();
		params.add("keyword", key);
		params.add("curpage", page + "");
		params.add("perpage", count + "");
		String result = http.requestUrl(getBaseURL()
				+ "/library/bookquery/search.aspx", getBasepost(), params);
		return Book.formList(result);
	}
	/**
	 * 获取入馆指南
	 * @param type
	 * @return
	 * @throws BookException
	 */
	public String getContent(int type) throws BookException{
		String result =null;
		String str = null;
		switch(type){
		case Task.TASK_E_NOTICE:
			result = http.requestUrl(getBaseURL()+"/library/guide/notice.aspx", getBaseget(), null);
			str = formResult(result);
			break;
		case Task.TASK_E_CARDGUID:
			result = http.requestUrl(getBaseURL()+"/library/guide/cardguide.aspx", getBaseget(), null);
			str = formResult(result);
			break;
		case Task.TASK_E_TIME:
			result = http.requestUrl(getBaseURL()+"/library/guide/time.aspx", getBaseget(), null);
			str = formResult(result);
			break;
		case Task.TASK_E_READER:
			result = http.requestUrl(getBaseURL()+"/library/guide/reader.aspx", getBaseget(), null);
			str = formResult(result);
			break;
		case Task.TASK_E_SERVICE:
			result = http.requestUrl(getBaseURL()+"/library/guide/service.aspx", getBaseget(), null);
			str = formResult(result);
			break;
		}
		return str;
	}
	
	/**
	 * 获取书籍的馆藏信息
	 * @param recordid
	 * @return
	 * @throws BookException
	 */
	public BookLoc getBookDetail(String recordid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("recordid", recordid);
		params.add("tablename", "bibliosm");//书籍
		String result = http.requestUrl(getBaseURL()+"/library/bookquery/detail.aspx", getBasepost(), params);
		return new BookLoc(result);
		
	}

	private String formResult(String result) throws BookException {
		try {
			JSONObject json = new JSONObject(result);
			return json.getString("content");
		} catch (JSONException e) {
			throw new BookException(e);
		}
	}
}
