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
	 * 实体类所在包0
	 */
	public final static String MODEL_PACKAGE = "com.cqvip.moblelib.entity";
	/**
	 * 服务器地址
	 * */
	public final static   String SERVER_URL = "http://mobile.cqvip.com";
	//public final static   String SERVER_URL = "http://192.168.20.61:8080";
	/**
	 * 服务器地址
	 * */
	//public final static   String SERVER_URL_ZJY = "http://192.168.20.61:8080";
	/**
	 * 图书馆id,2代表深圳职业技术学院图书馆
	 * */
	public final static   String LIBIRY_ID = "6";
	/**
	 * 深职院图书馆id 
	 * */
	public final static   String SZLG_LIB_ID = "044120";
	/**
	 * 书籍类型id, 4代表中刊，5代表深圳馆藏图书
	 */
	public final static int BOOK_ZK_TYPE = 4;
	/**
	 * 书籍类型id, 4代表中刊，5代表深圳馆藏图书/6,代表深圳职业技术学院
	 */	
	//public final static int BOOK_SZ_TYPE = 5;
	public final static int BOOK_SZ_TYPE = 6;
	//查询类型,关键字 
	public final static String QUERY_KEY = "subject";
	//查询类型,isbn
	public final static String QUERY_ISBN = "isbn";
	//查询类型,任意词
	public final static String QUERY_ALL = "all";
//	public final static String QUERY_ALL = "title";
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
	//查询表
	public final static String QUERY_TABLE = "bibliosm";
	//
	public final static String IMAGE_CACHE_DIR = "bookimg";
	
	public static final int ANNAOUCETYPE_HOTBOOK = 3;//新书推荐
	public static final int ANNAOUCETYPE_NEWBOOK = 4;//新书推荐
	public static final int ANNAOUCETYPE_NEWS = 2 ;//新闻动态
	public static final int ANNAOUCETYPE_FREESPEECH = 1;//公益讲座
	public static final int FAQ_QUESTION = 5;//公益讲座
	public static final int PROBLEM_ANNAOUCE = 6;//常见问题深职院
	
	public static Map<String, Object> datas = new HashMap<String, Object>();
	
	public static final String SERVER = "service";
	
	public static String userid;
	public static String readerid;
	public static String cqvipid;
	
	
	//馆内公告，接口
	public static final int ANNO_NEWS = 7;//新闻动态
	public static final int ANNO_MESS = 8;//通知公告
	public static final int ANNO_SUBJECT = 9;//专题讲座
	public static final int ANNO_PROFESSOR= 10;//专家讲座
	public static final int SUG_NEWBOOK= 11;//专家讲座
	
	
	//登录标识
	public static boolean islogin = false;
	//根据http://mobile.cqvip.com/qk/classlist.aspx?classid=0获取
	public static final int MEDIAL_TYPEID = 1;//医药卫生
	public static final int ENGINE_TYPEID = 63;//工程科学
	public static final int SOCIAL_TYPEID = 67;//人文社会
	public static final int NATURE_TYPEID = 64;//自然科学
	public static final int FORESTS_TYPEID = 66;//农林牧鱼
	
	public static final int BIG_PERPAGE = 500;//一次性获取500条记录
	
	
	
	
}
