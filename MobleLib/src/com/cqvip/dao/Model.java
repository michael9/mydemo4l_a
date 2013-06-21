package com.cqvip.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

/**
 * ʵ���࣬��ʵ��̳�
 * @author luojiang
 *
 */
public class Model {
	 /** 
     * ���get���� 
     */  
    protected Hashtable<String, Method> getMethods_ = null;  
    /** 
     * ���set���� 
     */  
    protected Hashtable<String, Method> setMethods_ = null;
    
    /**
     * get��set������ʼ״̬(0:δ��ʼ��, 1:��ʼ���ɹ�, 2:��ʼ��ʧ��)
     */
    protected int initMethodState_ = Operator.METHOD_UNINIT;
    
	/**
	 * ��������
	 */
	protected PrimaryKey primaryKey_ = null;
	
	/**
	 * ���캯��
	 * @param key
	 * 		��������
	 * @return
	 */
	public Model(PrimaryKey primaryKey){
		this.primaryKey_ = primaryKey;
	}
		
	/**
	 * ������
	 * @author luojiang
	 *
	 */
	public abstract static class Operator {
		/**
		 * get��set������ʼ��״̬��δ��ʼ����
		 */
		public final static int METHOD_UNINIT = 0;
		/**
		 * get��set������ʼ��״̬����ʼ���ɹ���
		 */
		public final static int METHOD_INIT_OK = 1;
		/**
		 * get��set������ʼ��״̬����ʼ��ʧ�ܣ�
		 */
		public final static int METHOD_INIT_FAILED = 2;
		
		/**
		 * ��ȡ����
		 * 
		 * @param model
		 *       ʵ�����
		 *
		 * @return ��������
		 */
		protected PrimaryKey getPrimaryKey(Model model) {
			return model.primaryKey_;
		}
		
		/**
		 * ��ȡ�ֶε�ֵ
		 * @param model
		 * 		ʵ�����
		 * @param fieldName
		 * 		�ֶ���
		 * @return �ֶ�ֵ
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
		 * �����ֶε�ֵ
		 * @param model
		 * 		ʵ�����
		 * @param fieldName
		 * 		�ֶ���
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
		 * ��ȡ�ֶ�����
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
		 * ��ʼ���ֶε�get��set����
		 * @param 
		 * 		
		 * @return ��ʼ��״̬
		 */
		protected int initMethods(Model model) {
			if(0 != model.initMethodState_) {
				return model.initMethodState_;
			}
			model.getMethods_ = new Hashtable<String, Method>();  
			model.setMethods_ = new Hashtable<String, Method>();  
			Class<?> modelClass = model.getClass();
			
			model.initMethodState_ = METHOD_INIT_OK;
			//�ֶ�
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
