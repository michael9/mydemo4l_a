package com.cqvip.moblelib.biz;

import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.cqvip.moblelib.activity.ActivityDlg;
import com.cqvip.moblelib.activity.BaseActivity;
import com.cqvip.moblelib.activity.BorrowAndOrderActivity;
import com.cqvip.moblelib.activity.DetailTextActivity;
import com.cqvip.moblelib.activity.ResultOnSearchActivity;
import com.cqvip.moblelib.base.IBookManagerActivity;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.moblelib.model.Book;
import com.cqvip.moblelib.model.BookLoc;
import com.cqvip.moblelib.model.BorrowBook;
import com.cqvip.moblelib.model.EBook;
import com.cqvip.moblelib.model.EbookDetail;
import com.cqvip.moblelib.model.Reader;
import com.cqvip.moblelib.model.Result;
import com.cqvip.moblelib.model.ShortBook;
import com.cqvip.moblelib.net.BookException;
import com.cqvip.moblelib.service.BookManager;

/**
 * ��������������
 * 
 * @author luojiang
 * 
 */
public class ManagerService extends Service implements Runnable {

	// �ӿڲ�
	public BookManager manager = new BookManager();
	// �߳̿���
	public static boolean isrun = false;
	// ȫ�־�̬����
	public static ManagerService mainService;
	// ���񼯺�
	private static ArrayList<Task> allTask = new ArrayList<Task>();
	// activit����
	public static ArrayList<IBookManagerActivity> allActivity = new ArrayList<IBookManagerActivity>();

	//

	public ManagerService() {
		mainService = this;
	}

	// �������
	public static void addNewTask(Task ts) {
		allTask.add(ts);
	}

