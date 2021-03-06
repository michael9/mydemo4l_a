
package com.cqvip.moblelib.biz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.cqvip.moblelib.activity.AdvancedBookActivity;
import com.cqvip.moblelib.activity.AnnouceListActivity;
import com.cqvip.moblelib.activity.BaseActivity;
import com.cqvip.moblelib.activity.BorrowAndOrderActivity;
import com.cqvip.moblelib.activity.CommentActivity;
import com.cqvip.moblelib.activity.DetailAdvancedBookActivity;
import com.cqvip.moblelib.activity.DetailBookActivity;
import com.cqvip.moblelib.activity.DetailTextActivity;
import com.cqvip.moblelib.activity.EBookSearchActivity;
import com.cqvip.moblelib.activity.EbookDetailActivity;
import com.cqvip.moblelib.activity.GroupOfReadersActivity;
import com.cqvip.moblelib.activity.MyFavorActivity;
import com.cqvip.moblelib.activity.ResultOnSearchActivity;
import com.cqvip.moblelib.base.IBookManagerActivity;
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
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.service.BookManager;

/**
 * 负责处理网络任务
 * 
 * @author luojiang
 * 
 */
public class ManagerService extends Service implements Runnable {

	// 接口层
	public BookManager manager = new BookManager();
	// 线程开关
	public static boolean isrun = false;
	// 全局静态引用
	public static ManagerService mainService;
	// 任务集合
	private static ArrayList<Task> allTask = new ArrayList<Task>();
	// activit集合
	public static ArrayList<IBookManagerActivity> allActivity = new ArrayList<IBookManagerActivity>();

	//

	public ManagerService() {
		mainService = this;
	}

	// 添加任务
	public static void addNewTask(Task ts) {
		allTask.add(ts);
	}

