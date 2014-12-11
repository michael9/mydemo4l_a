package com.cqvip.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import android.content.Context;

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
	
	
	   public static String getFromAssets(Context context,String fileName){
           try {
                InputStreamReader inputReader = new InputStreamReader( context.getResources().getAssets().open(fileName) );
               BufferedReader bufReader = new BufferedReader(inputReader);
               String line="";
               String Result="";
               while((line = bufReader.readLine()) != null)
                   Result += line;
               return Result;
           } catch (Exception e) {
               e.printStackTrace();
           }
           return null;
   }
}
