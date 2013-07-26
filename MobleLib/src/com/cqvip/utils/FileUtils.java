package com.cqvip.utils;

import java.io.File;

public class FileUtils {
	
	
	public static boolean DeleteFolder(String sPath) {   
	  //String  newpath = "D:/temp/wootionImageupload/20121028212042.jpg";
		boolean flag = false;   
	    File file = new File(sPath);   
	    if (!file.exists()) {    
	        return flag;   
	    } else {   
	        if (file.isFile()) {     
	            return deleteFile(sPath);   
	        } else {     
	            return deleteDirectory(sPath);   
	        }   
	    }   
	}
	private static boolean deleteDirectory(String sPath) {
		return false;
	}
	private static boolean deleteFile(String sPath) {
		boolean flag = false;   
	    File file = new File(sPath);   
	    if (file.isFile() && file.exists()) {   
	        file.delete();   
	        flag = true;   
	    }   
	    return flag;   
	}  
}
