package com.cqvip.moblelib.biz;

import java.util.Map;

public class Task {
	private int taskID;// ������
	private Map taskParam;// �������
	public static final int TASK_LOGIN = 1;
	public static final int TASK_GET_READERINFO = 2;//��ȡ������Ϣ
	public static final int TASK_QUERY_BOOK = 3;//��ѯ��ҳ
	public static final int TASK_QUERY_MORE= 4;//��ѯ����
	public static final int TASK_BORROW_LIST= 5;//����
	public static final int TASK_E_NOTICE= 6;//���ڹ���
	public static final int TASK_E_CARDGUID= 7;//��ָ֤��
	public static final int TASK_E_TIME= 8;//����ʱ��
	public static final int TASK_E_READER= 9;//������֪
	public static final int TASK_E_SERVICE= 10;//�������
	public static final int TASK_BOOK_INFO= 11;//��ȡ�����ϸ��Ϣ
	public static final int TASK_BOOK_RENEW= 12;//����
	public static final int TASK_USER_PWD= 13;//�޸�����
	public static final int TASK_QUERY_EBOOK= 14;//��ѯ������
	public static final int TASK_QUERY_EBOOK_MORE= 15;//��ѯ���������
	public static final int TASK_QUERY_EBOOK_DETAIL= 16;//��������ϸ
	public static final int TASK_EBOOK_DOWN= 17;//��ѯ���ص�ַ
	public static final int TASK_REFRESH= 18;//����
	public static final int TASK_GET_FAVOR= 19;//��ȡ�ղ��б�
	public static final int TASK_LIB_FAVOR= 20;//�ݲ�ͼ���ղ�
	public static final int TASK_CANCEL_FAVOR= 21;//ȡ���ղ�
	public static final int TASK_EBOOK_FAVOR= 22;//����ͼ���ղ�
	public static final int TASK_ADD_COMMENT= 23;//�������
	public static final int TASK_COMMENT_BOOKLIST= 24;//��ȡ�û����۹����鼮�б�
	public static final int TASK_COMMENT_LIST= 25;//��ȡΧ���鼮�������б�
	public static final int TASK_ANNOUNCE_SPEACH= 26;//����
	public static final int TASK_GET_HOTBOOK = 27; //��ȡ�����鼮
	
	
	
	//public static final int TASK_GET_HOTBOOK = 28; //��ȡ�����鼮
	
	
	
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