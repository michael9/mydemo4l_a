package com.cqvip.moblelib.db;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cqvip.dao.DaoException;
import com.cqvip.dao.MDAO;
import com.cqvip.dao.Model;
import com.cqvip.moblelib.constant.GlobleData;
import com.cqvip.utils.PackageUtils;

/**
 * dao，提供数据库增删改查
 * @author luojiang
 *
 */
public class Dao extends MDAO{

	private Context context;
	public Dao(Context context) {
		super(context, GlobleData.dbName);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		createTablesFromModels(arg0);
	}
	
	/**
	 * 查找所有实体类，并在数据库中创建相应的表
	 * 
	 * @param 
	 *       
	 * @return 
	 */	
	private void createTablesFromModels(SQLiteDatabase db) {
		List<Class<?>> classes = PackageUtils.getPackageClasses(context, GlobleData.MODEL_PACKAGE, Model.class);
		
    	//创建表
    	for (int i = 0; i < classes.size(); i++) {
   			@SuppressWarnings("unchecked")
			Class<? extends Model> modelClass = (Class<? extends Model>)classes.get(i);
   			
   			try {
   				createTableFromModel(db, modelClass);
   				Log.i("Dao", "create table ["+modelClass.getSimpleName()+"] ok");
   			} catch (DaoException e) {
   				Log.w("Dao", "create table ["+modelClass.getSimpleName()+"] failed ["+e.getMessage()+"]");
   			}
			
    	}
	}
}
