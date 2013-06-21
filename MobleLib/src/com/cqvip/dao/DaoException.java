package com.cqvip.dao;

import android.util.Log;

/**
 * ���ݿ����
 * @author luojiang
 *
 */
public class DaoException extends Exception {
	/**
	 * ���л�
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * �쳣����
	 */
	public enum ErrorType {
		/**
		 * δ֪����
		 */
		UNKOWN_ERROR,
		/**
		 * ����ʵ�����
		 */
		CANNOT_INSTANCE_MODEL,
		/**
		 * ʵ���get��set��������ȷ
		 */
		INVALID_MODEL_METHOD,
		/**
		 * �������ò���ȷ
		 */
		INVALID_PRIMARY_KEY,
		/**
		 * ���ݿ�ִ�д���
		 */
		DB_EXCEPTION
	}
		
	/**
	 * �쳣����
	 */
	private ErrorType type;
	
	/**
	 * ���캯��
	 * @param type
	 * 		�쳣����
	 */
	public DaoException(ErrorType type) {
		super(getMessageFromType(type));
		this.type = type;
	}
	
	/**
	 * ��ȡ�쳣����
	 * @return �쳣����
	 */
	public ErrorType getType() {
		return this.type;
	}
	
	/**
	 * ��ȡ�쳣���͵�����
	 * @param type
	 * @return
	 */
	public static String getMessageFromType(ErrorType type) {
		if(ErrorType.UNKOWN_ERROR == type) {
			return "δ֪����";
		} else if(ErrorType.CANNOT_INSTANCE_MODEL == type) {
			return "����ʵ�����";
		} else if(ErrorType.INVALID_MODEL_METHOD == type) {
			return "ʵ���get��set��������ȷ";
		} else if(ErrorType.INVALID_PRIMARY_KEY == type) {
			return "�������ò���ȷ";
		} else if(ErrorType.DB_EXCEPTION == type) {
			return "���ݿ�ִ�д���";
		}
		
		return "";
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
		Log.d("DaoException", getMessageFromType(this.type));
	}
	
	
}

