package com.cqvip.dao;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

import com.cqvip.dao.DaoException.ErrorType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * dao,提供增删改查
 * @author luojiang
 *
 */
public abstract  class MDAO extends SQLiteOpenHelper{
	/**
	 * 默认数据库版本
	 */
	public final static int    DB_VERSION = 1;
	/**
	 * Model的操作
	 */
	private Model.Operator modelOperator = new Model.Operator() {};
	/**
	 * 日期格式
	 */
	protected SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * 最后错误极
	 */
	/**
	 * 构造函数
	 * @param context
	 * 		Context对象
	 * @param name
	 * 		数据库名称
	 * @return	
	 */
	public MDAO(Context context, String name) {
		super(context, name, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}
	/**
	 * 构造函数
	 * @param context
	 * 		Context对象
	 * @param name
	 * 		数据库名称
	 * @param version
	 * 		数据库版本
	 * @return	
	 */
	public MDAO(Context context, String name, int version) {
		super(context, name, null, version);
		// TODO Auto-generated constructor stub
	}
	
	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
	 */
	@Override
	public abstract void onCreate(SQLiteDatabase arg0);

	/* (non-Javadoc)
	 * @see android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Cursor cur = db.query("sqlite_master", new String[]{"name"}, 
				null, null, null, null, null);
		for(int i = 0; i < cur.getCount(); i++){
			db.execSQL("drop table "+cur.getString(i));
		}

		onCreate(db);
	}
	
	/**
	 * 查询
	 * @param where
	 * 		SQL查询条件
	 * @param modelClass
	 * 		实体类
	 * @return	实体列表
	 */
	@SuppressWarnings("unchecked")
	public <T extends Model> List<T> query(String where, 
			Class<? extends Model> modelClass) throws DaoException{
		List<Model> list = new ArrayList<Model>();

		SQLiteDatabase db = getReadableDatabase();
		String tableName = modelClass.getSimpleName();
		
		Cursor cur = db.query(tableName, null, where, null, null, null, "id "+" desc",null);
		
		for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {

			// 创建实体类
			Model newObj;
			try {
				newObj = (Model) modelClass.getConstructor(new Class[] {}) // 构造函数中的参数值
						.newInstance(new Object[] {});// 创建出实例，内含参数
			} catch (Exception e) {
				e.printStackTrace();
				throw new DaoException(ErrorType.CANNOT_INSTANCE_MODEL);
			} 

			// 初始化实例函数
			if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(newObj)) {
				throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
			}
			
			for(int i = 0; i < cur.getColumnCount(); i++){
				Class<?> type = modelOperator.getFieldType(newObj, cur.getColumnName(i));
					
				if(Integer.class == type){
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getInt(i));
				} else if(Long.class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getLong(i));
				} else if(String.class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getString(i));
				} else if(Double.class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getDouble(i));
				} else if(Float.class == type){
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getFloat(i));
				} else if(Date.class == type) {
					try {
						modelOperator.setFieldValue(newObj, cur.getColumnName(i), 
								dateFormat.parse(cur.getString(i)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else if(byte[].class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getBlob(i));
				}
			}
		
			list.add(newObj);
		}
				
		cur.close();
		db.close();
		
		return (List<T>) list;
	}
	
	/**
	 * 根据a desc查询
	 * @param where
	 * 		SQL查询条件
	 * @param modelClass
	 * 		实体类
	 * @return	实体列表
	 */
	@SuppressWarnings("unchecked")
	public <T extends Model> List<T> query(String where, 
			Class<? extends Model> modelClass,String a) throws DaoException{
		List<Model> list = new ArrayList<Model>();

		SQLiteDatabase db = getReadableDatabase();
		String tableName = modelClass.getSimpleName();
		
		Cursor cur = db.query(tableName, null, where, null, null, null, a+" desc",null);
		
		for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {

			// 创建实体类
			Model newObj;
			try {
				newObj = (Model) modelClass.getConstructor(new Class[] {}) // 构造函数中的参数值
						.newInstance(new Object[] {});// 创建出实例，内含参数
			} catch (Exception e) {
				e.printStackTrace();
				throw new DaoException(ErrorType.CANNOT_INSTANCE_MODEL);
			} 

			// 初始化实例函数
			if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(newObj)) {
				throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
			}
			
			for(int i = 0; i < cur.getColumnCount(); i++){
				Class<?> type = modelOperator.getFieldType(newObj, cur.getColumnName(i));
					
				if(Integer.class == type){
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getInt(i));
				} else if(Long.class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getLong(i));
				} else if(String.class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getString(i));
				} else if(Double.class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getDouble(i));
				} else if(Float.class == type){
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getFloat(i));
				} else if(Date.class == type) {
					try {
						modelOperator.setFieldValue(newObj, cur.getColumnName(i), 
								dateFormat.parse(cur.getString(i)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else if(byte[].class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getBlob(i));
				}
			}
		
			list.add(newObj);
		}
				
		cur.close();
		db.close();
		
		return (List<T>) list;
	}
	
	/**
	 * 分页查询
	 * @param where
	 * 		SQL查询条件
	 * @param modelClass
	 * 		实体类
	 * @return	实体列表
	 */
	@SuppressWarnings("unchecked")
	public <T extends Model> List<T> query(String where,String limit, 
			Class<? extends Model> modelClass) throws DaoException{
		List<Model> list = new ArrayList<Model>();
		
		SQLiteDatabase db = getReadableDatabase();
		String tableName = modelClass.getSimpleName();
		
		Cursor cur = db.query(tableName, null, where, null, null, null, "id "+" desc",limit);
		
		for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			
			// 创建实体类
			Model newObj;
			try {
				newObj = (Model) modelClass.getConstructor(new Class[] {}) // 构造函数中的参数值
						.newInstance(new Object[] {});// 创建出实例，内含参数
			} catch (Exception e) {
				e.printStackTrace();
				throw new DaoException(ErrorType.CANNOT_INSTANCE_MODEL);
			} 
			
			// 初始化实例函数
			if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(newObj)) {
				throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
			}
			
			for(int i = 0; i < cur.getColumnCount(); i++){
				Class<?> type = modelOperator.getFieldType(newObj, cur.getColumnName(i));
				
				if(Integer.class == type){
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getInt(i));
				} else if(Long.class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getLong(i));
				} else if(String.class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getString(i));
				} else if(Double.class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getDouble(i));
				} else if(Float.class == type){
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getFloat(i));
				} else if(Date.class == type) {
					try {
						modelOperator.setFieldValue(newObj, cur.getColumnName(i), 
								dateFormat.parse(cur.getString(i)));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				} else if(byte[].class == type) {
					modelOperator.setFieldValue(newObj, cur.getColumnName(i), cur.getBlob(i));
				}
			}
			
			list.add(newObj);
		}
		
		cur.close();
		db.close();
		
		return (List<T>) list;
	}
	
	/**
	 * 通过主键查询
	 * @param model
	 * 		实体对象
	 * @return	查询到的实体，若没有则返回空
	 */
	public <T extends Model> T queryByKey(T model) throws DaoException {
		String[] keyNames = modelOperator.getPrimaryKey(model).getKeyNames();
		// 初始化实例函数
		if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(model)) {
			throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
		}
		// 检查主键
		if(null == keyNames || keyNames.length == 0) {
			throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
		}
		
		// 设置查询条件
		StringBuilder sbWhere = new StringBuilder("");
		for(int i = 0; i < keyNames.length; i++) {
			if(i > 0) {
				sbWhere.append(" and ");
			}
			sbWhere.append(keyNames[i]).append("=");
			if(modelOperator.getFieldType(model, keyNames[i]) == String.class){
				sbWhere.append("'").append(modelOperator.getFieldValue(model, keyNames[i])).append("'");
			} else {
				sbWhere.append(modelOperator.getFieldValue(model,keyNames[i]));
			}
		}
		// 查询数据
		List<T> list = query(sbWhere.toString(), model.getClass());
		
		if(list.size() > 0)
			return (T)list.get(0);
		else
			return null;
	}
	
	/**
	 * 更新
	 * @param model
	 * 		实体对象
	 * @return	成功返回true, 否则返回false
	 */
	public boolean update(Model model) throws DaoException {
		String[] keyNames = modelOperator.getPrimaryKey(model).getKeyNames();
		// 初始化实例函数
		if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(model)) {
			throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
		}
		// 检查主键
		if(null == keyNames || keyNames.length == 0) {
			throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
		}
		SQLiteDatabase db = getWritableDatabase();
		Class<? extends Model> objClassType = model.getClass();
		
		Field fields[] = objClassType.getDeclaredFields();
		
		ContentValues values = new ContentValues();
		// 设置更新字段
		for(int i = 0; i < fields.length; i++){
			Class<?> type = fields[i].getType();
			String fieldName = fields[i].getName();
			
			//添加值
			if(Integer.class == type){
				values.put(fieldName, (Integer)modelOperator.getFieldValue(model, fieldName));
			} else if(Long.class == type) {
				values.put(fieldName, (Long)modelOperator.getFieldValue(model, fieldName));
			} else if(String.class == type) {
				values.put(fieldName, (String) modelOperator.getFieldValue(model, fieldName));
			} else if(Double.class == type) {
				values.put(fieldName, (Double)modelOperator.getFieldValue(model, fieldName));
			} else if(Float.class == type){
				values.put(fieldName, (Float)modelOperator.getFieldValue(model, fieldName));
			} else if(Date.class == type) {
				values.put(fieldName, dateFormat.format((Date)modelOperator.getFieldValue(model, fieldName)));
			} else if(byte[].class == type) {
				values.put(fieldName, (byte[])modelOperator.getFieldValue(model, fieldName));
			}
		}
		
		// 设置条件
		StringBuilder sbWhere = new StringBuilder("");
		for(int i = 0; i < keyNames.length; i++) {
			if(i > 0) {
				sbWhere.append(" and ");
			}
			sbWhere.append(keyNames[i]).append("=");
			if(modelOperator.getFieldType(model, keyNames[i]) == String.class){
				sbWhere.append("'").append(modelOperator.getFieldValue(model, keyNames[i])).append("'");
			} else {
				sbWhere.append(modelOperator.getFieldValue(model,keyNames[i]));
			}
		}
		// 执行更新操作
		boolean ret = (db.update(model.getClass().getSimpleName(), values, sbWhere.toString(), null) == 1);
		db.close();
		
		return ret;
	}
	
	/**
	 * 删除
	 * @param model
	 * 		实体对象
	 * @return	成功返回true, 否则返回false
	 */
	public boolean delete(Model model) throws DaoException {
		String[] keyNames = modelOperator.getPrimaryKey(model).getKeyNames();
		// 初始化实例函数
		if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(model)) {
			throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
		}
		// 检查主键
		if(null == keyNames || keyNames.length == 0) {
			throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
		}
		SQLiteDatabase db = getWritableDatabase();
		
		// 设置条件
		StringBuilder sbWhere = new StringBuilder("");
		for(int i = 0; i < keyNames.length; i++) {
			if(i > 0) {
				sbWhere.append(" and ");
			}
			sbWhere.append(keyNames[i]).append("=");
			if(modelOperator.getFieldType(model, keyNames[i]) == String.class){
				sbWhere.append("'").append(modelOperator.getFieldValue(model, keyNames[i])).append("'");
			} else {
				sbWhere.append(modelOperator.getFieldValue(model,keyNames[i]));
			}
		}
		
		boolean ret = (db.delete(model.getClass().getSimpleName(), sbWhere.toString(), null) == 1);
		
		db.close();
		
		return ret;
	}
	
	/**
	 * 添加
	 * @param model
	 * 		实体对象
	 * @return	成功返回true, 否则返回false
	 */
	public boolean add(Model model) throws DaoException{
		// 初始化实例函数
		if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(model)) {
			throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
		}
		SQLiteDatabase db = getWritableDatabase();
		Class<? extends Model> objClassType = model.getClass();
		
		Field fields[] = objClassType.getDeclaredFields();
		PrimaryKey primaryKey = modelOperator.getPrimaryKey(model);
		String[] keyNames = primaryKey.getKeyNames();

		// 检查主键
		if(primaryKey.isAutoInc() && 1 != keyNames.length) {
			throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
		}
		
		ContentValues values = new ContentValues();
		for(int i = 0; i < fields.length; i++){
			Class<?> type = fields[i].getType();
			String fieldName = fields[i].getName();

			// 忽略自增字段
			if(primaryKey.isAutoInc() && fieldName.equals(keyNames[0])) {
				continue;
			}

			//添加值
			if(Integer.class == type){
				values.put(fieldName, (Integer)modelOperator.getFieldValue(model, fieldName));
			} else if(Long.class == type) {
				values.put(fieldName, (Long)modelOperator.getFieldValue(model, fieldName));
			} else if(String.class == type) {
				values.put(fieldName, (String) modelOperator.getFieldValue(model, fieldName));
			} else if(Double.class == type) {
				values.put(fieldName, (Double)modelOperator.getFieldValue(model, fieldName));
			} else if(Float.class == type){
				values.put(fieldName, (Float)modelOperator.getFieldValue(model, fieldName));
			} else if(Date.class == type) {
				values.put(fieldName, dateFormat.format((Date)modelOperator.getFieldValue(model, fieldName)));
			} else if(byte[].class == type) {
				values.put(fieldName, (byte[])modelOperator.getFieldValue(model, fieldName));
			}
			
		}
				
		boolean ret = (db.insert(model.getClass().getSimpleName(), null, values) != -1);
		db.close();
		
		return ret;		
	}
	
	/**
	 * 通过数据实体类创建表
	 * @param db
	 * 		数据库操作对象
	 * @param modelClass
	 * 		实体类
	 * @return 成功返回true, 否则返回false
	 */
	public void createTableFromModel(SQLiteDatabase db, 
			Class<? extends Model> modelClass) throws DaoException {
		//实体对象
		Model model = null;
		try {
			model = modelClass.newInstance();
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
			throw new DaoException(ErrorType.CANNOT_INSTANCE_MODEL);
		} catch (InstantiationException e) {
			//e.printStackTrace();
			throw new DaoException(ErrorType.CANNOT_INSTANCE_MODEL);
		}
		
		// 初始化实例函数
		if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(model)) {
			throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
		}

		//字段
		Field fields[] = modelClass.getDeclaredFields();
		//主键
		PrimaryKey primaryKey = modelOperator.getPrimaryKey(model);
		//主键数组
		String[] keyNames = primaryKey.getKeyNames();
		//SQL语句
		StringBuilder sbSQL = new StringBuilder();
		//字段前缀
		String   prefix = "";
		//字段后缀
		String   suffix = "";
		
		//必须要有主键
		if(null == keyNames || 0 == keyNames.length) {
			throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
		}
		
		sbSQL.append("create table if not exists ").append(modelClass.getSimpleName());
		sbSQL.append("(");
		// 验证增加自增主键
		if(primaryKey.isAutoInc()) {
			if (1 != keyNames.length) {
				throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
			}
			Class<?> keyType = modelOperator.getFieldType(model, keyNames[0]);
			if (Integer.class != keyType && Long.class != keyType) {
				throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
			}
			
			//主键
			sbSQL.append(keyNames[0]).append(" integer primary key autoincrement");
			prefix = ",";
		} else {
			suffix = ",";
		}
		
		for(int i = 0; i < fields.length; i++) {
			//忽略自增主键
			if(primaryKey.isAutoInc() && fields[i].getName().equals(keyNames[0])) {
				continue;
			}

			Class<?> keyType = fields[i].getType();
			if(Integer.class == keyType || Long.class == keyType){
				sbSQL.append(prefix).append(fields[i].getName()).append(" integer").append(suffix);
			} else if(Float.class == keyType){
				sbSQL.append(prefix).append(fields[i].getName()).append(" float").append(suffix);
			} else if(Double.class == keyType){
				sbSQL.append(prefix).append(fields[i].getName()).append(" double").append(suffix);
			} else if(String.class == keyType){
				sbSQL.append(prefix).append(fields[i].getName()).append(" text").append(suffix);
			} else if(byte[].class == keyType) {	
				sbSQL.append(prefix).append(fields[i].getName()).append(" blob").append(suffix);
			} else if(Date.class == keyType) {
				sbSQL.append(prefix).append(fields[i].getName()).append(" datetime").append(suffix);
			}
		} 

		// 设置主键
		if (!primaryKey.isAutoInc()) {
			// 组装主键
			sbSQL.append(" primary key(");
			for(int i = 0; i < keyNames.length; i++) {
				sbSQL.append(keyNames[i]);
				if(i < keyNames.length-1) {
					sbSQL.append(",");
				}
			}
			sbSQL.append(")");
		}
		sbSQL.append(")");
		
		try {
			db.execSQL(sbSQL.toString());
		} catch (SQLException e) {
			throw new DaoException(ErrorType.DB_EXCEPTION);
		}

	}
}