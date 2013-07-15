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
	/**
	 * 登陆
	 * 
	 * @param name
	 *            用户名
	 * @param pwd
	 *            密码
	 * @param libid
	 *            图书馆id,1代表深圳
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
	 * 获取读者信息
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
		
		String result = http.requestUrl(baseURL + "/library/user/borrowlist.aspx",baseget, params);
		return BorrowBook.formList(result);
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
		return http.requestUrl(baseURL, baseget, params);
	}



	/**
	 * 馆藏关键字查询接口
	 * 
	 * @param key 查询关键字
	 * @param page 页数
	 * @param count 条数
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
	 * 获取入馆指南
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
	 * 获取书籍的馆藏信息
	 * @param recordid
	 * @return
	 * @throws BookException
	 */
	public List<BookLoc> getBookDetail(String recordid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("recordid", recordid);
		params.add("tablename", "bibliosm");//书籍
		params.add("library", GlobleData.SZLG_LIB_ID);
		String result = http.requestUrl(baseURL+"/library/bookquery/detail.aspx", basepost, params);
		return BookLoc.formList(result);
		
	}
	
	/**
	 * 续借
	 * @param userind
	 * @param barcode
	 * @return
	 * @throws BookException
	 */
	public ShortBook reNew(String userind,String barcode) throws BookException{
		BookParameters params = new BookParameters();
		params.add("userind", userind);
		params.add("barcode", barcode);//书籍
		String result = http.requestUrl(baseURL+"/library/user/renew.aspx", basepost, params);
		return new ShortBook(Task.TASK_BOOK_RENEW,result);
	}
	
	/**
	 * 修改密码
	 * @param userid 
	 * @param newcode
	 * @param oldcode
	 * @return
	 * @throws BookException 
	 */
	public String changPassword(String userid,String newcode,String oldcode) throws BookException{
		BookParameters params = new BookParameters();
		params.add("userid", userid);
		params.add("newcode", newcode);//新密码
		params.add("oldcode", oldcode);//旧密码
		String result = http.requestUrl(baseURL+"/library/user/password.aspx", basepost, params);
		return result;
		
	}
	
	/**
	 * 查询电子资料
	 * @param key
	 * @param type
	 * @return
	 * @throws BookException
	 */
	public List<EBook> queryEBook(String key,int page,int count) throws BookException{
		BookParameters params = new BookParameters();
		params.add("title", key);
		params.add("curpage", page+"");//当前页数
		params.add("perpage",count+"" );//条数
		String result = http.requestUrl(baseURL+"/zk/search.aspx", basepost, params);
		return EBook.formList(result);
		
	}
	/**
	 * 查询电子资源详细
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
	 * 获取电子资料下载地址
	 * @param lngid
	 * @return 地址集合
	 * @throws BookException
	 */
	public List<ShortBook> articledown(String lngid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("lngid", lngid);
		String result = http.requestUrl(baseURL+"/zk/articledown.aspx", basepost, params);
		return ShortBook.formList(Task.TASK_EBOOK_DOWN, result);
	}
	
	/**
	 * 获取更新版本号
	 * @return
	 * @throws BookException
	 */
	public ShortBook getVerionCode() throws BookException{
		String result = http.requestUrl(baseURL+"/library/base/version.aspx", basepost,null);
		return new ShortBook(Task.TASK_REFRESH, result);
	}
	
	/**
	 * 添加收藏
	 * @param libid //图书馆id
	 * @param vipuserid //vip 用户id
	 * @param keyid //索书号
	 * @param typeid //类别 //4，中刊，5深圳
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
	 * 添加收藏
	 * @param libid //图书馆id
	 * @param vipuserid //vip 用户id
	 * @param keyid //索书号
	 * @param typeid //类别 //4，中刊，5深圳
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
	 * 获取收藏列表
	 * @param page 页数
	 * @param count 数量
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
	 * 取消收藏
	 * @param libid //图书馆id
	 * @param vipuserid //vip 用户id
	 * @param keyid //索书号
	 * @param typeid //类别 4中刊，5深图
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
	 * 获取用户所有评论过得书籍
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
	 * 获取某本书籍下面的所有评论
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
	 * 添加评论
	 * @param libid //图书馆id
	 * @param vipuserid //vip 用户id
	 * @param keyid //评论图书id,中刊为lngid，深圳为callno
	 * @param typeid //4，中刊，5深圳
	 * @param content //评论内容
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
	 * 添加评论
	 * @param libid //图书馆id
	 * @param vipuserid //vip 用户id
	 * @param keyid //评论图书id,中刊为lngid，深圳为callno
	 * @param typeid //4，中刊，5深圳
	 * @param content //评论内容
	 * @param recordid //记录id,book唯一标识
	 * @return
	 * @throws BookException
	 */
	public Result addComment(String libid,String vipuserid,String keyid,String typeid,String content,String recordid) throws BookException{
		BookParameters params = new BookParameters();
		params.add("libid", libid);
		params.add("vipuserid", vipuserid);
		if(keyid.contains(",")){//当从我的收藏点击评论，由于只有这个lngid（J228.5/1:4,863174）可供使用，所以在这判断一下
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
	 * 删除评论
	 * @param libid //图书馆id
	 * @param vipuserid //vip 用户id
	 * @param keyid //评论图书id,中刊为lngid，深圳为callno
	 * @param typeid //类别 //4，中刊，5深圳
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
	 * 获取讲座信息
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
	 * 热门书籍,新书通报
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
	 * 推荐模块书籍详细
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
	 * 新闻动态，公益讲座
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
	 * 公告内容详细
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
