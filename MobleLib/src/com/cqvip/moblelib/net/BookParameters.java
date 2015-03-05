package com.cqvip.moblelib.net;

import java.util.ArrayList;

import android.text.TextUtils;

public class BookParameters {
    //集合存放 参数名"name"
	private ArrayList<String> mKeys = new ArrayList<String>();
	//集合存放 参数值"zhang"
	private ArrayList<String> mValues=new ArrayList<String>();
	
	public BookParameters(){
		
	}
	/**
	 * 添加key和value
	 * @param key
	 * @param value
	 */
	public void add(String key, String value){
	    if(!TextUtils.isEmpty(key)&&!TextUtils.isEmpty(value)){
	        this.mKeys.add(key);
	        mValues.add(value);
	    }
	}
	/**
	 * 集合的大小
	 * @return
	 */
	public  int size(){
		return mKeys.size();
	}
	/**
	 * 更具序号获取key
	 * @param size
	 * @return
	 */
	public String getKey(int size) {
		if(size>=0&&size<this.mKeys.size()){
			return mKeys.get(size);
		}else{
		return "";
		}
	}
	/**
	 * 更具key，获取value的值
	 * @param key
	 * @return
	 */
	public String getValue(String key) {
		int index=getLocation(key);
		if(index>=0&&index<this.mKeys.size() ){
			return mValues.get(index);
		}else{
			return null;
		}
	}
	/**
	 * 
	 * @param location
	 * @return
	 */
	public String getValue(int location){
	    if(location>=0 && location < this.mKeys.size()){
	        
	        return  this.mValues.get(location);
	    }else{
	        return null;
	    }
		
	}
	/**
	 * 
	 * 返回key对应的序号
	 * @param key
	 * @return
	 */
	private int getLocation(String key){
		if(this.mKeys.contains(key)){
			return this.mKeys.indexOf(key);
		}
		return -1;
	}
}
