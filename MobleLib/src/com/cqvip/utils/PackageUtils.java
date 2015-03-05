package com.cqvip.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import dalvik.system.DexFile;

public class PackageUtils {
		
	   
		public static List<Class<?>> getPackageClasses(Context context, String packageName, Class<?> parentClass) {
			List<Class<?>> classes = new ArrayList<Class<?>>();
		    ApplicationInfo ai = context.getApplicationInfo();
		    String classPath = ai.sourceDir;

		    if(null == packageName) {
		    	packageName = "";
		    }
		    if(null == parentClass) {
		    	parentClass = Object.class;
		    }

		    DexFile dex = null;
		    try {
		        dex = new DexFile(classPath);
		        Enumeration<String> apkClassNames = dex.entries();
		        while (apkClassNames.hasMoreElements()) {
		            String className = apkClassNames.nextElement();
		            try {
		                Class<?> c = context.getClassLoader().loadClass(className);
		                if (parentClass.isAssignableFrom(c) &&
		                		c.getName().startsWith(packageName)) {
		                	classes.add(c);
		                }
		            } catch (ClassNotFoundException e) {
		                //e.printStackTrace();
		            }
		        }
		    } catch (IOException e) {
		        //e.printStackTrace();
		    } finally {
		        try {
		        	if(null != dex) {
		        		dex.close();
		        	}
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
		    }
		    
		    return classes;
		}
		
	    public static boolean isServiceRunning(Context conext, String className) {
	    	ActivityManager mActivityManager = (ActivityManager)conext.getSystemService(Context.ACTIVITY_SERVICE); 
	    	List<ActivityManager.RunningServiceInfo> serviceList = mActivityManager.getRunningServices(100);
	    	for (int i = 0; i < serviceList.size(); i ++) { 
	    		if(className.equals(serviceList.get(i).service.getClassName())) { 
	    			return true; 
	    		}
	    	}
	    	
	    	return false;
	    }
	}  
