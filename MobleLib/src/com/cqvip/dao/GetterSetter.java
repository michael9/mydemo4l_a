package com.cqvip.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射
 * @author luojiang
 *
 */
public class GetterSetter {
	/** 
     * java反射Model的set方法 
     *  
     * @param claz 
     * @param fieldName属性名 
     * @return 
     */  
    public static Method getSetter(Class<?> claz, String fieldName) {  
        try {  
            Class<?>[] parameterTypes = new Class[1];  
            Field field = claz.getDeclaredField(fieldName);  
            parameterTypes[0] = field.getType();// 返回参数类型  
            StringBuilder sb = new StringBuilder();  
            sb.append("set").append(fieldName.substring(0, 1).toUpperCase())  
                    .append(fieldName.substring(1));  
            Method method = claz.getMethod(sb.toString(), parameterTypes);  
            return method;  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    
    /** 
     * java反射Model的get方法 
     *  
     * @param claz 
     * @param fieldName属性名 
     * @return 
     */  
    public static Method getGetter(Class<?> claz, String fieldName) {  
        StringBuilder sb = new StringBuilder();  
        sb.append("get").append(fieldName.substring(0, 1).toUpperCase())  
                .append(fieldName.substring(1));  
        try {  
            Class<?>[] types = new Class[] {};  
            return claz.getMethod(sb.toString(), types);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return null;  
    } 
}