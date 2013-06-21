package com.cqvip.dao;

import android.util.Log;

/**
 * 数据库错误
 * @author luojiang
 *
 */
public class DaoException extends Exception {
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 异常类型
	 */
	public enum ErrorType {
		/**
		 * 未知错误
		 */
		UNKOWN_ERROR,
		/**
		 * 创建实体错误
		 */
		CANNOT_INSTANCE_MODEL,
		/**
		 * 实体的get或set方法不正确
		 */
		INVALID_MODEL_METHOD,
		/**
		 * 主键设置不正确
		 */
		INVALID_PRIMARY_KEY,
		/**
		 * 数据库执行错误
		 */
		DB_EXCEPTION
	}
		
	/**
	 * 异常类型
	 */
	private ErrorType type;
	
	/**
	 * 构造函数
	 * @param type
	 * 		异常类型
	 */
	public DaoException(ErrorType type) {
		super(getMessageFromType(type));
		this.type = type;
	}
	
	/**
	 * 获取异常类型
	 * @return 异常类型
	 */
	public ErrorType getType() {
		return this.type;
	}
	
	/**
	 * 获取异常类型的描述
	 * @param type
	 * @return
	 */
	public static String getMessageFromType(ErrorType type) {
		if(ErrorType.UNKOWN_ERROR == type) {
			return "未知错误";
		} else if(ErrorType.CANNOT_INSTANCE_MODEL == type) {
			return "创建实体错误";
		} else if(ErrorType.INVALID_MODEL_METHOD == type) {
			return "实体的get或set方法不正确";
		} else if(ErrorType.INVALID_PRIMARY_KEY == type) {
			return "主键设置不正确";
		} else if(ErrorType.DB_EXCEPTION == type) {
			return "数据库执行错误";
		}
		
		return "";
	}

	@Override
	public void printStackTrace() {
		super.printStackTrace();
		Log.d("DaoException", getMessageFromType(this.type));
	}
	
	
}

