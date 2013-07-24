package com.cqvip.moblelib.db;

import java.util.List;

import android.content.Context;

import com.cqvip.dao.DaoException;
import com.cqvip.moblelib.entity.MEbook;
import com.cqvip.moblelib.model.EBook;

public class MEBookDao extends Dao{

	public MEBookDao(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	/**
	 * ��������б�
	 */
	public void deleteAll(){
		getWritableDatabase().delete("MEbook", null, null);
	}
	/**
	 * ��������������б�
	 * @throws DaoException 
	 */
	public void deleteAll(int isdown) throws DaoException{
		List<MEbook> lists = queryall(isdown);
		for(MEbook book:lists){
			try {
				this.delete(book);
			} catch (DaoException e) {
				throw new DaoException(e.getType());
			}
		}
		
	}
	/**
	 * ɾ���û���Ϣ
	 * @param id
	 * @throws DaoException 
	 */
	public void delInfo(String lngid) throws DaoException{
		MEbook book = queryInfo(lngid);
		if(book==null){
			return; 
		}
		try {
			this.delete(book);
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
	}
	/**
	 * �����û���Ϣ
	 * @param user
	 * @throws DaoException
	 */
	public void saveInfo(EBook book,long downloadid,int isdownload) throws DaoException{
		MEbook mbook = changeToMEbook(book,downloadid,isdownload);
		try {
			this.add(mbook);
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
	}
	


	/**
	 * ����id��ѯ��¼
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public MEbook queryInfo(String lngid) throws DaoException{
		 StringBuilder where = new StringBuilder();
		   where.append("lngid=");
		   where.append(lngid);
		   List<MEbook> result = null;
		try {
			result =  this.query(where.toString(), MEbook.class);
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
		if(result==null||result.size()<=0){
			return null;
		}
		return result.get(0);
	}
	/**
	 * ����id��ѯ��¼
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public MEbook queryInfobydownid(String downloadid) throws DaoException{
		 StringBuilder where = new StringBuilder();
		   where.append("downloadid=");
		   where.append(downloadid);
		   List<MEbook> result = null;
		try {
			result =  this.query(where.toString(), MEbook.class);
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
		if(result==null||result.size()<=0){
			return null;
		}
		return result.get(0);
	}
	/**
	 * ��ѯ���м�¼
	 * @param id
	 * @return
	 * @throws DaoException
	 */
	public List<MEbook> queryall(int isdownloade) throws DaoException{
		 StringBuilder where = new StringBuilder();
		   where.append("isdownload=");
		   where.append(isdownloade);
		   List<MEbook> result = null;
		try {
			result =  this.query(where.toString(), MEbook.class);
		} catch (DaoException e) {
			throw new DaoException(e.getType());
		}
		if(result==null||result.size()<=0){
			return null;
		}
		return result;
	}
	private MEbook changeToMEbook(EBook book,long downloadid,int isdownload) {
		MEbook mbook = new MEbook();
		mbook.setLngid(book.getLngid());
		mbook.setName_c(book.getName_c());
		mbook.setNum(book.getNum());
		mbook.setImgurl(book.getImgurl());
		mbook.setWriter(book.getWriter());
		mbook.setRemark_c(book.getRemark_c());
		mbook.setPagecount(book.getPagecount());
		mbook.setPdfsize(book.getPdfsize());
		mbook.setTitle_c(book.getTitle_c());
		mbook.setPdfsize(book.getPdfsize());
		mbook.setBeginpage(book.getBeginpage());
		mbook.setEndpage(book.getEndpage());
		
		mbook.setDownloadid(downloadid);
		mbook.setIsdownload(isdownload);
		return mbook;
	}
		
	
}