	// 更新UI
	public Handler hander = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Task.TASK_LOGIN:
				IBookManagerActivity ibook = (IBookManagerActivity) ManagerService
						.getActivityByName("ActivityDlg");
				if (msg.arg1 != 0) {
					doException(1,msg, "ActivityDlg");
					break;
				}
				ibook.refresh(msg.obj);
				break;
			case Task.TASK_GET_READERINFO:
				IBookManagerActivity readinfo = (IBookManagerActivity) ManagerService
						.getActivityByName("ReaderinfoActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "ReaderinfoActivity");
					break;
				}
				readinfo.refresh(msg.obj);
				break;
			case Task.TASK_QUERY_BOOK:
				IBookManagerActivity book = (IBookManagerActivity) ManagerService
						.getActivityByName("ResultOnSearchActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "ResultOnSearchActivity");
					break;
				}
				book.refresh(ResultOnSearchActivity.GETFIRSTPAGE, msg.obj);
				break;
			case Task.TASK_QUERY_MORE:
				IBookManagerActivity more = (IBookManagerActivity) ManagerService
						.getActivityByName("ResultOnSearchActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "ResultOnSearchActivity");
					break;
				}
				more.refresh(ResultOnSearchActivity.GETNEXTPAGE, msg.obj);
				break;
			case Task.TASK_E_NOTICE:
				IBookManagerActivity notice = (IBookManagerActivity) ManagerService
						.getActivityByName("DetailTextActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "DetailTextActivity");
					break;
				}
				notice.refresh(DetailTextActivity.E_NOTICE, msg.obj);
				break;
			case Task.TASK_E_CARDGUID:
				IBookManagerActivity card = (IBookManagerActivity) ManagerService
						.getActivityByName("DetailTextActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "DetailTextActivity");
					break;
				}
				card.refresh(DetailTextActivity.E_CARDGUID, msg.obj);
				break;
			case Task.TASK_E_TIME:
				IBookManagerActivity time = (IBookManagerActivity) ManagerService
						.getActivityByName("DetailTextActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "DetailTextActivity");
					break;
				}
				time.refresh(DetailTextActivity.E_TIME, msg.obj);
				break;
			case Task.TASK_E_READER:
				IBookManagerActivity reader = (IBookManagerActivity) ManagerService
						.getActivityByName("DetailTextActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "DetailTextActivity");
					break;
				}
				reader.refresh(DetailTextActivity.E_READER, msg.obj);
				break;
			case Task.TASK_E_SERVICE:
				IBookManagerActivity service = (IBookManagerActivity) ManagerService
						.getActivityByName("DetailTextActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "DetailTextActivity");
					break;
				}
				service.refresh(DetailTextActivity.E_SERVICE, msg.obj);
				break;
			case Task.TASK_BOOK_INFO:
				IBookManagerActivity binfo = (IBookManagerActivity) ManagerService
						.getActivityByName("DetailBookActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "DetailBookActivity");
					break;
				}
				binfo.refresh(DetailBookActivity.GETBOOKINFO, msg.obj);
				break;
			// 修改密码
			case Task.TASK_USER_PWD:
				IBookManagerActivity pwd = (IBookManagerActivity) ManagerService
						.getActivityByName("DetailBookActivity");
				if (msg.arg1 != 0) {
					doException(4,msg, "DetailTextActivity");
					break;
				}
				pwd.refresh(msg.obj);
				break;
			// 借阅列表
			case Task.TASK_BORROW_LIST:
				IBookManagerActivity borrowlist = (IBookManagerActivity) ManagerService
						.getActivityByName("BorrowAndOrderActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "BorrowAndOrderActivity");
					break;
				}
				borrowlist.refresh(BorrowAndOrderActivity.BORROWLIST, msg.obj);
				break;
			// 续借
			case Task.TASK_BOOK_RENEW:
				IBookManagerActivity renew = (IBookManagerActivity) ManagerService
						.getActivityByName("BorrowAndOrderActivity");
				if (msg.arg1 != 0) {
					doException(3,msg, "BorrowAndOrderActivity");
					break;
				}
				renew.refresh(BorrowAndOrderActivity.RENEW, msg.obj);
				break;
			// 查询电子书
			case Task.TASK_QUERY_EBOOK:
				IBookManagerActivity ebooks = (IBookManagerActivity) ManagerService
						.getActivityByName("EBookSearchActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "EBookSearchActivity");
					break;
				}
				ebooks.refresh(EBookSearchActivity.GETFIRSTPAGE,msg.obj);
				break;	
				//查询电子书更多
			case Task.TASK_QUERY_EBOOK_MORE:
				IBookManagerActivity ebooksmore = (IBookManagerActivity) ManagerService.getActivityByName("EBookSearchActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "EBookSearchActivity");
					break;
				}
				ebooksmore.refresh(EBookSearchActivity.GETNEXTPAGE,msg.obj);
				break;	
				//查询电子书详细
			case Task.TASK_QUERY_EBOOK_DETAIL:
				IBookManagerActivity ebooksDtail = (IBookManagerActivity) ManagerService.getActivityByName("EBookActiviy");
				if (msg.arg1 != 0) {
					doException(2,msg, "EBookActiviy");
					break;
				}
				ebooksDtail.refresh(msg.obj);
				break;	
				//下载电子书
			case Task.TASK_EBOOK_DOWN:
				IBookManagerActivity down = (IBookManagerActivity) ManagerService.getActivityByName("EbookDetailActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "EbookDetailActivity");
					break;
				}
				down.refresh(EbookDetailActivity.GET_DETAIL,msg.obj);
				break;	
				//获取服务器apk版本号
			case Task.TASK_REFRESH:
				IBookManagerActivity refresh= (IBookManagerActivity) ManagerService.getActivityByName("MainMenuActivity");
				if (msg.arg1 != 0) {//不提示失败信息
					msg.arg1=0;
					break;
				}
				refresh.refresh(msg.obj);
				break;	
				//获取收藏列表19
			case Task.TASK_GET_FAVOR:
				MyFavorActivity favor= (MyFavorActivity) ManagerService.getActivityByName("MyFavorActivity");
				if (msg.arg1 != 0) {
					favor.onError(2);
					msg.arg1 = 0;
					break;
				}
				favor.refresh(MyFavorActivity.FAVOR,msg.obj,msg.arg2);
				break;	
				//馆藏图书收藏20
			case Task.TASK_LIB_FAVOR:
				IBookManagerActivity favor_lib = (IBookManagerActivity) ManagerService.getActivityByName("DetailBookActivity");
				if (msg.arg1 != 0) {
					doException(5,msg, "DetailBookActivity");
					break;
				}
				favor_lib.refresh(DetailBookActivity.FAVOR,msg.obj);
				break;	
				//取消收藏21
			case Task.TASK_CANCEL_FAVOR:
				MyFavorActivity favor_cancel = (MyFavorActivity) ManagerService.getActivityByName("MyFavorActivity");
				if (msg.arg1 != 0) {
					favor_cancel.onError(6);
					msg.arg1 = 0;
					break;
				}
				favor_cancel.refresh(MyFavorActivity.CANCELFAVOR,msg.obj);
				break;	
				//电子图书收藏22
			case Task.TASK_EBOOK_FAVOR:
				IBookManagerActivity favor_ebook = (IBookManagerActivity) ManagerService.getActivityByName("EbookDetailActivity");
				if (msg.arg1 != 0) {
					doException(5,msg, "EbookDetailActivity");
					break;
				}
				favor_ebook.refresh(EbookDetailActivity.ADD_FORVORITE,msg.obj);
				break;	
				//添加评论23
			case Task.TASK_ADD_COMMENT:
				IBookManagerActivity add_comment = (IBookManagerActivity) ManagerService.getActivityByName("CommentActivity");
				if (msg.arg1 != 0) {
					doException(6,msg, "CommentActivity");
					break;
				}
				add_comment.refresh(msg.what,msg.obj);
				break;	
				//获取用户评论过得书籍列表24
			case Task.TASK_COMMENT_BOOKLIST:
				GroupOfReadersActivity groupOfReadersActivity= (GroupOfReadersActivity) ManagerService.getActivityByName("GroupOfReadersActivity");
				if (msg.arg1 != 0) {
					groupOfReadersActivity.onError(2);
					msg.arg1 = 0;
					break;
				}
				groupOfReadersActivity.refresh(GroupOfReadersActivity.COMMENTLIST,msg.obj);
				break;	
			case Task.TASK_COMMENT_LIST:
			case Task.TASK_COMMENT_LIST_MORE:
				CommentActivity comments= (CommentActivity) ManagerService.getActivityByName("CommentActivity");
				if (msg.arg1 != 0) {
					comments.onError(2);
					msg.arg1 = 0;
					break;
				}
				comments.refresh(msg.what,msg.obj);
				break;	
			
