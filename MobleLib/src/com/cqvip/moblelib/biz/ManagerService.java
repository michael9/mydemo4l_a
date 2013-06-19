package com.cqvip.moblelib.biz;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.cqvip.moblelib.activity.DetailTextActivity;
import com.cqvip.moblelib.activity.ResultOnSearchActivity;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.moblelib.model.Reader;
import com.cqvip.moblelib.model.Result;
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
		case Task.TASK_LOGIN:
			//登陆
			Result result = manager.login((String)task.getTaskParam().get("id"),(String)task.getTaskParam().get("pwd"));
			//请求登陆
			//result = manager.login();
			msg.obj = result;
			break;
		case Task.TASK_GET_READERINFO:
			//登陆
			Reader reader = manager.getReaderInfo((String)task.getTaskParam().get("userid"));
			msg.obj = reader;
			break;	
		case Task.TASK_QUERY_BOOK:
			//登陆
			List<Book> books = manager.getBookSearch((String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),(Integer)task.getTaskParam().get("count"));
			msg.obj = books;
			break;	
		case Task.TASK_QUERY_MORE:
			List<Book> more = manager.getBookSearch((String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),(Integer)task.getTaskParam().get("count"));
			msg.obj = more;
			break;	
		case Task.TASK_E_NOTICE:
			String notic = manager.getContent(Task.TASK_E_NOTICE);
			msg.obj = notic;
			break;	
		case Task.TASK_E_CARDGUID:
			String card = manager.getContent(Task.TASK_E_CARDGUID);
			msg.obj = card;
			break;	
		case Task.TASK_E_TIME:
			String time = manager.getContent(Task.TASK_E_TIME);
			msg.obj = time;
			break;	
		case Task.TASK_E_SERVICE:
			String service = manager.getContent(Task.TASK_E_SERVICE);
			msg.obj = service;
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
				//登陆
				IBookManagerActivity ibook = (IBookManagerActivity) ManagerService.getActivityByName("MainMenuActivity");
				//刷新
				ibook.refresh(msg.obj);
			break;
			case Task.TASK_GET_READERINFO:
				//获取读者信息
				IBookManagerActivity readinfo = (IBookManagerActivity) ManagerService.getActivityByName("ReaderinfoActivity");
				//刷新
				readinfo.refresh(msg.obj);
			break;	
			case Task.TASK_QUERY_BOOK:
				//获取读者信息
				IBookManagerActivity book = (IBookManagerActivity) ManagerService.getActivityByName("ResultOnSearchActivity");
				//刷新
				book.refresh(ResultOnSearchActivity.GETFIRSTPAGE,msg.obj);
				break;	
			case Task.TASK_QUERY_MORE:
				//获取读者信息
				IBookManagerActivity more = (IBookManagerActivity) ManagerService.getActivityByName("ResultOnSearchActivity");
				//刷新
				more.refresh(ResultOnSearchActivity.GETNEXTPAGE,msg.obj);
				break;	
			case Task.TASK_E_NOTICE:
				//获取读者信息
				IBookManagerActivity notice = (IBookManagerActivity) ManagerService.getActivityByName("DetailBookActivity");
				//刷新
				notice.refresh(DetailTextActivity.E_NOTICE,msg.obj);
				break;	
			case Task.TASK_E_CARDGUID:
				//获取读者信息
				IBookManagerActivity card = (IBookManagerActivity) ManagerService.getActivityByName("DetailBookActivity");
				//刷新
				card.refresh(DetailTextActivity.E_CARDGUID,msg.obj);
				break;	
			case Task.TASK_E_TIME:
				//获取读者信息
				IBookManagerActivity time = (IBookManagerActivity) ManagerService.getActivityByName("DetailBookActivity");
				//刷新
				time.refresh(DetailTextActivity.E_TIME,msg.obj);
				break;	
			case Task.TASK_E_SERVICE:
				//获取读者信息
				IBookManagerActivity service = (IBookManagerActivity) ManagerService.getActivityByName("DetailBookActivity");
				//刷新
				service.refresh(DetailTextActivity.E_SERVICE,msg.obj);
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
		Thread t=new Thread(this);
		t.start();
	}


	@Override
	public void run() {
		while(isrun){ 
		Task lasttask=null;
		  Log.d("service", "..............run");
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
