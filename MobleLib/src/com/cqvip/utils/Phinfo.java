package com.cqvip.utils;



import java.util.Locale;

import org.json.JSONObject;

import android.app.ActivityManager;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class Phinfo {

    String phone_manufacturer = "";
    String phone_mode = "";
    String rom_vesion = "";
    String sdk_vesion = "";
    String screen_dm = "";
    String language = "";
    String country = "";
    String deviceid = "";
    String subscriberid = "";
    String memoryclass = "";
    String unique_id = "";
    String gps="";
    
    @Override
	public String toString() {
		return "phinfo [phone_manufacturer=" + phone_manufacturer
				+ ", phone_mode=" + phone_mode + ", rom_vesion=" + rom_vesion
				+ ", sdk_vesion=" + sdk_vesion + ", screen_dm=" + screen_dm
				+ ", Language=" + language + ", Country=" + country
				+ ", DeviceId=" + deviceid + ", SubscriberId=" + subscriberid
				+ ", MemoryClass=" + memoryclass + ", ANDROID_ID=" + unique_id
				+ "]";
	}

	public void fullbaseinfo(Context context) {
        try {
            phone_manufacturer = android.os.Build.MANUFACTURER;
            rom_vesion = android.os.Build.VERSION.RELEASE;
            sdk_vesion = android.os.Build.VERSION.SDK;
            phone_mode = android.os.Build.MODEL;
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            screen_dm = dm.widthPixels + "X" + dm.heightPixels;
            language = Locale.getDefault().getLanguage();
            country = Locale.getDefault().getCountry();
            unique_id = android.provider.Settings.System.getString(context.getContentResolver(), "android_id");
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            deviceid = tm.getDeviceId();
            subscriberid = tm.getSubscriberId();
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            memoryclass = "" + activityManager.getMemoryClass();
            Location mLocation = getLocation(context); 
            if(mLocation!=null){
            	gps=mLocation.getLongitude()+","+ mLocation.getLatitude();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
    //Get the Location by GPS or WIFI  
    public Location getLocation(Context context) {  
        LocationManager locMan = (LocationManager) context  
                .getSystemService(Context.LOCATION_SERVICE);  
        Location location = locMan  
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);  
        if (location == null) {  
            location = locMan  
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);  
        }  
        return location;  
    }  
	
	public String tojson(Phinfo phinfo){
        try { 
            JSONObject jsonObject = new JSONObject(); 
            jsonObject.put("phone_manufacturer", phone_manufacturer); 
            jsonObject.put("phone_mode", phone_mode); 
            jsonObject.put("rom_vesion", rom_vesion); 
            jsonObject.put("sdk_vesion", sdk_vesion); 
            jsonObject.put("screen_dm", screen_dm); 
            jsonObject.put("language", language); 
            jsonObject.put("country", country); 
            jsonObject.put("deviceid", deviceid); 
            jsonObject.put("subscriberid", subscriberid); 
            jsonObject.put("memoryclass", memoryclass); 
            jsonObject.put("unique_id", unique_id); 
            jsonObject.put("gps", gps);
            return jsonObject.toString(); 
        } catch (Exception e) { 
            // TODO Auto-generated catch block 
            e.printStackTrace(); 
        } 
		return "";
	}
}