			case Task.TASK_SUGGEST_NEWBOOK:	
			case Task.TASK_SUGGEST_NEWBOOK_MORE:
			case Task.TASK_SUGGEST_HOTBOOK:
			case Task.TASK_SUGGEST_HOTBOOK_MORE:
				AdvancedBookActivity adbook= (AdvancedBookActivity) ManagerService.getActivityByName("AdvancedBookActivity");
				if (msg.arg1 != 0) {
					adbook.onError(2);
					msg.arg1 = 0;
					break;
				}
				adbook.refresh(msg.what,msg.obj);
				break;
			case Task.TASK_SUGGEST_DETAIL:
				DetailAdvancedBookActivity adbookDetail= (DetailAdvancedBookActivity) ManagerService.getActivityByName("DetailAdvancedBookActivity");
				if (msg.arg1 != 0) {
					adbookDetail.onError(2);
					msg.arg1 = 0;
					break;
				}
				adbookDetail.refresh(msg.obj);
				break;
			case Task.TASK_ANNOUNCE_NEWS:	
			case Task.TASK_ANNOUNCE_NEWS_MORE:
			case Task.TASK_ANNOUNCE_WELFARE:
			case Task.TASK_ANNOUNCE_WELFARE_MORE:
			case Task.TASK_E_CAUTION:
			case Task.TASK_E_CAUTION_MORE:
				AnnouceListActivity annouce = (AnnouceListActivity) ManagerService.getActivityByName("AnnouceListActivity");
				if (msg.arg1 != 0) {
					annouce.onError(2);
					msg.arg1 = 0;
					break;
				}
				annouce.refresh(msg.what,msg.obj);
				break;
			case Task.TASK_ANNOUNCE_DETAIL:
				
