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
 * dao,�ṩ��ɾ�Ĳ�
 * @author luojiang
 *
 */
public abstract  class MDAO extends SQLiteOpenHelper{
	/**
	 * Ĭ�����ݿ�汾
	 */
	public final static int    DB_VERSION = 1;
	/**
	 * Model�Ĳ���
	 */
	private Model.Operator modelOperator = new Model.Operator() {};
	/**
	 * ���ڸ�ʽ
	 */
	protected SimpleDateFormat dateFormat =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	/**
	 * ������
	 */
	/**
	 * ���캯��
	 * @param context
	 * 		Context����
	 * @param name
	 * 		���ݿ�����
	 * @return	
	 */
	public MDAO(Context context, String name) {
		super(context, name, null, DB_VERSION);
		// TODO Auto-generated constructor stub
	}
	/**
	 * ���캯��
	 * @param context
	 * 		Context����
	 * @param name
	 * 		���ݿ�����
	 * @param version
	 * 		���ݿ�汾
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
	 * ��ѯ
	 * @param where
	 * 		SQL��ѯ����
	 * @param modelClass
	 * 		ʵ����
	 * @return	ʵ���б�
	 */
	@SuppressWarnings("unchecked")
	public <T extends Model> List<T> query(String where, 
			Class<? extends Model> modelClass) throws DaoException{
		List<Model> list = new ArrayList<Model>();

		SQLiteDatabase db = getReadableDatabase();
		String tableName = modelClass.getSimpleName();
		
		Cursor cur = db.query(tableName, null, where, null, null, null, "id "+" desc",null);
		
		for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {

			// ����ʵ����
			Model newObj;
			try {
				newObj = (Model) modelClass.getConstructor(new Class[] {}) // ���캯���еĲ���ֵ
						.newInstance(new Object[] {});// ������ʵ�����ں�����
			} catch (Exception e) {
				e.printStackTrace();
				throw new DaoException(ErrorType.CANNOT_INSTANCE_MODEL);
			} 

			// ��ʼ��ʵ������
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
	 * ��ҳ��ѯ
	 * @param where
	 * 		SQL��ѯ����
	 * @param modelClass
	 * 		ʵ����
	 * @return	ʵ���б�
	 */
	@SuppressWarnings("unchecked")
	public <T extends Model> List<T> query(String where,String limit, 
			Class<? extends Model> modelClass) throws DaoException{
		List<Model> list = new ArrayList<Model>();
		
		SQLiteDatabase db = getReadableDatabase();
		String tableName = modelClass.getSimpleName();
		
		Cursor cur = db.query(tableName, null, where, null, null, null, "id "+" desc",limit);
		
		for(cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
			
			// ����ʵ����
			Model newObj;
			try {
				newObj = (Model) modelClass.getConstructor(new Class[] {}) // ���캯���еĲ���ֵ
						.newInstance(new Object[] {});// ������ʵ�����ں�����
			} catch (Exception e) {
				e.printStackTrace();
				throw new DaoException(ErrorType.CANNOT_INSTANCE_MODEL);
			} 
			
			// ��ʼ��ʵ������
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
	 * ͨ��������ѯ
	 * @param model
	 * 		ʵ�����
	 * @return	��ѯ����ʵ�壬��û���򷵻ؿ�
	 */
	public <T extends Model> T queryByKey(T model) throws DaoException {
		String[] keyNames = modelOperator.getPrimaryKey(model).getKeyNames();
		// ��ʼ��ʵ������
		if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(model)) {
			throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
		}
		// �������
		if(null == keyNames || keyNames.length == 0) {
			throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
		}
		
		// ���ò�ѯ����
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
		// ��ѯ����
		List<T> list = query(sbWhere.toString(), model.getClass());
		
		if(list.size() > 0)
			return (T)list.get(0);
		else
			return null;
	}
	
	/**
	 * ����
	 * @param model
	 * 		ʵ�����
	 * @return	�ɹ�����true, ���򷵻�false
	 */
	public boolean update(Model model) throws DaoException {
		String[] keyNames = modelOperator.getPrimaryKey(model).getKeyNames();
		// ��ʼ��ʵ������
		if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(model)) {
			throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
		}
		// �������
		if(null == keyNames || keyNames.length == 0) {
			throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
		}
		SQLiteDatabase db = getWritableDatabase();
		Class<? extends Model> objClassType = model.getClass();
		
		Field fields[] = objClassType.getDeclaredFields();
		
		ContentValues values = new ContentValues();
		// ���ø����ֶ�
		for(int i = 0; i < fields.length; i++){
			Class<?> type = fields[i].getType();
			String fieldName = fields[i].getName();
			
			//���ֵ
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
		
		// ��������
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
		// ִ�и��²���
		boolean ret = (db.update(model.getClass().getSimpleName(), values, sbWhere.toString(), null) == 1);
		db.close();
		
		return ret;
	}
	
	/**
	 * ɾ��
	 * @param model
	 * 		ʵ�����
	 * @return	�ɹ�����true, ���򷵻�false
	 */
	public boolean delete(Model model) throws DaoException {
		String[] keyNames = modelOperator.getPrimaryKey(model).getKeyNames();
		// ��ʼ��ʵ������
		if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(model)) {
			throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
		}
		// �������
		if(null == keyNames || keyNames.length == 0) {
			throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
		}
		SQLiteDatabase db = getWritableDatabase();
		
		// ��������
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
	 * ���
	 * @param model
	 * 		ʵ�����
	 * @return	�ɹ�����true, ���򷵻�false
	 */
	public boolean add(Model model) throws DaoException{
		// ��ʼ��ʵ������
		if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(model)) {
			throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
		}
		SQLiteDatabase db = getWritableDatabase();
		Class<? extends Model> objClassType = model.getClass();
		
		Field fields[] = objClassType.getDeclaredFields();
		PrimaryKey primaryKey = modelOperator.getPrimaryKey(model);
		String[] keyNames = primaryKey.getKeyNames();

		// �������
		if(primaryKey.isAutoInc() && 1 != keyNames.length) {
			throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
		}
		
		ContentValues values = new ContentValues();
		for(int i = 0; i < fields.length; i++){
			Class<?> type = fields[i].getType();
			String fieldName = fields[i].getName();

			// ���������ֶ�
			if(primaryKey.isAutoInc() && fieldName.equals(keyNames[0])) {
				continue;
			}

			//���ֵ
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
	 * ͨ������ʵ���ഴ����
	 * @param db
	 * 		���ݿ��������
	 * @param modelClass
	 * 		ʵ����
	 * @return �ɹ�����true, ���򷵻�false
	 */
	public void createTableFromModel(SQLiteDatabase db, 
			Class<? extends Model> modelClass) throws DaoException {
		//ʵ�����
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
		
		// ��ʼ��ʵ������
		if(Model.Operator.METHOD_INIT_FAILED == modelOperator.initMethods(model)) {
			throw new DaoException(ErrorType.INVALID_MODEL_METHOD);
		}

		//�ֶ�
		Field fields[] = modelClass.getDeclaredFields();
		//����
		PrimaryKey primaryKey = modelOperator.getPrimaryKey(model);
		//��������
		String[] keyNames = primaryKey.getKeyNames();
		//SQL���
		StringBuilder sbSQL = new StringBuilder();
		//�ֶ�ǰ׺
		String   prefix = "";
		//�ֶκ�׺
		String   suffix = "";
		
		//����Ҫ������
		if(null == keyNames || 0 == keyNames.length) {
			throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
		}
		
		sbSQL.append("create table if not exists ").append(modelClass.getSimpleName());
		sbSQL.append("(");
		// ��֤������������
		if(primaryKey.isAutoInc()) {
			if (1 != keyNames.length) {
				throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
			}
			Class<?> keyType = modelOperator.getFieldType(model, keyNames[0]);
			if (Integer.class != keyType && Long.class != keyType) {
				throw new DaoException(ErrorType.INVALID_PRIMARY_KEY);
			}
			
			//����
			sbSQL.append(keyNames[0]).append(" integer primary key autoincrement");
			prefix = ",";
		} else {
			suffix = ",";
		}
		
		for(int i = 0; i < fields.length; i++) {
			//������������
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

		// ��������
		if (!primaryKey.isAutoInc()) {
			// ��װ����
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