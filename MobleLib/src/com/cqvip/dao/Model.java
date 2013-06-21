package com.cqvip.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

/**
 * 实体类，供实体继承
 * @author luojiang
 *
 */
public class Model {
	 /** 
     * 存放get方法 
     */  
    protected Hashtable<String, Method> getMethods_ = null;  
    /** 
     * 存放set方法 
     */  
    protected Hashtable<String, Method> setMethods_ = null;
    
    /**
     * get与set函数初始状态(0:未初始化, 1:初始化成功, 2:初始化失败)
     */
    protected int initMethodState_ = Operator.METHOD_UNINIT;
    
	/**
	 * 主键对象
	 */
	protected PrimaryKey primaryKey_ = null;
	
	/**
	 * 构造函数
	 * @param key
	 * 		主键对象
	 * @return
	 */
	public Model(PrimaryKey primaryKey){
		this.primaryKey_ = primaryKey;
	}
		
	/**
	 * 操作类
	 * @author luojiang
	 *
	 */
	public abstract static class Operator {
		/**
		 * get与set方法初始化状态（未初始化）
		 */
		public final static int METHOD_UNINIT = 0;
		/**
		 * get与set方法初始化状态（初始化成功）
		 */
		public final static int METHOD_INIT_OK = 1;
		/**
		 * get与set方法初始化状态（初始化失败）
		 */
		public final static int METHOD_INIT_FAILED = 2;
		
		/**
		 * 获取主键
		 * 
		 * @param model
		 *       实体对象
		 *
		 * @return 主键对象
		 */
		protected PrimaryKey getPrimaryKey(Model model) {
			return model.primaryKey_;
		}
		
		/**
		 * 获取字段的值
		 * @param model
		 * 		实体对象
		 * @param fieldName
		 * 		字段名
		 * @return 字段值
		 */
		protected Object getFieldValue(Model model, String fieldName){
			Method method = model.getMethods_.get(fieldName);  
	        try {  
	            return method.invoke(model, new Object[0]);  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
	        return null;  
		}
		
		/**
		 * 设置字段的值
		 * @param model
		 * 		实体对象
		 * @param fieldName
		 * 		字段名
		 * @return	
		 */
		protected void setFieldValue(Model model, String fieldName, Object value){
			Method method = model.setMethods_.get(fieldName);  
	        try {  
	            method.invoke(model, new Object[] {value});  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }  
		}
		
		/**
		 * 获取字段类型
		 * @param fieldName
		 * @return
		 * @throws NoSuchFieldException
		 */
		protected Class<?> getFieldType(Model model, String fieldName) {
			Class<?> type = null;
			try {
				type = model.getClass().getDeclaredField(fieldName).getType();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
			
			return type;
		}
		
		/**
		 * 初始化字段的get与set函数
		 * @param 
		 * 		
		 * @return 初始化状态
		 */
		protected int initMethods(Model model) {
			if(0 != model.initMethodState_) {
				return model.initMethodState_;
			}
			model.getMethods_ = new Hashtable<String, Method>();  
			model.setMethods_ = new Hashtable<String, Method>();  
			Class<?> modelClass = model.getClass();
			
			model.initMethodState_ = METHOD_INIT_OK;
			//字段
			Field fields[] = modelClass.getDeclaredFields();
			for(int i = 0; i < fields.length; i++) {
				Class<?> type = fields[i].getType();
				String   name = fields[i].getName();
				if(Integer.class == type || String.class == type ||
						Double.class == type ||	Float.class == type ||
						Long.class == type || Date.class == type ||
						byte[].class == type) {
					Method getter = GetterSetter.getGetter(modelClass, name);
					Method setter = GetterSetter.getSetter(modelClass, name);
					if(null != getter) {
						model.getMethods_.put(name, getter);
					} else {
						model.initMethodState_ = METHOD_INIT_FAILED;
					}
					if(null != setter) {
						model.setMethods_.put(name, setter);
					} else {
						model.initMethodState_ = METHOD_INIT_FAILED;
					}
				}
			}
			
			return model.initMethodState_;
		}

	}
}
