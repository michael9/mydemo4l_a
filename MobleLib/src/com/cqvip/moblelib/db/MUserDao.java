package com.cqvip.moblelib.db;

import java.util.List;

import android.content.Context;

import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.entity.MUser;

/**
 * 用户信息，dao
 * @author luojiang
 *
 */
public class MUserDao extends Dao{

	
	public MUserDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 保存用户信息
	 * @param user
	 * @throws DaoException
	 */
	public void saveInfo(MUser user) throws DaoException{
		try {
			this.add(user);
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
	}
	
	/**
	 * 删除用户信息
	 * @param id
	 * @throws DaoException 
	 */
	public void delInfo(String id) throws DaoException{
		MUser user = queryInfo(id);
		if(user==null){
			return; 
		}
		try {
			this.delete(user);
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
		
	}
	/**
	 * 查询用户信息
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public MUser queryInfo() throws DaoException{
		 StringBuilder where = new StringBuilder();
		   where.append("1=1");
		   List<MUser> result = null;
		try {
			result =  this.query(where.toString(), MUser.class);
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
		if(result==null||result.size()<=0){
			return null;
		}
		return result.get(0);
	}
	/**
	 * 查询用户信息
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public MUser queryInfo(String id) throws DaoException{
		 StringBuilder where = new StringBuilder();
		   MUser result = null;
		   where.append("cardno=");
		   where.append(id);
		try {
			result = (MUser) this.query(where.toString(), MUser.class);
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
		return result;
	}
	

}
