package com.cqvip.moblelib.constant;

import java.util.HashMap;
import java.util.Map;

public class GlobleData {

	/**
	 * Ĭ�����ݿ�汾
	 */
	public final static int    defaultDBVersion = 1;
	/**
	 * Ĭ�����ݿ�����
	 */
	public final static String dbName = "moblelib.db";
	/**
	 * ʵ�������ڰ�
	 */
	public final static String MODEL_PACKAGE = "com.cqvip.moblelib.entity";
	/**
	 * ��������ַ
	 * */
	public final static   String SERVER_URL = "http://mobile.cqvip.com";
	/**
	 * ͼ���id,1��������ͼ���
	 * */
	public final static   String LIBIRY_ID = "1";
	/**
	 * ����ͼ���id 
	 * */
	public final static   String SZLG_LIB_ID = "044120";
	/**
	 * �鼮����id, 4�����п���5�������ڹݲ�ͼ��
	 */
	public final static int BOOK_ZK_TYPE = 4;
	/**
	 * �鼮����id, 4�����п���5�������ڹݲ�ͼ��
	 */	
	public final static int BOOK_SZ_TYPE = 5;
	//��ѯ����,�ؼ��� 
	public final static String QUERY_KEY = "subject";
	//��ѯ����,isbn
	public final static String QUERY_ISBN = "isbn";
	//��ѯ����,�����
	public final static String QUERY_ALL = "all";
	//��ѯ����,����
	public final static String QUERY_TITLE = "title";
	//��ѯ����,���� 
	public final static String QUERY_AUTHOR = "author";
	//��ѯ����,�����
	public final static String QUERY_CALSSNO = "classno";
	//��ѯ����,�����
	public final static String QUERY_CALLNO = "callno";
	//��ѯ����,������
	public final static String QUERY_PUBLISHER = "publisher";
	//
	public final static String IMAGE_CACHE_DIR = "bookimg";
	
	
	
	public static Map<String, Object> datas = new HashMap<String, Object>();
	
	public static final String SERVER = "service";
	
	public static String userid;
	public static String readerid;
	public static String cqvipid;
	
	
	
}