	// ����UI
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
					doException(2,msg, "DetailTextActivity");
					break;
				}
				binfo.refresh(msg.obj);
				break;
			// �޸�����
			case Task.TASK_USER_PWD:
				IBookManagerActivity pwd = (IBookManagerActivity) ManagerService
						.getActivityByName("DetailBookActivity");
				if (msg.arg1 != 0) {
					doException(4,msg, "DetailTextActivity");
					break;
				}
				pwd.refresh(msg.obj);
				break;
			// �����б�
			case Task.TASK_BORROW_LIST:
				IBookManagerActivity borrowlist = (IBookManagerActivity) ManagerService
						.getActivityByName("BorrowAndOrderActivity");
				if (msg.arg1 != 0) {
					doException(2,msg, "BorrowAndOrderActivity");
					break;
				}
				borrowlist.refresh(BorrowAndOrderActivity.BORROWLIST, msg.obj);
				break;
			// ����
			case Task.TASK_BOOK_RENEW:
				IBookManagerActivity renew = (IBookManagerActivity) ManagerService
						.getActivityByName("BorrowAndOrderActivity");
				if (msg.arg1 != 0) {
					doException(3,msg, "BorrowAndOrderActivity");
					break;
				}
				renew.refresh(BorrowAndOrderActivity.RENEW, msg.obj);
				break;
			// ��ѯ������
			case Task.TASK_QUERY_EBOOK:
				IBookManagerActivity ebooks = (IBookManagerActivity) ManagerService
						.getActivityByName("EBookActiviy");
				if (msg.arg1 != 0) {
					doException(2,msg, "EBookActiviy");
					break;
				}
				ebooks.refresh(msg.obj);
				break;	
				//��ѯ���������
			case Task.TASK_QUERY_EBOOK_MORE:
				IBookManagerActivity ebooksmore = (IBookManagerActivity) ManagerService.getActivityByName("EBookActiviy");
				if (msg.arg1 != 0) {
					doException(2,msg, "EBookActiviy");
					break;
				}
				ebooksmore.refresh(msg.obj);
				break;	
				//��ѯ��������ϸ
			case Task.TASK_QUERY_EBOOK_DETAIL:
				IBookManagerActivity ebooksDtail = (IBookManagerActivity) ManagerService.getActivityByName("EBookActiviy");
				if (msg.arg1 != 0) {
					doException(2,msg, "EBookActiviy");
					break;
				}
				ebooksDtail.refresh(msg.obj);
				break;	
				//���ص�����
			case Task.TASK_EBOOK_DOWN:
				IBookManagerActivity down = (IBookManagerActivity) ManagerService.getActivityByName("EBookActiviy");
				if (msg.arg1 != 0) {
					doException(2,msg, "EBookActiviy");
					break;
				}
				down.refresh(msg.obj);
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
		
    //ִ������
    public void doTask(Task task){
    	Message msg = hander.obtainMessage();
		msg.what = task.getTaskID();//ˢ��uI
		try{
			switch(task.getTaskID()){
			//��½1
		case Task.TASK_LOGIN:
			Result result = manager.login((String)task.getTaskParam().get("id"),(String)task.getTaskParam().get("pwd"));
			msg.obj = result;
			break;
			//��ȡ������Ϣ2
		case Task.TASK_GET_READERINFO:
			Reader reader = manager.getReaderInfo((String)task.getTaskParam().get("userid"));
			msg.obj = reader;
			break;	
			//��ѯ�鼮��Ϣ3
		case Task.TASK_QUERY_BOOK:
			List<Book> books = manager.getBookSearch((String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),(Integer)task.getTaskParam().get("count"));
			msg.obj = books;
			break;	
			//��ѯ����4
		case Task.TASK_QUERY_MORE:
			List<Book> more = manager.getBookSearch((String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),(Integer)task.getTaskParam().get("count"));
			msg.obj = more;
			break;	
			//�����֪5
		case Task.TASK_E_NOTICE:
			String notic = manager.getContent(Task.TASK_E_NOTICE);
			msg.obj = notic;
			break;	
			//���ڹ���6
		case Task.TASK_E_CARDGUID:
			String card = manager.getContent(Task.TASK_E_CARDGUID);
			msg.obj = card;
			break;	
			//����ʱ��7
		case Task.TASK_E_TIME:
			String time = manager.getContent(Task.TASK_E_TIME);
			msg.obj = time;
			break;	
			//������֪8
		case Task.TASK_E_READER:
			String read = manager.getContent(Task.TASK_E_READER);
			msg.obj = read;
			break;	
			//�������9
		case Task.TASK_E_SERVICE:
			String service = manager.getContent(Task.TASK_E_SERVICE);
			msg.obj = service;
			break;	
			//��ȡ����ϸ��Ϣ10
		case Task.TASK_BOOK_INFO:
			BookLoc binfo = manager.getBookDetail((String)task.getTaskParam().get("id"));
			msg.obj = binfo;
			break;	
			//�޸�����11
		case Task.TASK_USER_PWD:
			String sucess = manager.changPassword((String)task.getTaskParam().get("userid"),(String)task.getTaskParam().get("newcode"),(String)task.getTaskParam().get("oldcode"));
			msg.obj = sucess;
			break;
			//����ͼ����Ϣ12
		case Task.TASK_BORROW_LIST:
			List<BorrowBook> borrowlist = manager.getLoanList((String)task.getTaskParam().get("id"));
			msg.obj = borrowlist;
			break;		
			//����13
		case Task.TASK_BOOK_RENEW:
			ShortBook renew = manager.reNew((String)task.getTaskParam().get("userid"),(String)task.getTaskParam().get("barcode"));
			msg.obj = renew;
			break;	
			//��ѯ������14
		case Task.TASK_QUERY_EBOOK:
			List<EBook> ebooks = manager.queryEBook((String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),(Integer)task.getTaskParam().get("count"));
			msg.obj = ebooks;
			break;	
			//��ѯ���������15
		case Task.TASK_QUERY_EBOOK_MORE:
			List<EBook> moreebooks = manager.queryEBook((String)task.getTaskParam().get("key"),(Integer)task.getTaskParam().get("page"),(Integer)task.getTaskParam().get("count"));
			msg.obj = moreebooks;
			break;	
			//��ѯ��������ϸ16
		case Task.TASK_QUERY_EBOOK_DETAIL:
			EbookDetail ebookDetail = manager.queryEBookDetail((String)task.getTaskParam().get("lngid"));
			msg.obj = ebookDetail;
			break;
			//��ѯ���������ص�ַ17
		case Task.TASK_EBOOK_DOWN:
			List<ShortBook> down = manager.articledown((String)task.getTaskParam().get("lngid"));
			msg.obj = down;
			break;	
			
			}
		} catch (BookException e) {
			msg.arg1 = -100;
			// msg.what = e.getStatusCode();
			Log.i("getStatusCode()", "==========stack=======" + msg.what);
		} catch (Exception e) {
			msg.arg1 = -100;
			Log.i("getStatusCode()", "==========stack=======-100");
		}finally{
			// ���͸���UI
			hander.sendMessage(msg);
			// ��������
			allTask.remove(task);
		}
	}
  //��������ȡ������activity����
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
			synchronized (allTask) {// ������
				if (allTask.size() > 0) {
					lasttask = allTask.get(0);
					doTask(lasttask);
				}
				// ִ������
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

