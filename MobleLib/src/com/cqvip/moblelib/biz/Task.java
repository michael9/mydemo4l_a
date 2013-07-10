package com.cqvip.moblelib.biz;

import java.util.Map;

public class Task {
	private int taskID;// 任务编号
	private Map taskParam;// 任务参数
	public static final int TASK_LOGIN = 1;
	public static final int TASK_GET_READERINFO = 2;//获取读者信息
	public static final int TASK_QUERY_BOOK = 3;//查询首页
	public static final int TASK_QUERY_MORE= 4;//查询更多
	public static final int TASK_BORROW_LIST= 5;//借阅
	public static final int TASK_E_NOTICE= 6;//馆内公告
	public static final int TASK_E_CARDGUID= 7;//办证指南
	public static final int TASK_E_TIME= 8;//开管时间
	public static final int TASK_E_READER= 9;//读者须知
	public static final int TASK_E_SERVICE= 10;//服务介绍
	public static final int TASK_BOOK_INFO= 11;//获取书的详细信息
	public static final int TASK_BOOK_RENEW= 12;//续借
	public static final int TASK_USER_PWD= 13;//修改密码
	public static final int TASK_QUERY_EBOOK= 14;//查询电子书
	public static final int TASK_QUERY_EBOOK_MORE= 15;//查询更多电子书
	public static final int TASK_QUERY_EBOOK_DETAIL= 16;//电子书详细
	public static final int TASK_EBOOK_DOWN= 17;//查询下载地址
	public static final int TASK_REFRESH= 18;//更新
	public static final int TASK_GET_FAVOR= 19;//获取收藏列表
	public static final int TASK_LIB_FAVOR= 20;//馆藏图书收藏
	public static final int TASK_CANCEL_FAVOR= 21;//取消收藏
	public static final int TASK_EBOOK_FAVOR= 22;//电子图书收藏
    public static final int TASK_ADD_COMMENT= 23;//添加评论
	public static final int TASK_COMMENT_BOOKLIST= 24;//获取用户评论过得书籍列表
	public static final int TASK_COMMENT_LIST= 25;//获取围绕书籍的评论列表
	public static final int TASK_ANNOUNCE_SPEACH= 26;//讲座安排
	public static final int TASK_ANNOUNCE_WELFARE= 28;//公益讲座
	public static final int TASK_ANNOUNCE_WELFARE_MORE= 29;//公益讲座
	public static final int TASK_ANNOUNCE_NEWS = 30;//新闻动态
	public static final int TASK_ANNOUNCE_NEWS_MORE = 31;//新闻动态
	public static final int TASK_ANNOUNCE_DETAIL = 32;//公告详细
	public static final int TASK_E_CAUTION = 33;//常见问题
	public static final int TASK_SUGGEST_HOTBOOK = 34; //获取热门书籍
	public static final int TASK_SUGGEST_NEWBOOK = 35;//获取新书推荐
	public static final int TASK_SUGGEST_HOTBOOK_MORE = 36; //获取热门书籍
	public static final int TASK_SUGGEST_NEWBOOK_MORE = 37;//获取新书推荐
	public static final int TASK_SUGGEST_DETAIL = 38;//推荐书籍详细
	public static final int TAST_CENTER_QUESTION= 39;//在线问答
	public static final int TASK_COMMENT_LIST_MORE= 40;//获取更多评论
	
	
	
	
	public Task(int id, Map param) {
		this.taskID = id;
		this.taskParam = param;
	}

	public int getTaskID() {
		return taskID;
	}

	public void setTaskID(int taskID) {
		this.taskID = taskID;
	}

	public Map getTaskParam() {
		return taskParam;
	}

	public void setTaskParam(Map taskParam) {
		this.taskParam = taskParam;
	}
}