package com.cqvip.moblelib.db;

import java.util.List;

import android.content.Context;

import com.cqvip.dao.DaoException;
import com.cqvip.dao.Model;
import com.cqvip.moblelib.entity.MEbook;

public class SearchHistoryDao<T extends Model> extends Dao {
	private T var;

	public SearchHistoryDao(Context context, T t) {
		super(context);
		var = t;
	}

	/**
	 * 清空所有列表
	 * 
	 * @throws DaoException
	 */
	public void deleteAll() throws DaoException {
		List<T> lists = queryall();
		if (lists != null) {
			for (T book : lists) {
				try {
					this.delete(book);
				} catch (DaoException e) {
					throw new DaoException(e.getType());
				}
			}
		}
	}

	/**
	 * 保存用户信息
	 * 
	 * @param user
	 * @throws DaoException
	 */
	public void saveInfo(T mbook) throws DaoException {
		try {
			this.add(mbook);
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
	}

	/**
	 * 查询所有记录
	 * 
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public List<T> queryall() throws DaoException {
		StringBuilder where = new StringBuilder();
		List<T> result = null;
		try {
			result = this.query(where.toString(), var.getClass());
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
		if (result == null || result.size() <= 0) {
			return null;
		}
		return result;
	}

	/**
	 * 根据name查询记录
	 * 
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public T queryInfo(String name) throws DaoException {
		StringBuilder where = new StringBuilder();
		where.append("name = '");
		where.append(name + "'");
		List<T> result = null;
		try {
			result = this.query(where.toString(), var.getClass());
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
		if (result == null || result.size() <= 0) {
			return null;
		}
		return result.get(0);
	}

	/**
	 * 根据date降序查询记录
	 * 
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public List<T> queryInfobydate() throws DaoException {
		StringBuilder where = new StringBuilder();
		List<T> result = null;
		try {
			result = this.query(where.toString(), var.getClass(), "date");
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
		if (result == null || result.size() <= 0) {
			return null;
		}
		return result;
	}

	/**
	 * 更新
	 * 
	 * @param book
	 * @throws DaoException
	 */
	public void updateState(T mbook) throws DaoException {
		try {
			this.update(mbook);
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
	}

}