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
 * dao���ṩ���ݿ���ɾ�Ĳ�
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
	 * ��������ʵ���࣬�������ݿ��д�����Ӧ�ı�
	 * 
	 * @param 
	 *       
	 * @return 
	 */	
	private void createTablesFromModels(SQLiteDatabase db) {
		List<Class<?>> classes = PackageUtils.getPackageClasses(context, GlobleData.MODEL_PACKAGE, Model.class);
		
    	//������
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
