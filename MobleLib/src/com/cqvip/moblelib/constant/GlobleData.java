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
	 * ͼ���id,1��������ͼ��� ,3����ѧԺ
	 * */
	public final static   String LIBIRY_ID = "3";
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
	
	public static final int ANNAOUCETYPE_HOTBOOK = 3;//�����Ƽ�
	public static final int ANNAOUCETYPE_NEWBOOK = 4;//�����Ƽ�
	public static final int ANNAOUCETYPE_NEWS = 2 ;//���Ŷ�̬
	public static final int ANNAOUCETYPE_FREESPEECH = 1;//���潲��
	public static final int FAQ_QUESTION = 5;//���潲��
	
	public static Map<String, Object> datas = new HashMap<String, Object>();
	
	public static final String SERVER = "service";
	
	public static String userid;
	public static String readerid;
	public static String cqvipid;
	
	//��¼��ʶ
	public static boolean islogin = false;
	//����http://mobile.cqvip.com/qk/classlist.aspx?classid=0��ȡ
	public static final int MEDIAL_TYPEID = 1;//ҽҩ����
	public static final int ENGINE_TYPEID = 63;//���̿�ѧ
	public static final int SOCIAL_TYPEID = 67;//�������
	public static final int NATURE_TYPEID = 64;//��Ȼ��ѧ
	public static final int FORESTS_TYPEID = 66;//ũ������
	
	public static final int BIG_PERPAGE = 500;//һ���Ի�ȡ500����¼
	
	public static final int ZLF_ACADEMIC = 2;//ѧλ����
	public static final int ZLF_CONFERENCE = 3;//����
	public static final int ZLF_PATENT = 4;//ר��
	public static final int ZLF_STANDARD= 5;//��׼
	public static final int ZLF_ACHIEVEMENT = 6;//�ɹ�
	public static final int ZLF_BOOK = 7;//�鼮
	
	
	
	
	
	
}
