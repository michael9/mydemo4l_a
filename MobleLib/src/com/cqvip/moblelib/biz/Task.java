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