				break;	
		   }
	    }
	};
	
	private void doException(int type,Message msg, String c_name) {
		if (msg.arg1 != 0) {
			BaseActivity baseActivity = (BaseActivity) ManagerService
					.getActivityByName(c_name);
			baseActivity.onError(type);
			msg.arg1 = 0;
		}
	}
		
    //执行任务
    public void doTask(Task task){
    	Message msg = hander.obtainMessage();
		msg.what = task.getTaskID();//刷新uI
		try{
			switch(task.getTaskID()){
			//登陆1
		case Task.TASK_LOGIN:
			Result result = manager.login((String)task.getTaskParam().get("id"),(String)task.getTaskParam().get("pwd"),GlobleData.LIBIRY_ID);
			msg.obj = result;
			break;
			//获取读者信息2
		case Task.TASK_GET_READERINFO:
			Reader reader = manager.getReaderInfo((String)task.getTaskParam().get("userid"));
			msg.obj = reader;
			break;	
			//查询书籍信息3
		case Task.TASK_QUERY_BOOK:
			List<Book> books = manager.getBookSearch(
					(String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),
					(Integer)task.getTaskParam().get("count"),(String)task.getTaskParam().get("library"),
					(String)task.getTaskParam().get("field"));
			msg.obj = books;
			break;	
			//查询更多4
		case Task.TASK_QUERY_MORE:
			List<Book> more = manager.getBookSearch(
					(String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),
					(Integer)task.getTaskParam().get("count"),(String)task.getTaskParam().get("library"),
					(String)task.getTaskParam().get("field"));
			msg.obj = more;
			break;	
			//入馆须知5
		case Task.TASK_E_NOTICE:
			String notic = manager.getContent(Task.TASK_E_NOTICE);
			msg.obj = notic;
			break;	
			//馆内公告6
		case Task.TASK_E_CARDGUID:
			String card = manager.getContent(Task.TASK_E_CARDGUID);
			msg.obj = card;
			break;	
			//开馆时间7
		case Task.TASK_E_TIME:
			String time = manager.getContent(Task.TASK_E_TIME);
			msg.obj = time;
			break;	
			//读者须知8
		case Task.TASK_E_READER:
			String read = manager.getContent(Task.TASK_E_READER);
			msg.obj = read;
			break;	
			//服务介绍9
		case Task.TASK_E_SERVICE:
			String service = manager.getContent(Task.TASK_E_SERVICE);
			msg.obj = service;
			break;	
			//获取书详细信息10
		case Task.TASK_BOOK_INFO:
			List<BookLoc> binfo = manager.getBookDetail((String)task.getTaskParam().get("id"));
			msg.obj = binfo;
			break;	
			//修改密码11
		case Task.TASK_USER_PWD:
			String sucess = manager.changPassword((String)task.getTaskParam().get("userid"),(String)task.getTaskParam().get("newcode"),(String)task.getTaskParam().get("oldcode"));
			msg.obj = sucess;
			break;
			//借阅图书信息12
		case Task.TASK_BORROW_LIST:
			List<BorrowBook> borrowlist = manager.getLoanList((String)task.getTaskParam().get("id"));
			msg.obj = borrowlist;
			break;		
			//续借13
		case Task.TASK_BOOK_RENEW:
			ShortBook renew = manager.reNew((String)task.getTaskParam().get("userid"),(String)task.getTaskParam().get("barcode"));
			msg.obj = renew;
			break;	
			//查询电子书14
		case Task.TASK_QUERY_EBOOK:
			List<EBook> ebooks = manager.queryEBook((String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),(Integer)task.getTaskParam().get("count"));
			msg.obj = ebooks;
			break;	
			//查询电子书更多15
		case Task.TASK_QUERY_EBOOK_MORE:
			List<EBook> moreebooks = manager.queryEBook((String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),(Integer)task.getTaskParam().get("count"));
			msg.obj = moreebooks;
			break;	
			//查询电子书详细16
		case Task.TASK_QUERY_EBOOK_DETAIL:
			EbookDetail ebookDetail = manager.queryEBookDetail((String)task.getTaskParam().get("lngid"));
			msg.obj = ebookDetail;
			break;
			//查询电子书下载地址17
		case Task.TASK_EBOOK_DOWN:
			List<ShortBook> down = manager.articledown((String)task.getTaskParam().get("lngid"));
			msg.obj = down;
			break;	
			//更新版本号18
		case Task.TASK_REFRESH:
			ShortBook refresh = manager.getVerionCode();
			msg.obj = refresh;
			break;	
			//获取收藏列表19
		case Task.TASK_GET_FAVOR:
			Map<Integer,List<Favorite>> getFavor = manager.getFavoriteList((String)task.getTaskParam().get("libid"), (String)task.getTaskParam().get("vipuserid"), (String)task.getTaskParam().get("curpage"), (String)task.getTaskParam().get("perpage"),(String)task.getTaskParam().get("typeid"));
			msg.obj = getFavor;
			msg.arg2=Integer.valueOf((String)task.getTaskParam().get("typeid"));
			break;	
			//馆藏图书收藏20
		case Task.TASK_LIB_FAVOR:
			Result result_addfavor = manager.addFavorite((String)task.getTaskParam().get("libid"), (String)task.getTaskParam().get("vipuserid"), (String)task.getTaskParam().get("keyid"), (String)task.getTaskParam().get("typeid"),(String)task.getTaskParam().get("recordid"));
			msg.obj = result_addfavor;
			break;		
			//取消收藏21
		case Task.TASK_CANCEL_FAVOR:
			Result result_cancelfavor = manager.destroyFavorite((String)task.getTaskParam().get("libid"), (String)task.getTaskParam().get("vipuserid"), (String)task.getTaskParam().get("keyid"), (String)task.getTaskParam().get("typeid"));
			msg.obj = result_cancelfavor;
			break;	
			//电子书收藏22
		case Task.TASK_EBOOK_FAVOR:
			Result result_addebookfavor = manager.addFavorite((String)task.getTaskParam().get("libid"), (String)task.getTaskParam().get("vipuserid"), (String)task.getTaskParam().get("keyid"), (String)task.getTaskParam().get("typeid"));
			msg.obj = result_addebookfavor;
			break;	
			//添加评论23
		case Task.TASK_ADD_COMMENT:
			Result result_addcomment = manager.addComment((String)task.getTaskParam().get("libid"), 
					(String)task.getTaskParam().get("vipuserid"), (String)task.getTaskParam().get("keyid"), 
					(String)task.getTaskParam().get("typeid"), (String)task.getTaskParam().get("content"),(String)task.getTaskParam().get("recordid"));
			msg.obj = result_addcomment;
			break;	
			//获取用户评论过得书籍列表24
		case Task.TASK_COMMENT_BOOKLIST:
			Map<Integer,List<Favorite>> getcommentbooklist = manager.getUserCommentBook((String)task.getTaskParam().get("libid"), (String)task.getTaskParam().get("vipuserid"), (String)task.getTaskParam().get("curpage"), (String)task.getTaskParam().get("perpage"),(String)task.getTaskParam().get("typeid"));
			msg.obj = getcommentbooklist;
			break;	
			//获取用户评论列表
		case Task.TASK_COMMENT_LIST:
		case Task.TASK_COMMENT_LIST_MORE:
			List<Comment> comments = manager.getCommentList((String)task.getTaskParam().get("typeid"), (String)task.getTaskParam().get("keyid"), (String)task.getTaskParam().get("page"), (String)task.getTaskParam().get("count"));
			msg.obj = comments;
			break;	
			//推荐书籍和更多
		case Task.TASK_SUGGEST_HOTBOOK:
		case Task.TASK_SUGGEST_HOTBOOK_MORE:
			List<ShortBook> hotbook = manager.getSuggestHotBook(Task.TASK_SUGGEST_HOTBOOK,GlobleData.LIBIRY_ID, GlobleData.ANNAOUCETYPE_HOTBOOK+"",  (String)task.getTaskParam().get("page"),  (String)task.getTaskParam().get("count"));
			msg.obj = hotbook;
			break;
			//新书和更多
		case Task.TASK_SUGGEST_NEWBOOK:
		case Task.TASK_SUGGEST_NEWBOOK_MORE:
			List<ShortBook> newbook = manager.getSuggestHotBook(Task.TASK_SUGGEST_NEWBOOK,GlobleData.LIBIRY_ID, GlobleData.ANNAOUCETYPE_NEWBOOK+"",  (String)task.getTaskParam().get("page"),  (String)task.getTaskParam().get("count"));
			msg.obj = newbook;
			break;
			//详细
		case Task.TASK_SUGGEST_DETAIL:
			String scontent = manager.getSuggestDetail(GlobleData.LIBIRY_ID,(String)task.getTaskParam().get("id"));
			msg.obj = scontent;
			break;
		case Task.TASK_ANNOUNCE_WELFARE:
		case Task.TASK_ANNOUNCE_WELFARE_MORE:
			List<ShortBook> freespeech = manager.getAnnounceNews(Task.TASK_ANNOUNCE_WELFARE,GlobleData.LIBIRY_ID,GlobleData.ANNAOUCETYPE_FREESPEECH+"",(String)task.getTaskParam().get("page"),  (String)task.getTaskParam().get("count"));
			msg.obj = freespeech;
			break;
		case Task.TASK_ANNOUNCE_NEWS:
		case Task.TASK_ANNOUNCE_NEWS_MORE:
			List<ShortBook> news = manager.getAnnounceNews(Task.TASK_ANNOUNCE_NEWS,GlobleData.LIBIRY_ID,GlobleData.ANNAOUCETYPE_NEWS+"", (String)task.getTaskParam().get("page"),  (String)task.getTaskParam().get("count"));
			msg.obj = news;
			break;
		case Task.TASK_E_CAUTION:
		case Task.TASK_E_CAUTION_MORE:
			List<ShortBook> caution = manager.getAnnouce(Task.TASK_E_CAUTION,GlobleData.LIBIRY_ID,GlobleData.FAQ_QUESTION+"", (String)task.getTaskParam().get("page"),  (String)task.getTaskParam().get("count"));
			msg.obj = caution;
			break;
		case Task.TASK_ANNOUNCE_DETAIL:
			String acontent = manager.getSuggestDetail(GlobleData.LIBIRY_ID,(String)task.getTaskParam().get("id"));
			msg.obj = acontent;
			break;	
			}
			
			
		} catch (BookException e) {
			System.out.println(e.getMessage());
			msg.arg1 = -100;
		} catch (Exception e) {
			msg.arg1 = -100;
			System.out.println(e.getMessage());
		}finally{
			// 发送更新UI
			hander.sendMessage(msg);
			// 销毁任务
			allTask.remove(task);
		}
	}
  //根据名字取集合内activity更新
    public static IBookManagerActivity getActivityByName(String name)
	    {   IBookManagerActivity ibookActivity=null;
	        for(IBookManagerActivity  ib:allActivity)
	        {
	        	if(ib.getClass().getName().indexOf(name)>=0)
	        	{
	        		ibookActivity=ib;
	        	}
	        }
	    	return ibookActivity;
	    }
    
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i("service", "========onCreate=========");
		mainService = this;
		if (GlobleData.datas.get(GlobleData.SERVER) == null) {
			GlobleData.datas.put(GlobleData.SERVER, this);
		}
		Thread t = new Thread(this);
		t.start();
	}

	@Override
	public void run() {
		while (isrun) {
			Task lasttask = null;
			synchronized (allTask) {// 接任务
				if (allTask.size() > 0) {
					lasttask = allTask.get(0);
					doTask(lasttask);
				}
				// 执行任务
			}
			try {
				Thread.sleep(1000);
			} catch (Exception e) {

			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

}

