package com.cqvip.moblelib.net;

import java.util.ArrayList;

import android.text.TextUtils;

public class BookParameters {
    //���ϴ�� ������"name"
	private ArrayList<String> mKeys = new ArrayList<String>();
	//���ϴ�� ����ֵ"zhang"
	private ArrayList<String> mValues=new ArrayList<String>();
	
	public BookParameters(){
		
	}
	/**
	 * ���key��value
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
	 * ���ϵĴ�С
	 * @return
	 */
	public  int size(){
		return mKeys.size();
	}
	/**
	 * ������Ż�ȡkey
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
	 * ����key����ȡvalue��ֵ
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
	 * ����key��Ӧ�����
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
