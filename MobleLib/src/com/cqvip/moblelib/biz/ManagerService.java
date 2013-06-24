package com.cqvip.moblelib.biz;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.cqvip.moblelib.activity.BorrowAndOrderActivity;
import com.cqvip.moblelib.activity.DetailTextActivity;
import com.cqvip.moblelib.activity.ResultOnSearchActivity;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BookLoc;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.Reader;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.service.BookManager;

/**
 * 负责处理网络任务
 * @author luojiang
 *
 */
public class ManagerService extends Service implements Runnable{
	
	//接口层
	public BookManager manager = new BookManager();
	//线程开关
	public static boolean isrun=false;
	//全局静态引用
	public static ManagerService mainService;
	//任务集合
	private static ArrayList<Task> allTask = new ArrayList<Task>();
	//activit集合
	public static ArrayList<IBookManagerActivity> allActivity=new ArrayList<IBookManagerActivity>();
	//
	
	public ManagerService(){
		mainService = this;
	}
	
	 //添加任务
    public static void addNewTask(Task ts)
    {
    	allTask.add(ts);
    }
   
    
    //更新UI
    
    
    
    //执行任务
    public void doTask(Task task){
    	Message msg = hander.obtainMessage();
		msg.what = task.getTaskID();//刷新uI
		try{
			switch(task.getTaskID()){
			//登陆1
		case Task.TASK_LOGIN:
			Result result = manager.login((String)task.getTaskParam().get("id"),(String)task.getTaskParam().get("pwd"));
			msg.obj = result;
			break;
			//获取读者信息2
		case Task.TASK_GET_READERINFO:
			Reader reader = manager.getReaderInfo((String)task.getTaskParam().get("userid"));
			msg.obj = reader;
			break;	
			//查询书籍信息3
		case Task.TASK_QUERY_BOOK:
			List<Book> books = manager.getBookSearch((String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),(Integer)task.getTaskParam().get("count"));
			msg.obj = books;
			break;	
			//查询更多4
		case Task.TASK_QUERY_MORE:
			List<Book> more = manager.getBookSearch((String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),(Integer)task.getTaskParam().get("count"));
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
			BookLoc binfo = manager.getBookDetail((String)task.getTaskParam().get("id"));
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
			List<EBook> ebooks = manager.queryEBook((String)task.getTaskParam().get("key"),(String)task.getTaskParam().get("type"));
			msg.obj = ebooks;
			break;	
			
			}
		}catch(Exception e){
			e.printStackTrace();
			msg.what = -100;
		}	
		//发送更新UI
		hander.sendMessage(msg);
		//销毁任务	
		allTask.remove(task);
    	
    	
    	
    }
    public Handler  hander = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case Task.TASK_LOGIN:
				IBookManagerActivity ibook = (IBookManagerActivity) ManagerService.getActivityByName("ActivityDlg");
				ibook.refresh(msg.obj);
			break;
			case Task.TASK_GET_READERINFO:
				IBookManagerActivity readinfo = (IBookManagerActivity) ManagerService.getActivityByName("ReaderinfoActivity");
				readinfo.refresh(msg.obj);
			break;	
			case Task.TASK_QUERY_BOOK:
				IBookManagerActivity book = (IBookManagerActivity) ManagerService.getActivityByName("ResultOnSearchActivity");
				book.refresh(ResultOnSearchActivity.GETFIRSTPAGE,msg.obj);
				break;	
			case Task.TASK_QUERY_MORE:
				IBookManagerActivity more = (IBookManagerActivity) ManagerService.getActivityByName("ResultOnSearchActivity");
				more.refresh(ResultOnSearchActivity.GETNEXTPAGE,msg.obj);
				break;	
			case Task.TASK_E_NOTICE:
				IBookManagerActivity notice = (IBookManagerActivity) ManagerService.getActivityByName("DetailTextActivity");
				notice.refresh(DetailTextActivity.E_NOTICE,msg.obj);
				break;	
			case Task.TASK_E_CARDGUID:
				IBookManagerActivity card = (IBookManagerActivity) ManagerService.getActivityByName("DetailTextActivity");
				card.refresh(DetailTextActivity.E_CARDGUID,msg.obj);
				break;	
			case Task.TASK_E_TIME:
				IBookManagerActivity time = (IBookManagerActivity) ManagerService.getActivityByName("DetailTextActivity");
				time.refresh(DetailTextActivity.E_TIME,msg.obj);
				break;	
			case Task.TASK_E_READER:
				IBookManagerActivity reader = (IBookManagerActivity) ManagerService.getActivityByName("DetailTextActivity");
				reader.refresh(DetailTextActivity.E_READER,msg.obj);
				break;	
			case Task.TASK_E_SERVICE:
				IBookManagerActivity service = (IBookManagerActivity) ManagerService.getActivityByName("DetailTextActivity");
				service.refresh(DetailTextActivity.E_SERVICE,msg.obj);
				break;	
			case Task.TASK_BOOK_INFO:
				IBookManagerActivity binfo = (IBookManagerActivity) ManagerService.getActivityByName("DetailBookActivity");
				binfo.refresh(msg.obj);
				break;	
				//修改密码
			case Task.TASK_USER_PWD:
				IBookManagerActivity pwd = (IBookManagerActivity) ManagerService.getActivityByName("DetailBookActivity");
				pwd.refresh(msg.obj);
				break;	
				//借阅列表
			case Task.TASK_BORROW_LIST:
				IBookManagerActivity borrowlist = (IBookManagerActivity) ManagerService.getActivityByName("BorrowAndOrderActivity");
				borrowlist.refresh(BorrowAndOrderActivity.BORROWLIST,msg.obj);
				break;	
				//续借
			case Task.TASK_BOOK_RENEW:
				IBookManagerActivity renew = (IBookManagerActivity) ManagerService.getActivityByName("BorrowAndOrderActivity");
				renew.refresh(BorrowAndOrderActivity.RENEW,msg.obj);
				break;	
				//查询电子书
			case Task.TASK_QUERY_EBOOK:
				IBookManagerActivity ebooks = (IBookManagerActivity) ManagerService.getActivityByName("EBookActiviy");
				ebooks.refresh(msg.obj);
				break;	
			}
		}
    	
    };
    
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
		mainService=this;
		if(GlobleData.datas.get(GlobleData.SERVER)==null){
		GlobleData.datas.put(GlobleData.SERVER, this);
		}
		Thread t=new Thread(this);
		t.start();
	}


	@Override
	public void run() {
		while(isrun){ 
		Task lasttask=null;
		  synchronized(allTask)
		  {//接任务	
		    if(allTask.size()>0)
		    {
		    	lasttask=allTask.get(0);
		    	doTask(lasttask);
		    }
		   //执行任务
		  }
		  try{
			  Thread.sleep(1000);
			  }catch(Exception e){
				  
			  }
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

}
