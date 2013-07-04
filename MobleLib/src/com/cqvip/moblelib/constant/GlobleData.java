package com.cqvip.moblelib.constant;

import java.util.HashMap;
import java.util.Map;

public class GlobleData {

	/**
	 * 默认数据库版本
	 */
	public final static int    defaultDBVersion = 1;
	/**
	 * 默认数据库名称
	 */
	public final static String dbName = "moblelib.db";
	/**
	 * 实体类所在包
	 */
	public final static String MODEL_PACKAGE = "com.cqvip.moblelib.entity";
	/**
	 * 服务器地址
	 * */
	public final static   String SERVER_URL = "http://mobile.cqvip.com";
	/**
	 * 图书馆id,1代表深圳图书馆
	 * */
	public final static   String LIBIRY_ID = "1";
	/**
	 * 龙岗图书馆id 
	 * */
	public final static   String SZLG_LIB_ID = "044120";
	/**
	 * 书籍类型id, 4代表中刊，5代表深圳馆藏图书
	 */
	public final static int BOOK_ZK_TYPE = 4;
	/**
	 * 书籍类型id, 4代表中刊，5代表深圳馆藏图书
	 */	
	public final static int BOOK_SZ_TYPE = 5;
	//查询类型,关键字 
	public final static String QUERY_KEY = "subject";
	//查询类型,isbn
	public final static String QUERY_ISBN = "isbn";
	//查询类型,任意词
	public final static String QUERY_ALL = "all";
	//查询类型,标题
	public final static String QUERY_TITLE = "title";
	//查询类型,作者 
	public final static String QUERY_AUTHOR = "author";
	//查询类型,分类号
	public final static String QUERY_CALSSNO = "classno";
	//查询类型,索书号
	public final static String QUERY_CALLNO = "callno";
	//查询类型,出版社
	public final static String QUERY_PUBLISHER = "publisher";
	//
	public final static String IMAGE_CACHE_DIR = "bookimg";
	
	
	
	public static Map<String, Object> datas = new HashMap<String, Object>();
	
	public static final String SERVER = "service";
	
	public static String userid;
	public static String readerid;
	public static String cqvipid;
	
	
	
}
